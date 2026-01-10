import { useState } from "react";
import { useMutation } from "@tanstack/react-query"
import fantasyQuestions from "../QuestionBank/FantasyQuestionsDb";
import schoolLifeQuestions from "../QuestionBank/SchoolLifeQuestionsDb";
import cyberpunkQuestions from "../QuestionBank/CyberpunkQuestionsDb";
import { QuestionTypes, WebStates } from "../Enums";
import "./Questions.css"

function GetQuestions({ setWebState, worldTypes, setQuestionHistory, questionHistory }) {
  let questionBank;

      // if (worldTypes == WorldTypes.Fantasy) {
      //   questionBank = fantasyQuestions;
      // } else if (worldTypes == WorldTypes.SchoolLife) {
      //   questionBank = schoolLifeQuestions;
      // } else if (worldTypes == WorldTypes.Cyberpunk) {
      //   questionBank = cyberpunkQuestions;
      // }

  questionBank = schoolLifeQuestions;

  const [question, setQuestion] = useState(questionBank[0]);
  const [answer, setAnswer] = useState("");
  const [questionIndex, setQuestionIndex] = useState(0);
  const [progress, setProgress] = useState(0);

  const questionMutation = useMutation({
    mutationFn: async ({ nextQuestion, answer, questionHistory }) => {
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
      setQuestion(prev => ({
        ...prev,
        question: data
      }));
    }
  })

  const submit = (event) => {
    if ((event.key == "Enter" && answer.trim() !== "")) {
      event.preventDefault();
      handleChoice(answer);
    }
  }

  const handleChoice = (answer) => {
    const updatedHistory = [...questionHistory, { question: question.question, answer }];
    setQuestionHistory(updatedHistory);

    const nextQuestionIndex = questionIndex + 1;
    setQuestionIndex(nextQuestionIndex);

    // If its at the end of the question bank
    // hide the question and show the personality
    if (nextQuestionIndex >= questionBank.length) {
      setWebState(WebStates.Personality);
    } else {
      setQuestion(questionBank[nextQuestionIndex]);

      setProgress(nextQuestionIndex / questionBank.length * 100);
      
      const nextQuestion = questionBank[nextQuestionIndex].question;
      questionMutation.mutate({ nextQuestion, answer, questionHistory: updatedHistory });
      setAnswer("");
    }
  }

  const textBoxChange = (event) => {
    setAnswer(event.target.value);
  }

  if (questionMutation.isPending) {
    return <div>Generating next question</div>;
  }

  return(
    <>
      <div className="questions-container">
        {question.question}

        {question.type === QuestionTypes.WordedMultipleChoice && question.choices.map((choice, index) => (
          <button 
            className="worded-multiple-choice-buttons"
            key={index} 
            onClick={() => handleChoice(choice)}
          > 
            {choice} 
          </button>
        ))}

        <div className="scale-multiple-choice-button-container">
          {question.type === QuestionTypes.ScaleMultipleChoice && (
            <>
              <div className="scale-multiple-choice-axis">{question.leftSide}</div>

              {question.choices.map((choice, index) => (
                <button
                  key={index}
                  onClick={() => handleChoice(choice)}
                >
                </button>
              ))}

              <div className="scale-multiple-choice-axis">{question.rightSide}</div>
            </>
          )}
        </div>

        <div className="yes-or-no-button-container">
          {question.type === QuestionTypes.YesOrNo && question.choices.map((choice, index) => (
              <button 
                className="yes-or-no-button"
                key={index} 
                onClick={() => handleChoice(choice)}
              > 
                {choice} 
              </button>
          ))}
        </div>

        {question.type === QuestionTypes.OpenEnded && (
          <textarea
            className="questions-textbox"
            onChange={textBoxChange} 
            value = {answer}
            onKeyDown={submit}
            placeholder="Input Text Here"
            maxLength={280}
          /> 
        )}
      </div>

      <div className="progress-bar">
        <div className="progress-bar-fill" style={{ width: `${progress}%` }}></div>
      </div>
    </>
  );
}

export default GetQuestions
