package com.doantotnghiep.service;

import com.doantotnghiep.dto.ExamDTO;

import java.util.List;

public interface IExamService {
    List<ExamDTO> findAll();
    ExamDTO findOneById(long examId);
    void save(ExamDTO examDTO);
    void delete(long examId);
}
