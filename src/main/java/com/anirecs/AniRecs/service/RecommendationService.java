package com.anirecs.AniRecs.service;

import com.anirecs.AniRecs.database.entities.*;
import com.anirecs.AniRecs.database.service.MALAnimeService;
import org.springframework.stereotype.Service;

import java.util.*;
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
        // Gets all the malIds in the users list
        List<MalAnime> usersList = userAnimeListService.createUserAnimeList(username);
        Set<Integer> userMalIds = usersList.stream()
                .map(MalAnime::getAnimeId)
                .collect(Collectors.toSet());

        Profile profile = profileStats.profileStats(usersList, username);

        Set<Integer> recMalIds = new HashSet<>();

        MalAnimeDatabase animeWithTop5GenreRec = getAnimeWithTop5Genre(profile.getTop5Genre(), userMalIds);
        recMalIds.add(animeWithTop5GenreRec.getMalId());

        MalAnimeDatabase mostWatchedGenreRec = getAnimeWithMostWatchedGenre(profile.getMostWatchedGenre(), userMalIds, recMalIds);
        recMalIds.add(mostWatchedGenreRec.getMalId());

        MalAnimeDatabase highestHighestRatedGenreRec = getAnimeWithHighestRatedGenre(profile.getHighestRatedGenre(), userMalIds, recMalIds);
        recMalIds.add(highestHighestRatedGenreRec.getMalId());

        MalAnimeDatabase lowestRatedGenreRec = getAnimeWithLowestRatedGenre(profile.getLowestRatedGenre(), userMalIds, recMalIds);
        recMalIds.add(lowestRatedGenreRec.getMalId());

        MalAnimeDatabase randomGenreRec = getAnimeWithRandomGenre(userMalIds, recMalIds);

        return new Recommendation(
            animeWithTop5GenreRec.toRecommendation(),
            mostWatchedGenreRec.toRecommendation(),
            highestHighestRatedGenreRec.toRecommendation(),
            lowestRatedGenreRec.toRecommendation(),
            randomGenreRec.toRecommendation()
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

    public List<MalAnimeRecommendation> getAnimeListWithPersonality(String personality) {
        List<MalAnimeDatabase> animeListDatabase = (MALAnimeService.findAnimesByGenreNameList(getGenresByPersonality(personality))).subList(0, 5);
        List<MalAnimeRecommendation> animeList = new ArrayList<>();
        for (MalAnimeDatabase anime: animeListDatabase){
            animeList.add(anime.toRecommendation());
        }
        return animeList;
    }

    private List<String> getGenresByPersonality(String personality) {
        String upperPersonality = personality.toUpperCase();

        if (upperPersonality.contains("EDGELORD") || upperPersonality.contains("UNWELL")) {
            return Arrays.asList("Horror", "Psychological", "Gore", "Tragedy");
        } else if (upperPersonality.contains("SHOUNEN")) {
            return Arrays.asList("Action", "Comedy", "Sci-Fi", "Space", "Supernatural", "Sports",
                    "Cars", "Racing", "Martial Arts", "Super Power", "Mecha", "Samurai");
        } else if (upperPersonality.contains("FANTASY")) {
            return Arrays.asList("Adventure", "Magic", "Fantasy", "Demons", "Isekai");
        } else if (upperPersonality.contains("HOPELESS") || upperPersonality.contains("ROMANTIC")) {
            return Arrays.asList("Romance", "Slice of Life", "School", "Youth", "Family");
        } else if (upperPersonality.contains("SOPHISTICATED") || upperPersonality.contains("TASTE")) {
            return Arrays.asList("Drama", "Mystery", "Police", "Thriller", "Political", "Mature", "Law");
        } else if (upperPersonality.contains("MASTER") || upperPersonality.contains("GOONER")) {
            return Arrays.asList("Ecchi", "Harem", "Hentai", "Doujinshi");
        } else {
            return Collections.emptyList();
        }
    }

    // Gets the highest rated anime with the most watched genre
    public MalAnimeDatabase getAnimeWithMostWatchedGenre(String mostWatchedGenre, Set<Integer> userList, Set<Integer> recMalIds) {
        return findRec(mostWatchedGenre, userList, recMalIds);
    }

    // Gets the highest rated anime with the second-highest rated genre
    public MalAnimeDatabase getAnimeWithHighestRatedGenre(String highestRatedGenre, Set<Integer> userList, Set<Integer> recMalIds) {
        return findRec(highestRatedGenre, userList, recMalIds);
    }

    // Gets the highest rated anime with the lowest rated genre
    public MalAnimeDatabase getAnimeWithLowestRatedGenre(String lowestRatedGenre, Set<Integer> userList, Set<Integer> recMalIds) {
        return findRec(lowestRatedGenre, userList, recMalIds);
    }

    // Gets anime with a random genre
    public MalAnimeDatabase getAnimeWithRandomGenre(Set<Integer> userList, Set<Integer> recMalIds) {
        Random rand = new Random();
        String genre = MALAnimeService.findByGenreId(rand.nextInt(61)).getGenreName();
        return findRec(genre, userList, recMalIds);
    }

    private MalAnimeDatabase findRec(String genre, Set<Integer> userList, Set<Integer> recMalIds) {
        int i = 1;
        MalAnimeDatabase animeRec = MALAnimeService.findHighestRatedAnimeWithGenreName(genre, i);
        while (checkIfRecIsInvalid(animeRec, userList, recMalIds)) {
            i++;
            animeRec = MALAnimeService.findHighestRatedAnimeWithGenreName(genre, i);
        }
        return animeRec;
    }

    private boolean checkIfRecIsInvalid(MalAnimeDatabase malAnimeDataBase, Set<Integer> userList, Set<Integer> recMalIds) {
        Integer malId = malAnimeDataBase.getMalId();
        //Integer kitsuId = malAnimeDataBase.getMalId(); Redundant line now
        return userList.contains(malId) || recMalIds.contains(malId);
    }
}