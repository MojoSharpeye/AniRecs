package com.anirecs.AniRecs.database.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Recommendation {
//    KitsuAnime animeWithTop5Genre;
//    KitsuAnime mostWatchedGenre;
//    KitsuAnime highestRatedGenre;
//    KitsuAnime lowestRatedGenre;
//    KitsuAnime randomGenre;

    String animeWithTop5Genre;
    String mostWatchedGenre;
    String highestRatedGenre;
    String lowestRatedGenre;
    String randomGenre;
}
