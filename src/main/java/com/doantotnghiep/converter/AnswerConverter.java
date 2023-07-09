package com.doantotnghiep.converter;

import com.doantotnghiep.dto.AnswerDTO;
import com.doantotnghiep.entity.Answer;
import com.doantotnghiep.entity.Question;
import com.doantotnghiep.service.IQuestionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnswerConverter {
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private ModelMapper modelMapper;

    public Answer toEntity(AnswerDTO answerDTO){
        Answer answer = modelMapper.map(answerDTO,Answer.class);
        answer.setQuestion(modelMapper.map(questionService.findOneById(answerDTO.getQuestionId()), Question.class));
        return answer;
    }

    public AnswerDTO toDTO(Answer answer){
        AnswerDTO answerDTO = modelMapper.map(answer,AnswerDTO.class);
        answerDTO.setQuestionId(answer.getQuestion().getQuestionId());
        return answerDTO;
    }
}
