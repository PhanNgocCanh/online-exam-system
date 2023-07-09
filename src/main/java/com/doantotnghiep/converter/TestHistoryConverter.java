package com.doantotnghiep.converter;

import com.doantotnghiep.dto.TestHistoryDTO;
import com.doantotnghiep.entity.*;
import com.doantotnghiep.service.IQuestionService;
import com.doantotnghiep.service.IStudentService;
import com.doantotnghiep.service.ITestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestHistoryConverter {
    @Autowired
    private IStudentService studentService;
    @Autowired
    private ITestService testService;
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private ModelMapper modelMapper;

    public TestHistory toEntity(TestHistoryDTO testHistoryDTO){
        TestHistory testHistory = modelMapper.map(testHistoryDTO,TestHistory.class);
        testHistory.setStudent(modelMapper.map(studentService.findOneById(testHistoryDTO.getStudentId()), Student.class));
        testHistory.setTest(modelMapper.map(testService.findOneById(testHistoryDTO.getTestId()), Test.class));
        testHistory.setQuestion(modelMapper.map(questionService.findOneById(testHistoryDTO.getQuestionId()), Question.class));
        TestHistoryKey testHistoryKey = new TestHistoryKey();
        testHistoryKey.setStudentId(testHistoryDTO.getStudentId());
        testHistoryKey.setTestId(testHistoryDTO.getTestId());
        testHistoryKey.setQuestionId(testHistoryDTO.getQuestionId());
        testHistory.setTestHistoryKey(testHistoryKey);
        return testHistory;
    }

    public TestHistoryDTO toDTO(TestHistory testHistory){
        TestHistoryDTO testHistoryDTO = modelMapper.map(testHistory,TestHistoryDTO.class);
        testHistoryDTO.setStudentId(testHistory.getStudent().getStudentId());
        testHistoryDTO.setTestId(testHistory.getTest().getTestId());
        testHistoryDTO.setQuestionId(testHistory.getQuestion().getQuestionId());
        return testHistoryDTO;
    }
}
