package com.anirecs.AniRecs.service;

import com.anirecs.AniRecs.database.entities.*;
import com.anirecs.AniRecs.database.service.MALAnimeService;
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
    private MALAnimeService MALAnimeService;

    public RecommendationService(ProfileStats profileStats, UserAnimeListService userAnimeListService,
                                 MALAnimeService MALAnimeService) {
        this.profileStats = profileStats;
        this.userAnimeListService = userAnimeListService;
        this.MALAnimeService = MALAnimeService;
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

        MalAnimeDatabase animeWithTop5GenreRec = getAnimeWithTop5Genre(profile.getTop5Genre(), userMalIds);
        recKitsuIds.add(animeWithTop5GenreRec.getMalId());

        MalAnimeDatabase mostWatchedGenreRec = getAnimeWithMostWatchedGenre(profile.getMostWatchedGenre(), userMalIds, recKitsuIds);
        recKitsuIds.add(mostWatchedGenreRec.getMalId());

        MalAnimeDatabase highestHighestRatedGenreRec = getAnimeWithHighestRatedGenre(profile.getHighestRatedGenre(), userMalIds, recKitsuIds);
        recKitsuIds.add(highestHighestRatedGenreRec.getMalId());

        MalAnimeDatabase lowestRatedGenreRec = getAnimeWithLowestRatedGenre(profile.getLowestRatedGenre(), userMalIds, recKitsuIds);
        recKitsuIds.add(lowestRatedGenreRec.getMalId());

        MalAnimeDatabase randomGenreRec = getAnimeWithRandomGenre(userMalIds, recKitsuIds);

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
    public MalAnimeDatabase getAnimeWithTop5Genre(List<String> top5Genres, Set<Integer> userList) {
        List<MalAnimeDatabase> animeListRec = MALAnimeService.findAnimesByGenreNameList(top5Genres);

        if (animeListRec == null || animeListRec.isEmpty()) {
            return null;
        }

        for (MalAnimeDatabase anime : animeListRec) {
            if (!userList.contains(anime.getMalId())) {
                return anime;
            }
        }

        return null;
    }

    // Gets the highest rated anime with the most watched genre
    public MalAnimeDatabase getAnimeWithMostWatchedGenre(String mostWatchedGenre, Set<Integer> userList, Set<Integer> recKitsuIds) {
        return findRec(mostWatchedGenre, userList, recKitsuIds);
    }

    // Gets the highest rated anime with the second-highest rated genre
    public MalAnimeDatabase getAnimeWithHighestRatedGenre(String highestRatedGenre, Set<Integer> userList, Set<Integer> recKitsuIds) {
        return findRec(highestRatedGenre, userList, recKitsuIds);
    }

    // Gets the highest rated anime with the lowest rated genre
    public MalAnimeDatabase getAnimeWithLowestRatedGenre(String lowestRatedGenre, Set<Integer> userList, Set<Integer> recKitsuIds) {
        return findRec(lowestRatedGenre, userList, recKitsuIds);
    }

    // Gets anime with a random genre
    public MalAnimeDatabase getAnimeWithRandomGenre(Set<Integer> userList, Set<Integer> recKitsuIds) {
        Random rand = new Random();
        String genre = MALAnimeService.findByGenreId(rand.nextInt(61)).getGenreName();
        return findRec(genre, userList, recKitsuIds);
    }

    private MalAnimeDatabase findRec(String genre, Set<Integer> userList, Set<Integer> recKitsuIds) {
        int i = 1;
        MalAnimeDatabase animeRec = MALAnimeService.findHighestRatedAnimeWithGenreName(genre, i);
        while (checkIfRecIsInvalid(animeRec, userList, recKitsuIds)) {
            i++;
            animeRec = MALAnimeService.findHighestRatedAnimeWithGenreName(genre, i);
        }
        return animeRec;
    }

    private boolean checkIfRecIsInvalid(MalAnimeDatabase malAnimeDataBase, Set<Integer> userList, Set<Integer> recKitsuIds) {
        Integer malId = malAnimeDataBase.getMalId();
        Integer kitsuId = malAnimeDataBase.getMalId();
        return userList.contains(malId) || recKitsuIds.contains(kitsuId);
    }
}