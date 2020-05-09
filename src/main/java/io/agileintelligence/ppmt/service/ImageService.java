package io.agileintelligence.ppmt.service;

import io.agileintelligence.ppmt.domain.ImageBO;
import io.agileintelligence.ppmt.domain.UserBO;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void saveImageFile(UserBO user, MultipartFile file);

    ImageBO findByUserId(Long userID);
}
