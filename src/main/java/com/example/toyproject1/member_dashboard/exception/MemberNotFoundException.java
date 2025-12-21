package com.example.toyproject1.member_dashboard.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(Long id) {
        super("회원이 존재하지 않습니다. id =" + id);
    }
}
