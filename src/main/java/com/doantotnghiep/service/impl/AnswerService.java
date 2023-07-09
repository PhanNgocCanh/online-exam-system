package com.doantotnghiep.service.impl;

import com.doantotnghiep.converter.AnswerConverter;
import com.doantotnghiep.dto.AnswerDTO;
import com.doantotnghiep.entity.Answer;
import com.doantotnghiep.entity.Question;
import com.doantotnghiep.repository.AnswerRepository;
import com.doantotnghiep.repository.QuestionRepository;
import com.doantotnghiep.service.IAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerService implements IAnswerService {
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private AnswerConverter answerConverter;
    @Autowired
    private QuestionRepository questionRepository;
    @Override
    public List<AnswerDTO> findByQuestionId(long questionId) {
        Question question = questionRepository.findById(questionId).get();
        List<Answer> answers = answerRepository.findByQuestion(question);
        List<AnswerDTO> answerDTOs = new ArrayList<>();
        answers.stream().forEach((answer -> {
            answerDTOs.add(answerConverter.toDTO(answer));
        }));
        return answerDTOs;
    }

    @Override
    public AnswerDTO findOneByAnswerId(long answerId) {
        Answer answer = answerRepository.findOneByAnswerId(answerId);
        AnswerDTO answerDTO = answerConverter.toDTO(answer);
        return answerDTO;
    }

    @Override
    public long save(AnswerDTO answerDTO) {
       long answerId =  answerRepository.save(answerConverter.toEntity(answerDTO)).getAnswerId();
       return answerId;
    }

    @Override
    public void delete(long answerId) {
       answerRepository.deleteById(answerId);
    }
}
