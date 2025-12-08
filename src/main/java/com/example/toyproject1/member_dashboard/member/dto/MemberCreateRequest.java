package com.example.toyproject1.member_dashboard.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

}
