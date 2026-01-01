package com.pedroferreira.deliveryapplication.application.service;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateStoreRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.*;
import com.pedroferreira.deliveryapplication.application.dto.response.admin_response.*;
import com.pedroferreira.deliveryapplication.domain.entity.*;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import com.pedroferreira.deliveryapplication.domain.repository.*;
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
    private final StoreRespository storeRepository;
    private final OrderRespository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public StoreResponse createStoreAsAdmin(Long adminId, CreateStoreRequest request) {
        Admin admin = findAdminById(adminId);

        if (!admin.canManageStores()) {
            throw new IllegalStateException("Admin não tem permissão para criar lojas");
        }

        // Validar email único
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
                .openingTime(parseTime(request.getOpeningTime()))
                .closingTime(parseTime(request.getClosingTime()))
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

        return DashboardReportResponse.builder()
                .today(getPeriodStats(startOfToday, now))
                .thisWeek(getPeriodStats(startOfWeek, now))
                .thisMonth(getPeriodStats(startOfMonth, now))
                .topStores(getTopStores(10))
                .ordersByStatus(getOrdersByStatus())
                .build();
    }

    @Transactional(readOnly = true)
    public SystemStatsResponse getSystemStats() {
        Long totalCustomers = customerRepository.count();
        Long totalSellers = sellerRepository.count();
        Long totalAdmins = adminRepository.count();
        Long totalStores = storeRepository.count();
        Long activeStores = (long) storeRepository.findByActiveTrue().size();
        Long totalProducts = productRepository.count();
        Long availableProducts = productRepository.countByAvailableTrue();
        Long totalOrders = orderRepository.count();

        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();
        Long ordersToday = orderRepository.countByCreatedAtAfter(startOfToday);

        return SystemStatsResponse.builder()
                .totalUsers(totalCustomers + totalSellers + totalAdmins)
                .totalCustomers(totalCustomers)
                .totalSellers(totalSellers)
                .totalAdmins(totalAdmins)
                .totalStores(totalStores)
                .activeStores(activeStores)
                .totalProducts(totalProducts)
                .availableProducts(availableProducts)
                .totalOrders(totalOrders)
                .ordersToday(ordersToday)
                .build();
    }

    @Transactional(readOnly = true)
    public List<StorePerfoRmanceResponse> getStorePerformanceReport() {
        List<Store> stores = storeRepository.findAll();

        return stores.stream().map(store -> {
            List<Order> orders = orderRepository.findByStoreId(store.getId());

            Integer totalOrders = orders.size();
            Integer completedOrders = (int) orders.stream()
                    .filter(o -> o.getStatus() == StatusOrder.DELIVERED)
                    .count();
            Integer canceledOrders = (int) orders.stream()
                    .filter(o -> o.getStatus() == StatusOrder.CANCELED)
                    .count();

            BigDecimal totalRevenue = orders.stream()
                    .filter(o -> o.getStatus() == StatusOrder.DELIVERED)
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Double completionRate = totalOrders > 0
                    ? (completedOrders.doubleValue() / totalOrders) * 100
                    : 0.0;

            Double cancellationRate = totalOrders > 0
                    ? (canceledOrders.doubleValue() / totalOrders) * 100
                    : 0.0;

            BigDecimal avgOrderValue = completedOrders > 0
                    ? totalRevenue.divide(BigDecimal.valueOf(completedOrders), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            return StorePerfoRmanceResponse.builder()
                    .storeId(store.getId())
                    .storeName(store.getName())
                    .category(store.getCategory())
                    .totalOrders(totalOrders)
                    .completedOrders(completedOrders)
                    .canceledOrders(canceledOrders)
                    .totalRevenue(totalRevenue)
                    .rating(store.getRating())
                    .completionRate(completionRate)
                    .cancellationRate(cancellationRate)
                    .averageOrderValue(avgOrderValue)
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CustomerReportResponse> getTopCustomersReport(int limit) {
        List<Customer> customers = customerRepository.findAll();

        return customers.stream()
                .map(customer -> {
                    List<Order> orders = orderRepository.findByCustomerId(customer.getId());

                    BigDecimal totalSpent = orders.stream()
                            .filter(o -> o.getStatus() == StatusOrder.DELIVERED)
                            .map(Order::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    LocalDateTime lastOrderDate = orders.stream()
                            .map(Order::getCreatedAt)
                            .max(LocalDateTime::compareTo)
                            .orElse(null);

                    BigDecimal avgOrderValue = orders.size() > 0
                            ? totalSpent.divide(BigDecimal.valueOf(orders.size()), 2, RoundingMode.HALF_UP)
                            : BigDecimal.ZERO;

                    return CustomerReportResponse.builder()
                            .customerId(customer.getId())
                            .username(customer.getUsername())
                            .email(customer.getEmail())
                            .totalOrders(orders.size())
                            .totalSpent(totalSpent)
                            .loyaltyPoints(customer.getLoyaltyPoints())
                            .lastOrderDate(lastOrderDate != null ? lastOrderDate.toLocalDate() : null)
                            .averageOrderValue(avgOrderValue)
                            .build();
                })
                .sorted((c1, c2) -> c2.getTotalSpent().compareTo(c1.getTotalSpent()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeliveryStatsResponse getDeliveryStats() {
        List<Order> deliveredOrders = orderRepository.findByStatus(StatusOrder.DELIVERED);

        if (deliveredOrders.isEmpty()) {
            return DeliveryStatsResponse.builder()
                    .averageDistance(BigDecimal.ZERO)
                    .averageDeliveryFee(BigDecimal.ZERO)
                    .totalDeliveryRevenue(BigDecimal.ZERO)
                    .minDistance(BigDecimal.ZERO)
                    .maxDistance(BigDecimal.ZERO)
                    .build();
        }

        BigDecimal totalDistance = deliveredOrders.stream()
                .map(Order::getDeliveryDistanceKm)
                .filter(d -> d != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal avgDistance = totalDistance
                .divide(BigDecimal.valueOf(deliveredOrders.size()), 2, RoundingMode.HALF_UP);

        BigDecimal totalDeliveryFee = deliveredOrders.stream()
                .map(Order::getDeliveryFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal avgDeliveryFee = totalDeliveryFee
                .divide(BigDecimal.valueOf(deliveredOrders.size()), 2, RoundingMode.HALF_UP);

        BigDecimal minDistance = deliveredOrders.stream()
                .map(Order::getDeliveryDistanceKm)
                .filter(d -> d != null)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal maxDistance = deliveredOrders.stream()
                .map(Order::getDeliveryDistanceKm)
                .filter(d -> d != null)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        return DeliveryStatsResponse.builder()
                .averageDistance(avgDistance)
                .averageDeliveryFee(avgDeliveryFee)
                .totalDeliveryRevenue(totalDeliveryFee)
                .minDistance(minDistance)
                .maxDistance(maxDistance)
                .build();
    }

    private PeriodStats getPeriodStats(LocalDateTime start, LocalDateTime end) {
        List<Order> orders = orderRepository.findByCreatedAtBetween(start, end);

        Long totalOrders = (long) orders.size();

        BigDecimal totalRevenue = orders.stream()
                .filter(o -> o.getStatus() == StatusOrder.DELIVERED)
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long uniqueCustomers = orders.stream()
                .map(o -> o.getCustomer().getId())
                .distinct()
                .count();

        Long activeStores = (long) storeRepository.findByActiveTrueAndOpenTrue().size();

        BigDecimal avgOrderValue = totalOrders > 0
                ? totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        Long canceledOrders = orders.stream()
                .filter(o -> o.getStatus() == StatusOrder.CANCELED)
                .count();

        Double cancellationRate = totalOrders > 0
                ? (canceledOrders.doubleValue() / totalOrders) * 100
                : 0.0;

        return PeriodStats.builder()
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .totalCustomers(uniqueCustomers)
                .activeStores(activeStores)
                .averageOrderValue(avgOrderValue)
                .cancellationRate(cancellationRate)
                .build();
    }

    private List<TopStoreResponse> getTopStores(int limit) {
        return storeRepository.findAll().stream()
                .map(store -> {
                    List<Order> orders = orderRepository.findByStoreIdAndStatus(
                            store.getId(), StatusOrder.DELIVERED);

                    BigDecimal revenue = orders.stream()
                            .map(Order::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return TopStoreResponse.builder()
                            .storeId(store.getId())
                            .storeName(store.getName())
                            .category(store.getCategory())
                            .totalOrders(orders.size())
                            .totalRevenue(revenue)
                            .rating(store.getRating())
                            .totalRatings(store.getTotalRatings())
                            .build();
                })
                .sorted((s1, s2) -> s2.getTotalRevenue().compareTo(s1.getTotalRevenue()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private List<OrdersByStatusResponse> getOrdersByStatus() {
        return List.of(StatusOrder.values()).stream()
                .map(status -> {
                    List<Order> orders = orderRepository.findByStatus(status);
                    BigDecimal totalValue = orders.stream()
                            .map(Order::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return OrdersByStatusResponse.builder()
                            .status(status.name())
                            .count((long) orders.size())
                            .totalValue(totalValue)
                            .build();
                })
                .collect(Collectors.toList());
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