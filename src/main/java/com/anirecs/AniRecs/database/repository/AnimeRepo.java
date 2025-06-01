package com.anirecs.AniRecs.database.repository;

import com.anirecs.AniRecs.database.entities.MalAnimeDatabase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface AnimeRepo extends JpaRepository<MalAnimeDatabase, Integer> {
    MalAnimeDatabase findByMalId(int malId);

    // Find by genreSet with name, order the results by averageRating in descending order
    List<MalAnimeDatabase> findByGenresSet_GenreNameOrderByAverageRatingDesc(String genreName, Pageable pageable);
}