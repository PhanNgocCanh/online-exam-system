package com.doantotnghiep.service;

import com.doantotnghiep.dto.TestDTO;
import com.doantotnghiep.dto.TestDetailDTO;

import java.util.List;

public interface ITestDetailService {
    TestDetailDTO findById(long id);
    TestDetailDTO findByTestIdAndQuestionId(long testId,long questionId);
    List<TestDetailDTO> findByTestId(long testId);
    List<TestDetailDTO> findByQuestionId(long questionId);
    void save(TestDetailDTO testDetailDTO);
    void delete(long testDetailDTO);
}
