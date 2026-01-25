package com.pedroferreira.deliveryapplication.presentation.controller;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateStoreRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.StoreResponse;
import com.pedroferreira.deliveryapplication.application.service.StoreService;
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
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Lojas", description = "Gerenciamento de lojas")
public class StoreContoller {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponse> createStore(@Valid @RequestBody CreateStoreRequest request) {
        log.info("POST /api/stores - Criando loja: {}", request.getName());
        StoreResponse response = storeService.createStore(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponse> getStore(@PathVariable Long id) {
        log.info("GET /api/stores/{} - Buscando loja", id);
        StoreResponse response = storeService.getStoreById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        log.info("GET /api/stores - Listando todas as lojas");
        List<StoreResponse> stores = storeService.getAllActiveStores();
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/open")
    public ResponseEntity<List<StoreResponse>> getOpenStores() {
        log.info("GET /api/stores/open - Listando lojas abertas");
        List<StoreResponse> stores = storeService.getOpenStores();
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/city/{cityId}")
    @Operation(
            summary = "Listar lojas por cidade",
            description = "Usuário primeiro seleciona cidade, depois vê as lojas disponíveis nela"
    )
    public ResponseEntity<List<StoreResponse>> getStoresByCity(
            @Parameter(description = "ID da cidade") @PathVariable Long cityId) {
        log.info("GET /api/stores/city/{} - Buscando lojas da cidade", cityId);
        List<StoreResponse> stores = storeService.getStoresByCity(cityId);
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/city/{cityId}/open")
    @Operation(
            summary = "Listar lojas abertas em uma cidade",
            description = "Retorna apenas lojas ativas e abertas em uma cidade específica"
    )
    public ResponseEntity<List<StoreResponse>> getOpenStoresByCity(
            @Parameter(description = "ID da cidade") @PathVariable Long cityId) {
        log.info("GET /api/stores/city/{}/open - Buscando lojas abertas da cidade", cityId);
        List<StoreResponse> stores = storeService.getOpenStoresByCity(cityId);
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<StoreResponse>> getStoresByCategory(@PathVariable String category) {
        log.info("GET /api/stores/category/{} - Buscando lojas por categoria", category);
        List<StoreResponse> stores = storeService.getStoresByCategory(category);
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StoreResponse>> searchStores(@RequestParam String q) {
        log.info("GET /api/stores/search?q={} - Pesquisando lojas", q);
        List<StoreResponse> stores = storeService.searchStores(q);
        return ResponseEntity.ok(stores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StoreResponse> updateStore(
            @PathVariable Long id,
            @Valid @RequestBody CreateStoreRequest request
    ) {
        log.info("PUT /api/stores/{} - Atualizando loja", id);
        StoreResponse response = storeService.updateStore(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/open")
    public ResponseEntity<Void> openStore(@PathVariable Long id) {
        log.info("PUT /api/stores/{}/open - Abrindo loja", id);
        storeService.openStore(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Void> closeStore(@PathVariable Long id) {
        log.info("PUT /api/stores/{}/close - Fechando loja", id);
        storeService.closeStore(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateStore(@PathVariable Long id) {
        log.info("PUT /api/stores/{}/activate - Ativando loja", id);
        storeService.activateStore(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateStore(@PathVariable Long id) {
        log.info("PUT /api/stores/{}/deactivate - Desativando loja", id);
        storeService.deactivateStore(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<Void> addRating(
            @PathVariable Long id,
            @RequestParam Integer stars
    ) {
        log.info("POST /api/stores/{}/rating - Avaliando loja com {} estrelas", id, stars);
        storeService.addRating(id, stars);
        return ResponseEntity.noContent().build();
    }
}