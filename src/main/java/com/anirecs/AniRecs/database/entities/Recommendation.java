package com.anirecs.AniRecs.database.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recommendation {
    Anime highestRatedGenre;
    Anime mostWatchedGenre;
    Anime secondHighestRatedGenre;
    Anime lowestRatedGenre;
    Anime randomGenre;

    public Recommendation(Anime highestRatedGenre, Anime mostWatchedGenre, Anime secondHighestRatedGenre,
                          Anime lowestRatedGenre, Anime randomGenre) {
        this.highestRatedGenre = highestRatedGenre;
        this.mostWatchedGenre = mostWatchedGenre;
        this.secondHighestRatedGenre = secondHighestRatedGenre;
        this.lowestRatedGenre = lowestRatedGenre;
        this.randomGenre = randomGenre;
    }
}
