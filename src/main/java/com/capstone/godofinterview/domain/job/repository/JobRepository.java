package com.capstone.godofinterview.domain.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.godofinterview.domain.job.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
    boolean existsByName(String name);
}
