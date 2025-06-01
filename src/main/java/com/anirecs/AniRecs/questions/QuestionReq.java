package com.anirecs.AniRecs.questions;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class QuestionReq {
    private String nextQuestion;
    private String answer;
    private List<Question> questionHistory;
}