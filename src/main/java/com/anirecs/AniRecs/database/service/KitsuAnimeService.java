package com.anirecs.AniRecs.database.service;

import com.anirecs.AniRecs.database.entities.Genres;
import com.anirecs.AniRecs.database.entities.KitsuAnime;
import com.anirecs.AniRecs.database.repository.AnimeRepo;
import com.anirecs.AniRecs.database.repository.GenreRepo;
import org.springframework.stereotype.Service;

@Service
public class KitsuAnimeService {

    private AnimeRepo animeRepo;

    private GenreRepo genreRepo;

    public KitsuAnimeService(AnimeRepo animeRepo, GenreRepo genreRepo) {
        this.animeRepo = animeRepo;
        this.genreRepo = genreRepo;
    }

    public void saveKitsuAnime(KitsuAnime kitsuAnime) {
        animeRepo.save(kitsuAnime);
    }

    public void saveGenre(Genres genre) {
        genreRepo.save(genre);
    }

    public Genres findByGenreName(String genre) {
        return genreRepo.findByGenreName(genre);
    }

    public KitsuAnime findByMalId(Integer malId) {
        return animeRepo.findByMalId(malId);
    }
}
