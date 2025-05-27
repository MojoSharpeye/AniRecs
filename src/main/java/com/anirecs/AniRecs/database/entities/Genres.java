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
@Table(name = "genre")
public class Genres {
    @Id
    @GeneratedValue
    private int genreId;
    private String genreName;

    @ManyToMany(mappedBy = "genresSet")
    private Set<KitsuAnime> animeDetails = new HashSet<>();
}
