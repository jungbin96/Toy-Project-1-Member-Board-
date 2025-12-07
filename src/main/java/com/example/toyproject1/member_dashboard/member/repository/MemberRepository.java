package com.example.toyproject1.member_dashboard.member.repository;

import com.example.toyproject1.member_dashboard.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);
}
