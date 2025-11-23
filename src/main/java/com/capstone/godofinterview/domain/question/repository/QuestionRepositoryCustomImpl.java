package com.capstone.godofinterview.domain.question.repository;

import static com.capstone.godofinterview.domain.question.entity.QQuestion.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.capstone.godofinterview.domain.question.dto.response.QuestionResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QuestionRepositoryCustomImpl implements QuestionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<QuestionResponse> findRandomQuestionsByJobId(Long jobId) {
        return queryFactory
            .select(Projections.constructor(QuestionResponse.class,
                question.id,
                question.content))
            .from(question)
            .where(
                jobIdEq(jobId),
                isNotDeleted()
            )
            .orderBy(Expressions.numberTemplate(Double.class, "function('RAND')").asc())
            .limit(5)
            .fetch();
    }

    @Override
    public List<QuestionResponse> findAllByJobIdAndDeletedAtIsNull(Long jobId) {
        return queryFactory
            .select(Projections.constructor(QuestionResponse.class,
                question.id,
                question.content))
            .from(question)
            .where(
                jobIdEq(jobId),
                isNotDeleted()
            )
            .fetch();
    }

    private BooleanExpression jobIdEq(Long jobId) {
        return question.job.id.eq(jobId);
    }

    private BooleanExpression isNotDeleted() {
        return question.deletedAt.isNull();
    }
}
