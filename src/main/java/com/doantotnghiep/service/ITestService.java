package com.doantotnghiep.service;

import com.doantotnghiep.dto.QuestionBankDTO;
import com.doantotnghiep.dto.QuestionDTO;
import com.doantotnghiep.dto.TestDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ITestService {
    List<TestDTO> findAll(Pageable pageable);
    List<TestDTO> findBySubjectId(int subjectId,Pageable pageable);
    TestDTO findOneById(long testId);

    List<TestDTO> findBySubjectId(int subjectId);

    long getTotalItem();
    long save(TestDTO testDTO);

    void delete(long testId);

}
