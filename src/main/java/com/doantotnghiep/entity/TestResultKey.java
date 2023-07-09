package com.doantotnghiep.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TestResultKey implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "StudentId")
    private long studentId;
    @Column(name = "TestId")
    private long testId;

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

    @Override
    public int hashCode() {
        return (int)(studentId+testId);
    }

    @Override
    public boolean equals(Object obj) {
        TestResultKey testResultKey = (TestResultKey) obj;
        return this.studentId!=testResultKey.getStudentId()&&this.testId!=testResultKey.getTestId();
    }
}
