package com.capstone.godofinterview.domain.job.repository;

import static com.capstone.godofinterview.domain.job.entity.QJob.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.capstone.godofinterview.domain.job.dto.response.JobResponse;
import com.capstone.godofinterview.domain.job.entity.JobCategory;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JobRepositoryCustomImpl implements JobRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<JobResponse> findJobsByCategory(JobCategory jobCategory) {

        return queryFactory
            .select(Projections.constructor(JobResponse.class,
                job.id,
                job.jobCategory,
                job.name
            ))
            .from(job)
            .where(
                jobCategoryEq(jobCategory),
                isNotDeleted()
            )
            .orderBy(job.name.asc())
            .fetch();
    }

    private BooleanExpression isNotDeleted() {
        return job.deletedAt.isNull();
    }

    private BooleanExpression jobCategoryEq(JobCategory jobCategory) {
        return job.jobCategory.eq(jobCategory);
    }
}
