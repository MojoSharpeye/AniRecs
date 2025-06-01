package com.anirecs.AniRecs.questions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Question {
    private String question;
    private String answer;

    public Question(String question) {
        this.question = question;
    }
}