package com.example.project.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.project.dto.store.OrderItemDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
@Component
public class ImageServiceImpl implements ImageService {
    @Value("classpath:static/python/test.py")
    private Resource pythonFile;

    @Override
    public String createGifticon(OrderItemDto orderItemDto) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        String python;

        python = isWindows ? "python" : "python3";

        String jsonData = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonData = objectMapper.writeValueAsString(orderItemDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ProcessBuilder builder = new ProcessBuilder();
        builder.environment().put("PYTHONIOENCODING", "utf-8");
        Process process = null;

        try {
            // pythonFile을 사용하여 경로 가져오기
            process = builder.command(python, pythonFile.getFile().getAbsolutePath())
                    .redirectErrorStream(true)
                    .start();

            try (OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream(), "utf-8")) {
                writer.write(jsonData);
                writer.flush();
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            br.close();

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "error";
    }

}
