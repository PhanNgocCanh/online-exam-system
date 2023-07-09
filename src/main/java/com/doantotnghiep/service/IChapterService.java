package com.doantotnghiep.service;

import com.doantotnghiep.dto.ChapterDTO;

import java.util.List;

public interface IChapterService {
    List<ChapterDTO> findAll();
    List<ChapterDTO> findByQuestionBankId(long questionBankId);
    ChapterDTO findOneById(long chapterId);

    long getTotalQuestion(long chapterId);

    void save(ChapterDTO chapterDTO);
    void delete(long chapterId);
}
