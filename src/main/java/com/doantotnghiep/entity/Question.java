package com.doantotnghiep.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QuestionId")
    private long questionId;
    @ManyToOne
    @JoinColumn(name = "ChapterId")
    private Chapter chapter;
    @Column(name = "QuestionContent", columnDefinition = "TEXT")
    private String questionContent;
    @Column(name = "FileName")
    private String fileName;
    @Column(name = "CorrectAnswer")
    private long correctAnswer;
    @Column(name = "AnswerLevel")
    private String level;
    @Column(name = "QuestionExplain", columnDefinition = "TEXT")
    private String explain;
    @Column(name = "CreateBy")
    private String createBy;
    @Column(name = "CreateAt")
    private Date createAt;
    @OneToMany(mappedBy = "question")
    List<TestHistory> testHistories = new ArrayList<>();
    @OneToMany(mappedBy = "question")
    private List<TestDetail> testDetails = new ArrayList<>();
    @OneToMany(mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();

    public long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(long questionId) {
        this.questionId = questionId;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(long correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
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

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
