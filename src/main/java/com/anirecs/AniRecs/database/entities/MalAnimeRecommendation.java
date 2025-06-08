package com.anirecs.AniRecs.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MalAnimeRecommendation {
    @Id
    private Integer malId;
    private String animeTitle;
    private double averageRating;
    private int episodeCount;
    private String status;
    private String posterUrl;
    private int averageEpDuration;
    private String showType;
    private String startDate;
    private Set<String> genreNames;
    private String synopsis;
}
