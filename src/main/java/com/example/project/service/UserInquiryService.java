package com.example.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.project.entity.User;
import com.example.project.entity.UserInquiry;
import com.example.project.repository.UserInquiryRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserInquiryService {

    private UserInquiryRepository userInquiryRepository;

    // 생성자 주입
    public UserInquiryService(UserInquiryRepository userInquiryRepository) {
        this.userInquiryRepository = userInquiryRepository;
    }

    // 사용자에 의한 문의 목록을 조회하는 메소드
    public List<UserInquiry> findByUser(User user) {
        return userInquiryRepository.findByUser(user);
    }

    // findByUser와 동일한 동작을 하는 메소드, 사용자에 의한 문의 목록을 반환
    public List<UserInquiry> getInquiriesByUser(User user) {
        return userInquiryRepository.findByUser(user);
    }

}
