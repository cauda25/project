package com.example.project.service;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class PythonEnvironmentService {

    private static final String VIRTUAL_ENV_PATH = "venv"; // 가상환경 폴더
    private static final String REQUIREMENTS_FILE_PATH = "requirements.txt"; // requirements.txt 경로

    // 가상환경이 없으면 생성하고 패키지 설치
    @EventListener(ApplicationReadyEvent.class)
    public void ensurePythonEnvironment() throws IOException, InterruptedException {
        if (!checkVirtualEnvExists()) {
            System.out.println("가상환경이 없습니다. 생성 중...");
            createVirtualEnv();
        }

        installPythonPackages();
    }

    // 가상환경 존재 여부 확인
    private boolean checkVirtualEnvExists() {
        File venvDir = new File(VIRTUAL_ENV_PATH);
        return venvDir.exists();
    }

    // 가상환경 생성
    private void createVirtualEnv() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("python", "-m", "venv", VIRTUAL_ENV_PATH);
        Process process = pb.start();
        process.waitFor();
        System.out.println("가상환경이 생성되었습니다.");
    }

    // requirements.txt에 정의된 패키지 설치
    private void installPythonPackages() throws IOException, InterruptedException {
        String pipPath = getPipPath();
        ProcessBuilder pb = new ProcessBuilder(pipPath, "install", "-r", REQUIREMENTS_FILE_PATH);
        Process process = pb.start();
        process.waitFor();
        System.out.println("requirements.txt에 명시된 패키지가 설치되었습니다.");
    }

    // 운영체제에 맞는 pip 경로 반환
    private String getPipPath() {
        String pipPath;
        // Windows 환경일 경우, 'Scripts' 폴더를 사용
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            pipPath = VIRTUAL_ENV_PATH + "/Scripts/pip"; // Windows
        } else {
            pipPath = VIRTUAL_ENV_PATH + "/bin/pip"; // Linux/macOS
        }
        return pipPath;
    }
}
