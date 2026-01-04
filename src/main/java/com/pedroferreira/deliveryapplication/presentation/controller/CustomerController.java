package com.pedroferreira.deliveryapplication.presentation.controller;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateCustomerRequest;
import com.pedroferreira.deliveryapplication.application.dto.requests.UpdateCustomerRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.CustomerResponse;
import com.pedroferreira.deliveryapplication.application.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Clientes", description = "Cadastro e gerenciamento de clientes")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    @Operation(
            summary = "Cadastrar novo cliente",
            description = "Qualquer pessoa pode se cadastrar como cliente"
    )
    public ResponseEntity<CustomerResponse> register(
            @Valid @RequestBody CreateCustomerRequest request
    ) {
       log.info("POST /api/customers/register - Cadastrando cliente: {}", request.getEmail());
       CustomerResponse response = customerService.createCustomer(request);
       log.info("Cliente cadastrado com sucesso - ID: {}", response.getId());
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar cliente por ID"
    )
    public ResponseEntity<CustomerResponse> getCustomer(
            @Parameter(description = "ID do cliente") @PathVariable Long id
    ) {
        log.info("GET /api/customers/{} - Buscando cliente", id);
        CustomerResponse response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    @Operation(
            summary = "Buscar cliente por email"
    )
    public ResponseEntity<CustomerResponse> getCustomerByEmail(
            @Parameter(description = "Email do cliente" ) @PathVariable String email
    ) {
        log.info("GET /api/customers/email/{} - Buscando cliente por email", email);
        CustomerResponse response = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar dados do cliente"
    )
    public ResponseEntity<CustomerResponse> updateCustomer(
            @Parameter(description = "ID do cliente") @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {
        log.info("PUT /api/customers/{} - Atualizando cliente", id);
        CustomerResponse response = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deactive")
    @Operation(
            summary = "Desativar conta do cliente"
    )
    public ResponseEntity<Void> deactivateCustomer(
            @Parameter(description = "ID do cliente")  @PathVariable Long id
    ) {
        log.info("PUT /api/customers/{}/deactivate - Desativando cliente", id);
        customerService.deactivateCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    @Operation(
            summary = "Reativar conta do cliente"
    )
    public ResponseEntity<Void> activateCustomer(
            @Parameter(description = "ID do cliente") @PathVariable Long id
    ) {
        log.info("PUT /api/customers/{}/activate - Ativando cliente", id);
        customerService.activateCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/loyalty-points")
    @Operation(
            summary = "Consultar pontos de fidelidade"
    )
    public ResponseEntity<Integer> getLoyaltyPoints(
            @Parameter(description = "ID do cliente")  @PathVariable Long id
    ) {
        log.info("GET /api/customers/{}/loyalty-points - Consultando pontos", id);
        Integer points = customerService.getLoyaltyPoints(id);
        return ResponseEntity.ok(points);
    }
}
