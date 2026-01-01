package com.pedroferreira.deliveryapplication.presentation.controller;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateProductRequest;
import com.pedroferreira.deliveryapplication.application.dto.requests.UpdateProductRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductContoller {

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request
            ) {
        log.info("POST /api/products - Criando produto: {}", request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id){
        log.info("GET /api/products/{} - Buscando produto", id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<ProductResponse>> getStoreProducts(@PathVariable Long storeId){
        log.info("GET /api/products/store/{} - Listando produtos da loja", storeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/store/{storeId}/available")
    public ResponseEntity<List<ProductResponse>> getAvailableProducts(@PathVariable Long storeId){
        log.info("GET /api/products/store/{}/available - Listando produtos dísponiveis", storeId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request){
        log.info("PUT /api/products/{} - Atualizando produto", id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long id){
        log.info("DELETE /api/products/{} - Deletando produto", id);
        return ResponseEntity.noContent().build();
    }
}
