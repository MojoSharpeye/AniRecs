package com.anirecs.AniRecs.database.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Anime {
    private int animeId;
    private String title;
    private String mainPictureUrl;
    private String status;  // e.g., "completed", "watching"
    private int score;
    private int episodesWatched;
    private int episodeDuration;
    private int numTimesRewatched;
    private double hoursWatched;
    private boolean isNSFW;

    public Anime (int animeId, String title, String mainPictureUrl, String status, int score, int episodesWatched,
                  int episodeDuration, int numTimesRewatched, boolean isNSFW) {
        this.animeId = animeId;
        this.title = title;
        this.mainPictureUrl = mainPictureUrl;
        this.status = status;
        this.score = score;
        this.episodesWatched = episodesWatched;
        this.episodeDuration = episodeDuration;
        this.numTimesRewatched = numTimesRewatched;
        this.hoursWatched = ((double) episodeDuration * (double) episodesWatched) / 3600;
        this.isNSFW = isNSFW;
    }

    // Override toString() method for easy printing
    @Override
    public String toString() {
        return String.format("%s (%d) - Status: %s | Score: %d | Episodes watched: %d", title, animeId, status, score, episodesWatched);
    }
}
