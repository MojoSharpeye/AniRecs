import styles from './RecAnimeCard.module.css';
import { useState } from 'react';
import { formatString, cleanSynopsis } from '../utils.js';

function RecAnimeCard({ anime, globalDisabled, setGlobalDisabled }) {
  const [isFlipped, setIsFlipped] = useState(true);
  const [hasFlippedOnce, setHasFlippedOnce] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [modalLocked, setModalLocked] = useState(false);

  const handleFlip = (e) => {
    e.stopPropagation();

    if (globalDisabled) return;

    if (!hasFlippedOnce) {
        setIsFlipped(false);
        setHasFlippedOnce(true);
        setGlobalDisabled(true); // Disable all cards

        setTimeout(() => {
            setShowModal(true);
            setModalLocked(true); // lock closing temporarily

            // Unlock closing after 1s
            setTimeout(() => {
            setGlobalDisabled(false); // re-enable cards
            setModalLocked(false);
            }, 1000);
        }, 600);
    } else {
      setShowModal(true);
    }
  };

  const closeModal = () => {
    if (modalLocked) return;
    setShowModal(false);
  };

  return (
    <>
      <div
        className={styles.cardContainer}
        onClick={handleFlip}
        style={{ pointerEvents: globalDisabled ? 'none' : 'auto' }}
      >
        <div className={`${styles.cardInner} ${isFlipped ? styles.flipped : ''}`}>
          <div className={styles.cardFront}>
            <img src={anime.posterUrl} alt={anime.animeTitle} />
          </div>
          <div className={styles.cardBack} />
        </div>
      </div>

        {showModal && (
            <div className={styles.modalOverlay} onClick={() => { if (!modalLocked) closeModal();}}>
                <div className={styles.modalContent} onClick={e => e.stopPropagation()}>
                    <img src={anime.posterUrl} alt={anime.animeTitle} />
                    
                    <div className={styles.infoSection}>
                        <h3>{anime.animeTitle}</h3>
                        <div className={styles.stats}>
                            <p><strong>Score:</strong> {(anime.averageRating).toFixed(1)}</p>
                            <p><strong>Status:</strong> {formatString(anime.status)}</p>
                            <p><strong>Genres:</strong> {anime.genreNames.join(', ')}</p>
                        </div>
                        <p className={styles.synopsis}>{cleanSynopsis(anime.synopsis)}</p>
                        <button onClick={closeModal}>Close</button>
                    </div>
                </div>
            </div>
        )}
    </>
  );
}

export default RecAnimeCard;
