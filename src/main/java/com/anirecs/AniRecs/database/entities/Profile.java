package com.anirecs.AniRecs.database.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Profile {
    String name;
    String personality;
    int numberAnimeWatched;
    double hoursWatched;

    MalAnime highestRatedMalAnime;
    List<MalAnime> top5MalAnime;

    String mostWatchedGenre;
    String highestRatedGenre;
    String lowestRatedGenre;
    List<String> top5Genre;
}
