import styles from './UserStatsCard.module.css'

function AverageScore({ averageScore }) {
    let message;
    if (averageScore >= 8) {
        message = "Do you have anything you don't like?"
    } else if (averageScore >= 5) {
        message = "You don't really lean to any side"
    } else if (averageScore >= 2) {
        message = "Wow you're a tough critic."
    } else {
        message = "Just stop, anime ain't for you."
    }

    return (
        <div className={styles.card}>
            <h1>On average, you rated anime a <strong>{averageScore.toFixed(1)}</strong> out of 10.</h1>
            <p>{message}</p>
        </div>
    );
}
export default AverageScore;