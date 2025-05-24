package com.anirecs.AniRecs.service;

import com.anirecs.AniRecs.database.entities.MalAnime;
import com.anirecs.AniRecs.database.entities.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetProfileStats {
    private GetGenreMap getGenreMap;

    public GetProfileStats(GetGenreMap getGenreMap) {
        this.getGenreMap = getGenreMap;
    }

    public Profile profileStats (List<MalAnime> malAnimeList, String name) {
        Map<String, List<Integer>> genreRatingMap = getGenreMap.genreRatingMap(malAnimeList);
        List<String> sortedGenresList = getGenreMap.sortGenres(genreRatingMap);

        String mostWatchedGenre = getGenreMap.findMostWatchedGenre(genreRatingMap);
        String personality = createPersonality(sortedGenresList);

        double totalHours = 0;
        for (MalAnime anime : malAnimeList) {
            totalHours += anime.getHoursWatched();
        }

        // Get the size of the sublist
        int sublistSize = Math.min(malAnimeList.size(), 5);
        int genreSublistSize = Math.min(sortedGenresList.size(), 5);

        // TODO: maybe put rewatches into account for sorting the highest rated anime
        return new Profile(
                name,
                personality,
                malAnimeList.size(),
                totalHours,
                malAnimeList.get(0), // assuming animeList is not empty
                malAnimeList.subList(0, sublistSize), // prevent out-of-bounds
                mostWatchedGenre,
                sortedGenresList.isEmpty() ? "" : sortedGenresList.get(0), // avoid null if list is empty
                sortedGenresList.subList(0, genreSublistSize) // prevent out-of-bounds
        );
    }

    private String createPersonality (List<String> sortedGenresList) {
        Set<String> edgelordUnwellGenres = new HashSet<>(Arrays.asList(
                "Horror", "Psychological", "Gore", "Tragedy"));
        Set<String> shounenGenres = new HashSet<>(Arrays.asList(
                "Action", "Comedy", "Sci-Fi", "Space", "Supernatural", "Sports", "Cars", "Racing",
                "Martial Arts", "Super Power", "Mecha", "Samurai"));
        Set<String> fantasyGenres = new HashSet<>(Arrays.asList(
                "Adventure", "Magic", "Fantasy", "Demons", "Isekai"));
        Set<String> hopelessRomanticGenres = new HashSet<>(Arrays.asList(
                "Romance", "Slice of Life", "School", "Youth", "Family"));
        Set<String> sophisticatedTasteGenres = new HashSet<>(Arrays.asList(
                "Drama", "Mystery", "Police", "Thriller", "Political", "Mature", "Law"));
        Set<String> goonerGenres = new HashSet<>(Arrays.asList(
                "Ecchi", "Harem", "Hentai", "Doujinshi"));

        for (String genre : sortedGenresList) {
            if (edgelordUnwellGenres.contains(genre)) {
                // honestly you should be locked up
                return "EDGELORD/UNWELL";
            } else if (shounenGenres.contains(genre)) {
                // go to bed kid
                return "SHOUNEN";
            } else if (fantasyGenres.contains(genre)) {
                return "FANTASY";
            } else if (hopelessRomanticGenres.contains(genre)) {
                // imagine what life could be
                return "HOPELESS ROMANTIC";
            } else if (sophisticatedTasteGenres.contains(genre)) {
                return "SOPHISTICATED TASTE";
            } else if (goonerGenres.contains(genre)) {
                // that's honestly impressive
                return "MASTER GOONER";
            }
        }

        return "CANNOT COMPUTE";
    }
}