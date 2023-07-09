package com.doantotnghiep.service;

import com.doantotnghiep.dto.RoomDetailDTO;

import java.util.List;

public interface IRoomDetailService {
    List<RoomDetailDTO> findAllByRoomId(long roomId);
    List<RoomDetailDTO> findAllByStudentId(long studentId);
    RoomDetailDTO findOneByRoomAndStudent(long roomId,long studentId);
    boolean testExitInRoom(long testId);

    RoomDetailDTO findByRoomDetailKey(long roomId, long studentId, long testId);
    void save(RoomDetailDTO roomDetailDTO);
    void delete(long roomId, long studentId, long testId);
}
