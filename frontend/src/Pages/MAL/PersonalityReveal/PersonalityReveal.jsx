import { useState, useRef, useEffect, useMemo } from "react";
import { motion, AnimatePresence } from "framer-motion";
import PersonalityInfo from "../PersonalityInfo/PersonalityInfo.jsx";
import styles from "./PersonalityReveal.module.css";

import Intro from "../../../assets/MAL/intro.mp4";

// GIFs
import FrierenGif from "../../../assets/MAL/PersonalityGifs/freieren.png";
import GojoGif from "../../../assets/MAL/PersonalityGifs/gojo.png";
import ReiGif from "../../../assets/MAL/PersonalityGifs/rei.png";
import KaguyaGif from "../../../assets/MAL/PersonalityGifs/kaguya.png";
import RiasGif from "../../../assets/MAL/PersonalityGifs/rias.png";
import ThorfinnGif from "../../../assets/MAL/PersonalityGifs/thorfinn.png";

// Backgrounds
import GojoBg from "../../../assets/MAL/PersonalityImgs/gojo.jpg";
import ReiBg from "../../../assets/MAL/PersonalityImgs/rei.jpg";
import RiasBg from "../../../assets/MAL/PersonalityImgs/rias.jpg";
import ThorfinnBg from "../../../assets/MAL/PersonalityImgs/thorfinn.jpg";
import KaguyaBg from "../../../assets/MAL/PersonalityImgs/kaguya.png";
import FrierenBg from "../../../assets/MAL/PersonalityImgs/frieren.png";

const PERSONALITIES = {
  FANTASY: { gif: FrierenGif, bg: FrierenBg },
  SHOUNEN: { gif: GojoGif, bg: GojoBg },
  "EDGELORD/UNWELL": { gif: ReiGif, bg: ReiBg },
  "HOPELESS ROMANTIC": { gif: KaguyaGif, bg: KaguyaBg },
  "SOPHISTICATED TASTE": { gif: ThorfinnGif, bg: ThorfinnBg },
  "MASTER GOONER": { gif: RiasGif, bg: RiasBg },
};

const PHASE = {
  IDLE: "idle",
  VIDEO: "video",
  FLASH: "flash",
  CHARACTER: "character",
  INFO: "info",
};

const gifVariants = {
  silhouette: {
    filter: "brightness(0) saturate(0)",
    scale: 10,
    left: "45%",
  },

  reveal: {
    filter: [
      "brightness(0) saturate(0)",
      "brightness(0) saturate(0)",
      "brightness(1.6) saturate(1.2)",
      "brightness(1) saturate(1)",
    ],
    scale: 3.3,
    left: "45%",
  },

  info: {
    scale: 2.8,
    left: "68%",
    filter: "brightness(1) saturate(1)",
  },
};

function PersonalityReveal({
  username,
  navigate,
  personalityType,
  setScrollLocked,
}) {
  const [phase, setPhase] = useState(PHASE.IDLE);

  const [selectedPersonality, setSelectedPersonality] = useState(
    personalityType || "SHOUNEN",
  );

  const videoRef = useRef(null);

  /* sync external personality */
  useEffect(() => {
    if (personalityType) setSelectedPersonality(personalityType);
  }, [personalityType]);

  /* unlock scroll on final phase */
  useEffect(() => {
    if (phase === PHASE.INFO) setScrollLocked(false);
  }, [phase, setScrollLocked]);

  const current = useMemo(
    () => PERSONALITIES[selectedPersonality] || {},
    [selectedPersonality],
  );

  const startSequence = () => {
    setPhase(PHASE.VIDEO);
    setScrollLocked(true);

    setTimeout(() => {
      videoRef.current?.play().catch(() => {});
    }, 100);
  };

  const handleCharacterClick = () => setPhase(PHASE.INFO);

  const handleClick = () => {
    if (phase === PHASE.IDLE) startSequence();
    else if (phase === PHASE.CHARACTER) handleCharacterClick();
  };

  return (
    <div className={styles.container} onClick={handleClick}>
      {/* Hint */}
      {phase === PHASE.IDLE && (
        <div className={styles.clickHint}>click anywhere to begin</div>
      )}

      {/* VIDEO */}
      <video
        ref={videoRef}
        src={Intro}
        className={styles.fullscreenVideo}
        muted
        playsInline
        onEnded={() => phase === PHASE.VIDEO && setPhase(PHASE.FLASH)}
      />

      {/* FLASH */}
      {phase === PHASE.FLASH && (
        <motion.div
          className={styles.whiteout}
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ duration: 0.8 }}
          onAnimationComplete={() => setPhase(PHASE.CHARACTER)}
        />
      )}

      {/* BACKGROUND */}
      <AnimatePresence>
        {[PHASE.CHARACTER, PHASE.INFO].includes(phase) && current.bg && (
          <motion.div
            key={selectedPersonality}
            className={styles.background}
            initial={{ opacity: 0, scale: 1.2 }}
            animate={{ opacity: 1, scale: 1 }}
            exit={{ opacity: 0, scale: 1.2 }}
            transition={{ duration: 0.4 }}
            style={{
              position: "absolute",
              inset: 0,
              backgroundImage: `url(${current.bg})`,
              backgroundSize: "cover",
              backgroundPosition: "center",
              zIndex: 1,
            }}
          />
        )}
      </AnimatePresence>

      {/* CHARACTER */}
      {[PHASE.CHARACTER, PHASE.INFO].includes(phase) && current.gif && (
        <motion.img
          key={selectedPersonality}
          src={current.gif}
          className={styles.gif}
          variants={gifVariants}
          initial="silhouette"
          animate={phase === PHASE.CHARACTER ? "reveal" : "info"}
          transition={{
            scale: { duration: 0.7, ease: "easeInOut" },
            left: { duration: 1, ease: "easeInOut" },
            filter: {
              duration: 0.9,
              ease: "easeOut",
              delay: phase === PHASE.CHARACTER ? 1 : 0,
            },
          }}
        />
      )}

      {/* INFO */}
      {phase === PHASE.INFO && (
        <motion.div
          className={styles.infoPanel}
          initial={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
          transition={{ duration: 1 }}
        >
          <PersonalityInfo
            username={username}
            navigate={navigate}
            selectedPersonality={selectedPersonality}
            setSelectedPersonality={setSelectedPersonality}
          />
        </motion.div>
      )}
    </div>
  );
}

export default PersonalityReveal;
