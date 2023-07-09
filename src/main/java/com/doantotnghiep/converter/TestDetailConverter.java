package com.doantotnghiep.converter;

import com.doantotnghiep.dto.TestDetailDTO;
import com.doantotnghiep.entity.Question;
import com.doantotnghiep.entity.Test;
import com.doantotnghiep.entity.TestDetail;
import com.doantotnghiep.repository.QuestionRepository;
import com.doantotnghiep.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestDetailConverter {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestRepository testRepository;

    public TestDetail toEntity(TestDetailDTO testDetailDTO){
        Question question = questionRepository.findById(testDetailDTO.getQuestionId()).get();
        Test test = testRepository.findById(testDetailDTO.getTestId()).get();
        TestDetail testDetail = new TestDetail();
        testDetail.setTest(test);
        testDetail.setQuestion(question);
        return testDetail;
    }

    public TestDetailDTO testDTO(TestDetail testDetail){
        TestDetailDTO testDetailDTO = new TestDetailDTO();
        testDetailDTO.setId(testDetail.getTestDetailKey());
        testDetailDTO.setTestId(testDetail.getTest().getTestId());
        testDetailDTO.setQuestionId(testDetail.getQuestion().getQuestionId());
        return testDetailDTO;
    }
}
