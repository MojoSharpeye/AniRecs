import { useState } from "react"
import QuestionBanktype from "./Path";
import GetQuestions from "./Questions";
import GetPersonality from "./Personality";
import GetRecommendation from "./Recommendation";
import { WebStates, QuestionTypes } from "./Enums";
import "./Apps.css"

function App() {
  const [webState, setWebState] = useState(WebStates.Choices);

  const [personality, setPersonality] = useState("");
  const [questionHistory, setQuestionHistory] = useState([]);
  
  const [questionType, setQuestionType] = useState(QuestionTypes.Fantasy);
  
  return (
    <div className="apps-container">
      {webState == WebStates.Choices && (
        <QuestionBanktype
          setQuestionType={setQuestionType}
          setWebState={setWebState}
        />)
      }

      {webState == WebStates.Questions && (
        <GetQuestions 
          setWebState={setWebState}
          questionType={questionType}
          setQuestionHistory={setQuestionHistory}
          questionHistory={questionHistory}
        />)
      }

      {webState == WebStates.Personality && (
        <GetPersonality 
          setWebState={setWebState}
          setPersonality={setPersonality}
          questionHistory={questionHistory}
        />)
      }

      {webState == WebStates.Recommendation && (<GetRecommendation personalityDescription={personality}/>)}
    </div>
  )
}

export default App
