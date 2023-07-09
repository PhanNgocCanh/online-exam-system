package com.doantotnghiep.service.impl;

import com.doantotnghiep.dto.ExamDTO;
import com.doantotnghiep.entity.Exam;
import com.doantotnghiep.repository.ExamRepository;
import com.doantotnghiep.service.IExamService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamService implements IExamService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ExamRepository examRepository;

    @Override
    public List<ExamDTO> findAll() {
        List<ExamDTO> examDTOs = new ArrayList<>();
        List<Exam> exams = examRepository.findAll();
        exams.stream().forEach((ex) -> {
            examDTOs.add(modelMapper.map(ex, ExamDTO.class));
        });
        return examDTOs;
    }

    @Override
    public ExamDTO findOneById(long examId) {
        Exam exam = examRepository.findById(examId).get();
        ExamDTO examDTO = modelMapper.map(exam, ExamDTO.class);
        return examDTO;
    }

    @Override
    public void save(ExamDTO examDTO) {
        examRepository.save(modelMapper.map(examDTO, Exam.class));
    }

    @Override
    public void delete(long examId) {
        examRepository.deleteById(examId);
    }
}
