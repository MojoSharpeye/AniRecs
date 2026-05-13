import styles from './Personality.module.css';
import { useEffect, useRef, useState } from 'react';
import Frieren from '../assets/frieren.mp4';
import Dog from '../assets/dog.mp4';
import CounterStrike from '../assets/CSGO.mp4';
import No from '../assets/no.mp4';
import Numa from '../assets/Numa Numa.mp4';
import Surprise from '../assets/surprise.mp4';

function Personality({ personality, handleButtonClick}) {
    let videoSource;
    let personalityText;
    let descriptionText;

    switch (personality) {
        case 'FANTASY':
            videoSource = Frieren;
            personalityText = 'Fantasy Lover';
            descriptionText = "You'd like to escape this dull, world enter another more enchanting dimension";
            break;
        case 'EDGELORD/UNWELL':
            videoSource = CounterStrike;
            personalityText = 'Unwell';
            descriptionText = 'Really dude you should get some help, before you hurt someone or yourself.';
            break;
        case 'SHOUNEN':
            videoSource = No;
            personalityText = 'Shounen';
            descriptionText = 'Good luck in 3rd grade, go and study for your spelling test';
            break;
        case 'HOPELESS ROMANTIC':
            videoSource = Dog;
            personalityText = 'Hopeless Romantic';
            descriptionText = 'Wishes for the world to be better';
            break;
        case 'SOPHISTICATED TASTE':
            videoSource = Numa;
            personalityText = 'Sophisticated Taste';
            descriptionText = 'You enjoy the thought provoking stuff. Stuff that makes you think. You probably look down on people who like simpler, more mainstream anime.';
            break;
        case 'MASTER GOONER':
            videoSource = Surprise;
            personalityText = 'Master Gooner';
            descriptionText = 'Actually impressive. how the fuck';
            break;
        default:
            return null;
    }

    const [isVisible, setIsVisible] = useState(false);
    const [showText, setShowText] = useState(false);
    const videoRef = useRef(null);
    const containerRef = useRef(null);

    // Start observing visibility
    useEffect(() => {
        const observer = new IntersectionObserver(
            ([entry]) => {
                if (entry.isIntersecting) {
                    setIsVisible(true);
                } else {
                    setIsVisible(false);
                }
            },
            {
                threshold: 0.6,
            }
        );

        if (containerRef.current) {
            observer.observe(containerRef.current);
        }

        return () => {
            if (containerRef.current) observer.unobserve(containerRef.current);
        };
    }, []);

    // Handle playback and text reveal
    useEffect(() => {
        const video = videoRef.current;
        if (video) {
            if (isVisible) {
                video.play();
                const timer = setTimeout(() => setShowText(true), 5000);
                return () => clearTimeout(timer);
            } else {
                video.pause();
                video.currentTime = 0;
                setShowText(false);
            }
        }
    }, [isVisible]);

    return (
        <div className={styles.container} ref={containerRef}>
            <video
                ref={videoRef}
                src={videoSource}
                className={styles.video}
                preload="auto"
                loop
                playsInline
                disableRemotePlayback
            />

            <div className={`${styles.overlay} ${showText ? styles.overlayVisible : ''}`}>
                <h1 className={styles.personality}>{personalityText}</h1>
                <p className={styles.description}>{descriptionText}</p>
            </div>

            {showText && (
                <button
                    className={styles.overlayButton}
                    onClick={() => handleButtonClick(7)}
                >
                    ⌵
                </button>
            )}
        </div>
    );
}

export default Personality;