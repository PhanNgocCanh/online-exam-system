package com.doantotnghiep.dto;

public class ChapterDTO {
    private long chapterId;
    private long questionBankId;
    private String chapterName;
    private long totalQuestion;

    public long getChapterId() {
        return chapterId;
    }

    public void setChapterId(long chapterId) {
        this.chapterId = chapterId;
    }

    public long getQuestionBankId() {
        return questionBankId;
    }

    public void setQuestionBankId(long questionBankId) {
        this.questionBankId = questionBankId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public long getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(long totalQuestion) {
        this.totalQuestion = totalQuestion;
    }
}
