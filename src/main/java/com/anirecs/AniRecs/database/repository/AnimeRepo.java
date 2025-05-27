package com.anirecs.AniRecs.database.repository;

import com.anirecs.AniRecs.database.entities.KitsuAnime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface AnimeRepo extends JpaRepository<KitsuAnime, Integer> {
    KitsuAnime findByMalId(int malId);

    // Find by genreSet with name, order the results by averageRating in descending order
    List<KitsuAnime> findByGenresSet_GenreNameOrderByAverageRatingDesc(String genreName, Pageable pageable);
}
