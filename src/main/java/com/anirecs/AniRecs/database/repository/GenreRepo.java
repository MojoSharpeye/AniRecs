package com.anirecs.AniRecs.database.repository;

import com.anirecs.AniRecs.database.entities.Genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepo extends JpaRepository<Genres, Integer> {
    Genres findByGenreName(String genreName);
}
