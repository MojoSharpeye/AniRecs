import { useState } from "react"
import GetQuestions from "./Questions";
import GetPersonality from "./Personality";

function App() {
  const [questionsVisible, setQuestionVisible] = useState(true);
  const [personalityVisible, setPersonalityVisible] = useState(false);
  const [questionHistory, setQuestionHistory] = useState([]);

  
  return (
    <div>
      {questionsVisible && (
        <GetQuestions 
          setQuestionVisible={setQuestionVisible}
          setPersonalityVisible={setPersonalityVisible}
          setQuestionHistory={setQuestionHistory}
          questionHistory={questionHistory}
        />)}
      {personalityVisible && <GetPersonality questionHistory={questionHistory}/>}
    </div>
  )
}

export default App
