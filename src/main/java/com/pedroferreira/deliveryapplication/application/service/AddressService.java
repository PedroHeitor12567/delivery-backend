package com.pedroferreira.deliveryapplication.application.service;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateAddressRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.AddressResponse;
import com.pedroferreira.deliveryapplication.application.usecase.ResourceNotFoundException;
import com.pedroferreira.deliveryapplication.domain.entity.Address;
import com.pedroferreira.deliveryapplication.domain.entity.City;
import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import com.pedroferreira.deliveryapplication.domain.repository.AddressRepository;
import com.pedroferreira.deliveryapplication.domain.repository.CityRepository;
import com.pedroferreira.deliveryapplication.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public AddressResponse createAddress(CreateAddressRequest request) {
        log.info("Criando endereço para cliente: {}", request.getCustomerId());

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));

        Address address = Address.builder()
                .customer(customer)
                .city(city)
                .street(request.getStreet())
                .number(request.getNumber())
                .complement(request.getComplement())
                .neighborhood(request.getNeighborhood())
                .zipCode(request.getZipCode())
                .reference(request.getReference())
                .isDefault(Boolean.TRUE.equals(request.getIsDefault()))
                .active(true)
                .build();

        if (Boolean.TRUE.equals(request.getIsDefault())) {
            List<Address> customerAddresses = addressRepository.findByCustomerId(customer.getId());
            customerAddresses.forEach(addr -> {
                addr.removeAsDefault();
                addressRepository.save(addr);
            });
        }

        Address saved = addressRepository.save(address);
        log.info("Endereço criado com sucesso - ID: {}", saved.getId());

        return AddressResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public AddressResponse getAddressById(Long id) {
        Address address = findAddressById(id);
        return AddressResponse.fromEntity(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getCustomerAddresses(Long customerId) {
        return addressRepository.findByCustomerIdAndActiveTrue(customerId).stream()
                .map(AddressResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> getCustomerAddressesByCity(Long customerId, Long cityId) {
        return addressRepository.findByCustomerIdAndCityId(customerId, cityId).stream()
                .map(AddressResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AddressResponse getDefaultAddress(Long customerId) {
        Address address = addressRepository.findByCustomerIdAndIsDefaultTrue(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço padrão não encontrado"));
        return AddressResponse.fromEntity(address);
    }

    @Transactional
    public void setAsDefault(Long addressId) {
        Address address = findAddressById(addressId);

        List<Address> customerAddresses = addressRepository.findByCustomerId(addressId);
        customerAddresses.forEach(addr -> {
            addr.removeAsDefault();
            addressRepository.save(addr);
        });

        address.setAsDefault();
        addressRepository.save(address);

        log.info("Endereço {} definido como padrão", addressId);
    }

    @Transactional
    public void deleteAddress(Long id) {
        Address address = findAddressById(id);
        address.deactivate();
        addressRepository.save(address);
        log.info("Endereço desativado - ID: {}", id);
    }

    private Address findAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado: " + id));
    }
}
