package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 스프링 빈으로 등록됨
@Transactional(readOnly = true) // 읽기 전용 트랜잭션 설정
@RequiredArgsConstructor // final 필드를 초기화하는 생성자를 자동으로 생성
public class MemberService {

    private final MemberRepository memberRepository; // MemberRepository 의존성 주입 받음

    // 회원 가입 메서드
    @Transactional // 읽기와 쓰기가 가능한 트랜잭션 설정
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member); // 회원 저장
        return member.getId(); // 저장된 회원의 ID 반환
    }

    // 중복 회원 검증 메서드
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName()); // 이름으로 회원 조회
        if (!findMembers.isEmpty()) { // 조회된 회원이 존재하면
            throw new IllegalStateException("이미 존재하는 회원입니다."); // 예외 발생
        }
    }

    // 회원 전체 조회 메서드
    public List<Member> findMembers() {
        return memberRepository.findAll(); // 모든 회원 조회
    }

    // 회원 단일 조회 메서드
    public Member findMember(Long memberId) {
        return memberRepository.findOne(memberId); // 특정 회원 조회
    }
}