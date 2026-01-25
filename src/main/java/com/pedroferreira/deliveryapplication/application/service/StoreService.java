package com.pedroferreira.deliveryapplication.application.service;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateStoreRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.StoreResponse;
import com.pedroferreira.deliveryapplication.application.usecase.BusinessException;
import com.pedroferreira.deliveryapplication.application.usecase.ResourceNotFoundException;
import com.pedroferreira.deliveryapplication.domain.entity.City;
import com.pedroferreira.deliveryapplication.domain.entity.Store;
import com.pedroferreira.deliveryapplication.domain.repository.CityRepository;
import com.pedroferreira.deliveryapplication.domain.repository.StoreRespository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreRespository storeRepository;
    private final CityRepository cityRepository;

    @Transactional
    public StoreResponse createStore(CreateStoreRequest request) {
        log.info("Criando loja: {} na cidade ID {}", request.getName(), request.getCity_id());
        if (storeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        City city = cityRepository.findById(request.getCity_id())
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));

        if (!city.isActive()) {
            throw new BusinessException("Não é possível criar loja em cidade inativa");
        }

        Store store = Store.builder()
                .name(request.getName())
                .description(request.getDescription())
                .city(city)
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .category(request.getCategory())
                .deliveryFeePerKm(request.getDeliveryFeePerKm())
                .baseDeliveryFee(request.getBaseDeliveryFee())
                .minimumOrder(request.getMinimumOrder())
                .openingTime(parseTime(request.getOpeningTime()))
                .closingTime(parseTime(request.getClosingTime()))
                .active(true)
                .open(false)
                .build();

        Store savedStore = storeRepository.save(store);
        log.info("Loja criada com sucesso - ID: {} na cidade: {}",
                savedStore.getId(), savedStore.getName());
        return StoreResponse.fromEntity(savedStore);
    }

    @Transactional
    public StoreResponse updateStore(Long storeId, CreateStoreRequest request) {
        Store store = findStoreById(storeId);

        if (request.getCity_id() != null && !request.getCity_id().equals(store.getCity().getId())) {
            City newCity = cityRepository.findById(request.getCity_id())
                    .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));

            if (!newCity.isActive()) {
                throw new BusinessException("Não é possível mover loja para cidade inativa");
            }

            store.setCity(newCity);
        }

        store.setName(request.getName());
        store.setDescription(request.getDescription());
        store.setPhone(request.getPhone());
        store.setAddress(request.getAddress());
        store.setCategory(request.getCategory());
        store.setDeliveryFeePerKm(request.getDeliveryFeePerKm());
        store.setBaseDeliveryFee(request.getBaseDeliveryFee());
        store.setMinimumOrder(request.getMinimumOrder());

        if (request.getOpeningTime() != null) {
            store.setOpeningTime(parseTime(request.getOpeningTime()));
        }
        if (request.getClosingTime() != null) {
            store.setClosingTime(parseTime(request.getOpeningTime()));
        }

        Store updatedStore = storeRepository.save(store);
        return StoreResponse.fromEntity(updatedStore);
    }

    @Transactional
    public void openStore(Long storeId) {
        Store store = findStoreById(storeId);
        store.openStore();
        storeRepository.save(store);
        log.info("Loja {} aberta", storeId);
    }

    @Transactional
    public void closeStore(Long storeId) {
        Store store = findStoreById(storeId);
        store.closeStore();
        storeRepository.save(store);
    }

    @Transactional
    public void activateStore(Long storeId) {
        Store store = findStoreById(storeId);
        store.activate();
        storeRepository.save(store);
    }

    @Transactional
    public void deactivateStore(Long storeId) {
        Store store = findStoreById(storeId);
        store.deactivate();
        storeRepository.save(store);
    }

    @Transactional
    public void addRating(Long storeId, Integer stars) {
        Store store = findStoreById(storeId);
        store.addRating(stars);
        storeRepository.save(store);
    }

    @Transactional(readOnly = true)
    public StoreResponse getStoreById(Long storeId) {
        Store store = findStoreById(storeId);
        return StoreResponse.fromEntity(store);
    }

    @Transactional(readOnly = true)
    public List<StoreResponse> getAllActiveStores() {
        return storeRepository.findByActiveTrue()
                .stream()
                .map(StoreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreResponse> getOpenStores() {
        return storeRepository.findByActiveTrueAndOpenTrue()
                .stream()
                .map(StoreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreResponse> getStoresByCity(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));

        return storeRepository.findByCityIdAndActiveTrue(cityId)
                .stream()
                .map(StoreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreResponse> getOpenStoresByCity(Long cityId) {
        City city = cityRepository.findById(cityId)
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));

        if (!city.isActive()) {
            throw new BusinessException("Cidade inativa");
        }

        return storeRepository.findByCityIdAndActiveTrueAndOpenTrue(cityId)
                .stream()
                .map(StoreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreResponse> getStoresByCategory(String category) {
        return storeRepository.findByCategory(category)
                .stream()
                .map(StoreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreResponse> searchStores(String search) {
        return storeRepository.searchOpenStores(search)
                .stream()
                .map(StoreResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private Store findStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("Loja não encontrada"));
    }

    private LocalTime parseTime(String time) {
        if (time == null || time.isBlank()) {
            return null;
        }

        return LocalTime.parse(time);
    }
}
