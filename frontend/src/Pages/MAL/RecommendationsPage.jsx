import { useParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import styles from "./RecommendationsPage.module.css";
import Loader from "../../Loader/Loader.jsx";
import Error from "../../Error/Error.jsx";
import { formatString, fetchProfile, cleanSynopsis } from "./utils.js";
import RecAnimeCard from "./RecAnimeCard/RecAnimeCard.jsx";
import { useState } from "react";

function RecommendationsPage() {
  const { username } = useParams();
  const [globalDisabled, setGlobalDisabled] = useState(false);

  const { data, error, isLoading } = useQuery({
    queryKey: ["profile", username],
    queryFn: () =>
      fetchProfile(
        `http://localhost:8080/api/anime/user/${username}/recommendation`,
      ),
    refetchOnWindowFocus: false,
  });

  // Some voodoo. Refetches entire recommendation again. TOO BAD! If you can watch 8000 anime, you can wait 5 seconds for a new recommendation.
  const { data: randomData, refetch: refetchRandom } = useQuery({
    queryKey: ["profileRandom", username],
    queryFn: () =>
      fetchProfile(
        `http://localhost:8080/api/anime/user/${username}/recommendation`,
      ),
    refetchOnWindowFocus: false,
    enabled: false,
  });

  if (isLoading || !data?.randomGenreAnime) {
    return <Loader />;
  }
  if (error) {
    return <Error />;
  }

  return (
    <>
      <h3 className={styles.heading}>Your Recommendations</h3>
      <div className={styles.list}>
        <RecAnimeCard
          anime={data.top5GenreAnime}
          globalDisabled={globalDisabled}
          setGlobalDisabled={setGlobalDisabled}
        />
        <RecAnimeCard
          anime={data.highestRatedGenreAnime}
          globalDisabled={globalDisabled}
          setGlobalDisabled={setGlobalDisabled}
        />
        <RecAnimeCard
          anime={data.lowestRatedGenreAnime}
          globalDisabled={globalDisabled}
          setGlobalDisabled={setGlobalDisabled}
        />
        <RecAnimeCard
          anime={data.mostWatchedGenreAnime}
          globalDisabled={globalDisabled}
          setGlobalDisabled={setGlobalDisabled}
        />
        <div>
          <RecAnimeCard
            anime={randomData?.randomGenreAnime || data.randomGenreAnime}
            globalDisabled={globalDisabled}
            setGlobalDisabled={setGlobalDisabled}
          />
          <button onClick={refetchRandom}>Refresh</button>
        </div>
      </div>
      <p className={styles.prompt}> ⏴ Click the cards to reveal ⏵ </p>
    </>
  );
}

export default RecommendationsPage;
