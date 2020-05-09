package io.agileintelligence.ppmt.service;

import io.agileintelligence.ppmt.domain.UserBO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserService {
    UserBO getUserDetails(String username);

    UserBO getUserDetails(Long userId);

    void uploadProfilePhoto(Long userId, MultipartFile file);

    void renderImageFromDB(Long id, HttpServletResponse response) throws IOException;
}