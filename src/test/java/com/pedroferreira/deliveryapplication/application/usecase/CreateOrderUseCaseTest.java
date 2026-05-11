package com.pedroferreira.deliveryapplication.application.usecase;

import com.pedroferreira.deliveryapplication.application.dto.requests.CreateOrderRequest;
import com.pedroferreira.deliveryapplication.application.dto.requests.ItemOrderDTO;
import com.pedroferreira.deliveryapplication.application.dto.response.OrderResponse;
import com.pedroferreira.deliveryapplication.domain.entity.*;
import com.pedroferreira.deliveryapplication.domain.enuns.StatusOrder;
import com.pedroferreira.deliveryapplication.domain.repository.CustomerRepository;
import com.pedroferreira.deliveryapplication.domain.repository.OrderRespository;
import com.pedroferreira.deliveryapplication.domain.repository.ProductRepository;
import com.pedroferreira.deliveryapplication.domain.repository.StoreRespository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateOrderUseCase Tests")
class CreateOrderUseCaseTest {

    @Mock
    private OrderRespository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StoreRespository storeRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CreateOrderUseCase createOrderUseCase;

    private Customer customer;
    private Store store;
    private Product product;
    private CreateOrderRequest request;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .username("testuser")
                .email("test@email.com")
                .password("password")
                .cpf("12345678901")
                .phone("11999999999")
                .address("Test Address")
                .build();

        store = Store.builder()
                .id(1L)
                .name("Test Store")
                .city(City.builder().build())
                .phone("11988888888")
                .email("store@test.com")
                .address("Store Address")
                .category("Hamburgueria")
                .deliveryFeePerKm(BigDecimal.valueOf(2.50))
                .baseDeliveryFee(BigDecimal.valueOf(5.00))
                .minimumOrder(BigDecimal.valueOf(20.00))
                .openingTime(LocalTime.of(0, 0))
                .closingTime(LocalTime.of(23, 59))
                .active(true)
                .open(true)
                .build();

        product = Product.builder()
                .id(1L)
                .name("X-Burger")
                .price(BigDecimal.valueOf(30.00))
                .available(true)
                .active(true)
                .store(store)
                .build();

        ItemOrderDTO itemDTO = ItemOrderDTO.builder()
                .productId(1L)
                .quantity(2)
                .observations("Sem cebola")
                .build();

        request = CreateOrderRequest.builder()
                .customerId(1L)
                .storeId(1L)
                .deliveryAddress("Delivery Address, 123")
                .deliveyDistanceKm(5.0)
                .items(List.of(itemDTO))
                .observations("Test order")
                .build();
    }

    @Test
    @DisplayName("Deve criar pedido com sucesso")
    void shouldCreateOrderSuccessfully() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Order savedOrder = new Order(customer, store, "Delivery Address, 123", 5.0, "Test order");
        savedOrder.setId(1L);
        ItemOrder item = new ItemOrder(product, 2, product.getPrice(), "Sem cebola");
        savedOrder.addItem(item);

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        OrderResponse response = createOrderUseCase.execute(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(StatusOrder.CREATED);
        assertThat(response.getItems()).hasSize(1);

        verify(customerRepository).findById(1L);
        verify(storeRepository).findById(1L);
        verify(productRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente não encontrado")
    void shouldThrowExceptionWhenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createOrderUseCase.execute(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Cliente não encontrado");

        verify(customerRepository).findById(1L);
        verifyNoInteractions(storeRepository, productRepository, orderRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando loja não encontrada")
    void shouldThrowExceptionWhenStoreNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createOrderUseCase.execute(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Loja não encontrada");

        verify(storeRepository).findById(1L);
        verifyNoInteractions(productRepository, orderRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando cliente inativo")
    void shouldThrowExceptionWhenCustomerInactive() {
        customer.disable();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThatThrownBy(() -> createOrderUseCase.execute(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Cliente está inativo");

        verify(customerRepository).findById(1L);
        verifyNoInteractions(storeRepository, productRepository, orderRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando loja fechada")
    void shouldThrowExceptionWhenStoreClosed() {
        store.closeStore();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));

        assertThatThrownBy(() -> createOrderUseCase.execute(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Loja está fechada no momento");

        verify(storeRepository).findById(1L);
        verifyNoInteractions(productRepository, orderRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não encontrado")
    void shouldThrowExceptionWhenProductNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createOrderUseCase.execute(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Produto não encontrado");

        verify(productRepository).findById(1L);
        verifyNoInteractions(orderRepository);
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto indisponível")
    void shouldThrowExceptionWhenProductUnavailable() {
        product.makeUnavailable();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> createOrderUseCase.execute(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Produto indiponível");

        verify(productRepository).findById(1L);
        verifyNoInteractions(orderRepository);
    }
}