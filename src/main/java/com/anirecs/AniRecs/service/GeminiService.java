package com.anirecs.AniRecs.service;

import com.anirecs.AniRecs.questions.Question;
import com.anirecs.AniRecs.questions.QuestionReq;
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

    public String generateNextQuestion(QuestionReq questionReq) {
        String prompt = "You are a game narrator. Based on the player's most recent action and the upcoming question, write the next scene. " +
                "Only describe what the player is doing now. Do not add invented events or past experiences. " +
                "The new scene must directly follow this history:\n\n" +
                "Player's last answer: " + questionReq.getAnswer() + "\n" +
                "Previous question: " + questionReq.getQuestionHistory().getLast() + "\n" +
                "Next question: \"" + questionReq.getNextQuestion() + "\"\n\n" +
                "Additional instructions:\n" +
                "- Do not repeat player’s answer.\n" +
                "- Do not be overly verbose and use everyday language\n" +
                "- Transition smoothly to the next question.\n" +
                "- You are to follow the 'additional info' sentences strictly but do not include it in the question. That is for your context\n" +
                "- If the question gives instructions, follow the instructions of the question is a must\n" +
                "- There must be no jumps in location. If you want to change the location, there must be a link\n";
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
        GenerateContentResponse response = client.models.generateContent("gemini-2.0-flash-001", prompt, null);

        return response.text();
    }
}