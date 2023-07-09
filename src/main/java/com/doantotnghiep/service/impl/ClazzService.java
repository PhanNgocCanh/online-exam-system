package com.doantotnghiep.service.impl;

import com.doantotnghiep.dto.CLazzDTO;
import com.doantotnghiep.entity.Clazz;
import com.doantotnghiep.repository.ClazzRepository;
import com.doantotnghiep.service.IClazzService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClazzService implements IClazzService {
    @Autowired
    private ClazzRepository clazzRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CLazzDTO> findAll() {
        List<CLazzDTO> cLazzDTOs = new ArrayList<>();
        List<Clazz> clazzes = clazzRepository.findAll();
        clazzes.stream().forEach((clazz)->{
            cLazzDTOs.add(modelMapper.map(clazz,CLazzDTO.class));
        });
        return cLazzDTOs;
    }

    @Override
    public CLazzDTO findOneById(long classId) {
        Clazz clazz = clazzRepository.findById(classId).get();
        CLazzDTO cLazzDTO = modelMapper.map(clazz,CLazzDTO.class);
        return cLazzDTO;
    }

    @Override
    public void save(CLazzDTO cLazzDTO) {

    }

    @Override
    public void delete(long classId) {

    }
}
