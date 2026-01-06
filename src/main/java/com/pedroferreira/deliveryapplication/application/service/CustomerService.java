package com.pedroferreira.deliveryapplication.application.service;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateCustomerRequest;
import com.pedroferreira.deliveryapplication.application.dto.requests.UpdateCustomerRequest;
import com.pedroferreira.deliveryapplication.application.dto.response.CustomerResponse;
import com.pedroferreira.deliveryapplication.application.usecase.BusinessException;
import com.pedroferreira.deliveryapplication.application.usecase.ResourceNotFoundException;
import com.pedroferreira.deliveryapplication.domain.entity.Customer;
import com.pedroferreira.deliveryapplication.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.info("Criando cliente: {}", request.getEmail());

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }

        if (customerRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("CPF já cadastrado");
        }

        Customer customer = Customer.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .cpf(request.getCpf())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        Customer saved = customerRepository.save(customer);
        log.info("Cliente criado com sucesso - ID: {}", saved.getId());

        return CustomerResponse.fromEntity(saved);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = findCustomerById(id);
        return CustomerResponse.fromEntity(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = findCustomerByEmail(email);
        return CustomerResponse.fromEntity(customer);
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, UpdateCustomerRequest request) {
        Customer customer = findCustomerById(id);

        if (request.getUsername() != null) {
            customer.setUsername(request.getUsername());
        }
        if (request.getPhone() != null) {
            customer.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            customer.setAddress(request.getAddress());
        }

        Customer updated = customerRepository.save(customer);
        log.info("Cliente atualizado - ID: {}", id);

        return CustomerResponse.fromEntity(updated);
    }

    @Transactional
    public void deactivateCustomer(Long id) {
        Customer customer = findCustomerById(id);
        customer.disable();
        customerRepository.save(customer);
        log.info("Cliente desativado - ID: {}", id);
    }

    @Transactional
    public void activateCustomer(Long id) {
        Customer customer = findCustomerById(id);
        customer.enable();
        customerRepository.save(customer);
        log.info("Cliente ativado - ID: {}", id);
    }

    @Transactional
    public Integer getLoyaltyPoints(Long id) {
        Customer customer = findCustomerById(id);
        return customer.getLoyaltyPoints();
    }

    private Customer findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado: " + id));
    }

    private Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com email: " + email));
    }
}
