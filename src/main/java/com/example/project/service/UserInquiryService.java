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

    public UserInquiryService(UserInquiryRepository userInquiryRepository) {
        this.userInquiryRepository = userInquiryRepository;
    }

    public List<UserInquiry> findByUser(User user) {
        return userInquiryRepository.findByUser(user);
    }
}
