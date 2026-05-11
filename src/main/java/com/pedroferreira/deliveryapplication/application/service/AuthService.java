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

    public AuthResponse authenticate(LoginRequest request) {
        log.info("Autenticando usuário: {}", request.getEmail());

        Optional<Customer> customerOpt = customerRepository.findByEmail(request.getEmail());
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            validatePassword(request.getPassword(), customer.getPassword());
            return buildAuthResponse(customer.getId(), customer.getUsername(),
                    customer.getEmail(), UserRole.CUSTOMER);
        }

        Optional<Seller> sellerOpt = sellerRepository.findByEmail(request.getEmail());
        if (sellerOpt.isPresent()) {
            Seller seller = sellerOpt.get();
            validatePassword(request.getPassword(), seller.getPassword());
            return buildAuthResponse(seller.getId(), seller.getUsername(),
                    seller.getEmail(), UserRole.SELLER);
        }

        Optional<Admin> adminOpt = adminRepository.findByEmail(request.getEmail());
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            validatePassword(request.getPassword(), admin.getPassword());
            return buildAuthResponse(admin.getId(), admin.getUsername(),
                    admin.getEmail(), UserRole.ADMIN);
        }

        throw new BusinessException("Credenciais inválidas");
    }

    public AuthResponse refreshToken(String oldToken) {
        log.info("Renovando token");

        try {
            // Validar token
            if (!jwtService.isTokenValid(oldToken)) {
                throw new BusinessException("Token inválido ou expirado");
            }

            // Extrair informações
            String email = jwtService.extractUsername(oldToken);
            String role = jwtService.extractRole(oldToken);

            if (email == null || role == null) {
                throw new BusinessException("Token inválido");
            }

            // Buscar usuário e gerar novo token
            Long userId = null;
            String username = null;

            switch (role) {
                case "CUSTOMER" -> {
                    Customer customer = customerRepository.findByEmail(email)
                            .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
                    userId = customer.getId();
                    username = customer.getUsername();
                }
                case "SELLER" -> {
                    Seller seller = sellerRepository.findByEmail(email)
                            .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
                    userId = seller.getId();
                    username = seller.getUsername();
                }
                case "ADMIN" -> {
                    Admin admin = adminRepository.findByEmail(email)
                            .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
                    userId = admin.getId();
                    username = admin.getUsername();
                }
                default -> throw new BusinessException("Role inválida");
            }

            // Gerar novo token
            String newToken = jwtService.generateToken(email, role);

            log.info("Token renovado com sucesso para: {}", email);

            return AuthResponse.builder()
                    .token(newToken)
                    .type("Bearer")
                    .role(role)
                    .userId(userId)
                    .username(username)
                    .email(email)
                    .build();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Erro ao renovar token: {}", e.getMessage());
            throw new BusinessException("Não foi possível renovar o token");
        }
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!rawPassword.equals(encodedPassword)) {
            throw new BusinessException("Credenciais inválidas");
        }
    }

    private AuthResponse buildAuthResponse(Long userId, String username,
                                           String email, UserRole role) {
        String token = jwtService.generateToken(email, role.name());

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