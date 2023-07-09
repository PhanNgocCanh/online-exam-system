package com.doantotnghiep.converter;

import com.doantotnghiep.dto.TestDTO;
import com.doantotnghiep.entity.Subject;
import com.doantotnghiep.entity.Test;
import com.doantotnghiep.service.ISubjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TestConverter {
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private ModelMapper modelMapper;

    public Test toEntity(TestDTO testDTO){
        Test test = modelMapper.map(testDTO,Test.class);
        test.setSubject(modelMapper.map(subjectService.findOneById(testDTO.getSubjectId()), Subject.class));
        return test;
    }

    public TestDTO toDTO(Test test){
        TestDTO testDTO = modelMapper.map(test,TestDTO.class);
        testDTO.setSubjectId(test.getSubject().getSubjectId());
        testDTO.setSubjectName(test.getSubject().getSubjectName());
        return testDTO;
    }
}
