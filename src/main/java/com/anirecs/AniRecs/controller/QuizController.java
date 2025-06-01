package com.anirecs.AniRecs.controller;

import com.anirecs.AniRecs.questions.Question;
import com.anirecs.AniRecs.questions.QuestionReq;
import com.anirecs.AniRecs.service.GeminiService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/quiz")
public class QuizController {
    private GeminiService geminiService;

    public QuizController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    // Frontend sends the answer and the next question so chatgpt can figure out how to link them (?)
    @PostMapping("/question")
    public String getNextQuestion(@RequestBody QuestionReq question) {
        return geminiService.generateNextQuestion(question);
    }

    // At the very end, frontend sends all the questions and their answers, ChatGPT does some magic and gives the personality.
    @PostMapping("/personality")
    public String getQuizPersonality(@RequestBody List<Question> questionsAndAnswers) {
        return geminiService.evaluatePersonality(questionsAndAnswers);
    }
}
