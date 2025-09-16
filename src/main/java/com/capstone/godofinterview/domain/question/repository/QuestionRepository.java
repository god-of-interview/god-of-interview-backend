package com.capstone.godofinterview.domain.question.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capstone.godofinterview.domain.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query(value = """
        SELECT * FROM question 
        WHERE job_id = :jobId 
        AND deleted_at IS NULL 
        ORDER BY RAND() 
        LIMIT 5
        """, nativeQuery = true)
    List<Question> findRandomQuestionsByJobId(@Param("jobId") Long jobId);
}
