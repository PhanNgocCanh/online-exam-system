package com.doantotnghiep.repository;

import com.doantotnghiep.entity.Chapter;
import com.doantotnghiep.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question,Long> {
    List<Question> findByChapter(Chapter chapter);
    List<Question> findByChapterAndLevel(Chapter chapter,String level);
    @Query(value = "Select * from Question where (:chapterId = 0 or chapterId = :chapterId) and " +
            "(:level is null or answerLevel = :level) and (:keyword is null or questionContent like %:keyword%)",nativeQuery = true)
    Page<Question> findBySearchAndFilter(@Param("chapterId") long chapterId,@Param("level") String level,
                                         @Param("keyword") String keyword, Pageable pageable);
}
