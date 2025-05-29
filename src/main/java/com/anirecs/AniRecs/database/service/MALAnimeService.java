package com.anirecs.AniRecs.database.service;

import com.anirecs.AniRecs.database.entities.Genres;
import com.anirecs.AniRecs.database.entities.MalAnimeDatabase;
import com.anirecs.AniRecs.database.repository.AnimeRepo;
import com.anirecs.AniRecs.database.repository.GenreRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MALAnimeService {

    private AnimeRepo animeRepo;

    private GenreRepo genreRepo;

    public MALAnimeService(AnimeRepo animeRepo, GenreRepo genreRepo) {
        this.animeRepo = animeRepo;
        this.genreRepo = genreRepo;
    }

    public void saveKitsuAnime(MalAnimeDatabase malAnimeDataBase) {
        animeRepo.save(malAnimeDataBase);
    } // saving it into SQL

    public void saveGenre(Genres genre) {
        genreRepo.save(genre);
    }

    public Genres findByGenreName(String genre) {
        return genreRepo.findByGenreName(genre);
    }

    public Genres findByGenreId(int id) {
        return genreRepo.findByGenreId(id);
    }

    public List<MalAnimeDatabase> findAnimesByGenreNameList(List<String> genreNameList) {
        return genreRepo.findAnimeByGenreNameList(genreNameList);
    }

    public MalAnimeDatabase findByMalId(Integer malId) {
        return animeRepo.findByMalId(malId);
    }

    // Finds the highest (nth from the top row) rated anime with the genre name
    public MalAnimeDatabase findHighestRatedAnimeWithGenreName(String genreName, int offsetFromTop) {
        List<MalAnimeDatabase> list = animeRepo.findByGenresSet_GenreNameOrderByAverageRatingDesc(genreName, PageRequest.of(0, offsetFromTop));
        return list.isEmpty() ? null : list.getLast();    }
    }
