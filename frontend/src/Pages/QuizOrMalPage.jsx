import './QuizOrMalPage.css';
import { useNavigate } from "react-router-dom";

function QuizOrMalPage() {
  const navigate = useNavigate();
  return (
    <div className="QuizOrMalPage-container">
        <button className="btn cube cube-hover" onClick={() => navigate("/quiz-paths")}>
          <div className="bg-top">
            <div className="bg-inner"></div>
          </div>
          <div className="bg-right">
            <div className="bg-inner"></div>
          </div>
          <div className="bg">
            <div className="bg-inner"></div>
          </div>
          <div className="text">QUIZ</div>
        </button>

        <div className="hover-banner">
          Take a quiz to get personalized anime recommendations!
        </div>

        <button className="btn2 cube2 cube-hover" type="button">
          <div className="bg-top">
            <div className="bg-inner"></div>
          </div>
          <div className="bg-right">
            <div className="bg-inner"></div>
          </div>
          <div className="bg">
            <div className="bg-inner"></div>
          </div>
          <div className="text">MAL</div>
        </button>

        <div className="hover-banner2">
          Enter your MAL username to get personalized anime recommendations!
        </div>

    </div>
  );
}

export default QuizOrMalPage;