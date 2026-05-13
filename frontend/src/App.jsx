import { useState } from "react";
import QuestionBanktype from "./Path";
import GetQuestions from "./Pages/Questions";
import GetPersonality from "./Personality";
import GetRecommendation from "./Recommendation";
import FrontPage from "./Pages/FrontPage";
import QuizOrMalPage from "./Pages/QuizOrMalPage";
import QuizPaths from "./Pages/Paths";
import ProfilePage from "./Pages/MAL/ProfilePage";
import RecommendationsPage from "./Pages/MAL/RecommendationsPage";
import MalUsername from "./Pages/MAL/MalUsername";
import Error from "./Error/Error";
import { WebStates, WorldTypes } from "./Enums";
import "./Apps.css";
import DecorativeIframeBackground from "./DecorativeIframeBackground";
import { Routes, Route, Navigate } from "react-router-dom";

function App() {
  return (
    <>
      <DecorativeIframeBackground />
      <Routes>
        <Route path="/" element={<FrontPage />} />
        <Route path="/quiz-or-mal" element={<QuizOrMalPage />} />
        <Route path="/quiz-paths" element={<QuizPaths />} />
        <Route path="/mal-username" element={<MalUsername />} />
        <Route
          path="/:username/profile"
          element={<ProfilePage />}
          errorElement={<Error />}
        />
        <Route
          path="/:username/recommendation"
          element={<RecommendationsPage />}
          errorElement={<Error />}
        />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </>
  );
}

export default App;

// const [webState, setWebState] = useState(WebStates.Choices);

// const [personality, setPersonality] = useState("");
// const [questionHistory, setQuestionHistory] = useState([]);

// const [worldTypes, setWorldTypes] = useState(WorldTypes.Fantasy);

// return (
//   <div className="apps-container">
//     {webState === WebStates.Choices && (
//       <QuestionBanktype
//         setWorldType={setWorldTypes}
//         setWebState={setWebState}
//       />)
//     }

//     {webState === WebStates.Questions && (
//       <GetQuestions
//         setWebState={setWebState}
//         worldTypes={worldTypes}
//         setQuestionHistory={setQuestionHistory}
//         questionHistory={questionHistory}
//       />)
//     }

//     {webState === WebStates.Personality && (
//       <GetPersonality
//         setWebState={setWebState}
//         setPersonality={setPersonality}
//         questionHistory={questionHistory}
//       />)
//     }

//     {webState === WebStates.Recommendation && (<GetRecommendation personalityDescription={personality}/>)}
//   </div>
// )
