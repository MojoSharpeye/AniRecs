package com.anirecs.AniRecs.service;

import com.anirecs.AniRecs.database.entities.MalAnime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

@Service
public class UserAnimeListService {
    // Creates a list of all watched anime
    public List<MalAnime> createUserAnimeList(String username) {
        List<MalAnime> animeList = new ArrayList<>();
        String nextPageUrl = "https://api.myanimelist.net/v2/users/" + username +
                "/animelist?limit=1000&sort=list_score&fields=list_status%7Bnum_times_rewatched%7D%2Caverage_episode_duration&nsfw=true";

        while (nextPageUrl != null) {
            JSONObject resultsJSONObj = getUserAnimeObject(nextPageUrl);
            JSONArray userAnimeList = getUserAnimeList(resultsJSONObj);
            if (userAnimeList == null) return null;

            // Convert the JSONArray, into an AnimeList for ease of use.
            for (Object obj : userAnimeList) {
                JSONObject anime = (JSONObject) obj;
                JSONObject animeNode = (JSONObject) anime.get("node");
                JSONObject animeStatus = (JSONObject) anime.get("list_status");

                // Extract the data from the JSON
                int score = ((Long) animeStatus.get("score")).intValue();
                if (score == 0) continue; // skip invalid scores

                boolean isNSFW = false;
                if ("gray".equals(animeNode.get("nsfw")) || "black".equals(animeNode.get("nsfw"))) {
                    isNSFW = true;
                }

                JSONObject mainPicture = (JSONObject) animeNode.get("main_picture");
                String largePictureUrl = (String) mainPicture.get("large");

                int animeId = ((Long) animeNode.get("id")).intValue(); // convert long to int
                String title = (String) animeNode.get("title");
                String statusString = (String) animeStatus.get("status");  // Completed, Watching, etc.
                int episodesWatched = ((Long) animeStatus.get("num_episodes_watched")).intValue();  // convert long to int
                int episodeDuration = ((Long) animeNode.get("average_episode_duration")).intValue();
                int numTimesRewatched = ((Long) animeStatus.get("num_times_rewatched")).intValue();

                // Create an Anime object, set the values and add it to the animeList.
                MalAnime malAnimeObject = new MalAnime(animeId, title, largePictureUrl, statusString, score,
                        episodesWatched, episodeDuration, numTimesRewatched, isNSFW);

                animeList.add(malAnimeObject);
            }
            if (animeList.isEmpty()) return null;
            //if there is a next page then update the URL and fetch again
            nextPageUrl = getNextPageUrl(resultsJSONObj);
        }

        return animeList;
    }

    private JSONObject getUserAnimeObject(String urlString) {
        try {
            HttpURLConnection apiConnection = fetchApiResponse(urlString);

            if(apiConnection.getResponseCode() != 200) return null;

            String jsonResponse = readApiResponse(apiConnection);

            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);

            return resultsJsonObj;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private String getNextPageUrl(JSONObject resultsJsonObj){
        JSONObject pagingObj = (JSONObject) resultsJsonObj.get("paging");

        if (pagingObj == null) return null;
        return (String) pagingObj.get("next");
    }

    private JSONArray getUserAnimeList (JSONObject resultsJsonObj) {
        return (JSONArray) resultsJsonObj.get("data");
    }

    @Value("${app.api.key}")
    private String apiKey;

    private HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URI uri = new URI(urlString);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-MAL-CLIENT-ID", apiKey);

            return conn;
        } catch(java.io.IOException | URISyntaxException e){
            e.printStackTrace();
        }

        return null;
    }

    private static String readApiResponse(HttpURLConnection apiConnection) {
        try {
            StringBuilder resultJson = new StringBuilder();

            Scanner scanner = new Scanner(apiConnection.getInputStream());

            while (scanner.hasNext()){
                resultJson.append(scanner.nextLine());
            }
            scanner.close();
            return resultJson.toString();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
