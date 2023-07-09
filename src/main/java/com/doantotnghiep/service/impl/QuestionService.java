package com.doantotnghiep.service.impl;

import com.doantotnghiep.constant.Alphabet;
import com.doantotnghiep.converter.ChapterConverter;
import com.doantotnghiep.converter.QuestionConverter;
import com.doantotnghiep.converter.TestConverter;
import com.doantotnghiep.dto.AnswerDTO;
import com.doantotnghiep.dto.QuestionDTO;
import com.doantotnghiep.dto.TestDTO;
import com.doantotnghiep.entity.Chapter;
import com.doantotnghiep.entity.Question;
import com.doantotnghiep.entity.Test;
import com.doantotnghiep.repository.QuestionRepository;
import com.doantotnghiep.repository.TestRepository;
import com.doantotnghiep.service.IAnswerService;
import com.doantotnghiep.service.IChapterService;
import com.doantotnghiep.service.IQuestionService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class QuestionService implements IQuestionService {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionConverter questionConverter;
    @Autowired
    private IChapterService chapterService;
    @Autowired
    private ChapterConverter chapterConverter;
    @Autowired
    private TestConverter testConverter;
    @Autowired
    private TestRepository testRepository;
    private long totalItem;

    @Override
    public List<QuestionDTO> findAll() {
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        List<Question> questions = questionRepository.findAll();
        totalItem = questionRepository.count();
        questions.stream().forEach((question) -> {
            questionDTOs.add(questionConverter.toDTO(question));
        });
        return questionDTOs;
    }

    @Override
    public List<QuestionDTO> findAll(Pageable pageable) {
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        List<Question> questions = questionRepository.findAll(pageable).getContent();
        totalItem = questionRepository.count();
        questions.stream().forEach((question) -> {
            questionDTOs.add(questionConverter.toDTO(question));
        });
        return questionDTOs;
    }

    @Override
    public List<QuestionDTO> findBySearchAndFilter(long chapterId, String level, String keyword, Pageable pageable) {
        List<Question> questions = questionRepository.findBySearchAndFilter(chapterId,level,keyword,pageable).getContent();
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        questions.stream().forEach(question -> {
            questionDTOs.add(questionConverter.toDTO(question));
        });
        this.totalItem = questionRepository.findBySearchAndFilter(chapterId,level,keyword,pageable).getTotalElements();
        return questionDTOs;
    }

    @Override
    public List<QuestionDTO> findByChapterId(long chapterId) {
        Chapter chapter = chapterConverter.toEntity(chapterService.findOneById(chapterId));
        List<Question> questions = questionRepository.findByChapter(chapter);
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        questions.stream().forEach((question) -> {
            questionDTOs.add(questionConverter.toDTO(question));
        });
        return questionDTOs;
    }

    @Override
    public List<QuestionDTO> findByChapterIdAndLevel(long chapterId, String level) {
        Chapter chapter = chapterConverter.toEntity(chapterService.findOneById(chapterId));
        List<Question> questions = questionRepository.findByChapterAndLevel(chapter,level);
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        questions.stream().forEach(question -> {
            questionDTOs.add(questionConverter.toDTO(question));
        });
        return questionDTOs;
    }

    @Override
    public List<QuestionDTO> getQuestionAutomaticForTest(long chapterId, String level, int numOfQuestion) {
        List<QuestionDTO> questionDTOs = findByChapterIdAndLevel(chapterId,level);
        List<QuestionDTO> questionsIntoTest = new ArrayList<>();
        while(questionsIntoTest.size()<numOfQuestion && questionDTOs.size()>0){
            int randomIndex = (int)(Math.random()*questionDTOs.size());
            questionsIntoTest.add(questionDTOs.get(randomIndex));
            questionDTOs.remove(randomIndex);
        }
        return questionsIntoTest;
    }

    @Transactional
    @Override
    public List<QuestionDTO> readQuestionFromFile(String fileName, long chapterId, IAnswerService answerService) throws IOException {
        List<QuestionDTO> questionDTOs = new ArrayList<>();
        Workbook workbook = null;
        FileInputStream fis = null;
        try{
            Resource resource = new ClassPathResource("/static/upload/"+fileName);
            fis = new FileInputStream(resource.getFile());
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            int index = 0;
            for (Row row : sheet){
                if(index<2){
                    index ++;
                    continue;
                }
                int numColumns = sheet.getRow(index).getLastCellNum() - 4;
                QuestionDTO questionDTO = new QuestionDTO();
                questionDTO.setChapterId(chapterId);
                if((row.getCell(0)==null&&row.getCell(1)==null)||
                        row.getCell(0).toString().equals("")) break;
                questionDTO.setQuestionContent(row.getCell(0).toString());
                questionDTO.setLevel(row.getCell(2).toString());
                questionDTO.setExplain(row.getCell(3)!=null?(row.getCell(3).toString().equals("")?null:row.getCell(3).toString()):null);
                questionDTO.setCreateAt(new Date());
                int posOfCorrectAnswer = (int)row.getCell(1).getNumericCellValue();
                int post = 0;
                int answerPosition = 4;
                long questionId = save(questionDTO);
                while(numColumns>0){
                    AnswerDTO answerDTO = new AnswerDTO();
                    answerDTO.setQuestionId(questionId);
                    answerDTO.setAnswerContent(row.getCell(answerPosition).toString());
                    answerDTO.setAnswerSymbol(Alphabet.getPosition(post));
                    long answerId = answerService.save(answerDTO);
                    if(post == posOfCorrectAnswer - 1){
                        questionDTO.setCorrectAnswer(answerId);
                    }
                    numColumns--;
                    answerPosition++;
                    post++;
                }
                questionDTO.setQuestionId(questionId);
                questionDTOs.add(questionDTO);
                long questionReSave = save(questionDTO);
                index++;

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            if(fis != null) fis.close();
            if(workbook!=null) workbook.close();
        }
        return questionDTOs;
    }

    @Override
    public QuestionDTO findOneById(long questionId) {
        Question question = questionRepository.findById(questionId).get();
        QuestionDTO questionDTO = questionConverter.toDTO(question);
        return questionDTO;
    }

    @Override
    public long getTotalItem() {
        return totalItem;
    }

    @Override
    public long save(QuestionDTO questionDTO) {
        Question question = new Question();
        questionDTO.setCreateAt(new Date());
        long questionId = questionRepository.save(questionConverter.toEntity(questionDTO)).getQuestionId();
        return questionId;
    }

    @Override
    public void delete(long questionId) {
        questionRepository.deleteById(questionId);
    }
}
