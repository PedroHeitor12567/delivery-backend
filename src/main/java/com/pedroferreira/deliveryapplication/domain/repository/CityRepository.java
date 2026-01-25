package com.pedroferreira.deliveryapplication.domain.repository;

import com.pedroferreira.deliveryapplication.domain.entity.City;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository {
    City save(City city);
    Optional<City> findById(Long id);
    Optional<City> findByNameAndState(String name, String state);
    List<City> findByState(String state);
    List<City> findByActiveTrue();
    List<City> findAll();
    boolean existsByNameAndState(String name, String state);
    void delete(City city);
    Long count();
}
