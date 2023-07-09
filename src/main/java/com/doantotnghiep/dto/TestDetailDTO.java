package com.doantotnghiep.dto;

public class TestDetailDTO {
    private long id;
    private long testId;
    private long questionId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    @Override
    public String toString() {
        return "TestDetailDTO{" +
                "id=" + id +
                ", testId=" + testId +
                ", questionId=" + questionId +
                '}';
    }
}
