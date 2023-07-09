package com.doantotnghiep.repository;

import com.doantotnghiep.entity.Question;
import com.doantotnghiep.entity.Test;
import com.doantotnghiep.entity.TestDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestDetailRepository extends JpaRepository<TestDetail, Long> {
    TestDetail findOneByTestAndQuestion(Test test, Question question);
    @Query(value = "select * from TestDetail where TestId = :testId",nativeQuery = true)
    List<TestDetail> findAllByTestId(@Param("testId") long testId);
    @Query(value = "select * from TestDetail where QuestionId = :questionId",nativeQuery = true)
    List<TestDetail> findAllByQuestionId(@Param("questionId") long questionId);
}
