import { useState } from "react"
import GetQuestions from "./Questions";
import GetPersonality from "./Personality";
import GetRecommendation from "./Recommendation";

function App() {
  const [questionsVisible, setQuestionVisible] = useState(true);
  const [personalityVisible, setPersonalityVisible] = useState(false);
  const [recommendationVisible, setRecommendationVisible] = useState(false);

  const [questionHistory, setQuestionHistory] = useState([]);

  
  return (
    <div>
      {questionsVisible && (
        <GetQuestions 
          setQuestionVisible={setQuestionVisible}
          setPersonalityVisible={setPersonalityVisible}
          setQuestionHistory={setQuestionHistory}
          questionHistory={questionHistory}
        />)
      }

      {personalityVisible && (
        <GetPersonality 
          setPersonalityVisible={setPersonalityVisible}
          setRecommendationVisible={setRecommendationVisible}
          questionHistory={questionHistory}
        />)
      }

      {recommendationVisible && (<GetRecommendation/>)}
    </div>
  )
}

export default App
