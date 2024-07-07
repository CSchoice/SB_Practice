package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        // JPQL 쿼리를 동적으로 생성하기 위한 StringBuilder 사용
        StringBuilder jpql = new StringBuilder("select o from Order o join o.member m");

        // 조건이 추가될 때마다 필요한 변수들을 선언
        boolean isFirstCondition = true;

        // 주문 상태 검색 조건 추가
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql.append(" where");
                isFirstCondition = false;
            } else {
                jpql.append(" and");
            }
            jpql.append(" o.status = :status");
        }

        // 회원 이름 검색 조건 추가
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql.append(" where");
                isFirstCondition = false;
            } else {
                jpql.append(" and");
            }
            jpql.append(" m.name like :name");
        }

        // JPQL을 이용하여 TypedQuery 생성
        TypedQuery<Order> query = em.createQuery(jpql.toString(), Order.class)
                .setMaxResults(1000); // 최대 1000건 제한

        // 파라미터 바인딩
        if (orderSearch.getOrderStatus() != null) {
            query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query.setParameter("name", "%" + orderSearch.getMemberName() + "%");
        }

        // 쿼리 실행 및 결과 반환
        return query.getResultList();
    }
}
