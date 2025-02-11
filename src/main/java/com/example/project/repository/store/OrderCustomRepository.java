package com.example.project.repository.store;

import java.util.List;

import com.querydsl.core.Tuple;

public interface OrderCustomRepository {

    List<Tuple> getOrderDetails(Long memberId);

}
