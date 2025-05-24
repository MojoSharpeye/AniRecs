package com.anirecs.AniRecs.service;

import com.anirecs.AniRecs.database.entities.Anime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

@Service
public class GetUserAnimeListService {
    public List<Anime> createUserAnimeList(String username) {
        JSONArray userAnimeList = getUserAnimeJSON(username);
        if (userAnimeList == null) return null;
        if (!hasRatedAnime(userAnimeList)) return null;

        // Convert the JSONArray, into an AnimeList for ease of use.
        List<Anime> animeList = new ArrayList<>();
        for (Object obj : userAnimeList) {
            JSONObject anime = (JSONObject) obj;
            JSONObject animeNode = (JSONObject) anime.get("node");
            JSONObject animeStatus = (JSONObject) anime.get("list_status");

            // Extract the data from the JSON
            int score = ((Long) animeStatus.get("score")).intValue();
            if (score == 0) continue; // skip invalid scores

            boolean isNSFW = false;
            if ("gray".equals((String) animeNode.get("nsfw")) || "black".equals((String) animeNode.get("nsfw"))) {
                isNSFW = true;
            }

            JSONObject mainPicture = (JSONObject) animeNode.get("main_picture");
            String mediumPictureUrl = (String) mainPicture.get("medium");

            int animeId = ((Long) animeNode.get("id")).intValue(); // convert long to int
            String title = (String) animeNode.get("title");
            String statusString = (String) animeStatus.get("status");  // Completed, Watching, etc.
            int episodesWatched = ((Long) animeStatus.get("num_episodes_watched")).intValue();  // convert long to int
            int episodeDuration = ((Long) animeNode.get("average_episode_duration")).intValue();
            int numTimesRewatched = ((Long) animeStatus.get("num_times_rewatched")).intValue();

            // Create an Anime object, set the values and add it to the animeList.
            Anime animeObject = new Anime(animeId, title, mediumPictureUrl, statusString, score,
                    episodesWatched, episodeDuration, numTimesRewatched, isNSFW);

            animeList.add(animeObject);
        }

        return animeList;
    }

    private static boolean hasRatedAnime (JSONArray userAnimeList) {
        for (int i = 0; i < userAnimeList.size(); i++) {
            JSONObject anime = (JSONObject) userAnimeList.get(i);
            JSONObject animeStatus = (JSONObject) anime.get("list_status");

            long score = (long) animeStatus.get("score");
            if (score != 0) return true;
        }
        System.out.println("You have not scored anything!");
        return false;
    }

    private JSONArray getUserAnimeJSON(String username) {
        //might want to make an array, for going to the next page. limit 1000, might be limiting?
        String urlString = "https://api.myanimelist.net/v2/users/" + username + "/animelist?limit=1000&sort=list_score&fields=list_status{num_times_rewatched},average_episode_duration,nsfw=true";

        try {
            HttpURLConnection apiConnection = fetchApiResponse(urlString);

            if(apiConnection.getResponseCode() != 200) return null;

            String jsonResponse = readApiResponse(apiConnection);

            JSONParser parser = new JSONParser();
            JSONObject resultsJsonObj = (JSONObject) parser.parse(jsonResponse);

            JSONArray userAnimeList = (JSONArray) resultsJsonObj.get("data");

            if(userAnimeList.isEmpty()) return null;

            return userAnimeList;


        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Value("${app.api.key}")
    private String apiKey;

    private HttpURLConnection fetchApiResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-MAL-CLIENT-ID", apiKey);

            return conn;
        }catch(java.io.IOException e){
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
