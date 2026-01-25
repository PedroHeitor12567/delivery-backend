package com.pedroferreira.deliveryapplication.application.service;

import com.pedroferreira.deliveryapplication.application.dto.requests.ApproveSellerRequest;
import com.pedroferreira.deliveryapplication.application.dto.requests.CreateProductRequest;
import com.pedroferreira.deliveryapplication.application.dto.requests.CreateStoreRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.*;
import com.pedroferreira.deliveryapplication.application.dto.response.admin_response.*;
import com.pedroferreira.deliveryapplication.application.usecase.BusinessException;
import com.pedroferreira.deliveryapplication.application.usecase.ResourceNotFoundException;
import com.pedroferreira.deliveryapplication.domain.entity.*;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import com.pedroferreira.deliveryapplication.domain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final StoreRespository storeRepository;
    private final OrderRespository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CityRepository cityRepository;

    private static final BigDecimal PLATFORM_FEE_PERCENTAGE = BigDecimal.valueOf(0.08);

    @Transactional(readOnly = true)
    public List<SellerApplicationResponse> getPendingApplications() {
        return List.of();
    }

    @Transactional
    public SellerResponse approveSellerApplication(Long applicationId, ApproveSellerRequest request) {
        log.info("Admin {} aprovando solicitações {}", request.getAdminId(), applicationId);

        Admin admin = findAdminById(request.getAdminId());

        City city = cityRepository.findByNameAndState(
                request.getStoreData().getName(),
                null // Você precisa adicionar state no DTO se quiser validar
        ).orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));

        if (!city.isActive()) {
            throw new BusinessException("Não é possível criar loja em cidade inativa");
        }

        Store store = Store.builder()
                .name(request.getStoreData().getName())
                .description(request.getStoreData().getDescription())
                .city(city)
                .phone(request.getStoreData().getPhone())
                .email(request.getStoreData().getEmail())
                .address(request.getStoreData().getAddress())
                .category(request.getStoreData().getCategory())
                .deliveryFeePerKm(request.getDeliveryFeePerKm())
                .baseDeliveryFee(request.getBaseDeliveryFee())
                .minimumOrder(request.getMinimumOrder())
                .active(true)
                .open(false)
                .createdBy(admin)
                .build();

        Store savedStore = storeRepository.save(store);

        Seller seller = Seller.builder()
                .username("seller_" + savedStore.getId())
                .email(savedStore.getEmail())
                .password("TEMP_PASS")
                .cpf("00000000000")
                .phone(savedStore.getPhone())
                .address(savedStore.getAddress())
                .store(savedStore)
                .build();

        Seller savedSeller = sellerRepository.save(seller);

        if (request.getInitialProducts() != null) {
            request.getInitialProducts().forEach(productReq -> {
                Product product = Product.builder()
                        .name(productReq.getName())
                        .description(productReq.getDescription())
                        .price(productReq.getPrice())
                        .imageUrl(productReq.getImageUrl())
                        .store(savedStore)
                        .preparationTime(productReq.getPreparationTime())
                        .available(true)
                        .active(true)
                        .build();
                productRepository.save(product);
            });
        }

        log.info("Vendedor criado com sucesso - ID: {}", savedSeller.getId());
        return SellerResponse.fromEntity(savedSeller);
    }

    @Transactional
    public void rejectSellerApplication(Long applicationId, String reason) {
        log.info("Rejeitando solicitações {} - Motivo: {}", applicationId, reason);
    }

    @Transactional(readOnly = true)
    public List<StoreResponse> getAllStores() {
        return storeRepository.findAll().stream()
                .map(StoreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void suspendStore(Long storeId, String reason) {
        Store store = findStoreById(storeId);
        store.deactivate();
        storeRepository.save(store);
        log.info("Loja {} suspensa. Motivo: {}", storeId, reason);
    }

    @Transactional
    public ProductResponse addProductToStore(Long storeId, CreateProductRequest request) {
        Store store = findStoreById(storeId);

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .imageUrl(request.getImageUrl())
                .store(store)
                .preparationTime(request.getPreparationTime())
                .available(true)
                .active(true)
                .build();

        Product saved = productRepository.save(product);
        log.info("Produto {} adicionado à loja {} pelo admin", saved.getId(), storeId);

        return ProductResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public PlatformRevenueResponse calculatePlatformRevenue() {
        List<Order> completedOrders = orderRepository.findByStatus(StatusOrder.DELIVERED);

        BigDecimal totalSalesValue = completedOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal platformFee = totalSalesValue.multiply(PLATFORM_FEE_PERCENTAGE)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal sellersRevenue = totalSalesValue.subtract(platformFee);

        BigDecimal averageFeePerOrder = completedOrders.isEmpty()
                ? BigDecimal.ZERO
                : platformFee.divide(BigDecimal.valueOf(completedOrders.size()), 2, RoundingMode.HALF_UP);

        log.info("Receita da plataforma calculada: R$ {} (8% de R$ {})", platformFee, totalSalesValue
        );

        return PlatformRevenueResponse.builder()
                .totalSalesValue(totalSalesValue)
                .platformFee(platformFee)
                .sellersRevenue(sellersRevenue)
                .totalCompletedOrders(completedOrders.size())
                .averageFeePerOrder(averageFeePerOrder)
                .build();
    }

    @Transactional(readOnly = true)
    public AllUsersResponse getAllUsers() {
        List<Customer> customers = customerRepository.findAll();
        List<Seller> sellers = sellerRepository.findAll();

        List<CustomerResponse> customerResponses = customers.stream()
                .map(CustomerResponse::fromEntity)
                .collect(Collectors.toList());

        List<SellerResponse> sellerResponses = sellers.stream()
                .map(SellerResponse::fromEntity)
                .collect(Collectors.toList());

        int activeUsers = (int) customers.stream().filter(User::isActive).count() + (int) sellers.stream().filter(User::isActive).count();

        return AllUsersResponse.builder()
                .customers(customerResponses)
                .sellers(sellerResponses)
                .totalCustomers(customers.size())
                .totalSellers(sellers.size())
                .totalActiveUsers(activeUsers)
                .build();
    }

    @Transactional
    public void banCustomer(Long customerId, String reason) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado"));

        customer.disable();
        customerRepository.save(customer);
        log.warn("Cliente {} banido. Motivo: {}", customerId, reason);
    }

    @Transactional
    public void banSeller(Long sellerId, String reason) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new BusinessException("Vendedor não encontrado"));

        seller.disable();
        sellerRepository.save(seller);

        if (seller.getStore() != null) {
            seller.getStore().deactivate();
            storeRepository.save(seller.getStore());
        }

        log.warn("Vendedor {} banido e loja suspensa. Motivo: {}", sellerId, reason);
    }

    @Transactional
    public StoreResponse createStoreAsAdmin(Long adminId, CreateStoreRequest request) {
        Admin admin = findAdminById(adminId);

        if (!admin.canManageStores()) {
            throw new IllegalStateException("Admin não tem permissão para criar lojas");
        }

        if (storeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        City city = cityRepository.findById(request.getCity_id())
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));

        if (!city.isActive()) {
            throw new BusinessException("Não é possível criar loja em cidade inativa");
        }

        Store store = Store.builder()
                .name(request.getName())
                .description(request.getDescription())
                .city(city)
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

    @Transactional(readOnly = true)
    public List<OrdemDetailResponse> getAllOrdersWithDetails() {
        List<Order> orders = orderRepository.findAll();

        return orders.stream().map(order -> {
            List<OrderItemDetail> items = order.getItems().stream()
                    .map(item -> OrderItemDetail.builder()
                            .productName(item.getProduct().getName())
                            .quantity(item.getQuantity())
                            .unitPrice(item.getUnitPrice())
                            .subtotal(item.getSubtotal())
                            .build())
                    .collect(Collectors.toList());

            return OrdemDetailResponse.builder()
                    .orderId(order.getId())
                    .customerName(order.getCustomer().getUsername())
                    .customerEmail(order.getCustomer().getEmail())
                    .customerPhone(order.getCustomer().getPhone())
                    .storeName(order.getStore().getName())
                    .storeCategory(order.getStore().getCategory())
                    .cityName(order.getStore().getCity().getName())
                    .deliveryAddress(order.getDeliveryAddress())
                    .totalAmount(order.getTotalAmount())
                    .deliveryFee(order.getDeliveryFee())
                    .status(order.getStatus().name())
                    .createdAt(order.getCreatedAt().toString())
                    .deliveredAt(order.getDeliveredAt() != null ? order.getDeliveredAt().toString() : null)
                    .items(items)
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SalesByCityResponse> getSalesByCity() {
        List<City> cities = cityRepository.findByActiveTrue();

        return cities.stream().map(city -> {
            List<Store> cityStores = storeRepository.findByCityAndActiveTrue(city.getName());

            List<Order> cityOrders = cityStores.stream()
                    .flatMap(store -> orderRepository.findByStoreIdAndStatus(store.getId(), StatusOrder.DELIVERED).stream())
                    .collect(Collectors.toList());

            BigDecimal totalRevenue = cityOrders.stream()
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal platformRevenue = totalRevenue.multiply(PLATFORM_FEE_PERCENTAGE)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal avgOrderValue = cityOrders.isEmpty()
                    ? BigDecimal.ZERO
                    : totalRevenue.divide(BigDecimal.valueOf(cityOrders.size()), 2, RoundingMode.HALF_UP);

            return SalesByCityResponse.builder()
                    .cityId(city.getId())
                    .state(city.getState())
                    .totalStores(cityStores.size())
                    .totalOrders(cityOrders.size())
                    .totalRevenue(totalRevenue)
                    .platformRevenue(platformRevenue)
                    .averageOrderValue(avgOrderValue)
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SalesByCityResponse> getSalesByCityFiltered(String cityName) {
        City city = cityRepository.findByNameAndState(cityName, null)
                .orElseThrow(() -> new BusinessException("Cidade não encontrada"));

        List<Store> cityStores = storeRepository.findByCityAndActiveTrue(city.getName());

        List<Order> cityOrders = cityStores.stream()
                .flatMap(store -> orderRepository.findByStoreIdAndStatus(store.getId(), StatusOrder.DELIVERED).stream())
                .collect(Collectors.toList());

        BigDecimal totalRevenue = cityOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal platformRevenue = totalRevenue.multiply(PLATFORM_FEE_PERCENTAGE)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal avgOrderValue = cityOrders.isEmpty()
                ? BigDecimal.ZERO
                : totalRevenue.divide(BigDecimal.valueOf(cityOrders.size()), 2, RoundingMode.HALF_UP);

        SalesByCityResponse response = SalesByCityResponse.builder()
                .cityId(city.getId())
                .state(city.getState())
                .totalStores(cityStores.size())
                .totalOrders(cityOrders.size())
                .totalRevenue(totalRevenue)
                .platformRevenue(platformRevenue)
                .averageOrderValue(avgOrderValue)
                .build();

        return List.of(response);
    }

    @Transactional(readOnly = true)
    public StoreComparisonResponse getStoresComparison(String cityName) {
        List<Store> stores = cityName != null
                ? storeRepository.findByCityAndActiveTrue(cityName)
                : storeRepository.findByActiveTrue();

        List<StoreComparisonItem> comparison = stores.stream().map(store -> {
                    List<Order> storeOrders = orderRepository.findByStoreIdAndStatus(store.getId(), StatusOrder.DELIVERED);

                    BigDecimal totalRevenue = storeOrders.stream()
                            .map(Order::getTotalAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal platformFee = totalRevenue.multiply(PLATFORM_FEE_PERCENTAGE)
                            .setScale(2, RoundingMode.HALF_UP);

                    return StoreComparisonItem.builder()
                            .storeId(store.getId())
                            .storeName(store.getName())
                            .cityName(store.getCity().getName())
                            .totalOrders(storeOrders.size())
                            .totalRevenue(totalRevenue)
                            .platformFee(platformFee)
                            .rating(store.getRating())
                            .build();
                })
                .sorted((s1, s2) -> s2.getTotalRevenue().compareTo(s1.getTotalRevenue()))
                .collect(Collectors.toList());

        return StoreComparisonResponse.builder()
                .stores(comparison)
                .period("Histórico Total")
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

    private LocalTime parseTime(String time) {
        if (time == null || time.isBlank()) {
            return null;
        }
        return LocalTime.parse(time);
    }

    private Admin findAdminById(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new BusinessException("Admin não encontrado"));
    }

    private Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new BusinessException("Loja não encontrada"));
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
}