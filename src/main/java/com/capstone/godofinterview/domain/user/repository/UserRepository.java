package com.capstone.godofinterview.domain.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capstone.godofinterview.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);

    @Query("""
        SELECT u FROM User u 
        WHERE u.deletedAt IS NULL 
        AND (:keyword = '' OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))) 
        ORDER BY u.nickname ASC
        """)
    Page<User> searchUsers(Pageable pageable, @Param("keyword") String keyword);
}
