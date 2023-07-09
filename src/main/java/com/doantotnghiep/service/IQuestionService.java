package com.doantotnghiep.service;

import com.doantotnghiep.dto.QuestionDTO;
import com.doantotnghiep.dto.TestDTO;
import com.doantotnghiep.service.impl.AnswerService;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface IQuestionService {
    List<QuestionDTO> findAll();
    List<QuestionDTO> findAll(Pageable pageable);
    List<QuestionDTO> findBySearchAndFilter(long chapterId, String level, String keyword, Pageable pageable);
    List<QuestionDTO> findByChapterId(long chapterId);
    List<QuestionDTO> findByChapterIdAndLevel(long chapterId,String level);
    List<QuestionDTO> getQuestionAutomaticForTest(long chapterId,String level,int numOfQuestion);
    List<QuestionDTO> readQuestionFromFile(String fileName, long chapterId, IAnswerService answerService) throws IOException;
    QuestionDTO findOneById(long questionId);

    long getTotalItem();
    long save(QuestionDTO questionDTO);
    void delete(long questionId);
}
