package com.anirecs.AniRecs.service;

import com.anirecs.AniRecs.database.entities.MalAnime;
import com.anirecs.AniRecs.database.entities.Genres;
import com.anirecs.AniRecs.database.entities.MalAnimeDatabase;
import com.anirecs.AniRecs.database.service.MALAnimeService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GenreMap {
    private MALAnimeService MALAnimeService;

    public GenreMap(MALAnimeService MALAnimeService) {
        this.MALAnimeService = MALAnimeService;
    }

    public Map<String, List<Double>> genreRatingMap(List<MalAnime> malAnimeList) {
        Map<String, List<Double>> genreRatingMap = new HashMap<>();

        for (MalAnime anime : malAnimeList) {
            MalAnimeDatabase animeDetails = MALAnimeService.findByMalId(anime.getAnimeId());
            if (animeDetails != null) {
                for (Genres genre : animeDetails.getGenresSet()) {
                   String genreName = genre.getGenreName();
                   double score = anime.getScore();
                   (genreRatingMap.computeIfAbsent(genreName, (key) -> new ArrayList<>())).add(score);
                }
            }
        }

        return genreRatingMap;
    }

    // Sort genres from highest to lowest
    public List<String> sortGenres(Map<String, List<Double>> genreRatingMap) {
        // Calculate the averages of the scores (if there are less than three scores, it is not included)
        Map<String, Double> genreAvgRatingMap = calculateAverageGenreRatingMap(genreRatingMap, 3);

        // if the genre list cannot be sorted (too few scores), sort the list again without the threshold
        if (genreAvgRatingMap.isEmpty()) genreAvgRatingMap = calculateAverageGenreRatingMap(genreRatingMap, 1);

        // Sort the averages
        List<Map.Entry<String, Double>> sortedGenres = new ArrayList<>(genreAvgRatingMap.entrySet());
        sortedGenres.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        List<String> sortedGenresList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : sortedGenres) {
            sortedGenresList.add(entry.getKey());
        }

        return sortedGenresList;
    }

    private static Map<String, Double> calculateAverageGenreRatingMap (Map<String, List<Double>> genreRatingMap, int threshold) {
        Map<String, Double> genreAvgRatingMap = new HashMap<>();

        for (Map.Entry<String, List<Double>> entry : genreRatingMap.entrySet()) {
            String genre = entry.getKey();
            List<Double> ratings = entry.getValue();

            if (ratings.size() < threshold) {
                continue;
            }

            int sum = 0;
            for (Double rating : ratings) {
                sum += rating;
            }
            double average = (double) sum / ratings.size();
            genreAvgRatingMap.put(genre, average);
        }

        return genreAvgRatingMap;
    }

    public String findMostWatchedGenre(Map<String, List<Double>> genreRatingMap) {
        String mostWatchedGenre = null;
        int max = 0;
        for (Map.Entry<String, List<Double>> entry : genreRatingMap.entrySet()) {
            int count = entry.getValue().size();
            if (count > max) {
                max = count;
                mostWatchedGenre = entry.getKey();
            }
        }
        return mostWatchedGenre;
    }
}