package com.doantotnghiep.service.impl;

import com.doantotnghiep.converter.StudentConverter;
import com.doantotnghiep.dto.StudentDTO;
import com.doantotnghiep.entity.Clazz;
import com.doantotnghiep.entity.Student;
import com.doantotnghiep.repository.ClazzRepository;
import com.doantotnghiep.repository.StudentRepository;
import com.doantotnghiep.service.IStudentService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StudentService implements IStudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentConverter studentConverter;
    @Autowired
    private ClazzRepository clazzRepository;
    private long total;
    @Override
    public List<StudentDTO> findAll(Pageable pageable) {
        List<Student> students = studentRepository.findAll(pageable).getContent();
        List<StudentDTO> studentDTOs = new ArrayList<>();
        students.stream().forEach((student)->{
            studentDTOs.add(studentConverter.toDTO(student));
        });
        total = studentRepository.count();
        return studentDTOs;
    }

    @Override
    public List<StudentDTO> findByClass(long classId,Pageable pageable) {
        Clazz clazz = clazzRepository.findById(classId).get();
        List<Student> students = studentRepository.findByClazz(clazz,pageable).getContent();
        List<StudentDTO> studentDTOs = new ArrayList<>();
        students.stream().forEach((student)->{
            studentDTOs.add(studentConverter.toDTO(student));
        });
        total = studentRepository.findByClazz(clazz,pageable).getTotalElements();
        return studentDTOs;
    }
    @Override
    public List<StudentDTO> findByClass(long classId) {
        Clazz clazz = clazzRepository.findById(classId).get();
        List<Student> students = studentRepository.findByClazz(clazz);
        List<StudentDTO> studentDTOs = new ArrayList<>();
        students.stream().forEach((student)->{
            studentDTOs.add(studentConverter.toDTO(student));
        });
        return studentDTOs;
    }
    @Override
    public long getTotalItem() {
        return this.total;
    }

    @Override
    public StudentDTO findOneById(long studentId) {
        Student student = studentRepository.findOneByStudentId(studentId);
        if(student!=null) {
            StudentDTO studentDTO = studentConverter.toDTO(student);
            return studentDTO;
        }else{
            return null;
        }
    }

    @Override
    public StudentDTO findOneByEmail(String email) {
        Student student = studentRepository.findOneByEmail(email);
        StudentDTO studentDTO = studentConverter.toDTO(student);
        return studentDTO;
    }

    @Override
    public void save(StudentDTO studentDTO) {
        studentRepository.save(studentConverter.toEntity(studentDTO));
    }

    @Override
    public List<StudentDTO> readStudentFromFile(String fileName) throws IOException {
        List<StudentDTO> studentDTOs = new ArrayList<>();
        Workbook workbook = null;
        FileInputStream fis = null;
        try{
            Resource resource = new ClassPathResource(fileName);
            fis = new FileInputStream(resource.getFile());
            workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);
            int index = 0;
            for(Row row : sheet){
                if(index<3){
                    index++;
                    continue;
                }
                StudentDTO studentDTO = new StudentDTO();
                studentDTO.setFullName(row.getCell(0).toString());
                studentDTO.setGender((int)row.getCell(1).getNumericCellValue());
                studentDTO.setBirthday(toDate(row.getCell(2).toString()));
                studentDTO.setEmail(row.getCell(3).toString());
                studentDTOs.add(studentDTO);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            if(fis != null) fis.close();
        }
        return studentDTOs;
    }
    public Date toDate(String birthday){
        try {
            Date date = new SimpleDateFormat("dd/MM/yyyy").parse(birthday);
            return date;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    @Override
    public void delete(long studentId) {

    }
}
