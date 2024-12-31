package com.example.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // username을 기준으로 User를 조회하는 메소드
    // 반환 타입은 Optional<User>로, 해당 username에 해당하는 사용자가 없을 수도 있기 때문에 Optional로 감싸서 반환
    Optional<User> findByUsername(String username);
}
