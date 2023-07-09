package com.doantotnghiep.converter;

import com.doantotnghiep.dto.RoomDetailDTO;
import com.doantotnghiep.entity.*;
import com.doantotnghiep.service.IRoomService;
import com.doantotnghiep.service.IStudentService;
import com.doantotnghiep.service.ITestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoomDetailConverter {
    @Autowired
    private IRoomService roomService;
    @Autowired
    private IStudentService studentService;
    @Autowired
    private ITestService testService;
    @Autowired
    private ModelMapper modelMapper;

    public RoomDetail toEntity(RoomDetailDTO roomDetailDTO){
        RoomDetail roomDetail = modelMapper.map(roomDetailDTO,RoomDetail.class);
        roomDetail.setRoom(modelMapper.map(roomService.findOneById(roomDetailDTO.getRoomId()), Room.class));
        roomDetail.setStudent(modelMapper.map(studentService.findOneById(roomDetailDTO.getStudentId()), Student.class));
        roomDetail.setTest(modelMapper.map(testService.findOneById(roomDetailDTO.getTestId()), Test.class));
        RoomDetailKey roomDetailKey = new RoomDetailKey();
        roomDetailKey.setRoomId(roomDetailDTO.getRoomId());
        roomDetailKey.setStudentId(roomDetailDTO.getStudentId());
        roomDetailKey.setTestId(roomDetailDTO.getTestId());
        roomDetail.setRoomDetailKey(roomDetailKey);
        return roomDetail;
    }

    public RoomDetailDTO toDTO(RoomDetail roomDetail){
        RoomDetailDTO roomDetailDTO = modelMapper.map(roomDetail,RoomDetailDTO.class);
        roomDetailDTO.setRoomId(roomDetail.getRoom().getRoomId());
        roomDetailDTO.setStudentId(roomDetail.getStudent().getStudentId());
        roomDetailDTO.setTestId(roomDetail.getTest().getTestId());
        return roomDetailDTO;
    }
}
