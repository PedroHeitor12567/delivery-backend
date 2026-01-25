# 🍔 Delivery Application - Sistema Completo de Delivery

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.7-brightgreen?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?style=for-the-badge&logo=postgresql)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

**Uma plataforma completa de delivery com arquitetura limpa, segurança robusta e escalabilidade**

[Documentação](#-documentação) •
[Instalação](#-instalação) •
[API Reference](#-api-reference) •
[Arquitetura](#-arquitetura) •
[Contribuir](#-contribuindo)

</div>

---

## 📑 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Tecnologias](#️-tecnologias)
- [Arquitetura](#️-arquitetura)
- [Modelo de Dados](#-modelo-de-dados)
- [Fluxos de Negócio](#-fluxos-de-negócio)
- [Instalação](#-instalação)
- [API Reference](#-api-reference)
- [Segurança](#-segurança)
- [Testes](#-testes)
- [Deploy](#-deploy)
- [Contribuindo](#-contribuindo)

---

## 🎯 Sobre o Projeto

O **Delivery Application** é uma plataforma completa para gerenciamento de pedidos de delivery, desenvolvida com as melhores práticas de **Clean Architecture**, **Domain-Driven Design (DDD)** e **Modular Monolith** usando Spring Modulith.

### 🌟 Características Principais

- ✅ **Multi-tenant**: Suporta múltiplas cidades e lojas
- ✅ **Três tipos de usuário**: Customer, Seller e Admin
- ✅ **Sistema de aprovação**: Sellers passam por aprovação administrativa
- ✅ **Gestão completa de pedidos**: Do carrinho à entrega
- ✅ **Cálculo dinâmico de frete**: Base + distância em KM
- ✅ **Sistema de avaliações**: Clientes avaliam lojas
- ✅ **Programa de fidelidade**: Pontos por compra
- ✅ **Auditoria completa**: Rastreio de todas as ações
- ✅ **API RESTful**: Documentada com Swagger/OpenAPI
- ✅ **Autenticação JWT**: Segurança por roles
- ✅ **Testes automatizados**: Unitários e de integração

---

## 🛠️ Tecnologias

### Core
- **Java 21** - Linguagem principal
- **Spring Boot 3.3.7** - Framework
- **Spring Modulith 1.2.5** - Modular Monolith
- **PostgreSQL 16** - Banco de dados

### Segurança
- **Spring Security 6** - Autenticação e autorização
- **JWT (JJWT 0.12.6)** - Tokens stateless
- **BCrypt** - Hash de senhas

### Documentação
- **SpringDoc OpenAPI 2.6.0** - Swagger UI
- **Jakarta Validation** - Validações de entrada

### Testes
- **JUnit 5** - Framework de testes
- **Testcontainers 1.19.7** - Testes de integração
- **AssertJ** - Assertions fluentes

### DevOps
- **Docker** - Containerização
- **Docker Compose** - Orquestração local
- **Maven Wrapper** - Build tool

---

## 🏗️ Arquitetura

O projeto segue os princípios de **Clean Architecture** e **DDD**, organizado em camadas bem definidas:
```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                    │
│              (Controllers, DTOs, Exception Handlers)     │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                   Application Layer                      │
│         (Use Cases, Services, Request/Response DTOs)     │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                     Domain Layer                         │
│        (Entities, Value Objects, Domain Services,        │
│              Repositories Interfaces, Enums)             │
└──────────────────────┬──────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────┐
│                 Infrastructure Layer                     │
│    (JPA Entities, Repository Implementations, Mappers,   │
│           Security, Configuration, Persistence)          │
└─────────────────────────────────────────────────────────┘
```

### 📦 Estrutura de Pacotes
```
com.pedroferreira.deliveryapplication/
│
├── presentation/           # Camada de apresentação
│   ├── controller/        # Controllers REST
│   └── exception/         # Exception handlers globais
│
├── application/           # Camada de aplicação
│   ├── dto/              # DTOs de request/response
│   │   ├── requests/     # DTOs de entrada
│   │   └── response/     # DTOs de saída
│   ├── service/          # Serviços de aplicação
│   └── usecase/          # Casos de uso específicos
│
├── domain/               # Camada de domínio
│   ├── entity/          # Entidades do domínio
│   ├── enuns/           # Enumerações
│   └── repository/      # Interfaces de repositórios
│
└── infrastructure/       # Camada de infraestrutura
    ├── config/          # Configurações (Security, OpenAPI)
    ├── persistence/     # JPA entities
    │   └── entity/
    ├── repository/      # Implementações de repositórios
    │   ├── impl/
    │   └── mapper/
    └── security/        # JWT, filtros, etc.
```

---

## 📊 Modelo de Dados

### Diagrama ER Simplificado
```
┌─────────┐         ┌─────────┐         ┌──────────┐
│  CITY   │────┬───▶│  STORE  │────────▶│ PRODUCT  │
└─────────┘    │    └─────────┘         └──────────┘
               │         │                     │
               │         │                     │
               │         ▼                     ▼
               │    ┌─────────┐         ┌──────────────┐
               │    │  ORDER  │────────▶│  ORDER_ITEM  │
               │    └─────────┘         └──────────────┘
               │         ▲
               │         │
               ▼         │
          ┌─────────┐   │
          │ ADDRESS │   │
          └─────────┘   │
               ▲        │
               │        │
          ┌────┴────┐   │
          │CUSTOMER │───┘
          └─────────┘
```

### 🔑 Entidades Principais

#### City (Cidade)
- **id**: Identificador único
- **name**: Nome da cidade
- **state**: Sigla do estado (2 letras)
- **active**: Se a cidade está ativa no sistema

#### Store (Loja)
- **id**: Identificador único
- **name**: Nome da loja
- **city_id**: Referência à cidade (FK)
- **category**: Categoria (Hamburgueria, Pizzaria, etc)
- **deliveryFeePerKm**: Taxa por quilômetro
- **baseDeliveryFee**: Taxa base de entrega
- **minimumOrder**: Pedido mínimo
- **rating**: Avaliação média (0-5)
- **active/open**: Status da loja

#### Customer (Cliente)
- **id**: Identificador único
- **email**: Email único
- **cpf**: CPF único
- **loyaltyPoints**: Pontos de fidelidade

#### Product (Produto)
- **id**: Identificador único
- **name**: Nome do produto
- **price**: Preço
- **store_id**: Referência à loja (FK)
- **available**: Se está disponível para venda

#### Order (Pedido)
- **id**: Identificador único
- **customer_id**: Referência ao cliente (FK)
- **store_id**: Referência à loja (FK)
- **status**: Status do pedido (CREATED, CONFIRMED, etc)
- **totalAmount**: Valor total
- **deliveryFee**: Taxa de entrega calculada

---

## 🔄 Fluxos de Negócio

### 1️⃣ Fluxo do Cliente
```
1. Cliente se cadastra (POST /api/customers/register)
   ↓
2. Cliente faz login (POST /api/auth/login) → Recebe JWT
   ↓
3. Cliente seleciona cidade (GET /api/cities)
   ↓
4. Cliente vê lojas disponíveis (GET /api/stores/city/{id}/open)
   ↓
5. Cliente escolhe loja e vê produtos (GET /api/products/store/{id}/available)
   ↓
6. Cliente cria pedido (POST /api/orders)
   ↓
7. Sistema calcula frete (baseDeliveryFee + distanceKm * deliveryFeePerKm)
   ↓
8. Cliente acompanha status (GET /api/orders/customer/{id})
```

### 2️⃣ Fluxo do Vendedor
```
1. Cliente solicita ser vendedor (POST /api/sellers/apply)
   ↓
2. Admin aprova solicitação (POST /api/admin/applications/{id}/approve)
   ↓
3. Sistema cria Seller + Store
   ↓
4. Seller adiciona produtos (POST /api/products)
   ↓
5. Seller abre loja (PUT /api/stores/{id}/open)
   ↓
6. Seller recebe pedidos (GET /api/sellers/{id}/orders/pending)
   ↓
7. Seller aceita pedido (PUT /api/sellers/orders/{id}/accept)
   ↓
8. Seller prepara e marca como pronto (PUT /api/sellers/orders/{id}/ready)
```

### 3️⃣ Estados do Pedido
```
CREATED → CONFIRMED → READY → LEFT_FOR_DELIVERY → DELIVERED
   ↓          ↓
CANCELED   CANCELED
```

**Regras:**
- Cliente pode cancelar até status `READY`
- Seller pode recusar em `CREATED` ou cancelar em `CONFIRMED`
- A partir de `LEFT_FOR_DELIVERY` não pode mais cancelar

---

## 🚀 Instalação

### Pré-requisitos

- **Java 21+** ([Download](https://adoptium.net/))
- **Maven 3.9+** (ou use o Maven Wrapper incluído)
- **PostgreSQL 16+** ([Download](https://www.postgresql.org/download/))
- **Docker** (opcional)

### Opção 1: Docker Compose (Recomendado)
```bash
# Clone o repositório
git clone https://github.com/pedroferreira/delivery-backend.git
cd delivery-backend

# Suba os containers
docker-compose up -d

# Acesse
# API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
```

### Opção 2: Instalação Local

#### 1. Configure o banco de dados
```bash
# Criar banco PostgreSQL
createdb deliverydb

# Ou via psql
psql -U postgres
CREATE DATABASE deliverydb;
\q
```

#### 2. Configure application.properties
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/deliverydb
spring.datasource.username=postgres
spring.datasource.password=postgres
```

#### 3. Execute a aplicação
```bash
# Com Maven Wrapper (recomendado)
./mvnw spring-boot:run

# Ou com Maven instalado
mvn spring-boot:run
```

#### 4. Execute a migration SQL

Execute o arquivo `V1__initial_migration.sql` no banco:
```bash
psql -U postgres -d deliverydb -f src/main/resources/db/migration/V1__initial_migration.sql
```

### Verificar Instalação
```bash
# Verificar saúde da API
curl http://localhost:8080/actuator/health

# Acessar Swagger
open http://localhost:8080/swagger-ui.html
```

---

## 📖 API Reference

### 🔐 Autenticação

Todos os endpoints (exceto públicos) requerem JWT:
```http
Authorization: Bearer {seu_token_jwt}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "senha123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "role": "CUSTOMER",
  "userId": 1,
  "username": "João Silva",
  "email": "user@example.com"
}
```

---

### 🏙️ Cidades

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|--------------|-----------|
| GET | `/api/cities` | ❌ | Listar cidades ativas |
| GET | `/api/cities/{id}` | ❌ | Buscar cidade por ID |
| POST | `/api/cities` | ✅ ADMIN | Criar nova cidade |
| PUT | `/api/cities/{id}/activate` | ✅ ADMIN | Ativar cidade |
| PUT | `/api/cities/{id}/deactivate` | ✅ ADMIN | Desativar cidade |

**Exemplo - Listar cidades:**
```http
GET /api/cities
```
```json
[
  {
    "id": 1,
    "name": "São Paulo",
    "state": "SP",
    "active": true
  }
]
```

---

### 🏪 Lojas

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|--------------|-----------|
| GET | `/api/stores/city/{cityId}/open` | ❌ | Lojas abertas na cidade |
| GET | `/api/stores/{id}` | ❌ | Detalhes da loja |
| GET | `/api/stores/category/{category}` | ❌ | Lojas por categoria |
| GET | `/api/stores/search?q={termo}` | ❌ | Pesquisar lojas |
| POST | `/api/stores` | ✅ SELLER/ADMIN | Criar loja |
| PUT | `/api/stores/{id}` | ✅ SELLER | Atualizar loja |
| PUT | `/api/stores/{id}/open` | ✅ SELLER | Abrir loja |
| PUT | `/api/stores/{id}/close` | ✅ SELLER | Fechar loja |
| POST | `/api/stores/{id}/rating?stars={1-5}` | ✅ CUSTOMER | Avaliar loja |

**Exemplo - Lojas abertas por cidade:**
```http
GET /api/stores/city/1/open
```
```json
[
  {
    "id": 1,
    "name": "Burger King",
    "description": "Hambúrgueres artesanais",
    "city": {
      "id": 1,
      "name": "São Paulo",
      "state": "SP"
    },
    "category": "Hamburgueria",
    "deliveryFeePerKM": 2.50,
    "baseDeliveryFee": 5.00,
    "minimumOrder": 20.00,
    "rating": 4.5,
    "open": true
  }
]
```

**Exemplo - Criar loja:**
```http
POST /api/stores
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Minha Pizzaria",
  "description": "As melhores pizzas",
  "cityId": 1,
  "phone": "11977777777",
  "email": "contato@pizzaria.com",
  "address": "Rua das Flores, 123",
  "category": "Pizzaria",
  "deliveryFeePerKm": 3.00,
  "baseDeliveryFee": 7.00,
  "minimumOrder": 30.00,
  "openingTime": "18:00",
  "closingTime": "23:00"
}
```

---

### 🍕 Produtos

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|--------------|-----------|
| GET | `/api/products/store/{id}/available` | ❌ | Produtos disponíveis |
| GET | `/api/products/{id}` | ❌ | Detalhes do produto |
| POST | `/api/products` | ✅ SELLER | Criar produto |
| PUT | `/api/products/{id}` | ✅ SELLER | Atualizar produto |
| PUT | `/api/products/{id}/unavailable` | ✅ SELLER | Marcar indisponível |
| PUT | `/api/products/{id}/available` | ✅ SELLER | Marcar disponível |
| DELETE | `/api/products/{id}` | ✅ SELLER | Deletar produto |

**Exemplo - Produtos disponíveis:**
```http
GET /api/products/store/1/available
```
```json
[
  {
    "id": 1,
    "name": "X-Burger",
    "description": "Hambúrguer com queijo",
    "price": 25.00,
    "imageUrl": "https://...",
    "available": true,
    "preparationTime": 20,
    "store": {
      "id": 1,
      "name": "Burger King",
      "category": "Hamburgueria"
    }
  }
]
```

**Exemplo - Criar produto:**
```http
POST /api/products
Authorization: Bearer {token}
Content-Type: application/json

{
  "name": "Pizza Margherita",
  "description": "Molho, mussarela e manjericão",
  "price": 45.00,
  "imageUrl": "https://example.com/pizza.jpg",
  "storeId": 1,
  "preparationTime": 30
}
```

---

### 📦 Pedidos

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|--------------|-----------|
| POST | `/api/orders` | ✅ CUSTOMER | Criar pedido |
| GET | `/api/orders/{id}` | ✅ ALL | Buscar pedido |
| GET | `/api/orders/customer/{id}` | ✅ CUSTOMER | Pedidos do cliente |
| GET | `/api/orders/store/{id}` | ✅ SELLER | Pedidos da loja |
| PUT | `/api/orders/{id}/cancel` | ✅ CUSTOMER | Cancelar pedido |
| PUT | `/api/sellers/orders/{id}/accept` | ✅ SELLER | Aceitar pedido |
| PUT | `/api/sellers/orders/{id}/refuse` | ✅ SELLER | Recusar pedido |
| PUT | `/api/sellers/orders/{id}/ready` | ✅ SELLER | Marcar pronto |

**Exemplo - Criar pedido:**
```http
POST /api/orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "customerId": 1,
  "storeId": 1,
  "deliveryAddress": "Rua A, 123 - Apto 45",
  "deliveyDistanceKm": 5.5,
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "observations": "Sem cebola"
    },
    {
      "productId": 3,
      "quantity": 1
    }
  ],
  "observations": "Interfone 45"
}
```

**Response:**
```json
{
  "id": 1,
  "status": "CREATED",
  "deliveryAddress": "Rua A, 123 - Apto 45",
  "deliveryFee": 18.75,
  "totalAmount": 68.75,
  "createdAt": "2025-01-25T14:30:00",
  "items": [...]
}
```

**Cálculo do frete:**
```
deliveryFee = baseDeliveryFee + (deliveyDistanceKm * deliveryFeePerKm)
deliveryFee = 5.00 + (5.5 * 2.50) = 18.75
```

---

### 👤 Clientes

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|--------------|-----------|
| POST | `/api/customers/register` | ❌ | Cadastrar cliente |
| GET | `/api/customers/{id}` | ✅ CUSTOMER | Buscar cliente |
| PUT | `/api/customers/{id}` | ✅ CUSTOMER | Atualizar dados |
| GET | `/api/customers/{id}/loyalty-points` | ✅ CUSTOMER | Ver pontos |

**Exemplo - Cadastro:**
```http
POST /api/customers/register
Content-Type: application/json

{
  "username": "João Silva",
  "email": "joao@email.com",
  "password": "senha123",
  "cpf": "12345678901",
  "phone": "11999999999",
  "address": "Rua A, 123"
}
```

---

### 🏬 Vendedores

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|--------------|-----------|
| POST | `/api/sellers/apply` | ✅ CUSTOMER | Solicitar ser vendedor |
| GET | `/api/sellers/{id}` | ✅ SELLER | Buscar vendedor |
| GET | `/api/sellers/{id}/orders` | ✅ SELLER | Pedidos da loja |
| GET | `/api/sellers/{id}/orders/pending` | ✅ SELLER | Pedidos pendentes |

**Exemplo - Solicitar ser vendedor:**
```http
POST /api/sellers/apply
Authorization: Bearer {token}
Content-Type: application/json

{
  "customerId": 1,
  "proposedStoreName": "Minha Lanchonete",
  "category": "Lanches",
  "description": "Lanches artesanais",
  "storeAddress": "Rua B, 456",
  "cityId": 1,
  "businessPhone": "11988887777",
  "whatsapp": "11988887777",
  "termsAccepted": "SIM"
}
```

---

### 👨‍💼 Administração

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|--------------|-----------|
| GET | `/api/admin/applications/pending` | ✅ ADMIN | Solicitações pendentes |
| POST | `/api/admin/applications/{id}/approve` | ✅ ADMIN | Aprovar vendedor |
| POST | `/api/admin/applications/{id}/reject` | ✅ ADMIN | Rejeitar vendedor |
| GET | `/api/admin/dashboard` | ✅ ADMIN | Dashboard geral |
| GET | `/api/admin/stats/system` | ✅ ADMIN | Estatísticas sistema |
| GET | `/api/admin/revenue/total` | ✅ ADMIN | Receita da plataforma |
| GET | `/api/admin/users/all` | ✅ ADMIN | Todos os usuários |
| PUT | `/api/admin/stores/{id}/suspend` | ✅ ADMIN | Suspender loja |
| PUT | `/api/admin/users/customer/{id}/ban` | ✅ ADMIN | Banir cliente |

**Exemplo - Dashboard:**
```http
GET /api/admin/dashboard
Authorization: Bearer {admin_token}
```
```json
{
  "today": {
    "totalOrders": 45,
    "totalRevenue": 2500.00,
    "activeStores": 12
  },
  "thisWeek": {...},
  "thisMonth": {...},
  "topStores": [...]
}
```

**Exemplo - Receita da plataforma:**
```http
GET /api/admin/revenue/total
Authorization: Bearer {admin_token}
```
```json
{
  "totalSalesValue": 50000.00,
  "platformFee": 4000.00,
  "sellersRevenue": 46000.00,
  "totalCompletedOrders": 250,
  "averageFeePerOrder": 16.00
}
```

> **Nota:** A plataforma cobra 8% sobre cada venda concluída.

---

### 📍 Endereços

| Método | Endpoint | Autenticação | Descrição |
|--------|----------|--------------|-----------|
| POST | `/api/addresses` | ✅ CUSTOMER | Criar endereço |
| GET | `/api/addresses/{id}` | ✅ CUSTOMER | Buscar endereço |
| GET | `/api/addresses/customer/{id}` | ✅ CUSTOMER | Endereços do cliente |
| PUT | `/api/addresses/{id}/set-default` | ✅ CUSTOMER | Marcar como padrão |
| DELETE | `/api/addresses/{id}` | ✅ CUSTOMER | Deletar endereço |

**Exemplo - Criar endereço:**
```http
POST /api/addresses
Authorization: Bearer {token}
Content-Type: application/json

{
  "customerId": 1,
  "cityId": 1,
  "street": "Rua das Flores",
  "number": "123",
  "complement": "Apto 45",
  "neighborhood": "Centro",
  "zipCode": "01234567",
  "reference": "Próximo ao mercado",
  "isDefault": true
}
```

---

## 🔒 Segurança

### Controle de Acesso por Role

| Recurso | Público | CUSTOMER | SELLER | ADMIN |
|---------|---------|----------|--------|-------|
| Listar cidades | ✅ | ✅ | ✅ | ✅ |
| Criar cidade | ❌ | ❌ | ❌ | ✅ |
| Ver lojas | ✅ | ✅ | ✅ | ✅ |
| Criar loja | ❌ | ❌ | ✅ | ✅ |
| Criar produto | ❌ | ❌ | ✅ | ✅ |
| Criar pedido | ❌ | ✅ | ❌ | ✅ |
| Gerenciar pedidos | ❌ | ❌ | ✅ | ✅ |
| Dashboard admin | ❌ | ❌ | ❌ | ✅ |

### Autenticação JWT

**Token incluído no header:**
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Token válido por:** 24 horas

**Renovar token:**
```http
POST /api/auth/refresh
Authorization: Bearer {old_token}
```

### Senhas

- Algoritmo: **BCrypt**
- Mínimo: 6 caracteres
- Hash automático no cadastro

### Auditoria

Todas as entidades registram:
- `created_by`: Quem criou
- `created_at`: Quando criou
- `updated_by`: Quem modificou
- `updated_at`: Quando modificou

---

## 🧪 Testes

### Executar Testes
```bash
# Todos os testes
./mvnw test

# Apenas testes unitários
./mvnw test -Dgroups=unit

# Apenas testes de integração
./mvnw test -Dgroups=integration

# Com cobertura
./mvnw test jacoco:report
```

### Cobertura Atual

- ✅ Testes Unitários: Domain Layer
- ✅ Testes de Integração: Repositories
- ✅ Testes de Use Cases
- 🔄 Testes E2E: Em desenvolvimento

### Testcontainers

Testes de integração usam PostgreSQL via Testcontainers:
```java
@Testcontainers
class OrderRepositoryTest extends BaseIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = 
        new PostgreSQLContainer<>("postgres:16-alpine");
}
```

---

## 🚀 Deploy

### Build para Produção
```bash
# Gerar JAR
./mvnw clean package -DskipTests

# JAR gerado em:
target/delivery-backend-0.0.1-SNAPSHOT.jar
```

### Executar JAR
```bash
java -jar target/delivery-backend-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:postgresql://db-host:5432/deliverydb \
  --spring.datasource.username=prod_user \
  --spring.datasource.password=prod_pass
```

### Docker
```bash
# Build image
docker build -t delivery-backend:latest .

# Run container
docker run -d \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/db \
  -e SPRING_DATASOURCE_USERNAME=user \
  -e SPRING_DATASOURCE_PASSWORD=pass \
  delivery-backend:latest
```

### Variáveis de Ambiente Importantes
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/deliverydb
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
JWT_SECRET=seu_secret_super_seguro_aqui
JWT_EXPIRATION=86400000
SERVER_PORT=8080
```

---

## 📈 Roadmap

### v1.1 (Em desenvolvimento)
- [ ] Sistema de cupons e promoções
- [ ] Notificações em tempo real (WebSocket)
- [ ] Upload de imagens de produtos
- [ ] Geolocalização de lojas

### v1.2 (Planejado)
- [ ] Chat entre cliente e loja
- [ ] Sistema de avaliações de produtos
- [ ] Programa de fidelidade avançado
- [ ] Relatórios em PDF

### v2.0 (Futuro)
- [ ] App Mobile (React Native)
- [ ] Integração com pagamentos
- [ ] Sistema de entregadores
- [ ] Multi-idioma

## 📄 Licença

Este projeto está sob a licença **MIT**.  
Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 👨‍💻 Autor

**Pedro Ferreira**

- 📧 Email: pedro@deliveryapp.com
- 💻 GitHub: [@pedroferreira](https://github.com/pedroferreira)
- 🔗 LinkedIn: Pedro Ferreira

---

## 🙏 Agradecimentos

- Spring Boot Team
- PostgreSQL Community
- Testcontainers Project
- Todos os contribuidores

---

<div align="center">

⭐ **Se este projeto te ajudou, considere dar uma estrela!**  
<br/>
Made with ❤️ by **Pedro Ferreira**

</div>
