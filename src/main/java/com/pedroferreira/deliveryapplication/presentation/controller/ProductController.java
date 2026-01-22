package com.pedroferreira.deliveryapplication.presentation.controller;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateProductRequest;
import com.pedroferreira.deliveryapplication.application.dto.requests.UpdateProductRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.ProductResponse;
import com.pedroferreira.deliveryapplication.application.service.ProductService;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Produtos", description = "Gerenciamento de produtos")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(
            summary = "Criar produto",
            description = "Vendedor cria novo produto para sua loja"
    )
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request
    ) {
        log.info("POST /api/products - Criando produto: {}", request.getName());
        ProductResponse response = productService.createProduct(request);
        log.info("Produto criado com sucesso - ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID")
    public ResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "ID do produto") @PathVariable Long id
    ) {
        log.info("GET /api/products/{} - Buscando produto", id);
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/store/{storeId}")
    @Operation(
            summary = "Listar produtos de uma loja",
            description = "Lista todos os produtos ativos de uma loja"
    )
    public ResponseEntity<List<ProductResponse>> getProductsByStore(
            @Parameter(description = "ID da loja") @PathVariable Long storeId
    ) {
        log.info("GET /api/products/store/{} - Buscando produtos da loja", storeId);
        List<ProductResponse> products = productService.getProductsByStore(storeId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/store/{storeId}/available")
    @Operation(
            summary = "Listar produtos disponíveis",
            description = "Lista apenas produtos disponíveis para venda"
    )
    public ResponseEntity<List<ProductResponse>> getAvailableProducts(
            @Parameter(description = "ID da loja") @PathVariable Long storeId
    ) {
        log.info("GET /api/products/store/{}/available - Buscando produtos disponíveis", storeId);
        List<ProductResponse> products = productService.getAvailableProductsByStore(storeId);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto")
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "ID do produto") @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        log.info("PUT /api/products/{} - Atualizando produto", id);
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/unavailable")
    @Operation(summary = "Marcar produto como indisponível")
    public ResponseEntity<Void> makeUnavailable(
            @Parameter(description = "ID do produto") @PathVariable Long id
    ) {
        log.info("PUT /api/products/{}/unavailable - Marcando produto como indisponível", id);
        productService.makeUnavailable(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/available")
    @Operation(summary = "Marcar produto como disponível")
    public ResponseEntity<Void> makeAvailable(
            @Parameter(description = "ID do produto") @PathVariable Long id
    ) {
        log.info("PUT /api/products/{}/available - Marcando produto como disponível", id);
        productService.makeAvailable(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar produto",
            description = "Desativa produto permanentemente"
    )
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID do produto") @PathVariable Long id
    ) {
        log.info("DELETE /api/products/{} - Deletando produto", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}