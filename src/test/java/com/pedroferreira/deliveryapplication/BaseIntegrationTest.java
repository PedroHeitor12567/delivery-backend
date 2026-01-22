package com.pedroferreira.deliveryapplication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Classe base para testes de integração usando Testcontainers.
 * Todos os testes de integração devem estender esta classe.
 */
@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public abstract class BaseIntegrationTest {

    /**
     * Container PostgreSQL compartilhado entre todos os testes.
     * Usa imagem alpine para ser mais leve.
     */
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("deliverydb_test")
            .withUsername("test")
            .withPassword("test")
            .withReuse(true); // Reutiliza container entre execuções

    /**
     * Configura dinamicamente as propriedades do datasource
     * para usar o container Testcontainers.
     */
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    /**
     * Teste básico para verificar se o contexto Spring carrega corretamente.
     */
    @Test
    void contextLoads() {
        // Se chegar aqui, o contexto carregou com sucesso
    }
}

/**
 * Exemplo de teste de integração de repositório
 */
/*
@SpringBootTest
@ActiveProfiles("test")
class UserRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndRetrieveUser() {
        // Given
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        // When
        User savedUser = userRepository.save(user);

        // Then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(userRepository.findById(savedUser.getId())).isPresent();
    }

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
    }
}
*/