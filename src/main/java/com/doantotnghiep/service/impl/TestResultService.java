package com.doantotnghiep.service.impl;

import com.doantotnghiep.converter.TestResultConverter;
import com.doantotnghiep.dto.TestResultDTO;
import com.doantotnghiep.entity.Student;
import com.doantotnghiep.entity.Test;
import com.doantotnghiep.entity.TestResult;
import com.doantotnghiep.entity.TestResultKey;
import com.doantotnghiep.repository.StudentRepository;
import com.doantotnghiep.repository.TestRepository;
import com.doantotnghiep.repository.TestResultRepository;
import com.doantotnghiep.service.ITestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestResultService implements ITestResultService {
    @Autowired
    private TestResultRepository testResultRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private TestResultConverter testResultConverter;

    @Override
    public List<TestResultDTO> findAllByStudentId(long studentId) {
        return null;
    }

    @Override
    public TestResultDTO findOneByTestResultKey(long studentId, long testId) {
        Student student = studentRepository.findOneByStudentId(studentId);
        Test test = testRepository.findById(testId).get();
        TestResult testResult = testResultRepository.findOneByStudentAndTest(student,test);
        TestResultDTO testResultDTO = testResultConverter.toDTO(testResult);
        return testResultDTO;
    }

    @Override
    public void save(TestResultDTO testResultDTO) {
        testResultRepository.save(testResultConverter.toEntity(testResultDTO));
    }

    @Override
    public void delete(long studentId, long testId) {

    }
}
