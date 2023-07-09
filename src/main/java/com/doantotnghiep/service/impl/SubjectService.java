package com.doantotnghiep.service.impl;

import com.doantotnghiep.dto.SubjectDTO;
import com.doantotnghiep.entity.Subject;
import com.doantotnghiep.repository.SubjectRepository;
import com.doantotnghiep.service.ISubjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubjectService implements ISubjectService {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<SubjectDTO> findAll() {
        List<SubjectDTO> subjectDTOs = new ArrayList<>();
        List<Subject> subjects = subjectRepository.findAll();
        subjects.stream().forEach((sb) -> {
            subjectDTOs.add(modelMapper.map(sb, SubjectDTO.class));
        });
        return subjectDTOs;
    }

    @Override
    public SubjectDTO findOneById(int subjectId) {
        Subject subject = subjectRepository.findById(subjectId).get();
        SubjectDTO subjectDTO = modelMapper.map(subject, SubjectDTO.class);
        return subjectDTO;
    }

    @Override
    public void save(SubjectDTO subjectDTO) {
        subjectRepository.save(modelMapper.map(subjectDTO, Subject.class));
    }

    @Override
    public void delete(int subjectId) {
        subjectRepository.deleteById(subjectId);
    }
}
