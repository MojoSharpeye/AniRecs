package com.anirecs.AniRecs.service;

import com.anirecs.AniRecs.database.entities.*;
import com.anirecs.AniRecs.database.service.KitsuAnimeService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    private ProfileStats profileStats;
    private UserAnimeListService userAnimeListService;
    private KitsuAnimeService kitsuAnimeService;

    public RecommendationService(ProfileStats profileStats, UserAnimeListService userAnimeListService,
                                 KitsuAnimeService kitsuAnimeService) {
        this.profileStats = profileStats;
        this.userAnimeListService = userAnimeListService;
        this.kitsuAnimeService = kitsuAnimeService;
    }

    public Recommendation getRecommendation(String username) {
        // Look through the database and return the anime with the top 5 genres
        // Look through the database and return the highest rated mostWatchedGenre;
        // Look through the database and return the highest rated secondHighestRatedGenre;
        // Look through the database and return the highest rated lowestRatedGenre;
        // Look through the database and return the highest rated randomGenre;

        // Gets all the malIds in the users list
        List<MalAnime> usersList = userAnimeListService.createUserAnimeList(username);
        Set<Integer> userMalIds = usersList.stream()
                .map(MalAnime::getAnimeId)
                .collect(Collectors.toSet());

        Profile profile = profileStats.profileStats(usersList, username);

        Set<Integer> recKitsuIds = new HashSet<>();

        KitsuAnime animeWithTop5GenreRec = getAnimeWithTop5Genre(profile.getTop5Genre(), userMalIds);
        recKitsuIds.add(animeWithTop5GenreRec.getKitsuId());

        KitsuAnime mostWatchedGenreRec = getAnimeWithMostWatchedGenre(profile.getMostWatchedGenre(), userMalIds, recKitsuIds);
        recKitsuIds.add(mostWatchedGenreRec.getKitsuId());

        KitsuAnime highestHighestRatedGenreRec = getAnimeWithHighestRatedGenre(profile.getHighestRatedGenre(), userMalIds, recKitsuIds);
        recKitsuIds.add(highestHighestRatedGenreRec.getKitsuId());

        KitsuAnime lowestRatedGenreRec = getAnimeWithLowestRatedGenre(profile.getLowestRatedGenre(), userMalIds, recKitsuIds);
        recKitsuIds.add(lowestRatedGenreRec.getKitsuId());

        KitsuAnime randomGenreRec = getAnimeWithRandomGenre(userMalIds, recKitsuIds);

        return new Recommendation(
            animeWithTop5GenreRec.getAnimeTitle(),
            mostWatchedGenreRec.getAnimeTitle(),
            highestHighestRatedGenreRec.getAnimeTitle(),
            lowestRatedGenreRec.getAnimeTitle(),
            randomGenreRec.getAnimeTitle()
        );
    }

    // Gets the highest rated anime that matches with users top 5 genres
    // If anime rating is below 8, then uses only 4 genres
    // Continues until an anime rating is above 8
    public KitsuAnime getAnimeWithTop5Genre(List<String> top5Genres, Set<Integer> userList) {
        List<KitsuAnime> animeListRec = kitsuAnimeService.findAnimesByGenreNameList(top5Genres);

        if (animeListRec == null || animeListRec.isEmpty()) {
            return null;
        }

        for (KitsuAnime anime : animeListRec) {
            if (!userList.contains(anime.getMalId())) {
                return anime;
            }
        }

        return null;
    }

    // Gets the highest rated anime with the most watched genre
    public KitsuAnime getAnimeWithMostWatchedGenre(String mostWatchedGenre, Set<Integer> userList, Set<Integer> recKitsuIds) {
        return findRec(mostWatchedGenre, userList, recKitsuIds);
    }

    // Gets the highest rated anime with the second-highest rated genre
    public KitsuAnime getAnimeWithHighestRatedGenre(String highestRatedGenre, Set<Integer> userList, Set<Integer> recKitsuIds) {
        return findRec(highestRatedGenre, userList, recKitsuIds);
    }

    // Gets the highest rated anime with the lowest rated genre
    public KitsuAnime getAnimeWithLowestRatedGenre(String lowestRatedGenre, Set<Integer> userList, Set<Integer> recKitsuIds) {
        return findRec(lowestRatedGenre, userList, recKitsuIds);
    }

    // Gets anime with a random genre
    public KitsuAnime getAnimeWithRandomGenre(Set<Integer> userList, Set<Integer> recKitsuIds) {
        Random rand = new Random();
        String genre = kitsuAnimeService.findByGenreId(rand.nextInt(61)).getGenreName();
        return findRec(genre, userList, recKitsuIds);
    }

    private KitsuAnime findRec(String genre, Set<Integer> userList, Set<Integer> recKitsuIds) {
        int i = 1;
        KitsuAnime animeRec = kitsuAnimeService.findHighestRatedAnimeWithGenreName(genre, i);
        while (checkIfRecIsInvalid(animeRec, userList, recKitsuIds)) {
            i++;
            animeRec = kitsuAnimeService.findHighestRatedAnimeWithGenreName(genre, i);
        }
        return animeRec;
    }

    private boolean checkIfRecIsInvalid(KitsuAnime kitsuAnime, Set<Integer> userList, Set<Integer> recKitsuIds) {
        Integer malId = kitsuAnime.getMalId();
        Integer kitsuId = kitsuAnime.getKitsuId();
        return userList.contains(malId) || recKitsuIds.contains(kitsuId);
    }
}