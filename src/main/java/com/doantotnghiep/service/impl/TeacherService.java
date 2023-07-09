package com.doantotnghiep.service.impl;

import com.doantotnghiep.dto.TeacherDTO;
import com.doantotnghiep.entity.Teacher;
import com.doantotnghiep.repository.TeacherRepository;
import com.doantotnghiep.service.ITeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TeacherService implements ITeacherService {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<TeacherDTO> findAll() {
        List<TeacherDTO> teacherDTOs = new ArrayList<>();
        List<Teacher> teachers = teacherRepository.findAll();
        teachers.stream().forEach((teacher) -> {
            teacherDTOs.add(modelMapper.map(teacher, TeacherDTO.class));
        });
        return teacherDTOs;
    }

    @Override
    public TeacherDTO findOne(long teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).get();
        TeacherDTO teacherDTO = modelMapper.map(teacher, TeacherDTO.class);
        return teacherDTO;
    }

    @Override
    public void save(TeacherDTO teacherDTO) {
        teacherRepository.save(modelMapper.map(teacherDTO,Teacher.class));
    }

    @Override
    public void delete(long teacherId) {

    }
}
