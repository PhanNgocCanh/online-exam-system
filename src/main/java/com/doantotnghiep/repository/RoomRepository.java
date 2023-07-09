package com.doantotnghiep.repository;

import com.doantotnghiep.entity.Exam;
import com.doantotnghiep.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Long> {
    List<Room> findByExam(Exam exam);
    Page<Room> findByExam(Exam exam, Pageable pageable);
}
