package com.example.project.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    String user;

    @Autowired
    private JavaMailSender javaMailSender;

    public String mailSend(Long orderId, String email) {
        String test = "성공";
        try {
            MimeMessage m = javaMailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(m, true, "UTF-8");
            h.setFrom(user);
            h.setTo(email);
            h.setSubject("CINEPLANET 기프티콘");
            h.setText("CINEPLANET에서 구매한 기프티콘이 발송되었습니다.");

            // 폴더 내의 모든 파일을 가져오기
            File folder = new File("src/main/resources/static/img/gifticon/");
            File[] files = folder.listFiles(
                    (dir, name) -> name.toLowerCase().endsWith(".png") && name.startsWith(orderId.toString()));

            if (files != null) {
                for (File file : files) {
                    h.addAttachment(file.getName(), file); // 각 이미지를 첨부
                }
            }

            javaMailSender.send(m);

            if (files != null) {
                for (File file : files) {
                    if (file.getName().startsWith(orderId.toString())) {
                        if (file.delete()) {
                            System.out.println("Deleted: " + file.getName());
                        } else {
                            System.out.println("Failed to delete: " + file.getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            test = "실패";
        }
        return test;
    }

}
