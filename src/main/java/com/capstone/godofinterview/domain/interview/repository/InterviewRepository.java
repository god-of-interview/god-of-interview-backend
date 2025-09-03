package com.capstone.godofinterview.domain.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.godofinterview.domain.interview.entity.Interview;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
}
