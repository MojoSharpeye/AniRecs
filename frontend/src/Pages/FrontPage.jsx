import './FrontPage.css';
import anirecslogo from '../assets/anirecslogo.png';
import { useNavigate } from "react-router-dom";

function FrontPage() {
  const navigate = useNavigate();
  return (
    <div className="frontpage-container">
      <img src={anirecslogo} alt="AniRecs Logo" className="logo" />
      <button className="start-button" onClick={() => navigate("/quiz-or-mal")}>
        <span className="text">START</span>
      </button>
      <h1 className="title">ANIRECS</h1>
      <h1 className="subtitle">PERSONALISED ANIME RECOMMENDATIONS</h1>
    </div>
  );
}

export default FrontPage;