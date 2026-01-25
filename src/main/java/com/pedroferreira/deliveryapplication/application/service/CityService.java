package com.pedroferreira.deliveryapplication.application.service;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateCityRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.CityResponse;
import com.pedroferreira.deliveryapplication.application.usecase.BusinessException;
import com.pedroferreira.deliveryapplication.application.usecase.ResourceNotFoundException;
import com.pedroferreira.deliveryapplication.domain.entity.City;
import com.pedroferreira.deliveryapplication.domain.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityService {

    private final CityRepository cityRepository;

    @Transactional
    public CityResponse createCity(CreateCityRequest request) {
        log.info("Criando cidade: {} - {}", request.getName(), request.getState());

        if (cityRepository.existsByNameAndState(request.getName(), request.getState())) {
            throw new BusinessException("Cidade já cadastrada");
        }

        City city = new City(request.getName(), request.getState());
        City saved = cityRepository.save(city);

        log.info("Cidade criada com sucesso - ID: {}", saved.getId());
        return CityResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public CityResponse getCityById(Long id) {
        City city = findCityById(id);
        return CityResponse.fromEntity(city);
    }

    @Transactional(readOnly = true)
    public List<CityResponse> getAllActiveCities() {
        return cityRepository.findByActiveTrue().stream()
                .map(CityResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CityResponse> getCitiesByState(String state) {
        return cityRepository.findByState(state).stream()
                .map(CityResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void activateCity(Long id) {
        City city = findCityById(id);
        city.activate();
        cityRepository.save(city);
        log.info("Cidade ativada - ID: {}", id);
    }

    @Transactional
    public void deactivateCity(Long id) {
        City city = findCityById(id);
        city.deactivate();
        cityRepository.save(city);
        log.info("Cidade deativada - ID: {}", id);
    }

    private City findCityById(Long id) {
        return cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada: " + id));
    }
}
