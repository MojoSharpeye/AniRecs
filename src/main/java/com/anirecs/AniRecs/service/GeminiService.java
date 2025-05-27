package com.anirecs.AniRecs.service;

import com.anirecs.AniRecs.database.entities.Question;
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
        String prompt =
                "Please link the user input, what they'd do in a situation and smoothly transition into the next question like a text based video game" +
                        "Reply ONLY with the a short recap of what action they took then the new question just pure text no parenthesis or chat bot text " +
                        "which transitions very smoothly and logically from the users action to the next question. You must use aspects of the next question" +
                        " The next question is: " +
                        nextQuestion.getQuestion() + "The user input is: " + nextQuestion.getAnswer();
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