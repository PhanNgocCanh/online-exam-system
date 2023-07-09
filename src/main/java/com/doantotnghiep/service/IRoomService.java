package com.doantotnghiep.service;

import com.doantotnghiep.dto.RoomDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRoomService {
    List<RoomDTO> findAll(Pageable pageable);
    List<RoomDTO> findByExam(long examId,Pageable pageable);
    List<RoomDTO> findAll(long examId);
    RoomDTO findOneById(long roomId);
    long getTotalItem();
    void save(RoomDTO roomDTO);
    void delete(long roomId);
}
