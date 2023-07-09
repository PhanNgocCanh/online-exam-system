package com.doantotnghiep.service.impl;

import com.doantotnghiep.converter.QuestionBankConverter;
import com.doantotnghiep.dto.QuestionBankDTO;
import com.doantotnghiep.dto.SubjectDTO;
import com.doantotnghiep.entity.QuestionBank;
import com.doantotnghiep.entity.Subject;
import com.doantotnghiep.repository.QuestionBankRepository;
import com.doantotnghiep.repository.SubjectRepository;
import com.doantotnghiep.service.IChapterService;
import com.doantotnghiep.service.IQuestionBankService;
import com.doantotnghiep.service.ISubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionBankService implements IQuestionBankService {
    @Autowired
    private QuestionBankRepository questionBankRepository;
    @Autowired
    private QuestionBankConverter questionBankConverter;
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private SubjectRepository subjectRepository;
    private IChapterService chapterService;
    @Autowired
    public QuestionBankService(@Lazy IChapterService chapterService){
        this.chapterService = chapterService;
    }
    @Override
    public List<QuestionBankDTO> findAll() {
        List<QuestionBankDTO> questionBankDTOs = new ArrayList<>();
        List<QuestionBank> questionBanks = questionBankRepository.findAll();
        questionBanks.stream().forEach((questionBank)->{
            QuestionBankDTO questionBankDTO = questionBankConverter.toDTO(questionBank);
            questionBankDTO.setSubjectName(subjectService.findOneById(questionBank.getSubject().getSubjectId()).getSubjectName());
            questionBankDTO.setTotalChapter(this.chapterService.findByQuestionBankId(questionBank.getQuestionBankId()).size());
            questionBankDTOs.add(questionBankDTO);
        });
        return questionBankDTOs;
    }

    @Override
    public QuestionBankDTO findOneByQuestionBankId(long questionBankId) {
        QuestionBank questionBank = questionBankRepository.findById(questionBankId).get();
        QuestionBankDTO questionBankDTO = questionBankConverter.toDTO(questionBank);
        questionBankDTO.setSubjectName(subjectService.findOneById(questionBank.getSubject().getSubjectId()).getSubjectName());
        return questionBankDTO;
    }

    @Override
    public List<QuestionBankDTO> findBySubjectId(int subjectId) {
        Subject subject = subjectRepository.findById(subjectId).get();
        List<QuestionBankDTO> questionBankDTOs = new ArrayList<>();
        List<QuestionBank> questionBanks = questionBankRepository.findBySubject(subject);
        questionBanks.stream().forEach((questionBank)->{
            QuestionBankDTO questionBankDTO = questionBankConverter.toDTO(questionBank);
            questionBankDTO.setSubjectName(subjectService.findOneById(questionBank.getSubject().getSubjectId()).getSubjectName());
            questionBankDTO.setTotalChapter(this.chapterService.findByQuestionBankId(questionBank.getQuestionBankId()).size());
            questionBankDTOs.add(questionBankDTO);
        });
        return questionBankDTOs;
    }

    @Override
    public void save(QuestionBankDTO questionBankDTO) {
        questionBankRepository.save(questionBankConverter.toEntity(questionBankDTO));
    }

    @Override
    public void delete(long questionBankId) {

    }
}
