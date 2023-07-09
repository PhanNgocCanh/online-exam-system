package com.doantotnghiep.entity;

import javax.persistence.*;

@Entity
@Table(name = "TestDetail")
public class TestDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long testDetailKey;
    @ManyToOne
    @JoinColumn(name = "TestId")
    private Test test;
    @ManyToOne
    @JoinColumn(name = "QuestionId")
    private Question question;

    public long getTestDetailKey() {
        return testDetailKey;
    }

    public void setTestDetailKey(long testDetailKey) {
        this.testDetailKey = testDetailKey;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
