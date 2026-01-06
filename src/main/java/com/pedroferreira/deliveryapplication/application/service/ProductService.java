package com.pedroferreira.deliveryapplication.application.service;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateProductRequest;
import com.pedroferreira.deliveryapplication.application.dto.requests.UpdateProductRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.ProductResponse;
import com.pedroferreira.deliveryapplication.application.usecase.ResourceNotFoundException;
import com.pedroferreira.deliveryapplication.domain.entity.Product;
import com.pedroferreira.deliveryapplication.domain.entity.Store;
import com.pedroferreira.deliveryapplication.domain.repository.ProductRepository;
import com.pedroferreira.deliveryapplication.domain.repository.StoreRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final StoreRespository storeRepository;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("Criando produto: {} para loja {}", request.getName(), request.getStoreId());

        Store store = findStoreById(request.getStoreId());

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
        log.info("Produto criado com sucesso - ID: {}", saved.getId());

        return ProductResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = findProductById(id);
        return ProductResponse.fromEntity(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByStore(Long storeId) {
        List<Product> products = productRepository.findByStoreIdAndActiveTrue(storeId);
        return products.stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAvailableProductsByStore(Long storeId) {
        List<Product> products = productRepository.findByStoreIdAndAvailableTrue(storeId);
        return products.stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = findProductById(id);

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }
        if (request.getAvailable() != null) {
            if (request.getAvailable()) {
                product.makeAvailable();
            } else {
                product.makeUnavailable();
            }
        }
        if (request.getPreparationTime() != null) {
            product.setPreparationTime(request.getPreparationTime());
        }

        Product updated = productRepository.save(product);
        log.info("Produto atualizado - ID: {}", id);

        return ProductResponse.fromEntity(updated);
    }

    @Transactional
    public void makeUnavailable(Long id) {
        Product product = findProductById(id);
        product.makeUnavailable();
        productRepository.save(product);
        log.info("Produto {} marcado como indisponível", id);
    }

    @Transactional
    public void makeAvailable(Long id) {
        Product product = findProductById(id);
        product.makeAvailable();
        productRepository.save(product);
        log.info("Produto {} marcado como disponível", id);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductById(id);
        product.deactivate();
        productRepository.save(product);
        log.info("Produto {} desativado permanentemente", id);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado: " + id));
    }

    private Store findStoreById(Long id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loja não encontrada: " + id));
    }
}