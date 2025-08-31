package com.capstone.godofinterview.domain.job.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capstone.godofinterview.domain.job.entity.Job;
import com.capstone.godofinterview.domain.job.entity.JobCategory;

public interface JobRepository extends JpaRepository<Job, Long> {
    boolean existsByName(String name);

    @Query("""
        SELECT j FROM Job j 
        WHERE j.jobCategory = :jobCategory 
        AND j.deletedAt IS NULL
        ORDER BY j.name ASC
        """)
    List<Job> findJobsByCategory(@Param("jobCategory") JobCategory jobCategory);
}
