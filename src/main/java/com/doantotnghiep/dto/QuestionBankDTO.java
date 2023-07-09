package com.doantotnghiep.dto;

import com.doantotnghiep.entity.Subject;

import java.util.Date;

public class QuestionBankDTO {
    private long questionBankId;
    private int subjectId;
    private String questionBankName;
    private String createBy;
    private Date createAt;
    private String subjectName;
    private int totalChapter;

    public long getQuestionBankId() {
        return questionBankId;
    }

    public void setQuestionBankId(long questionBankId) {
        this.questionBankId = questionBankId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getQuestionBankName() {
        return questionBankName;
    }

    public void setQuestionBankName(String questionBankName) {
        this.questionBankName = questionBankName;
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getTotalChapter() {
        return totalChapter;
    }

    public void setTotalChapter(int totalChapter) {
        this.totalChapter = totalChapter;
    }
}
