package com.doantotnghiep.repository;

import com.doantotnghiep.entity.Chapter;
import com.doantotnghiep.entity.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter,Long> {
    List<Chapter> findByQuestionBank(QuestionBank questionBankId);
}
