package com.anirecs.AniRecs.database.service;

import com.anirecs.AniRecs.database.entities.Genres;
import com.anirecs.AniRecs.database.entities.MalAnimeDatabase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

@Service
public class MalApiFullFetch {
    private static final String baseAPI = "https://api.myanimelist.net/v2/anime/ranking";

    @Value("${app.api.key}")
    private String apiKey;

    private final MALAnimeService animeService;
    private final ObjectMapper mapper = new ObjectMapper(); // parses JSON text

    public MalApiFullFetch(MALAnimeService animeService) {
        this.animeService = animeService;
    }

    // Loops through pages of (100) anime
    // The currentPage (offset) is set to 100, pattern will be (0, 100, 200, 300...)
    // Will return the next offset (return the same if theEndIsNear)
    public int getAllData(int currentPage) {
        boolean theEndIsNear = false;

        while (!theEndIsNear) {
            String url = String.format("%s?ranking_type=all&limit=100&offset=%d&fields=id,title,main_picture,synopsis,mean,status,genres,num_episodes,average_episode_duration,start_date,media_type", baseAPI, currentPage);
            try {
                HttpURLConnection apiConnection = fetchApiResponse(url);
                if (apiConnection.getResponseCode() != 200) {
                    System.out.println("\u001B[31mCould not connect to API for page: " + currentPage + "\u001B[0m");
                    System.out.println("Status Code: " + apiConnection.getResponseCode());
                    throw new Exception("Failed to fetch data from API");
                }

                // Parse info into JsonNode
                JsonNode root = mapper.readTree(apiConnection.getInputStream());
                JsonNode data = root.path("data");

                theEndIsNear = data.size() < 100;

                for (JsonNode wrapper : data) {
                    JsonNode node = wrapper.path("node");
                    MalAnimeDatabase a = toAnime(node);
                    if (!a.getGenresSet().isEmpty() && isValidAnime(a)) {
                        animeService.saveKitsuAnime(a);
                        System.out.println("Saved: " + a.getAnimeTitle() + " (" + a.getMalId() + ")");
                    }
                }

                currentPage = currentPage + 100;

                // rest for 0.5 seconds in between each fetch call (no sure if it actually affects anything)
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
                theEndIsNear = true;
                System.out.println("\u001B[32mDatabase is updated\u001B[0m");
            }
        }

        return currentPage;
    }

    // I changed the format to throw IOException so we don't have to do try & catch (prettier code)
    // Helper function that builds the fetch request
    private HttpURLConnection fetchApiResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("X-MAL-CLIENT-ID", apiKey);
        return conn;
    }

    // Helper function that builds the anime object
    private MalAnimeDatabase toAnime(JsonNode node) {
        MalAnimeDatabase anime = new MalAnimeDatabase();
        anime.setMalId(node.path("id").asInt());
        anime.setAnimeTitle(node.path("title").asText("No title"));
        anime.setAverageRating(node.path("mean").asDouble() * 10); // make MAL's 0-10 scale to 0-100 instead
        anime.setSynopsis(node.path("synopsis").asText(""));
        anime.setStatus(node.path("status").asText(""));
        anime.setPosterUrl(node.path("main_picture").path("medium").asText(""));
        anime.setEpisodeCount(node.path("num_episodes").asInt());
        anime.setAverageEpDuration(node.path("average_episode_duration").asInt());
        anime.setStartDate(node.path("start_date").asText(""));
        anime.setShowType(node.path("media_type").asText(""));

        // Literate through all genres in the genre object of said anime
        JsonNode genres = node.path("genres");
        for (JsonNode g : genres) {
            String name = g.path("name").asText();
            Genres genre = animeService.findByGenreName(name);
            if (genre == null) {
                genre = new Genres();
                genre.setGenreName(name);
                animeService.saveGenre(genre);
            }
            anime.addGenre(genre);
        }

        return anime;
    }

    // Helper function to exclude said types of "anime"
    // Add more exclusions if needed
    private boolean isValidAnime(MalAnimeDatabase anime) {
        // Exclude "music" entries
        return !Objects.equals(anime.getShowType(), "music");
    }
}