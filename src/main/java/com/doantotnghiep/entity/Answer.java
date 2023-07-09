package com.doantotnghiep.entity;

import javax.persistence.*;

@Entity
@Table(name = "Answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AnswerId")
    private long answerId;
    @ManyToOne
    @JoinColumn(name = "QuestionId")
    private Question question;
    @Column(name = "AnswerContent",columnDefinition = "TEXT")
    private String answerContent;
    @Column(name = "AnswerSymbol")
    private String answerSymbol;

    public long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(long answerId) {
        this.answerId = answerId;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
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
