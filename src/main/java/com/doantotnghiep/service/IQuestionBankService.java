package com.doantotnghiep.service;

import com.doantotnghiep.dto.QuestionBankDTO;

import java.util.List;


public interface IQuestionBankService {
    List<QuestionBankDTO> findAll();
    QuestionBankDTO findOneByQuestionBankId(long questionBankId);
    List<QuestionBankDTO> findBySubjectId(int subjectId);
    void save(QuestionBankDTO questionBankDTO);
    void delete(long questionBankId);
}
