package com.anirecs.AniRecs.controller;

import com.anirecs.AniRecs.database.entities.MalAnime;
import com.anirecs.AniRecs.database.entities.Profile;
import com.anirecs.AniRecs.service.GetGenreMap;
import com.anirecs.AniRecs.service.GetProfileStats;
import com.anirecs.AniRecs.service.GetRecommendation;
import com.anirecs.AniRecs.service.GetUserAnimeListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@RestController
@RequestMapping("/api/anime")
public class RecommendationController {
    private final GetUserAnimeListService getUserAnimeListService;
    private final GetProfileStats getProfileStats;
    private final GetRecommendation getRecommendation;
    private final GetGenreMap getGenreMap;

    public RecommendationController(GetUserAnimeListService getUserAnimeListService, GetProfileStats getProfileStats,
                                    GetRecommendation getRecommendation, GetGenreMap getGenreMap) {
        this.getUserAnimeListService = getUserAnimeListService;
        this.getProfileStats = getProfileStats;
        this.getRecommendation = getRecommendation;
        this.getGenreMap = getGenreMap;
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Profile> getUserAnime(@PathVariable String username) {
        // TODO: is this the right way to deal with errors on the site?
        List<MalAnime> anime = getUserAnimeListService.createUserAnimeList(username);
        if(anime == null || anime.isEmpty()) return ResponseEntity.badRequest().build();

        Profile profile = getProfileStats.profileStats(anime, username);
        return ResponseEntity.ok(profile);
    }

//    @GetMapping("/user/{username}/recommendations")
//    public List<KitsuAnime> getUserRecommendations(@PathVariable String username) {
//        // TODO Seems stupid to have to run this all again even though, we found it on the last page. Should we just combine the routes?
//        List<Anime> anime = getUserAnimeListService.createUserAnimeList(username);
//        Map<String, List<Integer>> genreRatingMap = getGenreMap.genreRatingMap(anime);
//        List<String> sortedGenresList = getGenreMap.sortGenres(genreRatingMap);
//        List<KitsuAnime> recommendations = getRecommendation.recommendAnime(sortedGenresList.subList(0,5));
//
//        return recommendations;
//    }
}
