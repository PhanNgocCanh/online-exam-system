package com.doantotnghiep.dto;

public class AnswerDTO {
    private long answerId;
    private long questionId;
    private String answerContent;
    private String answerSymbol;

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public void setAnswerContent(String answerContent) {
        this.answerContent = answerContent;
    }

    public String getAnswerSymbol() {
        return answerSymbol;
    }

    public void setAnswerSymbol(String answerSymbol) {
        this.answerSymbol = answerSymbol;
    }
}
