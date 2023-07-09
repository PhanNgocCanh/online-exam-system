package com.doantotnghiep.service.impl;

import com.doantotnghiep.converter.TestHistoryConverter;
import com.doantotnghiep.dto.TestHistoryDTO;
import com.doantotnghiep.entity.Student;
import com.doantotnghiep.entity.TestHistory;
import com.doantotnghiep.repository.StudentRepository;
import com.doantotnghiep.repository.TestHistoryRepository;
import com.doantotnghiep.service.ITestHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestHistoryService implements ITestHistoryService {
    @Autowired
    private TestHistoryRepository testHistoryRepository;
    @Autowired
    private TestHistoryConverter testHistoryConverter;
    @Autowired
    private StudentRepository studentRepository;
    @Override
    public List<TestHistoryDTO> findByStudentId(long studentId) {
        Student student = studentRepository.findById(studentId).get();
        List<TestHistory> testHistories = testHistoryRepository.findByStudent(student);
        List<TestHistoryDTO> testHistoryDTOs = new ArrayList<>();
        testHistories.stream().forEach(testHistory -> {
            testHistoryDTOs.add(testHistoryConverter.toDTO(testHistory));
        });
        return testHistoryDTOs;
    }

    @Override
    public TestHistoryDTO findByTestHistoryKey(long studentId, long testId, long questionId) {
        TestHistory testHistory = testHistoryRepository.findByTestHistoryKey(studentId,testId,questionId);
        TestHistoryDTO testHistoryDTO = testHistoryConverter.toDTO(testHistory);
        return testHistoryDTO;
    }

    @Override
    public void save(TestHistoryDTO testHistoryDTO) {
        testHistoryRepository.save(testHistoryConverter.toEntity(testHistoryDTO));
    }

    @Override
    public void delete(long studentId, long testId, long questionId) {

    }
}
