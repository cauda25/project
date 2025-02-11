package com.example.project.repository.store;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.project.entity.Member;
import com.example.project.entity.constant.OrderStatus;
import com.example.project.entity.store.Order;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {

    List<Order> findByMemberMidAndStatus(Long memberId, OrderStatus orderStatus);

    boolean existsByMemberMidAndStatus(Long memberId, OrderStatus orderStatus);

    List<Order> findByUpdateDateBefore(LocalDateTime updateDate);

    boolean existsByIdAndStatus(Long orderId, OrderStatus orderStatus);

    @Modifying
    @Transactional
    @Query("DELETE FROM Order o WHERE o.member.mid = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

}
