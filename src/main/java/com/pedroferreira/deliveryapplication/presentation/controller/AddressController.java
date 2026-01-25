package com.pedroferreira.deliveryapplication.presentation.controller;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateAddressRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.AddressResponse;
import com.pedroferreira.deliveryapplication.application.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Endereços", description = "Gerenciamento de endereços dos clientes")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    @Operation(summary = "Criar novo endereço", description = "Cliente cria endereço em uma cidade")
    public ResponseEntity<AddressResponse> createAddress(@Valid @RequestBody CreateAddressRequest request) {
        log.info("POST /api/addresses - Criando endereço para cliente {}", request.getCustomerId());
        AddressResponse response = addressService.createAddress(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar endereço por ID")
    public ResponseEntity<AddressResponse> getAddress(
            @Parameter(description = "ID do endereço") @PathVariable Long id) {
        log.info("GET /api/addresses/{} - Buscando endereço", id);
        AddressResponse response = addressService.getAddressById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Listar endereços do cliente")
    public ResponseEntity<List<AddressResponse>> getCustomerAddresses(
            @Parameter(description = "ID do cliente") @PathVariable Long customerId) {
        log.info("GET /api/addresses/customer/{} - Listando endereços", customerId);
        List<AddressResponse> addresses = addressService.getCustomerAddresses(customerId);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/customer/{customerId}/city/{cityId}")
    @Operation(summary = "Listar endereços do cliente em uma cidade específica")
    public ResponseEntity<List<AddressResponse>> getCustomerAddressesByCity(
            @Parameter(description = "ID do cliente") @PathVariable Long customerId,
            @Parameter(description = "ID da cidade") @PathVariable Long cityId) {
        log.info("GET /api/addresses/customer/{}/city/{} - Listando endereços", customerId, cityId);
        List<AddressResponse> addresses = addressService.getCustomerAddressesByCity(customerId, cityId);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/customer/{customerId}/default")
    @Operation(summary = "Buscar endereço padrão do cliente")
    public ResponseEntity<AddressResponse> getDefaultAddress(
            @Parameter(description = "ID do cliente") @PathVariable Long customerId) {
        log.info("GET /api/addresses/customer/{}/default - Buscando endereço padrão", customerId);
        AddressResponse response = addressService.getDefaultAddress(customerId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/set-default")
    @Operation(summary = "Definir endereço como padrão")
    public ResponseEntity<Void> setAsDefault(
            @Parameter(description = "ID do endereço") @PathVariable Long id) {
        log.info("PUT /api/addresses/{}/set-default - Definindo como padrão", id);
        addressService.setAsDefault(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar endereço")
    public ResponseEntity<Void> deleteAddress(
            @Parameter(description = "ID do endereço") @PathVariable Long id) {
        log.info("DELETE /api/addresses/{} - Deletando endereço", id);
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
