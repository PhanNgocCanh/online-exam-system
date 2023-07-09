package com.doantotnghiep.repository;

import com.doantotnghiep.entity.QuestionBank;
import com.doantotnghiep.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionBankRepository extends JpaRepository<QuestionBank,Long> {
    List<QuestionBank> findBySubject(Subject subject);
}
