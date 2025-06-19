package com.anirecs.AniRecs.service;

import com.anirecs.AniRecs.database.entities.MalAnime;
import com.anirecs.AniRecs.database.entities.Profile;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProfileStats {
    private GenreMap genreMap;

    public ProfileStats(GenreMap genreMap) {
        this.genreMap = genreMap;
    }

    public Profile profileStats (List<MalAnime> malAnimeList, String name) {
        Map<String, List<Double>> genreRatingMap = genreMap.genreRatingMap(malAnimeList);
        List<String> sortedGenresList = genreMap.sortGenres(genreRatingMap);

        String mostWatchedGenre = genreMap.findMostWatchedGenre(genreRatingMap);
        String personality = createPersonality(sortedGenresList);

        double totalHours = 0;
        double avgScore = 0;
        for (MalAnime anime : malAnimeList) {
            avgScore += anime.getScore();
            totalHours += anime.getHoursWatched();
        }

        avgScore = avgScore / malAnimeList.size();

        // Get the size of the sublist
        int sublistSize = Math.min(malAnimeList.size(), 5);
        int genreSublistSize = Math.min(sortedGenresList.size(), 5);

        // TODO: maybe put rewatches into account for sorting the highest rated anime
        return new Profile(
                name,
                personality,
                malAnimeList.size(),
                totalHours,
                avgScore,
                malAnimeList.getFirst(), // assuming animeList is not empty
                malAnimeList.subList(0, sublistSize), // prevent out-of-bounds
                mostWatchedGenre,
                sortedGenresList.isEmpty() ? "" : sortedGenresList.getFirst(), // avoid null if list is empty
                sortedGenresList.isEmpty() ? "" : sortedGenresList.getLast(),
                sortedGenresList.subList(0, genreSublistSize) // prevent out-of-bounds
        );
    }

    private String createPersonality (List<String> sortedGenresList) {
        Set<String> edgelordUnwellGenres = new HashSet<>(Arrays.asList(
                "Psychological", "Gore", "Survival", "CGDCT", "Otaku Culture", "Organized Crime", "Mahou Shoujo", "Idols(Female)", "Idols(Male)"));
        Set<String> shounenGenres = new HashSet<>(Arrays.asList(
                "Shounen", "Action", "Sci-Fi", "Comedy", "Gag Humor", "Parody", "Samurai", "Mecha", "Super Power", "Combat Sports", "Sports",
                "Team Sports", "Martial Arts", "Racing", "Kids"));
        Set<String> fantasyGenres = new HashSet<>(Arrays.asList(
                "Adventure", "Fantasy", "Vampire", "Mythology", "Urban Fantasy", "Reincarnation", "Isekai", "Villainess"));
        Set<String> hopelessRomanticGenres = new HashSet<>(Arrays.asList(
                "Romance", "School", "Shoujo", "Childcare", "CGDCT", "Iyashikei", "Slice of Life", "Josei", "Love Polygon", "Workplace",
                "Performing Arts", "Gourmet", "Showbiz", "Avant Garde", "Mahou Shoujo", "Love Status Quo", "Boys Love", "Girls Love","Visual Arts", "Pets"));
        Set<String> sophisticatedTasteGenres = new HashSet<>(Arrays.asList(
                "Drama", "Military", "Suspense", "Time Travel", "Historical","Seinen", "Mystery", "Medical", "Detective", "Horror", "High Stakes Game"));
        Set<String> goonerGenres = new HashSet<>(Arrays.asList(
                "Anthropomorphic", "Ecchi", "Harem", "Reverse Harem", "Crossdressing", "Magical Sex Shift", "Hentai", "Erotica"));

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