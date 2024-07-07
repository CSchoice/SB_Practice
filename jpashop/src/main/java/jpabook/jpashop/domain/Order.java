package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id; // 주문 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 주문 회원

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>(); // 주문 상품 목록

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배송 정보

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]

    // 연관 관계 메서드: 주문 회원 설정
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this); // 양방향 연관관계 설정
    }

    // 연관 관계 메서드: 주문 상품 추가
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this); // 양방향 연관관계 설정
    }

    // 연관 관계 메서드: 배송 정보 설정
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this); // 양방향 연관관계 설정
    }

    // 생성 메서드: 주문을 생성하는 정적 메서드입니다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order(); // Order 객체를 생성합니다.
        order.setMember(member); // 주문을 하는 회원을 설정합니다.
        order.setDelivery(delivery); // 배송 정보를 설정합니다.
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem); // 주문에 포함될 상품들을 추가합니다.
        }
        order.setStatus(OrderStatus.ORDER); // 주문 상태를 '주문 완료'로 설정합니다.
        order.setOrderDate(LocalDateTime.now()); // 주문 일시를 현재 시간으로 설정합니다.
        return order; // 생성된 주문 객체를 반환합니다.
    }

    // 비즈니스 로직: 주문을 취소하는 메서드입니다.
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다."); // 배송이 완료된 경우 취소할 수 없도록 예외를 발생시킵니다.
        }
        this.setStatus(OrderStatus.CANCEL); // 주문 상태를 '주문 취소'로 변경합니다.
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel(); // 각 주문 상품들에 대해 취소 처리를 수행합니다.
        }
    }

    // 조회 로직: 전체 주문 가격 조회
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice(); // 각 주문 상품의 총 가격을 합산
        }
        return totalPrice;
    }
}