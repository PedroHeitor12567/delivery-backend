package com.pedroferreira.deliveryapplication.infrastructure.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    // Quando tiver repositório:
    // private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        // 🔴 EXEMPLO TEMPORÁRIO
        if (!username.equals("admin")) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        return User.builder()
                .username("admin")
                .password("{noop}admin123") // apenas teste
                .roles("ADMIN")
                .build();
    }
}
