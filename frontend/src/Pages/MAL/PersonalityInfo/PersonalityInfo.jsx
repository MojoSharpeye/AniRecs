import styles from "./PersonalityInfo.module.css";

// Small character icons
import GojoSm from "../../../assets/MAL/PersonalityImgs/SmallVer/gojo.png";
import ReiSm from "../../../assets/MAL/PersonalityImgs/SmallVer/rei.png";
import RiasSm from "../../../assets/MAL/PersonalityImgs/SmallVer/rias.png";
import ThorfinnSm from "../../../assets/MAL/PersonalityImgs/SmallVer/thorfinn.png";
import YuruSm from "../../../assets/MAL/PersonalityImgs/SmallVer/kaguya.png";
import FrierenSm from "../../../assets/MAL/PersonalityImgs/SmallVer/frieren.png";

function PersonalityInfo({
  username,
  navigate,
  selectedPersonality,
  setSelectedPersonality,
}) {
  const personalities = {
    SHOUNEN: {
      name: "The Hero",
      description:
        "Energetic, optimistic, and always striving to overcome obstacles, you inspire others with your determination and bold spirit.",
      img: GojoSm,
    },
    FANTASY: {
      name: "Fantasy Lover",
      description:
        "Imaginative and thoughtful, you find wonder in new worlds and value deep connections with those around you.",
      img: FrierenSm,
    },
    "SOPHISTICATED TASTE": {
      name: "The Thinker",
      description:
        "Analytical and introspective, you seek meaning in every experience and approach life with curiosity and depth.",
      img: ThorfinnSm,
    },
    "EDGELORD/UNWELL": {
      name: "Unwell",
      description:
        "Reserved and mysterious, you often keep your feelings hidden and navigate life with quiet resilience and depth.",
      img: ReiSm,
    },
    "HOPELESS ROMANTIC": {
      name: "Hopeless Romantic",
      description:
        "Gentle and idealistic, you cherish heartfelt moments and believe in the power of love and meaningful relationships.",
      img: YuruSm,
    },
    "MASTER GOONER": {
      name: "The Gooner",
      description:
        "Confident and charismatic, you naturally take the lead and inspire those around you with your bold presence.",
      img: RiasSm,
    },
  };

  return (
    <div className={styles.container}>
      {/* Personality Name */}
      <h1 className={styles.personality}>
        {personalities[selectedPersonality].name}
      </h1>

      {/* Description */}
      <p className={styles.description}>
        {personalities[selectedPersonality].description}
      </p>

      {/* Small Image */}
      <img
        className={styles.rectangleDiv}
        src={personalities[selectedPersonality].img}
        alt={selectedPersonality}
      />

      {/* Switch personalities */}
      <p className={styles.description}>Check out the other personalities</p>
      <div className={styles.buttonContainer}>
        {Object.keys(personalities).map((type) => (
          <button
            key={type}
            className={`${styles.button} ${selectedPersonality === type ? styles.selected : ""}`}
            onClick={() => setSelectedPersonality(type)}
          >
            <img src={personalities[type].img} alt={type} />
          </button>
        ))}
      </div>

      {/* Recommendation Button */}
      <button
        className={styles.recommendationButton}
        onClick={() => navigate(`/${username}/recommendation`)}
      >
        Get your recommendation!
      </button>
    </div>
  );
}

export default PersonalityInfo;
