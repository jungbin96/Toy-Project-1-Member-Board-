package com.example.toyproject1.member_dashboard.member.service;

import com.example.toyproject1.member_dashboard.member.dto.MemberCreateRequest;
import com.example.toyproject1.member_dashboard.member.dto.MemberResponse;
import com.example.toyproject1.member_dashboard.member.dto.MemberUpdateRequest;
import com.example.toyproject1.member_dashboard.member.entity.Member;
import com.example.toyproject1.member_dashboard.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

/*  왜 클래스 전체에 readOnly=true를 걸고, 쓰기 메서드만 @Transactional을 다시 붙일까?
    이유 1 — 안전함
    조회 메서드에서 실수로 엔티티를 수정해도 DB에 반영되지 않음.

    이유 2 — 실수 방지
    "쓰기 로직"인 메서드만 명확하게 @Transactional 붙이니까
        한눈에 어떤 메서드가 DB를 변경하는지 파악 가능.

    이유 3 — 성능 최적화
    조회용 메서드는 훨씬 빠르고 부하 적음.
*/
    private final MemberRepository memberRepository;

    //회원 생성
    @Transactional
    public MemberResponse createMember(MemberCreateRequest request){
        if(memberRepository.existsByEmail(request.getEmail())){
            throw  new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();

        Member saved = memberRepository.save(member);

        return MemberResponse.from(saved);

    }

    //회원 단일 조회
    public MemberResponse getMember(Long id){
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다. id =" +id));

        return MemberResponse.from(member);
    }

    //회원 전체 조회
    public List<MemberResponse> getMembers(){
        return memberRepository.findAll() //Member Entity
                .stream() //리스트를 데이터 흐름처럼 처리
                .map(MemberResponse::from) // 엔티티를 dto로 변경 Member Entity -> MemberResponse DTO
                .toList(); //리스트 반환
    }

    //회원 수정
    @Transactional
    public MemberResponse updateMember(Long id, MemberUpdateRequest request){
        Member member= memberRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("회원이 조재하지 않습니다.id=" + id));

        member.update(request.getName(), request.getEmail(), request.getStatus());
        return MemberResponse.from(member);
    }


    //회원 삭제
    @Transactional
    public void deleteMember(Long id){
        if(!memberRepository.existsById(id)){
            throw  new IllegalArgumentException("회원이 존재하지 않습니다.id="+id);
        }
        memberRepository.deleteById(id);
    }

}
