import { useMutation } from "@tanstack/react-query"
import { useEffect } from "react";

function GetPersonality({ questionHistory }) {
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
  
  useEffect(() => {
    personalityMutation.mutate();
  }, []);

  if (personalityMutation.isSuccess) {
    return <div>Personality: {personalityMutation.data}</div>;
  }

  return( <div>Updating personality...</div>);
}

export default GetPersonality
