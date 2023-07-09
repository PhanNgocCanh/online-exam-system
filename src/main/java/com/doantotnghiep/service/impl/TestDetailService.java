package com.doantotnghiep.service.impl;

import com.doantotnghiep.converter.TestDetailConverter;
import com.doantotnghiep.dto.TestDetailDTO;
import com.doantotnghiep.entity.Question;
import com.doantotnghiep.entity.Test;
import com.doantotnghiep.entity.TestDetail;
import com.doantotnghiep.repository.QuestionRepository;
import com.doantotnghiep.repository.TestDetailRepository;
import com.doantotnghiep.repository.TestRepository;
import com.doantotnghiep.service.ITestDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestDetailService implements ITestDetailService {
    @Autowired
    private TestDetailRepository testDetailRepository;
    @Autowired
    private TestDetailConverter testDetailConverter;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Override
    public TestDetailDTO findById(long id) {
        TestDetail testDetail = testDetailRepository.findById(id).get();
        TestDetailDTO testDetailDTO = testDetailConverter.testDTO(testDetail);
        return testDetailDTO;
    }

    @Override
    public TestDetailDTO findByTestIdAndQuestionId(long testId, long questionId) {
        Test test = testRepository.findById(testId).get();
        Question question = questionRepository.findById(questionId).get();
        TestDetailDTO testDetailDTO = testDetailConverter.testDTO(testDetailRepository.findOneByTestAndQuestion(test,question));
        return testDetailDTO;
    }

    @Override
    public List<TestDetailDTO> findByTestId(long testId) {
        List<TestDetail> testDetails = testDetailRepository.findAllByTestId(testId);
        List<TestDetailDTO> testDetailDTOs = new ArrayList<>();
        testDetails.stream().forEach(testDetail -> {
            testDetailDTOs.add(testDetailConverter.testDTO(testDetail));
        });
        return testDetailDTOs;
    }

    @Override
    public List<TestDetailDTO> findByQuestionId(long questionId) {
        List<TestDetail> testDetails = testDetailRepository.findAllByQuestionId(questionId);
        List<TestDetailDTO> testDetailDTOs = new ArrayList<>();
        testDetails.stream().forEach(testDetail -> {
            testDetailDTOs.add(testDetailConverter.testDTO(testDetail));
        });
        return testDetailDTOs;
    }

    @Override
    public void save(TestDetailDTO testDetailDTO) {
        testDetailRepository.save(testDetailConverter.toEntity(testDetailDTO));
    }

    @Override
    public void delete(long id) {
        testDetailRepository.deleteById(id);
    }
}
