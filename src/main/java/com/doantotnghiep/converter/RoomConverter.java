package com.doantotnghiep.converter;

import com.doantotnghiep.dto.RoomDTO;
import com.doantotnghiep.entity.Exam;
import com.doantotnghiep.entity.Room;
import com.doantotnghiep.entity.Subject;
import com.doantotnghiep.entity.Teacher;
import com.doantotnghiep.service.IExamService;
import com.doantotnghiep.service.ISubjectService;
import com.doantotnghiep.service.ITeacherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomConverter {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private ISubjectService subjectService;
    @Autowired
    private IExamService examService;

    public Room toEntity(RoomDTO roomDTO){
        Room room = modelMapper.map(roomDTO,Room.class);
        room.setTeacher(modelMapper.map(teacherService.findOne(roomDTO.getTeacherId()), Teacher.class));
        room.setExam(modelMapper.map(examService.findOneById(roomDTO.getExamId()), Exam.class));
        room.setSubject(modelMapper.map(subjectService.findOneById(roomDTO.getSubjectId()), Subject.class));
        return room;
    }

    public RoomDTO toDTO(Room room){
        RoomDTO roomDTO = modelMapper.map(room,RoomDTO.class);
        roomDTO.setTeacherId(room.getTeacher().getTeacherId());
        roomDTO.setExamId(room.getExam().getExamId());
        roomDTO.setSubjectId(room.getSubject().getSubjectId());
        return roomDTO;
    }
}
