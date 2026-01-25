CREATE TABLE IF NOT EXISTS cities (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    state VARCHAR(2) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    CONSTRAINT uk_city_name_state UNIQUE (name, state)
    );

CREATE INDEX idx_cities_active ON cities(active);
CREATE INDEX idx_cities_state ON cities(state);


CREATE TABLE IF NOT EXISTS admins (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(300) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    role VARCHAR(20) NOT NULL DEFAULT 'ADMIN',
    full_access BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,


    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
    );

CREATE INDEX idx_admins_email ON admins(email);
CREATE INDEX idx_admins_active ON admins(active);


CREATE TABLE IF NOT EXISTS customers (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(300) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    oauth_provider VARCHAR(50),
    oauth_id VARCHAR(150),
    loyalty_points INTEGER NOT NULL DEFAULT 0,


    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100)
    );

CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_cpf ON customers(cpf);
CREATE INDEX idx_customers_active ON customers(active);


CREATE TABLE IF NOT EXISTS stores (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    city_id BIGINT NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    address VARCHAR(300) NOT NULL,
    category VARCHAR(50) NOT NULL,
    opening_time TIME,
    closing_time TIME,
    delivery_fee_per_km DECIMAL(10,2) NOT NULL,
    base_delivery_fee DECIMAL(10,2) NOT NULL,
    minimum_order DECIMAL(10,2) NOT NULL,
    created_by_admin_id BIGINT,
    active BOOLEAN NOT NULL DEFAULT true,
    open BOOLEAN NOT NULL DEFAULT false,
    rating DECIMAL(3,2) DEFAULT 0.00,
    total_ratings INTEGER DEFAULT 0,
    total_sales INTEGER DEFAULT 0,

    -- Auditoria
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100),

    CONSTRAINT fk_store_city FOREIGN KEY (city_id) REFERENCES cities(id),
    CONSTRAINT fk_store_admin FOREIGN KEY (created_by_admin_id) REFERENCES admins(id)
    );

CREATE INDEX idx_stores_city_id ON stores(city_id);
CREATE INDEX idx_stores_active ON stores(active);
CREATE INDEX idx_stores_category ON stores(category);
CREATE INDEX idx_stores_city_active_open ON stores(city_id, active, open);

CREATE TABLE IF NOT EXISTS sellers (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    address VARCHAR(300) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    role VARCHAR(20) NOT NULL DEFAULT 'SELLER',
    store_id BIGINT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100),

    CONSTRAINT fk_seller_store FOREIGN KEY (store_id) REFERENCES stores(id)
    );

CREATE INDEX idx_sellers_email ON sellers(email);
CREATE INDEX idx_sellers_store_id ON sellers(store_id);

CREATE TABLE IF NOT EXISTS addresses (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    city_id BIGINT NOT NULL,
    street VARCHAR(200) NOT NULL,
    number VARCHAR(20) NOT NULL,
    complement VARCHAR(100),
    neighborhood VARCHAR(100) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    reference TEXT,
    is_default BOOLEAN NOT NULL DEFAULT false,
    active BOOLEAN NOT NULL DEFAULT true,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100),

    CONSTRAINT fk_address_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_address_city FOREIGN KEY (city_id) REFERENCES cities(id)
    );

CREATE INDEX idx_addresses_customer_id ON addresses(customer_id);
CREATE INDEX idx_addresses_city_id ON addresses(city_id);
CREATE INDEX idx_addresses_customer_default ON addresses(customer_id, is_default);

CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url VARCHAR(500),
    store_id BIGINT NOT NULL,
    available BOOLEAN NOT NULL DEFAULT true,
    preparation_time INTEGER,
    active BOOLEAN NOT NULL DEFAULT true,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100),
    updated_at TIMESTAMP,
    updated_by VARCHAR(100),

    CONSTRAINT fk_product_store FOREIGN KEY (store_id) REFERENCES stores(id)
    );

CREATE INDEX idx_products_store_id ON products(store_id);
CREATE INDEX idx_products_available ON products(available);
CREATE INDEX idx_products_store_available ON products(store_id, available, active);


CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    store_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'CREATED',
    delivery_address VARCHAR(500) NOT NULL,
    delivery_distance_km DECIMAL(5,2),
    delivery_fee DECIMAL(10,2) NOT NULL,
    discount DECIMAL(10,2) DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL,
    observations TEXT,
    cancellation_reason TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    confirmed_at TIMESTAMP,
    ready_at TIMESTAMP,
    delivered_at TIMESTAMP,
    canceled_at TIMESTAMP,

    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_order_store FOREIGN KEY (store_id) REFERENCES stores(id)
    );

CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_orders_store_id ON orders(store_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);


CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    discount DECIMAL(10,2) DEFAULT 0.00,
    observations TEXT,

    CONSTRAINT fk_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_product FOREIGN KEY (product_id) REFERENCES products(id)
    );

CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_order_items_product_id ON order_items(product_id);


CREATE TABLE IF NOT EXISTS seller_applications (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    proposed_store_name VARCHAR(150) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT,
    store_address VARCHAR(300) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(2) NOT NULL,
    business_phone VARCHAR(20) NOT NULL,
    whatsapp VARCHAR(20),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP,
    rejection_reason TEXT,
    processed_by_admin_id BIGINT,

    CONSTRAINT fk_application_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
    CONSTRAINT fk_application_admin FOREIGN KEY (processed_by_admin_id) REFERENCES admins(id)
    );

CREATE INDEX idx_applications_status ON seller_applications(status);
CREATE INDEX idx_applications_customer_id ON seller_applications(customer_id);


INSERT INTO admins (username, email, password, cpf, phone, address, full_access)
VALUES ('admin', 'admin@delivery.com', 'admin123',
        '00000000000', '11999999999', 'Admin Address', true)
    ON CONFLICT (email) DO NOTHING;

-- Inserir cidades exemplo
INSERT INTO cities (name, state, active) VALUES
                                             ('São Paulo', 'SP', true),
                                             ('Rio de Janeiro', 'RJ', true),
                                             ('Belo Horizonte', 'MG', true),
                                             ('Brasília', 'DF', true),
                                             ('Salvador', 'BA', true),
                                             ('Fortaleza', 'CE', true),
                                             ('Recife', 'PE', true),
                                             ('Porto Alegre', 'RS', true),
                                             ('Curitiba', 'PR', true),
                                             ('Natal', 'RN', true)
    ON CONFLICT (name, state) DO NOTHING;