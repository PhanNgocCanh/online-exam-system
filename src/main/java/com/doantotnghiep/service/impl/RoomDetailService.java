package com.doantotnghiep.service.impl;

import com.doantotnghiep.converter.RoomDetailConverter;
import com.doantotnghiep.dto.RoomDetailDTO;
import com.doantotnghiep.entity.Room;
import com.doantotnghiep.entity.RoomDetail;
import com.doantotnghiep.entity.Student;
import com.doantotnghiep.entity.Test;
import com.doantotnghiep.repository.RoomDetailRepository;
import com.doantotnghiep.repository.RoomRepository;
import com.doantotnghiep.repository.StudentRepository;
import com.doantotnghiep.repository.TestRepository;
import com.doantotnghiep.service.IRoomDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class RoomDetailService implements IRoomDetailService {
    @Autowired
    private RoomDetailRepository roomDetailRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private RoomDetailConverter roomDetailConverter;

    @Override
    public List<RoomDetailDTO> findAllByRoomId(long roomId) {
        Room room = roomRepository.findById(roomId).get();
        List<RoomDetail> roomDetails = roomDetailRepository.findByRoom(room);
        List<RoomDetailDTO> roomDetailDTOs = new ArrayList<>();
        roomDetails.stream().forEach(roomDetail -> {
            roomDetailDTOs.add(roomDetailConverter.toDTO(roomDetail));
        });
        return roomDetailDTOs;
    }

    @Override
    public List<RoomDetailDTO> findAllByStudentId(long studentId) {
        Student student = studentRepository.findOneByStudentId(studentId);
        List<RoomDetail> roomDetails = roomDetailRepository.findByStudentId(student.getStudentId());
        List<RoomDetailDTO> roomDetailDTOs = new ArrayList<>();
        roomDetails.stream().forEach(roomDetail -> {
            roomDetailDTOs.add(roomDetailConverter.toDTO(roomDetail));
        });
        return roomDetailDTOs;
    }

    @Override
    public RoomDetailDTO findOneByRoomAndStudent(long roomId, long studentId) {
        Room room = roomRepository.findById(roomId).get();
        Student student = studentRepository.findById(studentId).get();
        RoomDetail roomDetail = roomDetailRepository.findOneByRoomAndStudent(room,student);
        RoomDetailDTO roomDetailDTO = roomDetailConverter.toDTO(roomDetail);
        return roomDetailDTO;
    }

    @Override
    public boolean testExitInRoom(long testId) {
        Test test = testRepository.findById(testId).get();
        List<RoomDetail> roomDetails = roomDetailRepository.findByTest(test);
        if (roomDetails.size() > 0) return true;
        else return false;
    }

    @Override
    public RoomDetailDTO findByRoomDetailKey(long roomId, long studentId, long testId) {
        return null;
    }

    @Override
    public void save(RoomDetailDTO roomDetailDTO) {
        roomDetailRepository.save(roomDetailConverter.toEntity(roomDetailDTO));
    }

    @Override
    public void delete(long roomId, long studentId, long testId) {

    }
}
