package com.example.trivia.model;

import androidx.annotation.NonNull;

public class Question {
    private String questionText;
    private boolean answer;

    public Question() {
    }

    public Question(String questionText, boolean answer) {
        this.questionText = questionText;
        this.answer = answer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public boolean getAnswer() {
        return answer;
    }


    @NonNull
    @Override
    public String toString() {
        return "Question = " + this.questionText + "\nAnswer = " + this.answer;
    }
}
