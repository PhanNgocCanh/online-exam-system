package com.doantotnghiep.dto;

import java.util.ArrayList;
import java.util.List;

public class SubjectChapter {
    private int subjectId;
    private String subjectName;
    private List<ChapterDTO> chapters = new ArrayList<>();

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public List<ChapterDTO> getChapters() {
        return chapters;
    }

    public void setChapters(List<ChapterDTO> chapters) {
        this.chapters = chapters;
    }
}
