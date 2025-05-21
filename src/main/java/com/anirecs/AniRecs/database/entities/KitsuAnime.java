package com.anirecs.AniRecs.database.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "anime")
public class KitsuAnime {
    @Id
    private int kitsuId;
    private Integer malId;
    private String animeTitle;
    private double averageRating;
    private int episodeCount;
    private String status;
    private String releaseDate;
    private String posterUrl;
    private String ageRating;
    private String showType;

    @Column(columnDefinition = "TEXT")
    private String synopsis;

    @ManyToMany
    @JoinTable(
            name = "anime_genres",
            joinColumns = @JoinColumn(name = "anime_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genres> genresSet = new HashSet<>();

    public void addGenre(Genres genre) {
        this.genresSet.add(genre);
        genre.getAnimeDetails().add(this);
    }

    public void removeGenre(Genres genre) {
        genre.getAnimeDetails().remove(this);
        genresSet.remove((genre));
    }
}
