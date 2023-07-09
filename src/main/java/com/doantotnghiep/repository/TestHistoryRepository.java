package com.doantotnghiep.repository;

import com.doantotnghiep.entity.Student;
import com.doantotnghiep.entity.TestHistory;
import com.doantotnghiep.entity.TestHistoryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestHistoryRepository extends JpaRepository<TestHistory, TestHistoryKey> {
    List<TestHistory> findByStudent(Student student);
    @Query(value = "select * from TestHistory where studentId = :studentId and testId = :testId and questionId = :questionId",
            nativeQuery = true)
    TestHistory findByTestHistoryKey(@Param("studentId") long studentId,@Param("testId") long testId,@Param("questionId") long questionId);
}
