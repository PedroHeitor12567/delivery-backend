package com.pedroferreira.deliveryapplication.presentation.controller;

import com.pedroferreira.deliveryapplication.application.dto.requests.ApproveSellerRequest;
import com.pedroferreira.deliveryapplication.application.dto.requests.CreateProductRequest;
import com.pedroferreira.deliveryapplication.application.dto.requests.CreateStoreRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.*;
import com.pedroferreira.deliveryapplication.application.dto.response.admin_response.*;
import com.pedroferreira.deliveryapplication.application.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Administração", description = "Endpoints exclusivos para administradores")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/applications/pending")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Listar solicitações pendentes",
            description = "Lista todos os clientes que solicitaram se tornar vendedores"
    )
    public ResponseEntity<List<SellerApplicationResponse>> getPendingApplications() {
        log.info("GET /api/admin/applications/pending - Listando solicitações pendentes");
        List<SellerApplicationResponse> applications = adminService.getPendingApplications();
        return ResponseEntity.ok(applications);
    }

    @PostMapping("/applications/{applicationId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Aprovar solicitação de vendedor",
            description = "Admin aprova cliente para ser vendedor, cria loja e produtos iniciais"
    )
    public ResponseEntity<SellerResponse> approveSellerApplication(
            @Parameter(description = "ID da solicitação") @PathVariable Long applicationId,
            @Valid @RequestBody ApproveSellerRequest request
    ) {
        log.info("POST /api/admin/applications/{}/approve - Aprovando solicitação", applicationId);
        SellerResponse response = adminService.approveSellerApplication(applicationId, request);
        log.info("Vendedor criado com sucesso - ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/applications/{applicationId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Rejeitar solicitação de vendedor"
    )
    public ResponseEntity<Void> rejectSellerApplication(
            @Parameter(description = "ID da solicitação") @PathVariable Long applicationId,
            @Parameter(description = "Motivo da rejeição") @RequestParam String reason
    ) {
        log.info("POST /api/admin/applications/{}/reject - Rejeitando solicitação",  applicationId);
        adminService.rejectSellerApplication(applicationId, reason);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/stores")
    @PreAuthorize("hasRole('ADMIN') and #request.adminId == authentication.principal.id")
    @Operation(
            summary = "Criar loja (Admin)",
            description = "Admin cria loja manualmente pra um vendedor"
    )
    public ResponseEntity<StoreResponse> createStore(
            @Parameter(description = "ID do admin") @RequestParam Long adminId,
            @Valid @RequestBody CreateStoreRequest request
    ) {
        log.info("POST /api/admin/stores - Admin {} criando loja", adminId);
        StoreResponse response = adminService.createStoreAsAdmin(adminId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/stores")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Listar todas as lojas (incluindo inativas)"
    )
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        log.info("GET /api/admin/stores - Listando todas as lojas");
        List<StoreResponse> stores = adminService.getAllStores();
        return ResponseEntity.ok(stores);
    }

    @PutMapping("/stores/{id}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Suspender loja"
    )
    public ResponseEntity<Void> suspendStore(
            @Parameter(description = "ID da loja") @PathVariable Long id,
            @Parameter(description = "Motivo da suspensão") @RequestParam String reason
    ) {
        log.info("PUT /api/admin/stores/{}/suspend - Suspendendo loja", id);
        adminService.suspendStore(id, reason);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/stores/{storeId}/products")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Adicionar produto a uma loja (Admin)"
    )
    public ResponseEntity<ProductResponse> addProductToStore(
            @Parameter(description = "ID da loja") @PathVariable Long storeId,
            @Valid @RequestBody CreateProductRequest request
    ) {
        log.info("POST /api/admin/stores/{}/products - Adicionando produto", storeId);
        ProductResponse response = adminService.addProductToStore(storeId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Dashboard geral do sistema"
    )
    public ResponseEntity<DashboardReportResponse> getDashboard() {
        log.info("GET /api/admin/dashboard - Carregando dashboard");
        DashboardReportResponse dashboard = adminService.getDashboardReport();
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/stats/system")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Estatísticas gerais do sistema"
    )
    public ResponseEntity<SystemStatsResponse> getSystemStats() {
        log.info("GET /api/admin/stats/system - Carregando estatísticas do sistema");
        SystemStatsResponse stats = adminService.getSystemStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/stores")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Performance de todas as lojas"
    )
    public ResponseEntity<List<StorePerfoRmanceResponse>> getStoresPerformance() {
        List<StorePerfoRmanceResponse> performance = adminService.getStorePerformanceReport();
        return ResponseEntity.ok(performance);
    }

    @GetMapping("/stats/customers/top")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Top clientes (maiores compradores)"
    )
    public ResponseEntity<List<CustomerReportResponse>> getTopCustomers(
            @Parameter(description = "Quantidade de clientes") @RequestParam(defaultValue = "10") int limit
    ) {
        log.info("GET /api/admin/stats/customers/top?limit={} - Top clientes", limit);
        List<CustomerReportResponse> topCustomers = adminService.getTopCustomersReport(limit);
        return ResponseEntity.ok(topCustomers);
    }

    @GetMapping("/stats/delivery")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Estatísticas de entregas"
    )
    public ResponseEntity<DeliveryStatsResponse> getDeliveryStats() {
        log.info("GET /api/admin/stats/delivery - Estatísticas de entregas");
        DeliveryStatsResponse stats = adminService.getDeliveryStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/revenue/total")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Receita total da plataforma",
            description = "Calcula 8% da taxa sobre todas as vendas concluídas"
    )
    public ResponseEntity<PlatformRevenueResponse> getPlatformRevenue() {
        log.info("GET /api/admin/revenue/total - Calculando receita da plataforma");
        PlatformRevenueResponse revenue = adminService.calculatePlatformRevenue();
        return ResponseEntity.ok(revenue);
    }

    @GetMapping("/users/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Listar todos os usuários"
    )
    public ResponseEntity<AllUsersResponse> getAllUsers() {
        log.info("GET /api/admin/users/all - Listando todos os usuários");
        AllUsersResponse users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/customer/{id}/ban")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Banir cliente"
    )
    public ResponseEntity<Void> banCustomer(
            @Parameter(description = "ID do cliente") @PathVariable Long id,
            @Parameter(description = "Motivo do banimento") @RequestParam String reason
    ) {
        log.info("PUT /api/admin/users/customer/{}/ban - Banindo cliente", id);
        adminService.banCustomer(id, reason);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/seller/{id}/ban")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Banir vendedor (suspende loja também)"
    )
    public ResponseEntity<Void> banSeller(
            @Parameter(description = "ID do vendedor") @PathVariable Long id,
            @Parameter(description = "Motivo do banimento") @RequestParam String reason
    ) {
        log.info("PUT /api/admin/users/seller/{}/ban - Banindo vendedor", id);
        adminService.banSeller(id, reason);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/orders/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Listar todos os pedidos com detalhes completos",
            description = "Admin tem acesso a todos os pedidos com informações de clientes e lojas"
    )
    public ResponseEntity<List<OrdemDetailResponse>> getAllOrders() {
        log.info("GET /api/admin/orders/all - Listando todos os pedidos");
        List<OrdemDetailResponse> orders = adminService.getAllOrdersWithDetails();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/sales/by-city")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Relatório de vendas por cidade",
            description = "Histórico de vendas agrupado por cidade com receita da plataforma"
    )
    public ResponseEntity<List<SalesByCityResponse>> getSalesByCity() {
        log.info("GET /api/admin/sales/by-city - Gerando relatório por cidade");
        List<SalesByCityResponse> sales = adminService.getSalesByCity();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/sales/by-city/{cityName}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Relatório de vendas filtrado por cidade",
            description = "Vendas de uma cidade específica"
    )
    public ResponseEntity<List<SalesByCityResponse>> getSalesByCityFiltered(
            @Parameter(description = "Nome da cidade") @PathVariable String cityName
    ) {
        log.info("GET /api/admin/sales/by-city/{} - Relatório da cidade", cityName);
        List<SalesByCityResponse> sales = adminService.getSalesByCityFiltered(cityName);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/stores/comparison")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Comparação de vendas entre lojas",
            description = "Gráfico comparativo de performance das lojas"
    )
    public ResponseEntity<StoreComparisonResponse> getStoresComparison(
            @Parameter(description = "Filtrar por cidade (opcional)") @RequestParam(required = false) String city
    ) {
        log.info("GET /api/admin/stores/comparison - Comparando lojas");
        StoreComparisonResponse comparison = adminService.getStoresComparison(city);
        return ResponseEntity.ok(comparison);
    }
}