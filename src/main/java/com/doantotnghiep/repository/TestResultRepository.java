package com.doantotnghiep.repository;

import com.doantotnghiep.entity.Student;
import com.doantotnghiep.entity.Test;
import com.doantotnghiep.entity.TestResult;
import com.doantotnghiep.entity.TestResultKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultRepository extends JpaRepository<TestResult, TestResultKey> {
    TestResult findOneByStudentAndTest(Student student, Test test);
}
