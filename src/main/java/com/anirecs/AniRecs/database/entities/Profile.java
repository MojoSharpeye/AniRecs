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
    Anime highestRatedAnime;
    List<Anime> topAnime;
    String mostWatchedGenre;
    String highestRatedGenre;
    List<String> topGenre;

    public Profile(String name, String personality, int numberAnimeWatched, double hoursWatched, Anime highestRatedAnime,
                   List<Anime> topAnime, String mostWatchedGenre, String highestRatedGenre, List<String> topGenre) {
        this.name = name;
        this.personality = personality;
        this.numberAnimeWatched = numberAnimeWatched;
        this.hoursWatched = hoursWatched;
        this.highestRatedAnime = highestRatedAnime;
        this.topAnime = topAnime;
        this.highestRatedGenre = highestRatedGenre;
        this.topGenre = topGenre;
        this.mostWatchedGenre = mostWatchedGenre;
    }
}
