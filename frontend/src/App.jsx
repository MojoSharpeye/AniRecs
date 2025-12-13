import { useState } from "react"
import QuestionBanktype from "./Path";
import GetQuestions from "./Questions";
import GetPersonality from "./Personality";
import GetRecommendation from "./Recommendation";
import { WebStates, WorldTypes } from "./Enums";
import "./Apps.css"
import { AppRoutes } from './Routes';
import DecorativeIframeBackground from "./DecorativeIframeBackground";

function App() {
  return (
    <>
      <DecorativeIframeBackground />
      <AppRoutes />
    </>
  );
}

export default App

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
