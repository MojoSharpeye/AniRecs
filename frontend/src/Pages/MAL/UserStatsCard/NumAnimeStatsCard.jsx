import styles from './UserStatsCard.module.css'

function UserStatsCard({ numberAnimeWatched }){
    let message;
    if (numberAnimeWatched >= 500) {
        message = "You've watched more animes than most people will in their lifetime. Get help."
    } else if (numberAnimeWatched >= 200) {
        message = "You ever been outside before? Give it a try."
    } else if (numberAnimeWatched >= 100) {
        message = "Get out while you still can."
    } else {
        message = "Why are you here? Do you even like anime?"
    }

    return (
        <div className={styles.card}>
            <h1>You've watched <strong>{numberAnimeWatched}</strong> anime.</h1>
            <p>{message}</p>
        </div>
    );
}
export default UserStatsCard