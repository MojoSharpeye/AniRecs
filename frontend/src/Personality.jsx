import { useMutation } from "@tanstack/react-query"
import { useEffect } from "react";
import { WebStates } from "./Enums";
import "./Personality.css"

function GetPersonality({ setWebState, setPersonality, questionHistory }) {
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
    }
  })

  const handleClick = (event) => {
    event.stopPropagation();
    setPersonality(personalityMutation.data);
    setWebState(WebStates.Recommendation);
  }
  
  useEffect(() => {
    personalityMutation.mutate();
  }, []);

  if (personalityMutation.isSuccess) {
    const rawData = personalityMutation.data;

    const personality = rawData.split("Personality:")[1]?.split("Explaination:")[0]?.trim();
    const explaination = rawData.split("Explaination:")[1]?.trim();

    return (
      <div className="personality-textbox">
        <h2>{personality}</h2>
        <p>{explaination}</p>
        <button onClick={(event) => handleClick(event)}>Click here for your recommendations</button>
      </div>
    );
  }

  if (personalityMutation.isPending) {
    return <div>Updating personality...</div>;
  }

  return null;
}

export default GetPersonality;
