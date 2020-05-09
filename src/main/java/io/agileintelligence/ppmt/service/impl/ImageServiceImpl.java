package io.agileintelligence.ppmt.service.impl;

import io.agileintelligence.ppmt.domain.ImageBO;
import io.agileintelligence.ppmt.domain.UserBO;
import io.agileintelligence.ppmt.repository.ImageRepository;
import io.agileintelligence.ppmt.service.ImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Override
    public void saveImageFile(UserBO user, MultipartFile file) {
        try {

            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;
            for (byte b : file.getBytes()) {
                byteObjects[i++] = b;
            }

            ImageBO imageBO = new ImageBO(user, file.getContentType(), byteObjects);
            imageRepository.save(imageBO);

        } catch (IOException e) {
            //TODO: handle better
            LOGGER.debug("Error occurred {}", e);

            e.printStackTrace();
        }
    }

    @Override
    public ImageBO findByUserId(Long userId) {
        return imageRepository.findByUserBO(userId);
    }
}
