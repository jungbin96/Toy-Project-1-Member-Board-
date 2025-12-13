package com.example.toyproject1.member_dashboard.member.service;

import com.example.toyproject1.member_dashboard.member.dto.MemberCreateRequest;
import com.example.toyproject1.member_dashboard.member.dto.MemberResponse;
import com.example.toyproject1.member_dashboard.member.dto.MemberUpdateRequest;
import com.example.toyproject1.member_dashboard.member.entity.Member;
import com.example.toyproject1.member_dashboard.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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

    @Test
    void getMember_success(){
        //given
        Long memberId = 1L;

        Member member = Member.builder()
                .id(memberId)
                .name("표표표")
                .email("xg961215@gmail.com")
                .build();

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

        //when
        MemberResponse response = memberService.getMember(memberId);

        //then
        assertThat(response.getId()).isEqualTo(memberId);
        assertThat(response.getName()).isEqualTo("표표표");
        assertThat(response.getEmail()).isEqualTo("xg961215@gmail.com");

        then(memberRepository).should().findById(memberId);
    }

    @Test
    void getMember_fail_whenMemberNotFound(){
        //given
        Long memberId = 99L;

        given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(()-> memberService.getMember(memberId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원이 존재하지 않습니다. id =" +memberId);

        then(memberRepository).should().findById(memberId);
    }

    @Test
    void getMembers_success(){
        //given
        Member m1 = Member.builder()
                .id(1L)
                .name("홍길동")
                .email("aaa@naver.com")
                .build();

        Member m2 = Member.builder()
                .id(2L)
                .name("박박박")
                .email("bbb@naver.com")
                .build();

        given(memberRepository.findAll()).willReturn(List.of(m1,m2));

        //when
        List<MemberResponse> result = memberService.getMembers();

        //then
        assertThat(result).hasSize(2);

        // id/name/email이 매핑되었는지까지 검증(핵심)
        assertThat(result.getFirst().getId()).isEqualTo(1L);
        assertThat(result.getFirst().getName()).isEqualTo("홍길동");
        assertThat(result.getFirst().getEmail()).isEqualTo("aaa@naver.com");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getName()).isEqualTo("박박박");
        assertThat(result.get(1).getEmail()).isEqualTo("bbb@naver.com");

        then(memberRepository).should().findAll();
    }

    @Test
    void getMembers_success_whenEmpty(){
        //given
        given(memberRepository.findAll()).willReturn(List.of());

        //when
        List<MemberResponse> result = memberService.getMembers();

        //then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();

        then(memberRepository).should().findAll();

    }

    @Test
    void updateMember_success(){
        //given
        Long memberId = 1L;

        Member member = Member.builder()
                .id(memberId)
                .name("홍길동")
                .email("aaa@naver.com")
                .build();

        MemberUpdateRequest request =  new MemberUpdateRequest("표표표", "bbb@naver.com");

        given(memberRepository.findById(memberId))
                .willReturn(Optional.of(member));

        //when
        MemberResponse response = memberService.updateMember(memberId,request);

        //then: 응답이 수정된 값으로 나오는지
        assertThat(response.getId()).isEqualTo(memberId);
        assertThat(response.getName()).isEqualTo("표표표");
        assertThat(response.getEmail()).isEqualTo("bbb@naver.com");

        // then: 실제 엔티티도 수정됐는지(서비스 로직 검증)
        assertThat(member.getName()).isEqualTo("표표표");
        assertThat(member.getEmail()).isEqualTo("bbb@naver.com");

        then(memberRepository).should().findById(memberId);
    }

    @Test
    void updateMember_fail_whenMemberNotFound(){
        //given
        Long memberId = 99L;

        MemberUpdateRequest request =
                new MemberUpdateRequest("표표표", "bbb@naver.com");

        given(memberRepository.findById(memberId))
                .willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(()->memberService.updateMember(memberId,request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원이 조재하지 않습니다.id=" + memberId);

        then(memberRepository).should().findById(memberId);
    }
}
