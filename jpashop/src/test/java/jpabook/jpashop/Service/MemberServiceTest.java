package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class) // Spring Runner를 사용하여 테스트 실행
@SpringBootTest // Spring Boot 애플리케이션 컨텍스트를 로드하여 테스트
@Transactional // 각 테스트 메서드에 대해 트랜잭션을 시작하고 테스트 후 롤백
public class MemberServiceTest {

    @Autowired
    MemberService memberService; // MemberService 의존성 주입

    @Autowired
    MemberRepository memberRepository; // MemberRepository 의존성 주입

    @Test
    // @Rollback(false) // 롤백을 하지 않고 데이터를 영구적으로 저장하려면 주석 해제
    public void 회원가입() {
        // Given
        Member member = new Member();
        member.setName("kim");

        // When
        Long saveId = memberService.join(member); // 회원 가입 서비스 호출

        // Then
        assertEquals(member, memberRepository.findOne(saveId)); // 회원이 정상적으로 저장되었는지 확인
    }

    @Test(expected = IllegalStateException.class) // 해당 예외가 발생해야 테스트 통과
    public void 중복_회원_예외() {
        // Given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // When
        memberService.join(member1); // 첫 번째 회원 가입 시도
        memberService.join(member2); // 두 번째 회원 가입 시도 - 예외 발생해야 함

        fail("예외가 발생해야 한다"); // 예외가 발생하지 않으면 테스트 실패
    }
}
