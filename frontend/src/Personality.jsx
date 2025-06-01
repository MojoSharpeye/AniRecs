import { useMutation } from "@tanstack/react-query"
import { useEffect } from "react";

function GetPersonality({ setPersonalityVisible, setRecommendationVisible, questionHistory }) {
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
    setPersonalityVisible(false);
    setRecommendationVisible(true);
  }
  
  useEffect(() => {
    personalityMutation.mutate();
  }, []);

  if (personalityMutation.isSuccess) {
    return( 
      <div>
          <div>Personality: {personalityMutation.data}</div>;
          <button onClick={(event) => handleClick(event)}>Click here for your recommendation</button>;
      </div>
    );
  }

  if (personalityMutation.isPending) {
    return <div>Updating personality...</div>;
  }

  return null;
}

export default GetPersonality
