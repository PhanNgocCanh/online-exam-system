package com.doantotnghiep.service;

import com.doantotnghiep.dto.CLazzDTO;

import java.util.List;

public interface IClazzService {
    List<CLazzDTO> findAll();
    CLazzDTO findOneById(long classId);
    void save(CLazzDTO cLazzDTO);
    void delete(long classId);
}
