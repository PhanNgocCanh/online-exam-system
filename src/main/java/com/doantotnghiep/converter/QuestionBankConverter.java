package com.doantotnghiep.converter;

import com.doantotnghiep.dto.QuestionBankDTO;
import com.doantotnghiep.entity.QuestionBank;
import com.doantotnghiep.entity.Subject;
import com.doantotnghiep.service.IChapterService;
import com.doantotnghiep.service.ISubjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionBankConverter {
    @Autowired
    private ISubjectService subjectService;

    @Autowired
    private ModelMapper modelMapper;

    public QuestionBank toEntity(QuestionBankDTO questionBankDTO){
        QuestionBank questionBank = modelMapper.map(questionBankDTO,QuestionBank.class);
        questionBank.setSubject(modelMapper.map(subjectService.findOneById(questionBankDTO.getSubjectId()), Subject.class));
        return questionBank;
    }

    public QuestionBankDTO toDTO(QuestionBank questionBank){
        QuestionBankDTO questionBankDTO = modelMapper.map(questionBank,QuestionBankDTO.class);
        questionBankDTO.setSubjectId(questionBank.getSubject().getSubjectId());
        return questionBankDTO;
    }
}
