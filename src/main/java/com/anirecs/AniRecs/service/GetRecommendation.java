package com.anirecs.AniRecs.service;

import com.anirecs.AniRecs.database.entities.Genres;
import com.anirecs.AniRecs.database.entities.KitsuAnime;
import com.anirecs.AniRecs.database.entities.Recommendation;
import com.anirecs.AniRecs.database.service.KitsuAnimeService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class GetRecommendation {
}

// Get genre by genreName
// Get animes with top genres
// ideally also sort by most top genres
// sort animes by score
// give top 5 animes