package com.capstone.godofinterview.domain.question.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.godofinterview.domain.question.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>, QuestionRepositoryCustom {

    // @Query(value = """
    //     SELECT * FROM question
    //     WHERE job_id = :jobId
    //     AND deleted_at IS NULL
    //     ORDER BY RAND()
    //     LIMIT 5
    //     """, nativeQuery = true)
    // List<Question> findRandomQuestionsByJobId(@Param("jobId") Long jobId);
}
