package com.pedroferreira.deliveryapplication.presentation.controller;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateStoreRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.StoreResponse;
import com.pedroferreira.deliveryapplication.application.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreContoller {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<StoreResponse> createStore(@Valid @RequestBody CreateStoreRequest request){
        StoreResponse response = storeService.createStore(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponse> updateStore(
            @PathVariable Long storeId,
            @Valid @RequestBody CreateStoreRequest request
    ) {
        StoreResponse response = storeService.updateStore(storeId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreResponse> getStoreById(@PathVariable Long storeId) {
        StoreResponse response = storeService.getStoreById(storeId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAllActiveStores() {
        List<StoreResponse> stores = storeService.getAllActiveStores();
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/open")
    public ResponseEntity<List<StoreResponse>> getOpenStores() {
        List<StoreResponse> stores = storeService.getOpenStores();
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<StoreResponse>> getStoresByCategory(@PathVariable String category) {
        List<StoreResponse> stores = storeService.getStoresByCategory(category);
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StoreResponse>> searchStores(@RequestParam String search) {
        List<StoreResponse> stores = storeService.searchStores(search);
        return ResponseEntity.ok(stores);
    }

    @PutMapping("/{storeId}/open")
    public ResponseEntity<Void> openStore(@PathVariable Long storeId) {
        storeService.openStore(storeId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{storeId}/close")
    public ResponseEntity<Void> closeStore(@PathVariable Long storeId) {
        storeService.closeStore(storeId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{storeId}/activate")
    public ResponseEntity<Void> activateStore(@PathVariable Long storeId) {
        storeService.activateStore(storeId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{storeId}/deactivate")
    public ResponseEntity<Void> deactivateStore(@PathVariable Long storeId) {
        storeService.deactivateStore(storeId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{storeId}/rating")
    public ResponseEntity<Void> addRating(
            @PathVariable Long storeId,
            @RequestParam Integer stars
    ) {
        storeService.addRating(storeId, stars);
        return ResponseEntity.noContent().build();
    }
}
