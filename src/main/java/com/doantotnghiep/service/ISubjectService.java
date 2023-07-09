package com.doantotnghiep.service;

import com.doantotnghiep.dto.SubjectDTO;

import java.util.List;

public interface ISubjectService {
    List<SubjectDTO> findAll();
    SubjectDTO findOneById(int subjectId);
    void save(SubjectDTO subjectDTO);
    void delete(int subjectId);

}
