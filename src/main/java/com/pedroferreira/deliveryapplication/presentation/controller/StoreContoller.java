package com.pedroferreira.deliveryapplication.presentation.controller;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateStoreRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.StoreResponse;
import com.pedroferreira.deliveryapplication.application.service.StoreService;
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
public class StoreContoller {

    @PostMapping
    public ResponseEntity<StoreResponse> createStore(@Valid @RequestBody CreateStoreRequest request){
        log.info("POST /api/stores - Criando loja: {}", request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreResponse> getStore(@PathVariable Long id) {
        log.info("GET /api/stores/{} - Buscando loja", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        log.info("GET /api/stores - Listando todas as lojas");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/open")
    public ResponseEntity<List<StoreResponse>> getOpenStores() {
        log.info("GET /api/stores/open - Listando lojas abertas");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<StoreResponse>> getStoresByCategory(@PathVariable String category) {
        log.info("GET /api/stores/category/{} - Buscando lojas por categoria", category);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<StoreResponse>> searchStores(@RequestParam String q) {
        log.info("GET /api/stores/search?q={} - Pesquisando lojas", q);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/open")
    public ResponseEntity<Void> openStore(@PathVariable Long id) {
        log.info("PUT /api/stores/{}/open - Abrindo loja", id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/close")
    public ResponseEntity<Void> closeStore(@PathVariable Long id) {
        log.info("PUT /api/stores/{}/closed - Fechando loja", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/rating")
    public ResponseEntity<Void> addRating(
            @PathVariable Long id,
            @RequestParam Integer stars
    ) {
        log.info("POST /api/stores/{}/rating - Avaliando loja com {} estrelas", id, stars);
        return ResponseEntity.noContent().build();
    }
}
