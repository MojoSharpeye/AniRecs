import { useMutation } from "@tanstack/react-query"
import { useEffect } from "react";

function GetRecommendation({ personalityDescription }) {
    const recMutation = useMutation({
    mutationFn: async () => {
      const personality = personalityDescription.split(".")[0]; 
      console.log(`http://localhost:8080/api/anime/user/${encodeURIComponent(personality)}/precommendation`);
      const res = await fetch(`http://localhost:8080/api/anime/user/${encodeURIComponent(personality)}/precommendation`);

      return res.json();
    }
  })
  
  useEffect(() => {
    recMutation.mutate();
  }, []);

  if (recMutation.isSuccess) {
    return <div>Recommendation: {recMutation.data}</div>;
  }

  if (recMutation.isLoading) {
    return <div>Fetching recommendations...</div>;
  }

  return null;
}

export default GetRecommendation
