package com.anirecs.AniRecs.database.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recommendation {
    MalAnime highestRatedGenre;
    MalAnime mostWatchedGenre;
    MalAnime secondHighestRatedGenre;
    MalAnime lowestRatedGenre;
    MalAnime randomGenre;

    public Recommendation(MalAnime highestRatedGenre, MalAnime mostWatchedGenre, MalAnime secondHighestRatedGenre,
                          MalAnime lowestRatedGenre, MalAnime randomGenre) {
        this.highestRatedGenre = highestRatedGenre;
        this.mostWatchedGenre = mostWatchedGenre;
        this.secondHighestRatedGenre = secondHighestRatedGenre;
        this.lowestRatedGenre = lowestRatedGenre;
        this.randomGenre = randomGenre;
    }
}
