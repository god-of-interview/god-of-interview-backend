package com.capstone.godofinterview.domain.user.repository;

import static com.capstone.godofinterview.domain.user.entity.QUser.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.capstone.godofinterview.domain.user.dto.response.UserResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserResponse> searchUsers(Pageable pageable, String keyword) {

        List<UserResponse> users = queryFactory
            .select(Projections.constructor(UserResponse.class,
                user.id,
                user.nickname,
                user.gender,
                user.bio,
                user.birth
            ))
            .from(user)
            .where(
                isNotDeleted(),
                searchConditions(keyword))
            .orderBy(user.nickname.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(user.count())
            .from(user)
            .where(
                isNotDeleted(),
                searchConditions(keyword)
            );

        return PageableExecutionUtils.getPage(users, pageable, countQuery::fetchOne);
    }

    private BooleanExpression isNotDeleted() {
        return user.deletedAt.isNull();
    }

    private BooleanExpression searchConditions(String keyword) {
        return StringUtils.hasText(keyword) ? user.nickname.containsIgnoreCase(keyword) : null;
    }

}
