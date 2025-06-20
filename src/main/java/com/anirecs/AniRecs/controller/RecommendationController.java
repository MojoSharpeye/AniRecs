package com.anirecs.AniRecs.controller;

import com.anirecs.AniRecs.database.entities.*;
import com.anirecs.AniRecs.service.GenreMap;
import com.anirecs.AniRecs.service.ProfileStats;
import com.anirecs.AniRecs.service.RecommendationService;
import com.anirecs.AniRecs.service.UserAnimeListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/anime")
public class RecommendationController {
    private final UserAnimeListService userAnimeListService;
    private final ProfileStats profileStats;
    private final RecommendationService recommendationService;

    public RecommendationController(UserAnimeListService userAnimeListService, ProfileStats profileStats,
                                    RecommendationService recommendationService, GenreMap genreMap) {
        this.userAnimeListService = userAnimeListService;
        this.profileStats = profileStats;
        this.recommendationService = recommendationService;
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Profile> getUserAnime(@PathVariable String username) {
        // TODO: Check if this is the right way to deal with errors on the site
        List<MalAnime> anime = userAnimeListService.createUserAnimeList(username);
        
        if(anime == null || anime.isEmpty()) return ResponseEntity.badRequest().build();

        Profile profile = profileStats.profileStats(anime, username);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/user/{username}/recommendation")
    public Recommendation getRecommendation(@PathVariable String username) {
        long startTime = System.nanoTime();

        Recommendation recommendation = recommendationService.getRecommendation(username);

        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000;
        System.out.println("Execution time for getRecommendation: " + durationInMillis + " ms");

        return recommendation;
    }

    @GetMapping("/user/precommendation")
    public List<MalAnimeRecommendation> getRecommendationNoUser(@RequestParam String personality) {
        long startTime = System.nanoTime();

        List<MalAnimeRecommendation> reccList = recommendationService.getAnimeListWithPersonality(personality);

        System.out.println(reccList);

        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000;
        System.out.println("Execution time for getRecommendation: " + durationInMillis + " ms");

        return reccList;
    }
}
