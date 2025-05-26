package com.anirecs.AniRecs.questions;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class QuizNode {
    String question;
    private Map<String, Integer> choices;
}

