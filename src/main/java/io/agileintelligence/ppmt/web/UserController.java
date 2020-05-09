package io.agileintelligence.ppmt.web;

import io.agileintelligence.ppmt.domain.UserBO;
import io.agileintelligence.ppmt.service.MapValidationErrorService;
import io.agileintelligence.ppmt.service.UserService;
import io.agileintelligence.ppmt.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);


    @GetMapping
    public ResponseEntity<?> getUserDetails(@NotBlank @RequestParam("username") String username, BindingResult result) {
        UserBO userBO = userService.getUserDetails(username);
        userValidator.validate(userBO, result);

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) {
            return errorMap;
        }

        return new ResponseEntity<>(userBO, HttpStatus.OK);
    }

    @PostMapping(path = "{userId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadUserProfileImage(@PathVariable("userId") Long userId,
                                       @RequestParam("file") MultipartFile file){
        LOGGER.info("File name: {} to be uploaded.", file.getName());
        userService.uploadProfilePhoto(userId, file);
        LOGGER.info(String.format("File name '%s' uploaded successfully.", file.getOriginalFilename()));
    }

    @GetMapping("{userId}/image")
    public void renderImageFromDB(@PathVariable Long userId, HttpServletResponse response) throws IOException {
        userService.renderImageFromDB(userId, response);
    }
}