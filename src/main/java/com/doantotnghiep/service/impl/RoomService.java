package com.doantotnghiep.service.impl;

import com.doantotnghiep.converter.RoomConverter;
import com.doantotnghiep.dto.RoomDTO;
import com.doantotnghiep.entity.Exam;
import com.doantotnghiep.entity.Room;
import com.doantotnghiep.repository.ExamRepository;
import com.doantotnghiep.repository.RoomRepository;
import com.doantotnghiep.service.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService implements IRoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomConverter roomConverter;
    @Autowired
    private ExamRepository examRepository;
    private long totalItem;
    @Override
    public List<RoomDTO> findAll(Pageable pageable) {
        List<Room> rooms = roomRepository.findAll(pageable).getContent();
        List<RoomDTO> roomDTOs = new ArrayList<>();
        rooms.stream().forEach(room -> {
            roomDTOs.add(roomConverter.toDTO(room));
        });
        this.totalItem = roomRepository.count();
        return roomDTOs;
    }

    @Override
    public List<RoomDTO> findByExam(long examId, Pageable pageable) {
        Exam exam = examRepository.findById(examId).get();
        List<Room> rooms = roomRepository.findByExam(exam,pageable).getContent();
        List<RoomDTO> roomDTOs = new ArrayList<>();
        rooms.stream().forEach(room -> {
            roomDTOs.add(roomConverter.toDTO(room));
        });
        this.totalItem = roomRepository.findByExam(exam,pageable).getTotalElements();
        return roomDTOs;
    }

    @Override
    public List<RoomDTO> findAll(long examId) {
        Exam exam = examRepository.findById(examId).get();
        List<Room> rooms = roomRepository.findByExam(exam);
        List<RoomDTO> roomDTOs = new ArrayList<>();
        rooms.stream().forEach(room -> {
            roomDTOs.add(roomConverter.toDTO(room));
        });
        return roomDTOs;
    }

    @Override
    public RoomDTO findOneById(long roomId) {
        Room room = roomRepository.findById(roomId).get();
        RoomDTO roomDTO = roomConverter.toDTO(room);
        return roomDTO;
    }

    @Override
    public long getTotalItem() {
        return this.totalItem;
    }

    @Override
    public void save(RoomDTO roomDTO) {
        roomRepository.save(roomConverter.toEntity(roomDTO));
    }

    @Override
    public void delete(long roomId) {

    }
}
