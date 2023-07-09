package com.doantotnghiep.repository;

import com.doantotnghiep.entity.Clazz;
import com.doantotnghiep.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Page<Student> findByClazz(Clazz clazz, Pageable pageable);
    List<Student> findByClazz(Clazz clazz);
    Student findOneByStudentId(long studentId);
    Student findOneByEmail(String email);
}
