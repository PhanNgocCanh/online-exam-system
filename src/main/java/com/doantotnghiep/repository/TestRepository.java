package com.doantotnghiep.repository;

import com.doantotnghiep.entity.Subject;
import com.doantotnghiep.entity.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, Long> {
    Page<Test> findBySubject(Subject subject, Pageable pageable);
    List<Test> findBySubject(Subject subject);
}
