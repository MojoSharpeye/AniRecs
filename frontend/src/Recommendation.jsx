import { useMutation } from "@tanstack/react-query"
import { useEffect } from "react";

function GetRecommendation({ personality }) {
    const recMutation = useMutation({
    mutationFn: async () => {
      const res = await fetch(`http://localhost:8080/user/${personality}/recommendation`);

      return res.text();
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
