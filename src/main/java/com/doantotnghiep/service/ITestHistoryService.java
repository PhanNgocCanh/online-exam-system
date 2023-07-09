package com.doantotnghiep.service;

import com.doantotnghiep.dto.TestHistoryDTO;

import java.util.List;

public interface ITestHistoryService {
    List<TestHistoryDTO> findByStudentId(long studentId);
    TestHistoryDTO findByTestHistoryKey(long studentId,long testId,long questionId);
    void save(TestHistoryDTO testHistoryDTO);
    void delete(long studentId,long testId,long questionId);
}
