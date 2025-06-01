import { useState } from "react";
import { useMutation } from "@tanstack/react-query"
import questions from "./FantasyQuestionsDb";

function GetQuestions({ setQuestionVisible, setPersonalityVisible, setQuestionHistory, questionHistory }) {
  const [question, setQuestion] = useState("You spawn on the streets of a world full of magic and adventure, what is the first thing that you want to do?");
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

      console.log({
        nextQuestion,
        answer,
        questionHistory
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
      if (nextQuestionIndex >= questions.length) {
        setQuestionVisible(false);
        setPersonalityVisible(true);
      } else {
        const nextQuestion = questions[nextQuestionIndex];
        
        questionMutation.mutate({ nextQuestion, answer, questionHistory: updatedHistory });
        setAnswer("");
      }
    }
  }

  const textChange = event => {
    setAnswer(event.target.value);
  }

  return(
    <div>
      <input 
        onChange={textChange} 
        value = {answer}
        onKeyDown={submit}
      />
      {question}
    </div>
  );
}

export default GetQuestions
