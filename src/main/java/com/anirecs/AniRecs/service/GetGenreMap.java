package com.anirecs.AniRecs.service;

import com.anirecs.AniRecs.database.entities.Anime;
import com.anirecs.AniRecs.database.entities.Genres;
import com.anirecs.AniRecs.database.entities.KitsuAnime;
import com.anirecs.AniRecs.database.service.KitsuAnimeService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetGenreMap {
    private KitsuAnimeService kitsuAnimeService;

    public GetGenreMap (KitsuAnimeService kitsuAnimeService) {
        this.kitsuAnimeService = kitsuAnimeService;
    }

    public Map<String, List<Integer>> genreRatingMap(List<Anime> animeList) {
        Map<String, List<Integer>> genreRatingMap = new HashMap();

        for (Anime anime : animeList) {
            KitsuAnime animeDetails = kitsuAnimeService.findByMalId(anime.getAnimeId());
            if (animeDetails != null) {
                for (Genres genre : animeDetails.getGenresSet()) {
                   String genreName = genre.getGenreName();
                   int score = anime.getScore();
                   ((List)genreRatingMap.computeIfAbsent(genreName, (key) -> new ArrayList())).add(score);
                }
            }
        }

        return genreRatingMap;
    }

    public List<String> sortGenres(Map<String, List<Integer>> genreRatingMap) {
        // Calculate the averages of the scores (if there are less than three scores, it is not included)
        Map<String, Integer> genreAvgRatingMap = calculateAverageGenreRatingMap(genreRatingMap, 3);

        // if the genre list cannot be sorted (too few scores), sort the list again without the threshold
        if (genreAvgRatingMap.isEmpty()) genreAvgRatingMap = calculateAverageGenreRatingMap(genreRatingMap, 1);

        // Sort the averages
        List<Map.Entry<String, Integer>> sortedGenres = new ArrayList<>(genreAvgRatingMap.entrySet());
        sortedGenres.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        List<String> sortedGenresList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedGenres) {
            sortedGenresList.add(entry.getKey());
        }

        return sortedGenresList;
    }

    private static Map<String, Integer> calculateAverageGenreRatingMap (Map<String, List<Integer>> genreRatingMap, int threshold) {
        Map<String, Integer> genreAvgRatingMap = new HashMap();
        for (Map.Entry<String, List<Integer>> entry : genreRatingMap.entrySet()) {
            String genre = entry.getKey();
            List<Integer> ratings = entry.getValue();

            if (ratings.size() < threshold) {
                continue;
            }

            int sum = 0;
            for (Integer rating : ratings) {
                sum += rating;
            }
            int average = sum / ratings.size();
            genreAvgRatingMap.put(genre, average);
        }
        return genreAvgRatingMap;
    }

    public String findMostWatchedGenre(Map<String, List<Integer>> genreRatingMap) {
        String mostWatchedGenre = null;
        int max = 0;
        for (Map.Entry<String, List<Integer>> entry : genreRatingMap.entrySet()) {
            int count = entry.getValue().size();
            if (count > max) {
                max = count;
                mostWatchedGenre = entry.getKey();
            }
        }
        return mostWatchedGenre;
    }
}