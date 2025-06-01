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
                "Gore", "CGDCT", "Mahou Shoujo", "Organized Crime", "Otaku Culture", "Psychological", "Delinquents", "High Stakes Game"));
        Set<String> shounenGenres = new HashSet<>(Arrays.asList(
                "Action", "Comedy", "Sci-Fi", "Sports", "Combat Sports", "Gag Humor", "Martial Arts", "Mecha",
                "Parody", "Racing", "Super Power", "Team Sports", "Samurai", "Shounen"));
        Set<String> fantasyGenres = new HashSet<>(Arrays.asList(
                "Adventure", "Fantasy", "Isekai", "Reincarnation", "Vampire", "Historical", "Mythology"));
        Set<String> hopelessRomanticGenres = new HashSet<>(Arrays.asList(
                "Boys Love", "Girls Love", "Romance", "Slice of Life", "Childcare", "Love Polygon", "Love Status Quo", "Iyashikei",
                "Mahou Shoujo", "Pets", "School", "Josei", "Shoujo", "Idols (Female)", "Idols (Male)"));
        Set<String> sophisticatedTasteGenres = new HashSet<>(Arrays.asList(
                "Avant Garde", "Drama", "Horror", "Mystery", "Suspense", "Detective", "Educational", "Medical", "Seinen", "Time Travel", "Historical", "Psychological"));
        Set<String> goonerGenres = new HashSet<>(Arrays.asList(
                "Ecchi", "Erotica", "Hentai", "Anthropomorphic", "Harem", "Magical Sex Shift", "Reverse Harem"));

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