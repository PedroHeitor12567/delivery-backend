package com.pedroferreira.deliveryapplication.application.service;

import com.pedroferreira.deliveryapplication.application.dto.requests.LoginRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.AuthResponse;
import com.pedroferreira.deliveryapplication.application.usecase.BusinessException;
import com.pedroferreira.deliveryapplication.domain.entity.Admin;
import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import com.pedroferreira.deliveryapplication.domain.entity.Seller;
import com.pedroferreira.deliveryapplication.domain.enuns.UserRole;
import com.pedroferreira.deliveryapplication.domain.repository.AdminRepository;
import com.pedroferreira.deliveryapplication.domain.repository.CustomerRepository;
import com.pedroferreira.deliveryapplication.domain.repository.SellerRepository;
import com.pedroferreira.deliveryapplication.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticate(LoginRequest request) {
        log.info("Autenticando usuário: {}", request.getEmail());

        Optional<Customer> customerOpt = customerRepository.findByEmail(request.getEmail());
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            validatePassword(request.getPassword(), customer.getPassword());
            return buildAuthResponse(customer.getId(), customer.getUsername(), customer.getEmail(), UserRole.CUSTOMER);
        }

        Optional<Seller> sellerOpt = sellerRepository.findByEmail(request.getEmail());
        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get();
            validatePassword(request.getPassword(), seller.getPassword());
            return buildAuthResponse(seller.getId(), seller.getUsername(), seller.getEmail(), UserRole.SELLER);
        }

        Optional<Admin> adminOpt = adminRepository.findByEmail(request.getEmail());
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            validatePassword(request.getPassword(), admin.getPassword());
            return buildAuthResponse(admin.getId(), admin.getUsername(), admin.getEmail(), UserRole.ADMIN);
        }

        throw new BusinessException("Credenciais inválidas");
    }

    public AuthResponse refreshToken(String oldToken) {
        String username = jwtService.extractUsername(oldToken);
        String role = jwtService.extractRole(oldToken);

        String newToken = jwtService.generateToken(username, role);

        return AuthResponse.builder()
                .token(newToken)
                .type("Bearer")
                .role(role)
                .username(username)
                .build();
    }

    private void validatePassword(String rawPassword, String encodedPassword) {

        if (!rawPassword.equals(encodedPassword)) {
            throw new BusinessException("Credenciais inválidas");
        }
    }

    private AuthResponse buildAuthResponse(Long userId, String username, String email, UserRole role) {
        String token = jwtService.generateToken(username, role.name());

        log.info("Login bem-sucedido para {} com role {}", email, role);

        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .role(role.name())
                .userId(userId)
                .username(username)
                .email(email)
                .build();
    }
}
