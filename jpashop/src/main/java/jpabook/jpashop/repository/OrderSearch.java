package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import jpabook.jpashop.domain.Order;

import java.util.List;

@Repository
public class OrderSearch {

    @PersistenceContext
    private EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        // 실제로는 orderSearch를 이용하여 동적 쿼리를 생성하거나 필요한 쿼리를 작성해야 함
        return em.createQuery("SELECT o FROM Order o", Order.class)
                .getResultList();
    }

    // 추가적인 검색 로직을 구현할 수 있음

}
