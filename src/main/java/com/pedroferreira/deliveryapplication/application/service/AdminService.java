package com.pedroferreira.deliveryapplication.application.service;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateStoreRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.*;
import com.pedroferreira.deliveryapplication.domain.entity.*;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import com.pedroferreira.deliveryapplication.infrastructure.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public StoreResponse createStoreAsAdmin(Long adminId, CreateStoreRequest request) {
        Admin admin = findAdminById(adminId);

        if (!admin.canManageStores()) {
            throw new IllegalStateException("Admin não tem permissão para criar lojas");
        }

        if (storeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Store store = Store.builder()
                .name(request.getName())
                .description(request.getDescription())
                .city(request.getCity())
                .state(request.getState())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .category(request.getCategory())
                .deliveryFeePerKm(request.getDeliveryFeePerKm())
                .baseDeliveryFee(request.getBaseDeliveryFee())
                .minimumOrder(request.getMinimumOrder())
                .openingTime(request.getOpeningTime())
                .closingTime(request.getClosingTime())
                .active(true)
                .open(false)
                .createdBy(admin)
                .build();

        Store savedStore = storeRepository.save(store);
        return StoreResponse.fromEntity(savedStore);
    }

    @Transactional(readOnly = true)
    public DashboardReportResponse getDashboardReport() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfToday = now.toLocalDate().atStartOfDay();
        LocalDateTime startOfWeek = now.minusDays(7);
        LocalDateTime startOfMonth = now.minusDays(30);

        return null;
    }

    private PeriodStats getPeriodStats(LocalDateTime start, LocalDateTime end) {

    }

    private Admin findAdminById(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin não encontrado"));
    }

    private LocalTime parseTime(String time) {
        if (time == null || time.isBlank()) {
            return null;
        }
        return LocalTime.parse(time);
    }
}
