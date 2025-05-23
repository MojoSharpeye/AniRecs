package com.anirecs.AniRecs.database.service;

import com.anirecs.AniRecs.database.entities.Genres;
import com.anirecs.AniRecs.database.entities.KitsuAnime;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Service
public class KitsuApiFullFetch {
    private KitsuAnimeService kitsuAnimeService;

    public KitsuApiFullFetch(KitsuAnimeService kitsuAnimeService) {
        this.kitsuAnimeService = kitsuAnimeService;
    }

    public Integer getAllData(int currentPage) {
        String baseAPI = "https://kitsu.io/api/edge";

        int currPage = currentPage;
        boolean lastPage = false;

        // Loops through pages of (20) animes
        while (!lastPage) {
            String urlString = baseAPI + "/anime?page[limit]=20&page[offset]=" + currPage;

            try {
                HttpURLConnection apiConnection = fetchApiResponse(urlString);

                if (apiConnection.getResponseCode() != 200) {
                    System.out.println("\u001B[31mCould not connect to API for page: " + currPage + "\u001B[0m");
                    throw new Exception("Failed to fetch data from API");
                }

                String jsonResponse = readApiResponse(apiConnection);

                lastPage = saveResponseToDatabase(baseAPI, jsonResponse);

                currPage += 20;

                System.out.println();
                System.out.println("\u001B[32mPage: " + currPage + "\u001B[0m");
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("\u001B[32mDatabase is updated\u001B[0m");
        }

        return currPage;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String readApiResponse(HttpURLConnection apiConnection) {
        try {
            StringBuilder resultJson = new StringBuilder();

            Scanner scanner = new Scanner(apiConnection.getInputStream());

            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }

            scanner.close();

            return resultJson.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    // For each anime on the currPage, get the anime info
    // If there is no anime on the page, then it is the last page
    private boolean saveResponseToDatabase(String baseApi, String jsonResponse) throws JsonProcessingException {
        JsonNode dataArray = pathToData(jsonResponse);

        if (dataArray.isEmpty()) {
            return true;
        }

        for (int i = 0; i < dataArray.size(); i++) {
            JsonNode anime = dataArray.get(i);

            JsonNode idNode = anime.path("id");
            JsonNode attributes = anime.path("attributes");
            JsonNode avgRatingNode = attributes.path("averageRating");
            JsonNode synopsis = attributes.path("synopsis");
            JsonNode episodeCount = attributes.path("episodeCount");
            JsonNode releaseStatus = attributes.path("status");
            JsonNode releaseDate = attributes.path("startDate");
            JsonNode posterURL = attributes.path("posterImage").path("small");
            JsonNode ageRating = attributes.path("ageRating");
            JsonNode showType = attributes.path("subtype");
            JsonNode titlesNode = attributes.path("titles");

            KitsuAnime kitsuAnime = new KitsuAnime();

            kitsuAnime.setKitsuId(idNode.asInt());
            kitsuAnime.setMalId(getMalId(baseApi, idNode.asInt()));
            kitsuAnime.setAnimeTitle(getAnimeTitle(titlesNode));
            kitsuAnime.setAverageRating(avgRatingNode.asDouble());
            kitsuAnime.setSynopsis(synopsis.asText());
            kitsuAnime.setEpisodeCount(episodeCount.asInt());
            kitsuAnime.setStatus(releaseStatus.asText());
            kitsuAnime.setReleaseDate(releaseDate.asText());
            kitsuAnime.setPosterUrl(posterURL.asText());
            kitsuAnime.setAgeRating(ageRating.asText());
            kitsuAnime.setShowType(showType.asText());
            boolean animeHasGenres = addGenresToAnime(baseApi, idNode.asInt(), kitsuAnime);

            // Only save if anime has genres
            if (animeHasGenres) {
                kitsuAnimeService.saveKitsuAnime(kitsuAnime);
                System.out.println("\u001B[32mFinished data for: " + getAnimeTitle(titlesNode) + " (" + idNode.asInt() + ")\u001B[0m");
            }

            System.out.println();
        }

        return false;
    }

    private Integer getMalId(String baseApi, int kitsuId) {
        try {
            HttpURLConnection apiConnection = fetchApiResponse(baseApi + "/anime/" + kitsuId + "/mappings");

            if (apiConnection.getResponseCode() != 200) {
                System.out.println("\u001B[31mCould not connect to API for " + kitsuId + "\u001B[0m");
                return -1;
            }

            String jsonResponse = readApiResponse(apiConnection);

            JsonNode data = pathToData(jsonResponse);

            if (data.isNull() || data.isEmpty()) {
                return -1;
            }

            JsonNode malNode;
            for (int i = 0; i < data.size(); i++) {
                malNode = data.get(i);
                JsonNode attributes = malNode.path("attributes");

                // Checks it's not MAL
                if (Objects.equals(attributes.path("externalSite").asText(), "myanimelist/anime")) {
                    return attributes.path("externalId").asInt();
                }
            }

            // If malNode was not found
            return -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean addGenresToAnime(String baseApi, int kitsuId, KitsuAnime kitsuAnime) {
        try {
            HttpURLConnection apiConnection = fetchApiResponse(baseApi + "/anime/" + kitsuId + "/genres");

            if (apiConnection.getResponseCode() != 200) {
                System.out.println("\u001B[31mCould not connect to API for " + kitsuAnime.getKitsuId() + "\u001B[0m");
                return false;
            }

            String jsonResponse = readApiResponse(apiConnection);

            JsonNode data = pathToData(jsonResponse);

            if (data.isNull() || data.isEmpty()) {
                System.out.println("\u001B[31mNo genre on API for " + kitsuAnime.getKitsuId() + "\u001B[0m");
                return false;
            }

            for (int i = 0; i < data.size(); i++) {
                JsonNode elements = data.get(i);
                JsonNode genreNode = elements.path("attributes").path("name");
                String genreName = genreNode.asText();

                Genres genre = kitsuAnimeService.findByGenreName(genreName);

                if (kitsuAnimeService.findByGenreName(genreName) == null) {
                    genre = new Genres();
                    genre.setGenreName(genreName);
                    kitsuAnimeService.saveGenre(genre);
                }

                if (isValidAnime(kitsuAnime)) {
                    kitsuAnime.addGenre(genre);
                } else {
                    return false;
                }
            }

            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JsonNode pathToData(String jsonResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonRootNode = objectMapper.readTree(jsonResponse);
        return jsonRootNode.path("data");
    }

    private String getAnimeTitle(JsonNode titleNode) {
        String animeTitle = "No title";

        // Get the titles from fields and add it to a keys list
        // e.g. "en": "Cowboy Bebop" will get "Cowboy Bebop"
        List<String> titles = new ArrayList<>();
        Iterator<Map.Entry<String, JsonNode>> iterator = titleNode.fields();
        iterator.forEachRemaining(entry -> titles.add(entry.getValue().asText()));

        for (String title : titles) {
            if (title != null && !title.trim().isEmpty() && !title.equalsIgnoreCase("null")) {
                return title;
            }
        }

        return animeTitle;
    }

    private boolean isValidAnime(KitsuAnime kitsuAnime) {
        if (kitsuAnime.getMalId() == -1) {
            System.out.println("\u001B[31m" + kitsuAnime.getKitsuId() + " does not have a valid malId\u001B[0m");
            return false;
        }

        if (Objects.equals(kitsuAnime.getAnimeTitle(), "No title")) {
            System.out.println("\u001B[31m" + kitsuAnime.getKitsuId() + " does not have a valid title\u001B[0m");
            return false;
        }

        if (Objects.equals(kitsuAnime.getShowType(), "music")) {
            System.out.println("\u001B[31m" + kitsuAnime.getKitsuId() + " does not have a valid show_type\u001B[0m");
            return false;
        }

        return true;
    }
}
