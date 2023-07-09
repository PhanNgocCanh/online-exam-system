package com.doantotnghiep.repository;

import com.doantotnghiep.entity.Answer;
import com.doantotnghiep.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer,Long> {
    Answer findOneByAnswerId(long answerId);
    List<Answer> findByQuestion(Question question);
}
