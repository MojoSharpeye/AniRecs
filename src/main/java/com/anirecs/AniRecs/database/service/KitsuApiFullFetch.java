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

    public void getAllData() {
        String baseAPI = "https://kitsu.io/api/edge";

        int curPage = 0;
        boolean lastPage = false;

        // Loops through pages of (20) animes
        while (!lastPage) {
            String urlString = baseAPI + "/anime?page[limit]=20&page[offset]=" + curPage;

            try {
                HttpURLConnection apiConnection = fetchApiResponse(urlString);

                if (apiConnection.getResponseCode() != 200) {
                    System.out.println("Error: Could not connect to API");
                    return;
                }

                String jsonResponse = readApiResponse(apiConnection);

                lastPage = saveResponseToDatabase(baseAPI, jsonResponse);

                curPage += 20;

                System.out.println();
                System.out.println("\u001B[32mPage: " + curPage + "\u001B[0m");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

            // First title is the English one always (if it exists)
            String animeTitle = "No title";
            JsonNode titlesNode = attributes.path("titles");
            if (titlesNode.fields().hasNext()) {
                animeTitle = titlesNode.fields().next().getValue().asText();
            }

            KitsuAnime kitsuAnime = new KitsuAnime();

            kitsuAnime.setKitsuId(idNode.asInt());
            kitsuAnime.setMalId(getMalId(baseApi, idNode.asInt()));
            kitsuAnime.setAnimeTitle(animeTitle);
            kitsuAnime.setAverageRating(avgRatingNode.asDouble());
            kitsuAnime.setSynopsis(synopsis.asText());
            kitsuAnime.setEpisodeCount(episodeCount.asInt());
            kitsuAnime.setStatus(releaseStatus.asText());
            kitsuAnime.setReleaseDate(releaseDate.asText());
            kitsuAnime.setPosterUrl(posterURL.asText());
            kitsuAnime.setAgeRating(ageRating.asText());
            kitsuAnime.setShowType(showType.asText());
            boolean animeHasGenres = addGenresToAnime(baseApi, idNode.asInt(), kitsuAnime);

            System.out.println();
            System.out.println("\u001B[32mFinished data for: " + animeTitle + " (" + idNode.asInt() + ")\u001B[0m");

            // Only save if anime has genres
            if (animeHasGenres) {
                kitsuAnimeService.saveKitsuAnime(kitsuAnime);
            }
        }

        return false;
    }

    private Integer getMalId(String baseApi, int kitsuId) {
        try {
            HttpURLConnection apiConnection = fetchApiResponse(baseApi + "/anime/" + kitsuId + "/mappings");

            if (apiConnection.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return -1;
            }

            String jsonResponse = readApiResponse(apiConnection);

            JsonNode data = pathToData(jsonResponse);

            if (data.isNull() || data.isEmpty()) {
                System.out.println("\u001B[31mNo MAL ID found\u001B[0m");
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
            System.out.println("\u001B[31mMAL data is null (" + kitsuId + ")\u001B[0m");
            return -1;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean addGenresToAnime(String baseApi, int kitsuId, KitsuAnime kitsuAnime) {
        try {
            HttpURLConnection apiConnection = fetchApiResponse(baseApi + "/anime/" + kitsuId + "/genres");

            if (apiConnection.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return false;
            }

            String jsonResponse = readApiResponse(apiConnection);

            JsonNode data = pathToData(jsonResponse);

            if (data.isNull() || data.isEmpty()) {
                System.out.println("No genre found on API");
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

                kitsuAnime.addGenre(genre);
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
}
