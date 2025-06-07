import { useState } from "react";
import { useMutation } from "@tanstack/react-query"
import fantasyQuestions from "./QuestionBank/FantasyQuestionsDb";
import schoolLifeQuestions from "./QuestionBank/SchoolLifeQuestionsDb";
import { QuestionTypes, WebStates } from "./Enums";
import "./Questions.css"

function GetQuestions({ setWebState, questionType, setQuestionHistory, questionHistory }) {
  let questionBank;

  if (questionType == QuestionTypes.Fantasy) {
    questionBank = fantasyQuestions;
  } else if (questionType = QuestionTypes.SchoolLife) {
    questionBank = schoolLifeQuestions;
  }

  const [question, setQuestion] = useState(questionBank[0]);
  const [answer, setAnswer] = useState("Input answer here");
  const [questionIndex, setQuestionIndex] = useState(0);

  const questionMutation = useMutation({
    mutationFn: async ({nextQuestion, answer, questionHistory}) => {
      const res = await fetch("http://localhost:8080/quiz/question", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          nextQuestion,
          answer,
          questionHistory
        }),
      });

      return res.text();
    },

    onSuccess: (data) => {
      setQuestion(data);
    }
  })

  const submit = (event) => {
    if (event.key == "Enter") {
      const updatedHistory = [...questionHistory, { question, answer }];
      setQuestionHistory(updatedHistory);

      const nextQuestionIndex = questionIndex + 1;
      setQuestionIndex(nextQuestionIndex);

      // If its at the end of the question bank
      // hide the question and show the personality
      if (nextQuestionIndex >= questionBank.length) {
        setWebState(WebStates.Personality);
      } else {
        const nextQuestion = questionBank[nextQuestionIndex];
        
        questionMutation.mutate({ nextQuestion, answer, questionHistory: updatedHistory });
        setAnswer("");
      }
    }
  }

  const textChange = event => {
    setAnswer(event.target.value);
  }

  return(
    <div className="questions-container">
      {question}

      <input 
        className="questions-textbox"
        onChange={textChange} 
        value = {answer}
        onKeyDown={submit}
      /> 
    </div>
  );
}

export default GetQuestions
