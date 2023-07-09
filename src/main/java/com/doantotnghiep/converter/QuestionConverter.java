package com.doantotnghiep.converter;

import com.doantotnghiep.dto.QuestionDTO;
import com.doantotnghiep.entity.Chapter;
import com.doantotnghiep.entity.Question;
import com.doantotnghiep.service.IChapterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class QuestionConverter {
    @Autowired
    private IChapterService chapterService;
    @Autowired
    private ModelMapper modelMapper;

    public Question toEntity(QuestionDTO questionDTO){
        Question question = modelMapper.map(questionDTO,Question.class);
        question.setChapter(modelMapper.map(chapterService.findOneById(questionDTO.getChapterId()), Chapter.class));
        return question;
    }

    public QuestionDTO toDTO(Question question){
        QuestionDTO questionDTO = modelMapper.map(question,QuestionDTO.class);
        questionDTO.setChapterId(question.getChapter().getChapterId());
        return questionDTO;
    }
}
