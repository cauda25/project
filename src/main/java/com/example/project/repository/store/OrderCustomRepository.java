package com.example.project.repository.store;

import java.util.List;

import com.example.project.entity.store.Order;
import com.querydsl.core.Tuple;

public interface OrderCustomRepository {

    List<Tuple> getOrderDetails(Long memberId);

}
