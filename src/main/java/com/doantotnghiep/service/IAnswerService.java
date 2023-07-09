package com.doantotnghiep.service;

import com.doantotnghiep.dto.AnswerDTO;

import java.util.List;

public interface IAnswerService {
    List<AnswerDTO> findByQuestionId(long questionId);
    AnswerDTO findOneByAnswerId(long answerId);
    long save(AnswerDTO answerDTO);
    void delete(long answerId);
}
