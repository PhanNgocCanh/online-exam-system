package com.doantotnghiep.entity;

import javax.persistence.*;

@Entity
@Table(name = "TestResult")
public class TestResult {
    @EmbeddedId
    private TestResultKey testResultKey;
    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "StudentId")
    private Student student;
    @ManyToOne
    @MapsId("testId")
    @JoinColumn(name = "TestId")
    private Test test;
    @Column(name = "Mark")
    private double mark;

    @Column(name = "TotalCorrectAnswer")
    private int totalCorrectAnswer;
    public TestResultKey getTestResultKey() {
        return testResultKey;
    }

    public void setTestResultKey(TestResultKey testResultKey) {
        this.testResultKey = testResultKey;
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

    public double getMark() {
        return mark;
    }

    public void setMark(double mark) {
        this.mark = mark;
    }

    public int getTotalCorrectAnswer() {
        return totalCorrectAnswer;
    }

    public void setTotalCorrectAnswer(int totalCorrectAnswer) {
        this.totalCorrectAnswer = totalCorrectAnswer;
    }
}
