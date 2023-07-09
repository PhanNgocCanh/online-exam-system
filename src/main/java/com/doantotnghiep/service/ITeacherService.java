package com.doantotnghiep.service;

import com.doantotnghiep.dto.TeacherDTO;

import java.util.List;

public interface ITeacherService {
    List<TeacherDTO> findAll();
    TeacherDTO findOne(long teacherId);
    void save(TeacherDTO teacherDTO);
    void delete(long teacherId);
}
