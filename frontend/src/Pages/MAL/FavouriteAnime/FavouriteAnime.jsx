import styles from './FavouriteAnime.module.css';
import { useState } from 'react';
import { formatString } from '../utils';

function FavouriteAnime({ anime }) {
    const totalCards = 5;
    const favAnime = anime[0];
    const restOfAnime = anime.slice(1);

    const missingCount = totalCards - anime.length;
    const placeholders = Array.from({ length: missingCount });

    return (
        <div className={styles.container}>
            <h1>Top 5 Anime</h1>
            {/* First anime with image on the left, details on the right */}
            <div className={styles.favAnime}>
                <img src={favAnime.mainPictureUrl} alt={favAnime.title} className={styles.favAnimeImage} />
                <div className={styles.favAnimeDetails}>
                    <h3>{favAnime.title}</h3>
                    <div className={styles.favAnimeInfo}>
                        <p><strong>Score:</strong> {favAnime.score}/10</p>
                        <p><strong>Status:</strong> {formatString(favAnime.status)}</p>
                        <p><strong>Episodes Watched:</strong> {favAnime.episodesWatched}</p>
                    </div>
                </div>
            </div>

            {/* Other anime in a row layout */}
            <div className={styles.otherAnime}>
                {restOfAnime.map((animeItem, index) => (
                    <div key={index} className={styles.animeItem}>
                        <img src={animeItem.mainPictureUrl} alt={animeItem.title} className={styles.animeImage} />
                        <div className={styles.animeDetails}>
                            <h4>{animeItem.title}</h4>
                            <p><strong>Score:</strong> {animeItem.score}/10</p>
                        </div>
                    </div>
                ))}

                {placeholders.map((_, index) => (
                    <div key={`placeholder-${index}`} className={styles.animeItem}>
                        <div className={styles.placeholderCard}>?</div>
                        <div className={styles.animeDetails}>
                            <h4>Unknown</h4>
                            <p><strong>Score:</strong> ?</p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
export default FavouriteAnime;