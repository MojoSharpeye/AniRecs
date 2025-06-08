import { useMutation } from "@tanstack/react-query"
import { useEffect } from "react";

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
        <div>
            <h2>Anime Recommendations</h2>
            <ul>
            {recMutation.data.map((anime) => (
                <li key={anime.malId}>
                <h3>{anime.animeTitle}</h3>
                <p>Rating: {anime.averageRating}</p>
                <p>Episodes: {anime.episodeCount}</p>
                <p>Status: {anime.status}</p>
                <img
                    src={anime.posterUrl}
                    alt={anime.animeTitle}
                    style={{ width: "100px", height: "auto" }}
                />
                <p>Genres: {anime.genreNames.join(", ")}</p> {/* Display genres */}
                <p>{anime.synopsis}</p>
                </li>
            ))}
            </ul>
        </div>
    );
  }

  if (recMutation.isLoading) {
    return <div>Fetching recommendations...</div>;
  }

  return null;
}

export default GetRecommendation
