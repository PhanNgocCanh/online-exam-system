package com.doantotnghiep.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TestHistoryKey implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "StudentId")
    private long studentId;
    @Column(name = "TestId")
    private long testId;
    @Column(name = "QuestionId")
    private long questionId;

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
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
    public int hashCode() {
        return (int)(studentId+testId+questionId);
    }

    @Override
    public boolean equals(Object obj) {
        TestHistoryKey testHistoryKey = (TestHistoryKey) obj;
        return this.studentId!=testHistoryKey.getStudentId()&&this.questionId!=testHistoryKey.getQuestionId()&&
                this.testId!=testHistoryKey.getTestId();
    }
}
