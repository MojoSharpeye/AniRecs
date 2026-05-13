import { useParams, useNavigate } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import { useState, useEffect, useRef } from "react";
import { fetchProfile } from "./utils.js";

import Loader from "../../Loader/Loader.jsx";
import Error from "../../Error/Error.jsx";
import Top5Genres from "./UserStatsCard/Top5Genres.jsx";
import NumAnimeStatsCard from "./UserStatsCard/NumAnimeStatsCard.jsx";
import HoursWasted from "./UserStatsCard/HoursWasted.jsx";
import FavouriteAnime from "./FavouriteAnime/FavouriteAnime.jsx";
import AverageScore from "./UserStatsCard/AverageScore.jsx";
import PersonalityReveal from "./PersonalityReveal/PersonalityReveal.jsx";

import Intro from "../../assets/MAL/intro.mp4";

import styles from "./ProfilePage.module.css";

function ProfilePage() {
  const { username } = useParams();
  const navigate = useNavigate();

  const containerRef = useRef(null);
  const sectionRefs = useRef([]);
  const sectionOrder = [
    "greeting",
    "numberAnimeWatched",
    "hoursWasted",
    "top5Genres",
    "avgScore",
    "top5Anime",
    "personalityReveal",
  ];

  const { data, error, isLoading } = useQuery({
    queryKey: ["profile", username],
    queryFn: () =>
      fetchProfile(`http://localhost:8080/api/anime/user/${username}`),
    refetchOnWindowFocus: false,
  });

  const [activeIndex, setActiveIndex] = useState(0);
  const [seenSections, setSeenSections] = useState(new Set());
  const [scrollLocked, setScrollLocked] = useState(true);

  useEffect(() => {
    function preventScroll(e) {
      const keys = [
        "ArrowUp",
        "ArrowDown",
        "ArrowLeft",
        "ArrowRight",
        "Space",
        "PageUp",
        "PageDown",
      ];
      if (
        scrollLocked &&
        (e.type === "wheel" ||
          e.type === "touchmove" ||
          (e.type === "keydown" && keys.includes(e.key)))
      ) {
        e.preventDefault();
        e.stopPropagation();
        return false;
      }
    }
    if (scrollLocked) {
      document.body.style.overflow = "hidden";
      window.addEventListener("wheel", preventScroll, { passive: false });
      window.addEventListener("touchmove", preventScroll, { passive: false });
      window.addEventListener("keydown", preventScroll, { passive: false });
    } else {
      document.body.style.overflow = "";
      window.removeEventListener("wheel", preventScroll);
      window.removeEventListener("touchmove", preventScroll);
      window.removeEventListener("keydown", preventScroll);
    }
    return () => {
      window.removeEventListener("wheel", preventScroll);
      window.removeEventListener("touchmove", preventScroll);
      window.removeEventListener("keydown", preventScroll);
    };
  }, [scrollLocked]);

  const handleButtonClick = (targetIndex) => {
    setScrollLocked(false);
    sectionRefs.current[targetIndex]?.scrollIntoView({ behavior: "smooth" });
  };

  const handleDotClick = (index) => {
    if (!scrollLocked) {
      sectionRefs.current[index]?.scrollIntoView({ behavior: "smooth" });
    }
  };

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            const idx = sectionRefs.current.findIndex(
              (ref) => ref === entry.target,
            );
            if (idx !== -1) {
              setActiveIndex(idx);
              setSeenSections((prev) => new Set(prev).add(idx));
            }
          }
        });
      },
      { threshold: 0.5 },
    );

    setTimeout(() => {
      sectionRefs.current.forEach((ref) => ref && observer.observe(ref));
    }, 0);

    return () => observer.disconnect();
  }, [sectionOrder]);

  if (isLoading) return <Loader />;
  if (error) return <Error />;

  return (
    <>
      <div ref={containerRef} className={styles.scrollContainer}>
        {sectionOrder.map((sec, idx) => (
          <div
            key={sec}
            ref={(el) => (sectionRefs.current[idx] = el)}
            className={styles.snapSection}
          >
            {sec === "personalityReveal" ? (
              <PersonalityReveal
                username={username}
                navigate={navigate}
                personalityType={data.personality}
                videoSource={Intro}
                setScrollLocked={setScrollLocked}
              />
            ) : (
              <div
                className={`${styles.fade} ${seenSections.has(idx) ? styles["fade-enter-active"] : styles["fade-enter"]}`}
              >
                {sec === "greeting" ? (
                  <div>
                    <h1 className={styles.greeting}>Hey, {data.name}!</h1>
                    <button
                      className={styles.scrollButton}
                      onClick={() => handleButtonClick(1)}
                    >
                      ⌵
                    </button>
                  </div>
                ) : (
                  renderSection(sec, data)
                )}
              </div>
            )}
          </div>
        ))}
      </div>

      {!scrollLocked && (
        <div className={styles.dots}>
          {sectionOrder.map((_, i) => (
            <span
              key={i}
              className={`${styles.dot} ${i === activeIndex ? styles.activeDot : ""}`}
              onClick={() => handleDotClick(i)}
            />
          ))}
        </div>
      )}
    </>
  );
}

const renderSection = (sec, data) => {
  switch (sec) {
    case "numberAnimeWatched":
      return <NumAnimeStatsCard numberAnimeWatched={data.numberAnimeWatched} />;
    case "hoursWasted":
      return <HoursWasted hoursWatched={data.hoursWatched} />;
    case "top5Genres":
      return <Top5Genres top5Genre={data.top5Genre} />;
    case "avgScore":
      return <AverageScore averageScore={data.avgScore} />;
    case "top5Anime":
      return <FavouriteAnime anime={data.top5MalAnime} />;
    default:
      return null;
  }
};

// function ProfilePage() {
//   const { username } = useParams();
//   const navigate = useNavigate();

//   const { data, error, isLoading } = useQuery({
//     queryKey: ["profile", username],
//     queryFn: () =>
//       fetchProfile(`http://localhost:8080/api/anime/user/${username}`),
//     refetchOnWindowFocus: false,
//   });

//   if (isLoading) return <Loader />;
//   if (error) return <Error />;

//   return (
//     <div>
//       <PersonalityReveal
//         username={username}
//         navigate={navigate}
//         personalityType={data.personality}
//         videoSource={Intro} // Replace 'Intro' with the actual video source if needed
//         setScrollLocked={() => {}} // Assuming you don't need to lock scrolling here
//       />
//     </div>
//   );
// }

export default ProfilePage;
