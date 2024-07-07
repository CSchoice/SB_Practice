package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item); // 신규 아이템일 경우 영속화하여 저장
        } else {
            em.merge(item); // 이미 존재하는 아이템일 경우 병합하여 업데이트
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id); // 주어진 ID에 해당하는 아이템을 조회하여 반환
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList(); // 모든 아이템을 조회하여 리스트로 반환
    }
}