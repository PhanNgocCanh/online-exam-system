package com.doantotnghiep.service;

import com.doantotnghiep.dto.TestResultDTO;

import java.util.List;

public interface ITestResultService {
    List<TestResultDTO> findAllByStudentId(long studentId);
    TestResultDTO findOneByTestResultKey(long studentId,long testId);
    void save(TestResultDTO testResultDTO);
    void delete(long studentId,long testId);
}
