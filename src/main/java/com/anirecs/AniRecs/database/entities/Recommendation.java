package com.anirecs.AniRecs.database.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Recommendation {
    MalAnimeRecommendation top5GenreAnime;
    MalAnimeRecommendation mostWatchedGenreAnime;
    MalAnimeRecommendation highestRatedGenreAnime;
    MalAnimeRecommendation lowestRatedGenreAnime;
    MalAnimeRecommendation randomGenreAnime;
}
