import styles from './Top5Genres.module.css'

function Top5Genres({ top5Genre }){
    return(
        <div className={styles.topGenres}>
            <h1>Your top rated genres were</h1>
            <ol className={styles.genre}>
                {top5Genre && top5Genre.map((genre, index) => (
                    <li>{genre}</li>
                ))}
            </ol>
        </div>
    );
}
export default Top5Genres;