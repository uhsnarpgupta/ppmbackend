package io.agileintelligence.ppmt.web;

import io.agileintelligence.ppmt.domain.UserBO;
import io.agileintelligence.ppmt.exceptions.AuthenticationException;
import io.agileintelligence.ppmt.payload.JWTLoginSucessReponse;
import io.agileintelligence.ppmt.payload.LoginRequest;
import io.agileintelligence.ppmt.payload.SignUpRequest;
import io.agileintelligence.ppmt.repository.UserRepository;
import io.agileintelligence.ppmt.security.JwtTokenProvider;
import io.agileintelligence.ppmt.security.JwtTokenResponse;
import io.agileintelligence.ppmt.security.JwtUserDetails;
import io.agileintelligence.ppmt.security.JwtUserDetailsService;
import io.agileintelligence.ppmt.service.MapValidationErrorService;
import io.agileintelligence.ppmt.validator.SignUpValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Objects;

import static io.agileintelligence.ppmt.security.SecurityConstants.HEADER_STRING;
import static io.agileintelligence.ppmt.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class JwtAuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationController.class);

    @Value("${jwt.http.request.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private SignUpValidator signUpValidator;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult result) {
        LOGGER.info("Login Request : {}", loginRequest);

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) return errorMap;

        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(loginRequest.getUsername());

        final String token = TOKEN_PREFIX + tokenProvider.generateToken(userDetails);

        return ResponseEntity.ok(new JWTLoginSucessReponse(true, token));
    }

    //@GetMapping("${jwt.refresh.token.uri}")
    @GetMapping("/refresh")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(HEADER_STRING);
        final String token = authToken.substring(7);
        String username = tokenProvider.getUsernameFromToken(token);
        JwtUserDetails user = (JwtUserDetails) jwtUserDetailsService.loadUserByUsername(username);

        if (tokenProvider.canTokenBeRefreshed(token)) {
            String refreshedToken = tokenProvider.refreshToken(token);
            return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest, BindingResult result) {
        LOGGER.info("SignUp Request : {}", signUpRequest);

        // Validate passwords match
        signUpValidator.validate(signUpRequest, result);

        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(result);
        if (errorMap != null) {
            return errorMap;
        }

        // Creating userBO's account
        UserBO userBO = new UserBO(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        // Make sure that password and confirmPassword match
        userBO.setPassword(passwordEncoder.encode(userBO.getPassword()));

        // We don't persist or show the confirmPassword
        userBO.setConfirmPassword("");

        userRepository.save(userBO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(signUpRequest.getUsername()).toUri();

        return ResponseEntity.created(location).body("User: " +signUpRequest.getName() + " registered successfully");
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("INVALID_CREDENTIALS", e);
        }
    }
}