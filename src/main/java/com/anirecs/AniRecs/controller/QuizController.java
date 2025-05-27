package com.anirecs.AniRecs.controller;

import com.anirecs.AniRecs.database.entities.Question;
import com.anirecs.AniRecs.service.GeminiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quiz")
public class QuizController {
    private GeminiService geminiService;

    public QuizController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    // Frontend sends the answer and the next question so chatgpt can figure out how to link them (?)
    @PostMapping("/next")
    public String getNextQuestion(@RequestBody Question nextQuestion) {
        String response = geminiService.generateNextQuestion(nextQuestion);
        return response;
    }

    // At the very end, frontend sends all the questions and their answers, ChatGPT does some magic and gives the personality.
    @PostMapping("/personality")
    public String getQuizPersonality(@RequestBody List<Question> answers) {
        String response = geminiService.evaluatePersonality(answers);
        return response;
    }
}
