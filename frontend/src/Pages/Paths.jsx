import "./Paths.css";
import { useNavigate } from "react-router-dom";

function Paths() {
  const navigate = useNavigate();
  return (
    <div className="Paths-container">
      {/* <h1 className="Paths-title">Choose your world</h1> */}

      <button
        className="btnz cubez cube-hoverz"
        onClick={() => navigate("/quiz-paths")}
      >
        <div className="bg-top">
          <div className="bg-inner"></div>
        </div>
        <div className="bg-right">
          <div className="bg-inner"></div>
        </div>
        <div className="bg">
          <div className="bg-inner"></div>
        </div>
        <div className="text">FANTASY</div>
      </button>

      <button className="btnz2 cubez2 cube-hoverz" type="button">
        <div className="bg-top">
          <div className="bg-inner"></div>
        </div>
        <div className="bg-right">
          <div className="bg-inner"></div>
        </div>
        <div className="bg">
          <div className="bg-inner"></div>
        </div>
        <div className="text">SCHOOL</div>
      </button>

      <button className="btnz3 cubez3 cube-hoverz" type="button">
        <div className="bg-top">
          <div className="bg-inner"></div>
        </div>
        <div className="bg-right">
          <div className="bg-inner"></div>
        </div>
        <div className="bg">
          <div className="bg-inner"></div>
        </div>
        <div className="text">CYBERPUNK</div>
      </button>
    </div>
  );
}

export default Paths;
