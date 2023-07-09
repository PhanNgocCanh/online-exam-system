package com.doantotnghiep.dto;

import java.util.ArrayList;
import java.util.List;

public class QuestionAnswerDTO {
    private long questionId;
    private String questionContent;
    private List<AnswerDTO> answerDTOs = new ArrayList<>();
    private long correctAnswer;
    private long answerSelected;
    private String answerExplain;
    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public List<AnswerDTO> getAnswerDTOs() {
        return answerDTOs;
    }

    public void setAnswerDTOs(List<AnswerDTO> answerDTOs) {
        this.answerDTOs = answerDTOs;
    }

    public long getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(long correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public long getAnswerSelected() {
        return answerSelected;
    }

    public void setAnswerSelected(long answerSelected) {
        this.answerSelected = answerSelected;
    }

    public String getAnswerExplain() {
        return answerExplain;
    }

    public void setAnswerExplain(String answerExplain) {
        this.answerExplain = answerExplain;
    }
}
