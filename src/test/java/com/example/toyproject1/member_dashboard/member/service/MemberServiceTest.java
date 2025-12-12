package com.example.toyproject1.member_dashboard.member.service;

import com.example.toyproject1.member_dashboard.member.dto.MemberCreateRequest;
import com.example.toyproject1.member_dashboard.member.dto.MemberResponse;
import com.example.toyproject1.member_dashboard.member.entity.Member;
import com.example.toyproject1.member_dashboard.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    @Test
    void createMember_success(){
        //given
        MemberCreateRequest request = new MemberCreateRequest("표표표","xg961215@gmail.com");

        Member savedMember = Member.builder()
                .id(3L)
                .name("표표표")
                .email("xg961215@gmail.com")
                .build();

        //email 중복체크
        given(memberRepository.existsByEmail(request.getEmail())).willReturn(false);
        //savedMember 반환
        given(memberRepository.save(any(Member.class))).willReturn(savedMember);

        //when(실제 테스트실행)
        MemberResponse response = memberService.createMember(request);

        //then(결과 검증)
        assertThat(response.getId()).isEqualTo(3L);
        assertThat(response.getName()).isEqualTo("표표표");
        assertThat(response.getEmail()).isEqualTo("xg961215@gmail.com");

        //실제로 레포가 이렇게 호출됐는지 검증
        then(memberRepository).should().existsByEmail("xg961215@gmail.com");
        then(memberRepository).should().save(any(Member.class));
    }

    @Test
    void createMember_fail_whenEmailDuplicated(){
        //given
        MemberCreateRequest request = new MemberCreateRequest("표표표","xg961215@gmail.com");

        //이메일 이미 존재한다고 가정
        given(memberRepository.existsByEmail(request.getEmail())).willReturn(true);

        //when & then (예외 검증)
        assertThatThrownBy(()-> memberService.createMember(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용중인 이메일입니다.");

        //레포 호출 검증
        then(memberRepository).should().existsByEmail("xg961215@gmail.com");
        then(memberRepository).should(never()).save(any(Member.class));

    }

}
