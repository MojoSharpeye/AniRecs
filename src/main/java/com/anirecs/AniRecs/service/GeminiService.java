package com.anirecs.AniRecs.service;

import com.anirecs.AniRecs.questions.Question;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeminiService {
    private Client client;

    public GeminiService() {
        this.client = new Client();
    }

    public String generateNextQuestion(Question nextQuestion) {
        String prompt = "Player inputs: " + nextQuestion.getAnswer() + ". Pretend you are a game narrator and create a scene " +
                "with the player's input and link it into the next question: \"" + nextQuestion.getQuestion() +
                "\". Do NOT explain, summarize, or act like a chatbot. Keep descriptions brief. Follow the question as closely " +
                "as possible";
        GenerateContentResponse response = client.models.generateContent("gemini-2.0-flash-001", prompt, null);
        return response.text();
    }

    public String evaluatePersonality(List<Question> answers) {
        StringBuilder promptBuilder = new StringBuilder("Your job is to find the users personality based on their answers to the questions" +
                "There are 5 personality types, EDGELORD/UNWELL, SHOUNEN, FANTASY, HOPELESS ROMANTIC, SOPHISTICATED TASTE, and MASTER GOONER. They are as follows:" +
                "edgelordUnwellGenres = Horror, Psychological, Gore, Tragedy\n" +
                "shounenGenres = Action, Comedy, Sci-Fi, Space, Supernatural, Sports, Cars, Racing, Martial Arts, Super Power, Mecha, Samurai\n" +
                "fantasyGenres = Adventure, Magic, Fantasy, Demons, Isekai\n" +
                "hopelessRomanticGenres = Romance, Slice of Life, School, Youth, Family\n" +
                "sophisticatedTasteGenres = Drama, Mystery, Police, Thriller, Political, Mature, Law\n" +
                "goonerGenres = Ecchi, Harem, Hentai, Doujinshi" +
                "Reply ONLY what personality in the 5 personalities the user input sounds like and give a short explanation why. if the user input" +
                "doesnt give an idea of their personality just type \"ERROR\"" +
                "Here are the user's responses to the quiz:\n\n");

        for (int i = 0; i < answers.size(); i++) {
            Question q = answers.get(i);
            promptBuilder.append("Q").append(i + 1).append(": ").append(q.getQuestion()).append("\n");
            promptBuilder.append("A").append(i + 1).append(": ").append(q.getAnswer()).append("\n\n");
        }

        String prompt = promptBuilder.toString();
        System.out.println(prompt);
        GenerateContentResponse response = client.models.generateContent("gemini-2.0-flash-001", prompt, null);
        return response.text();
    }
}