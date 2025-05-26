package com.anirecs.AniRecs.database.repository;

import com.anirecs.AniRecs.database.entities.Genres;
import com.anirecs.AniRecs.database.entities.KitsuAnime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreRepo extends JpaRepository<Genres, Integer> {
    Genres findByGenreName(String genreName);
    Genres findByGenreId(int id);

    @Query(
            "SELECT anime FROM KitsuAnime anime JOIN anime.genresSet genre " +
            "WHERE genre.genreName in :genreNames AND anime.averageRating >= 80 " +
            "GROUP BY anime.id " +
            "ORDER BY COUNT(DISTINCT genre.genreName) DESC, anime.averageRating DESC"
    )
    List<KitsuAnime> findAnimeByGenreNameList(@Param("genreNames") List<String> genreNames);
}
