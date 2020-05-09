package io.agileintelligence.ppmt.security;

import io.agileintelligence.ppmt.domain.UserBO;
import io.agileintelligence.ppmt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    static List<JwtUserDetails> inMemoryUserList = new ArrayList<>();

    @Autowired
    private UserRepository userRepository;

    static {
        inMemoryUserList.add(new JwtUserDetails(1L, "in28minutes",
                "$2a$10$3zHzb.Npv1hfZbLEU5qsdOju/tk2je6W6PnNnY.c1ujWPcZh4PL6e", "ROLE_USER_2"));

        inMemoryUserList.add(new JwtUserDetails(1L, "username",
                "$2a$10$S5wcHNWynS9f5LE6YTY.eeHldQ1VsZy5bcI.cMRtBy6xHNPlumrx.", "ROLE_USER_1"));
    }

    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<JwtUserDetails> findFirst = inMemoryUserList.stream()
                .filter(user -> user.getUsername().equals(username)).findFirst();

        if (!findFirst.isPresent()) {
            throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username));
        }

        return findFirst.get();
    }*/

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        UserBO userBO = userRepository.findByUsername(name);
        if (userBO == null) {
            throw new UsernameNotFoundException("UserBO not found with username or email : " + name);
        }

        return UserPrincipal.create(userBO);
    }

    @Transactional
    public UserDetails loadUserById(Long id)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        UserBO userBO = userRepository.findById(id).orElseThrow(() ->
                new UsernameNotFoundException("UserBO not found with username or email : " + id)
        );

        return UserPrincipal.create(userBO);
    }
}
