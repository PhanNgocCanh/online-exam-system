package com.doantotnghiep.converter;

import com.doantotnghiep.dto.TestDTO;
import com.doantotnghiep.dto.TestResultDTO;
import com.doantotnghiep.entity.Student;
import com.doantotnghiep.entity.Test;
import com.doantotnghiep.entity.TestResult;
import com.doantotnghiep.entity.TestResultKey;
import com.doantotnghiep.service.IStudentService;
import com.doantotnghiep.service.ITestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestResultConverter {
    @Autowired
    private IStudentService studentService;
    @Autowired
    private ITestService testService;
    @Autowired
    private ModelMapper modelMapper;

    public TestResult toEntity(TestResultDTO testResultDTO){
        TestResult testResult = modelMapper.map(testResultDTO,TestResult.class);
        testResult.setStudent(modelMapper.map(studentService.findOneById(testResultDTO.getStudentId()), Student.class));
        testResult.setTest(modelMapper.map(testService.findOneById(testResultDTO.getTestId()), Test.class));
        TestResultKey testResultKey = new TestResultKey();
        testResultKey.setStudentId(testResultDTO.getStudentId());
        testResultKey.setTestId(testResultDTO.getTestId());
        testResult.setTestResultKey(testResultKey);
        return testResult;
    }

    public TestResultDTO toDTO(TestResult testResult){
        TestResultDTO testResultDTO = modelMapper.map(testResult,TestResultDTO.class);
        testResultDTO.setStudentId(testResult.getStudent().getStudentId());
        testResultDTO.setTestId(testResult.getTest().getTestId());
        return testResultDTO;
    }
}
