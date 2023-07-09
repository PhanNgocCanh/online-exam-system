package com.doantotnghiep.service.impl;

import com.doantotnghiep.converter.ChapterConverter;
import com.doantotnghiep.converter.QuestionBankConverter;
import com.doantotnghiep.dto.ChapterDTO;
import com.doantotnghiep.entity.Chapter;
import com.doantotnghiep.entity.QuestionBank;
import com.doantotnghiep.repository.ChapterRepository;
import com.doantotnghiep.service.IChapterService;
import com.doantotnghiep.service.IQuestionBankService;
import com.doantotnghiep.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChapterService implements IChapterService {
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private ChapterConverter chapterConverter;
    @Autowired
    private IQuestionBankService questionBankService;
    @Autowired
    private QuestionBankConverter questionBankConverter;
    private IQuestionService questionService;

    @Autowired
    public ChapterService(@Lazy IQuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public List<ChapterDTO> findAll() {
        List<Chapter> chapters = chapterRepository.findAll();
        List<ChapterDTO> chapterDTOs = new ArrayList<>();
        chapters.stream().forEach(chapter -> {
            chapterDTOs.add(chapterConverter.toDTO(chapter));
        });
        return chapterDTOs;
    }

    @Override
    public List<ChapterDTO> findByQuestionBankId(long questionBankId) {
        QuestionBank questionBank = questionBankConverter.toEntity(questionBankService.findOneByQuestionBankId(questionBankId));
        List<Chapter> chapters = chapterRepository.findByQuestionBank(questionBank);
        List<ChapterDTO> chapterDTOs = new ArrayList<>();
        chapters.stream().forEach((chapter) -> {
            ChapterDTO chapterDTO = chapterConverter.toDTO(chapter);
            chapterDTO.setTotalQuestion(getTotalQuestion(chapter.getChapterId()));
            chapterDTOs.add(chapterDTO);
        });
        return chapterDTOs;
    }

    @Override
    public ChapterDTO findOneById(long chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId).get();
        ChapterDTO chapterDTO = chapterConverter.toDTO(chapter);
        return chapterDTO;
    }
    @Override
    public long getTotalQuestion(long chapterId){
        return questionService.findByChapterId(chapterId).size();
    }
    @Override
    public void save(ChapterDTO chapterDTO) {
        chapterRepository.save(chapterConverter.toEntity(chapterDTO));
    }

    @Override
    public void delete(long chapterId) {
        chapterRepository.deleteById(chapterId);
    }
}
