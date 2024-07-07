package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository // 스프링 빈으로 등록됨
public class MemberRepository {

    @PersistenceContext // 엔티티 매니저 주입
    private EntityManager em;

    // 회원 저장 메서드
    public void save(final Member member) {
        em.persist(member); // 전달받은 회원 엔티티를 영속화하여 저장
    }

    // ID로 회원 조회 메서드
    public Member findOne(final Long id) {
        return em.find(Member.class, id); // 주어진 ID에 해당하는 회원을 조회하여 반환
    }

    // 모든 회원 조회 메서드
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList(); // 모든 회원을 조회하여 리스트로 반환
    }

    // 이름으로 회원 조회 메서드
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name) // 이름 매개변수 설정
                .getResultList(); // 해당 이름을 가진 모든 회원을 조회하여 리스트로 반환
    }
}
