package com.pedroferreira.deliveryapplication.presentation.controller;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateCityRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.CityResponse;
import com.pedroferreira.deliveryapplication.application.service.CityService;
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
@RequestMapping("/api/cities")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cidades", description = "Gerenciamento de cidades")
public class CityController {

    private final CityService cityService;

    @PostMapping
    @Operation(summary = "Criar nova cidade (Admin)", description = "Apenas admins podem criar cidades")
    public ResponseEntity<CityResponse> createCity(
            @Valid @RequestBody CreateCityRequest request
    ) {
        log.info("POST /api/cities - Criando cidade: {}", request.getName());
        CityResponse response = cityService.createCity(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar cidade por ID"
    )
    public ResponseEntity<CityResponse> getCity(
            @Parameter(description = "ID da cidade") @PathVariable Long id
    ) {
        log.info("GET /api/cities/{} - Buscando cidade", id);
        CityResponse response = cityService.getCityById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar todas as cidades ativas"
    )
    public ResponseEntity<List<CityResponse>> getAllCities() {
        log.info("GET /api/cities - Listando cidades ativas");
        List<CityResponse> cities = cityService.getAllActiveCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/state/{state}")
    @Operation(summary = "Buscar cidades por estado")
    public ResponseEntity<List<CityResponse>> getCitiesByState(
            @Parameter(description = "Sigla do estado") @PathVariable String state) {
        log.info("GET /api/cities/state/{} - Buscando cidades", state);
        List<CityResponse> cities = cityService.getCitiesByState(state);
        return ResponseEntity.ok(cities);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Ativar cidade (Admin)")
    public ResponseEntity<Void> activateCity(
            @Parameter(description = "ID da cidade") @PathVariable Long id) {
        log.info("PUT /api/cities/{}/activate - Ativando cidade", id);
        cityService.activateCity(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Desativar cidade (Admin)")
    public ResponseEntity<Void> deactivateCity(
            @Parameter(description = "ID da cidade") @PathVariable Long id) {
        log.info("PUT /api/cities/{}/deactivate - Desativando cidade", id);
        cityService.deactivateCity(id);
        return ResponseEntity.noContent().build();
    }
}
