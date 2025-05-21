package com.anirecs.AniRecs.database.repository;

import com.anirecs.AniRecs.database.entities.KitsuAnime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepo extends JpaRepository<KitsuAnime, Long> {
    KitsuAnime findByMalId(int malId);
}
