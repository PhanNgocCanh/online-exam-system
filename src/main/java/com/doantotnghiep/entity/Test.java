package com.doantotnghiep.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Test")
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TestId")
    private long testId;
    @ManyToOne
    @JoinColumn(name = "SubjectId")
    private Subject subject;
    @Column(name = "TestName")
    private String testName;
    @Column(name =  "MaxScore")
    private int maxScore;
    @Column(name = "TotalSentence")
    private int totalSentence;
    @Column(name = "MaxTime")
    private int maxTime;
    @Column(name = "CreateAt")
    private Date createAt;
    @OneToMany(mappedBy = "test")
    private List<RoomDetail> roomDetails = new ArrayList<>();
    @OneToMany(mappedBy = "test")
    private List<TestResult> testResults = new ArrayList<>();
    @OneToMany(mappedBy = "test")
    private List<TestHistory> testHistories = new ArrayList<>();
    @OneToMany(mappedBy = "test")
    private List<TestDetail> testDetails = new ArrayList<>();

    public long getTestId() {
        return testId;
    }

    public void setTestId(long testId) {
        this.testId = testId;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getTotalSentence() {
        return totalSentence;
    }

    public void setTotalSentence(int totalSentence) {
        this.totalSentence = totalSentence;
    }

    public int getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public List<RoomDetail> getRoomDetails() {
        return roomDetails;
    }

    public void setRoomDetails(List<RoomDetail> roomDetails) {
        this.roomDetails = roomDetails;
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }

    public void setTestResults(List<TestResult> testResults) {
        this.testResults = testResults;
    }

    public List<TestHistory> getTestHistories() {
        return testHistories;
    }

    public void setTestHistories(List<TestHistory> testHistories) {
        this.testHistories = testHistories;
    }

    public List<TestDetail> getTestDetails() {
        return testDetails;
    }

    public void setTestDetails(List<TestDetail> testDetails) {
        this.testDetails = testDetails;
    }
}
