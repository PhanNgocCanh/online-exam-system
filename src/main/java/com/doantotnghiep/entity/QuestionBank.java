package com.doantotnghiep.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "QuestionBank")
public class QuestionBank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QuestionBankId")
    private long questionBankId;
    @ManyToOne
    @JoinColumn(name = "SubjectId")
    private Subject subject;
    @Column(name = "QuestionBankName")
    private String questionBankName;
    @Column(name = "CreateBy")
    private String createBy;
    @Column(name = "CreateAt")
    private Date createAt;
    @OneToMany(mappedBy = "questionBank")
    private List<Chapter> chapters = new ArrayList<>();

    public long getQuestionBankId() {
        return questionBankId;
    }

    public void setQuestionBankId(long questionBankId) {
        this.questionBankId = questionBankId;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
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

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }
}
