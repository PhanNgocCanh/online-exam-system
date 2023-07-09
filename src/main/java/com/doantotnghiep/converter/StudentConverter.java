package com.doantotnghiep.converter;

import com.doantotnghiep.dto.StudentDTO;
import com.doantotnghiep.entity.Clazz;
import com.doantotnghiep.entity.Student;
import com.doantotnghiep.service.IClazzService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StudentConverter {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private IClazzService clazzService;

    public Student toEntity(StudentDTO studentDTO){
        Student student = modelMapper.map(studentDTO,Student.class);
        student.setClazz(modelMapper.map(clazzService.findOneById(studentDTO.getClassId()), Clazz.class));
        return student;
    }

    public StudentDTO toDTO(Student student){
        StudentDTO studentDTO = modelMapper.map(student,StudentDTO.class);
        long classId = student.getClazz().getClassId();
        studentDTO.setClassId(classId);
        studentDTO.setClassName(clazzService.findOneById(classId).getClassName());
        return studentDTO;
    }
}
