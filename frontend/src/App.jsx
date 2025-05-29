import { useState } from "react";
import { useMutation } from "@tanstack/react-query"
import questions from "./Questions";
import { useEffect } from "react";

function App() {
  const [question, setQuestion] = useState("You spawn in a world full of magic and adventure, what is the first thing that you want to do?");
  const [answer, setAnswer] = useState("Input answer here");
  const [questionIndex, setQuestionIndex] = useState(1);
  const [questionHistory, setQuestionHistory] = useState([]);

  const questionMutation = useMutation({
    mutationFn: async (question1) => {
      const res = await fetch("http://localhost:8080/quiz/question", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(question1),
      });

      return res.text();
    },

    onSuccess: (data) => {
      setQuestion(data);
    }
  })

  const personalityMutation = useMutation({
    mutationFn: async () => {
      const res = await fetch("http://localhost:8080/quiz/personality", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(questionHistory),
      });

      return res.text();
    },

    onSuccess: (data) => {
      setQuestion(data);
    }
  })

  const submit = (event) => {
    if (event.key == "Enter") {
      setQuestionHistory([...questionHistory, { question, answer }]);

      const newQuestionIndex = questionIndex + 1;
      setQuestionIndex(newQuestionIndex);
      let newQuestion = questions[questionIndex];

      if (questionIndex >= questions.length) {
        personalityMutation.mutate();
      } else {
        questionMutation.mutate({ question: newQuestion, answer })
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
        onKeyDown={submit}/>
      {question}
    </div>
  );
}

export default App
