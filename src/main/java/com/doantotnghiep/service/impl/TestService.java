package com.doantotnghiep.service.impl;

import com.doantotnghiep.converter.QuestionConverter;
import com.doantotnghiep.converter.TestConverter;
import com.doantotnghiep.dto.TestDTO;
import com.doantotnghiep.entity.Subject;
import com.doantotnghiep.entity.Test;
import com.doantotnghiep.repository.QuestionRepository;
import com.doantotnghiep.repository.SubjectRepository;
import com.doantotnghiep.repository.TestRepository;
import com.doantotnghiep.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService implements ITestService {
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private TestConverter testConverter;
    @Autowired
    private QuestionConverter questionConverter;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    private long total;

    @Override
    public List<TestDTO> findAll(Pageable pageable) {
        List<Test> tests = testRepository.findAll(pageable).getContent();
        List<TestDTO> testDTOs = new ArrayList<>();
        tests.stream().forEach(test -> {
            testDTOs.add(testConverter.toDTO(test));
        });
        this.total = testRepository.count();
        return testDTOs;
    }

    @Override
    public TestDTO findOneById(long testId) {
        Test test = testRepository.findById(testId).get();
        TestDTO testDTO = testConverter.toDTO(test);
        return testDTO;
    }

    @Override
    public List<TestDTO> findBySubjectId(int subjectId, Pageable pageable) {
        Subject subject = subjectRepository.findById(subjectId).get();
        List<Test> tests = testRepository.findBySubject(subject, pageable).getContent();
        List<TestDTO> testDTOs = new ArrayList<>();
        tests.stream().forEach(test -> {
            testDTOs.add(testConverter.toDTO(test));
        });
        this.total = testRepository.findBySubject(subject, pageable).getTotalElements();
        return testDTOs;
    }
    @Override
    public List<TestDTO> findBySubjectId(int subjectId) {
        Subject subject = subjectRepository.findById(subjectId).get();
        List<Test> tests = testRepository.findBySubject(subject);
        List<TestDTO> testDTOs = new ArrayList<>();
        tests.stream().forEach(test -> {
            testDTOs.add(testConverter.toDTO(test));
        });
        return testDTOs;
    }
    @Override
    public long getTotalItem() {
        return this.total;
    }

    @Override
    public long save(TestDTO testDTO) {
        long testId = testRepository.save(testConverter.toEntity(testDTO)).getTestId();
        return testId;
    }

    @Override
    public void delete(long testId) {
        testRepository.deleteById(testId);
    }

}
