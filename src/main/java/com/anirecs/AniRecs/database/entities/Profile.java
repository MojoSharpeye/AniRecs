package com.anirecs.AniRecs.database.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Profile {
    String name;
    String personality;
    int numberAnimeWatched;
    double hoursWatched;
    MalAnime highestRatedMalAnime;
    List<MalAnime> topMalAnime;
    String mostWatchedGenre;
    String highestRatedGenre;
    List<String> topGenre;

    public Profile(String name, String personality, int numberAnimeWatched, double hoursWatched, MalAnime highestRatedMalAnime,
                   List<MalAnime> topMalAnime, String mostWatchedGenre, String highestRatedGenre, List<String> topGenre) {
        this.name = name;
        this.personality = personality;
        this.numberAnimeWatched = numberAnimeWatched;
        this.hoursWatched = hoursWatched;
        this.highestRatedMalAnime = highestRatedMalAnime;
        this.topMalAnime = topMalAnime;
        this.highestRatedGenre = highestRatedGenre;
        this.topGenre = topGenre;
        this.mostWatchedGenre = mostWatchedGenre;
    }
}
