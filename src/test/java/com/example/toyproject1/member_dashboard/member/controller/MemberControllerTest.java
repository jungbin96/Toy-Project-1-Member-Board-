package com.example.toyproject1.member_dashboard.member.controller;

import com.example.toyproject1.member_dashboard.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(MemberController.class)
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


}
