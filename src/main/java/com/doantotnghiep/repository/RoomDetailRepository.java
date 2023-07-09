package com.doantotnghiep.repository;

import com.doantotnghiep.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomDetailRepository extends JpaRepository<RoomDetail, RoomDetailKey> {
    List<RoomDetail> findByRoom(Room room);
    List<RoomDetail> findByTest(Test test);
    List<RoomDetail> findByStudent(Student student);
    RoomDetail findOneByRoomAndStudent(Room room,Student student);
    @Query(value = "select * from RoomDetail rd inner join Room r on rd.roomId=r.roomId where rd.studentId = :studentId order by r.Start desc",nativeQuery = true)
    List<RoomDetail> findByStudentId(@Param("studentId") long studentId);
}
