package com.doantotnghiep.entity;

import javax.persistence.*;

@Entity
@Table(name = "TestHistory")
public class TestHistory {
    @EmbeddedId
    private TestHistoryKey testHistoryKey;
    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "StudentId")
    private Student student;
    @ManyToOne
    @MapsId("testId")
    @JoinColumn(name = "TestId")
    private Test test;
    @ManyToOne
    @MapsId("questionId")
    @JoinColumn(name = "QuestionId")
    private Question question;
    @Column(name = "Answer")
    private long answer;

    public TestHistoryKey getTestHistoryKey() {
        return testHistoryKey;
    }

    public void setTestHistoryKey(TestHistoryKey testHistoryKey) {
        this.testHistoryKey = testHistoryKey;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public long getAnswer() {
        return answer;
    }

    public void setAnswer(long answer) {
        this.answer = answer;
    }
}
