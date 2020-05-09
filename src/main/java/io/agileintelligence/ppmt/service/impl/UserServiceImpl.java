package io.agileintelligence.ppmt.service.impl;

import io.agileintelligence.ppmt.domain.ImageBO;
import io.agileintelligence.ppmt.domain.UserBO;
import io.agileintelligence.ppmt.repository.UserRepository;
import io.agileintelligence.ppmt.service.ImageService;
import io.agileintelligence.ppmt.service.UserService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.springframework.util.MimeTypeUtils.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageService imageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Override
    public UserBO getUserDetails(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserBO getUserDetails(Long userId) {
        return userRepository.getById(userId);
    }

    @Override
    public void uploadProfilePhoto(Long userId, MultipartFile file) {
        // 1. Check if image is not empty
        isFileEmpty(file);
        // 2. If file is an image
        isImage(file);

        // 3. The user exists in our database
        UserBO user = getUserDetails(userId);

        if (user != null) {
            imageService.saveImageFile(user, file);
            ImageBO image = imageService.findByUserId(userId);
            user.setImage(image);
            userRepository.save(user);
            LOGGER.info("Image Successfully uploaded for userId : {}", userId);
        }
    }

    @Override
    public void renderImageFromDB(Long id, HttpServletResponse response) throws IOException {
        UserBO user = getUserDetails(id);

        if (user != null && user.getImage() != null) {
            String contentType = user.getImage().getType();
            LOGGER.info("Image Content Type {}", contentType);

            Byte[] image = user.getImage().getImage();
            if (image != null) {
                byte[] byteArray = new byte[image.length];
                int i = 0;
                LOGGER.info("Rendered profile image {}", image.length);

                for (Byte wrappedByte : image) {
                    byteArray[i++] = wrappedByte; //auto unboxing
                }

                response.setContentType(contentType);
                InputStream is = new ByteArrayInputStream(byteArray);
                IOUtils.copy(is, response.getOutputStream());
            }
        }
    }

    /*private Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }*/

    private void isImage(MultipartFile file) {
        if (!Arrays.asList(IMAGE_JPEG, IMAGE_PNG, IMAGE_GIF).contains(file.getContentType())) {
            throw new IllegalStateException("File must be an image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("Cannot upload empty file [ " + file.getSize() + "]");
        }
    }
}
