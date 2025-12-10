package com.example.toyproject1.member_dashboard.member.controller;

import com.example.toyproject1.member_dashboard.member.dto.MemberCreateRequest;
import com.example.toyproject1.member_dashboard.member.dto.MemberResponse;
import com.example.toyproject1.member_dashboard.member.dto.MemberUpdateRequest;
import com.example.toyproject1.member_dashboard.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {
    private final MemberService memberService;

    //회원 생성
    @PostMapping
    public ResponseEntity<MemberResponse> createMember(@RequestBody MemberCreateRequest request){
        MemberResponse response = memberService.createMember(request);
        return ResponseEntity
                .created(URI.create("/api/members/"+response.getId()))
                .body(response);
    }
    //회원 전체 조회
    @GetMapping
    public ResponseEntity<List<MemberResponse>> getAllMembers(){
        return ResponseEntity.ok(memberService.getMembers());
    }

    //회원 단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long id){
        return ResponseEntity.ok(memberService.getMember(id));
    }

    //회원 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long id, @RequestBody MemberUpdateRequest request){
        return ResponseEntity.ok(memberService.updateMember(id,request));
    }

    //회원 정보 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id){
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

}
