package com.doantotnghiep.converter;

import com.doantotnghiep.dto.ChapterDTO;
import com.doantotnghiep.entity.Chapter;
import com.doantotnghiep.entity.QuestionBank;
import com.doantotnghiep.service.IQuestionBankService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChapterConverter {
    @Autowired
    private IQuestionBankService questionBankService;
    @Autowired
    private ModelMapper modelMapper;

    public Chapter toEntity(ChapterDTO chapterDTO){
        Chapter chapter = modelMapper.map(chapterDTO,Chapter.class);
        chapter.setQuestionBank(modelMapper.map(questionBankService.findOneByQuestionBankId(chapterDTO.getQuestionBankId()),
                QuestionBank.class));
        return chapter;
    }

    public ChapterDTO toDTO(Chapter chapter){
        ChapterDTO chapterDTO = modelMapper.map(chapter,ChapterDTO.class);
        chapterDTO.setQuestionBankId(chapter.getQuestionBank().getQuestionBankId());
        return chapterDTO;
    }
}
