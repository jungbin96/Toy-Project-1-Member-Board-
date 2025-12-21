package com.example.toyproject1.member_dashboard.member.controller;

import com.example.toyproject1.member_dashboard.exception.MemberNotFoundException;
import com.example.toyproject1.member_dashboard.global.GlobalExceptionHandler;
import com.example.toyproject1.member_dashboard.member.dto.MemberResponse;
import com.example.toyproject1.member_dashboard.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.context.annotation.Import;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)

public class MemberControllerTest {

    /*[ MockMvc ]
            ↓ (가짜 HTTP 요청)
            [ MemberController ]   ← 실제
            ↓
            [ MemberService ]      ← Mockito 가짜
            */


    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    MemberService memberService;


    @Test
    void 회원_단건_조회_성공() throws Exception {
        //given
        MemberResponse response = MemberResponse.builder()
                .id(1L)
                .email("aaa@naver.com")
                .name("pyo")
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        given(memberService.getMember(1L)).willReturn(response);

        //when & then
        mockMvc.perform(get("/api/members/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("pyo"))
                .andExpect(jsonPath("$.email").value("aaa@naver.com"));
    }

    @Test
    void 회원_단건_조회_실패_존재하지_않음() throws Exception {
        given(memberService.getMember(999L)).willThrow(new MemberNotFoundException(999L));

        mockMvc.perform(get("/api/members/{id}",999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("회원이 존재하지 않습니다. id =999"));
    }

}
