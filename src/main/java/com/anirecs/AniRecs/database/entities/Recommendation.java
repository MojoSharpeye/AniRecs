package com.anirecs.AniRecs.database.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class Recommendation {
    String animeWithTop5Genre;
    String mostWatchedGenre;
    String highestRatedGenre;
    String lowestRatedGenre;
    String randomGenre;
}
