package com.pedroferreira.deliveryapplication.infrastructure.repository;

import com.pedroferreira.deliveryapplication.domain.entity.Product;
import com.pedroferreira.deliveryapplication.domain.repository.ProductRepository;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.ProductJpaEntity;
import com.pedroferreira.deliveryapplication.infrastructure.persistence.entity.StoreJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepositorySpring jpaRepository;
    private final StoreJpaRepositorySpring storeJpaRepository;

    @Override
    public Product save(Product product) {
        ProductJpaEntity jpaEntity = ProductJpaEntity.fromDomain(product);

        if (product.getStore() != null && product.getStore().getId() != null) {
            StoreJpaEntity store = storeJpaRepository.findById(product.getStore().getId())
                    .orElseThrow(() -> new RuntimeException("Store not found"));
            jpaEntity.setStore(store);
        }

        ProductJpaEntity saved = jpaRepository.save(jpaEntity);
        return saved.toDomain();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id)
                .map(ProductJpaEntity::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream()
                .map(ProductJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByStoreId(Long storeId) {
        return jpaRepository.findByStoreId(storeId).stream()
                .map(ProductJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByStoreIdAndAvailableTrue(Long storeId) {
        return jpaRepository.findByStoreIdAndAvailableTrue(storeId).stream()
                .map(ProductJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByStoreIdAndActiveTrue(Long storeId) {
        return jpaRepository.findByStoreIdAndActiveTrue(storeId).stream()
                .map(ProductJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Long countByAvailableTrue() {
        return jpaRepository.countByAvailableTrue();
    }

    @Override
    public void delete(Product product) {
        if (product.getId() != null) {
            jpaRepository.deleteById(product.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }
}

interface ProductJpaRepositorySpring extends JpaRepository<ProductJpaEntity, Long> {
    List<ProductJpaEntity> findByStoreId(Long storeId);
    List<ProductJpaEntity> findByStoreIdAndAvailableTrue(Long storeId);
    List<ProductJpaEntity> findByStoreIdAndActiveTrue(Long storeId);
    Long countByAvailableTrue();
}
