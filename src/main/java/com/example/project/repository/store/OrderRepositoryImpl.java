package com.example.project.repository.store;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.example.project.entity.constant.OrderStatus;
import com.example.project.entity.store.Order;
import com.example.project.entity.store.QOrder;
import com.example.project.entity.store.QOrderItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;

public class OrderRepositoryImpl extends QuerydslRepositorySupport implements OrderCustomRepository {

    public OrderRepositoryImpl() {
        super(Order.class);
    }

    @Override
    public List<Tuple> getOrderDetails(Long memberId) {
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;

        JPQLQuery<Tuple> query = from(orderItem)
                .join(orderItem.order, order)
                .fetchJoin()
                .select(order, orderItem);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(order.member.mid.eq(memberId));
        builder.and(order.status.eq(OrderStatus.COMPLETED));

        query.where(builder);

        return query.fetch();

    }

}
