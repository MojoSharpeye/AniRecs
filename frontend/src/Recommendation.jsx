import { useMutation } from "@tanstack/react-query"
import { useEffect } from "react";
import styles from './Recommendation.module.css'

function GetRecommendation({ personalityDescription }) {
    const recMutation = useMutation({
    mutationFn: async () => {
      const personality = personalityDescription.split(" ").slice(0, 3).join(" ");
      console.log(`http://localhost:8080/api/anime/user/precommendation?personality=${encodeURIComponent(personality)}`);
      const res = await fetch(`http://localhost:8080/api/anime/user/precommendation?personality=${encodeURIComponent(personality)}`);

      return res.json();
    }
  })
  
  useEffect(() => {
    recMutation.mutate();
  }, []);

  if (recMutation.isSuccess) {
    return (
        <div className={styles.rec}>
            <h2>Anime Recommendations</h2>
            <div className={styles.recommendationList}>
            {recMutation.data.map((anime) => (
                <div key={anime.malId} className={styles.recommendationItem}>
                <h3>{anime.animeTitle}</h3>
                <p><strong>Rating:</strong> {anime.averageRating}</p>
                <p><strong>Episodes:</strong> {anime.episodeCount}</p>
                <p><strong>Status:</strong> {anime.status}</p>
                <img
                    src={anime.posterUrl}
                    alt={anime.animeTitle}
                    className={styles.animePoster}
                />
                <p><strong>Genres:</strong> {anime.genreNames.join(", ")}</p>
                <p>{anime.synopsis}</p>
                </div>
            ))}
            </div>
        </div>
    );
  }

  if (recMutation.isLoading) {
    return <div>Fetching recommendations...</div>;
  }

  return null;
}

export default GetRecommendation
