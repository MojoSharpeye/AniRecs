import styles from './UserStatsCard.module.css'

function UserStatsCard({ hoursWatched }){
    const totalHours = Math.round(hoursWatched);

    const months = Math.floor(totalHours / (30 * 24));
    const days = Math.floor((totalHours % (30 * 24)) / 24);
    const hours = totalHours % 24;

    let preMessage;
    let postMessage;
    
    if (hoursWatched >= 20) {
        preMessage = "Ever feel like your life is slipping through your fingers?";
        postMessage = "of your life watching anime."
    } else {
        preMessage = "You're not a real anime fan."
        postMessage = "watching anime casually."
    }

    return (
        <div className={styles.card}>
            <h1>{preMessage}</h1>
            <p> 
                You've spent{' '}
                {months > 0 && `${months} month${months !== 1 ? 's' : ''}, `} 
                {days > 0 && `${days} day${days !== 1 ? 's' : ''}, `}
                {hours > 0 && `${hours} hour${hours !== 1 ? 's' : ''} `}
                {postMessage}
            </p>
        </div>
    );
}
export default UserStatsCard