package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "order_price")
    private int orderPrice; // 주문 당시 가격

    private int count; // 주문 수량

    //== 생성 메서드 ==//
    /**
     * 주문 상품 생성 메서드
     *
     * @param item 주문하려는 상품
     * @param orderPrice 주문 당시 가격
     * @param count 주문 수량
     * @return 생성된 OrderItem 인스턴스
     */
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        item.removeStock(count); // 상품 재고 감소
        return orderItem;
    }

    //== 비즈니스 로직 ==//
    /**
     * 주문 취소 시, 상품의 재고를 복원하는 메서드
     */
    public void cancel() {
        getItem().addStock(count); // 주문 취소 시 상품 재고를 복원
    }

    //== 조회 로직 ==//
    /**
     * 주문 상품의 총 가격을 계산하는 메서드
     *
     * @return 주문 상품의 총 가격
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount(); // 주문 가격 * 주문 수량
    }
}