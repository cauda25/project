package com.example.project.admin.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.project.admin.Entity.Admin;
import com.example.project.admin.Entity.UserEntity;
import com.example.project.admin.Entity.constant.StatusRole;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

   Optional<UserEntity> findByUserId(String username);

   @Query("SELECT u FROM UserEntity u WHERE u.lastLogin < :lastTime AND u.statusRole = :statusRole")
   List<UserEntity> findInactiveUsers(LocalDateTime lastTime, StatusRole statusRole);

   // List<UserEntity> findByStatus(StatusRole StatusRole);

}
