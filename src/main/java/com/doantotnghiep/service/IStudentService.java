package com.doantotnghiep.service;

import com.doantotnghiep.dto.StudentDTO;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface IStudentService {
    List<StudentDTO> findAll(Pageable pageable);
    List<StudentDTO> findByClass(long classId,Pageable pageable);
    StudentDTO findOneById(long studentId);
    StudentDTO findOneByEmail(String email);
    List<StudentDTO> findByClass(long classId);

    long getTotalItem();
    void save(StudentDTO studentDTO);
    List<StudentDTO> readStudentFromFile(String fileName) throws IOException;
    void delete(long studentId);
}
