package com.alshadows.product.config;

import com.alshadows.product.model.Role;
import com.alshadows.product.model.User;
import com.alshadows.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .build();

            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .role(Role.ROLE_USER)
                    .build();

            userRepository.save(admin);
            userRepository.save(user);
        }
    }
}

