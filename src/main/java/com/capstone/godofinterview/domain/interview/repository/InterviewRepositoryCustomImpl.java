package com.capstone.godofinterview.domain.interview.repository;

import static com.capstone.godofinterview.domain.interview.entity.QInterview.*;
import static com.capstone.godofinterview.domain.job.entity.QJob.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.capstone.godofinterview.domain.interview.dto.response.InterviewRecordResponse;
import com.capstone.godofinterview.domain.interview.entity.InterviewStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class InterviewRepositoryCustomImpl implements InterviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<InterviewRecordResponse> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, InterviewStatus status,
        Pageable pageable) {

        List<InterviewRecordResponse> contents = queryFactory
            .select(Projections.constructor(InterviewRecordResponse.class,
                interview.id,
                interview.job.name,
                interview.status,
                interview.createdAt
            ))
            .from(interview)
            .join(interview.job, job)
            .where(
                isNotDeleted(),
                userIdEq(userId),
                statusEq(status)
            )
            .orderBy(interview.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(interview.count())
            .from(interview)
            .join(interview.job, job)
            .where(
                isNotDeleted(),
                userIdEq(userId),
                statusEq(status)
            );

        return PageableExecutionUtils.getPage(contents, pageable, countQuery::fetchOne);
    }

    private BooleanExpression statusEq(InterviewStatus status) {
        return interview.status.eq(status);
    }

    private BooleanExpression userIdEq(Long userId) {
        return interview.user.id.eq(userId);
    }

    private BooleanExpression isNotDeleted() {
        return interview.deletedAt.isNull();
    }

}
