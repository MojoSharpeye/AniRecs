import './FrontPage.css';
import anirecslogo from '../assets/anirecslogo.png';

function FrontPage() {
  return (
    <div className="frontpage-container">
      <img src={anirecslogo} alt="AniRecs Logo" className="frontpage-AniRecslogo" />
      <button className="start-button">
        <span className="text">START</span>
      </button>
    </div>
  );
}

export default FrontPage;