package com.capstone.godofinterview.domain.job.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.godofinterview.domain.job.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long>, JobRepositoryCustom {
    boolean existsByName(String name);

    // @Query("""
    //     SELECT j FROM Job j
    //     WHERE j.jobCategory = :jobCategory
    //     AND j.deletedAt IS NULL
    //     ORDER BY j.name ASC
    //     """)
    // List<Job> findJobsByCategory(@Param("jobCategory") JobCategory jobCategory);
}
