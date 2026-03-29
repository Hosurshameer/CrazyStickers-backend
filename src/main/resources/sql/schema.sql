CREATE TABLE IF NOT EXISTS products
(
    product_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(250)                          NOT NULL,
    description VARCHAR(500)                          NOT NULL,
    price       DECIMAL(10, 2)                        NOT NULL,
    popularity  INT                                   NOT NULL,
    image_url   VARCHAR(500),
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by  VARCHAR(20)                           NOT NULL,
    updated_at  TIMESTAMP   DEFAULT NULL,
    updated_by  VARCHAR(20) DEFAULT NULL
    );

    CREATE TABLE IF NOT EXISTS customers
    (
        customer_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
        name          VARCHAR(100)                          NOT NULL,
        email         VARCHAR(100)                          NOT NULL UNIQUE,
        mobile_number VARCHAR(15)                           NOT NULL,
        password_hash VARCHAR(500)                          NOT NULL,
        created_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
        created_by    VARCHAR(20)                           NOT NULL,
        updated_at    TIMESTAMP   DEFAULT NULL,
        updated_by    VARCHAR(20) DEFAULT NULL,
        UNIQUE KEY unique_email (email),
        UNIQUE KEY unique_mobile_number (mobile_number)
        );


    CREATE TABLE IF NOT EXISTS contacts
    (
        contact_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
        name          VARCHAR(100)                          NOT NULL,
        email         VARCHAR(100)                          NOT NULL,
        mobile_number VARCHAR(15)                           NOT NULL,
        message       VARCHAR(500)                          NOT NULL,
        created_at    TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
        created_by    VARCHAR(20)                           NOT NULL,
        updated_at    TIMESTAMP   DEFAULT NULL,
        updated_by    VARCHAR(20) DEFAULT NULL
        );



CREATE TABLE IF NOT EXISTS address
(
    address_id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id       BIGINT NOT NULL UNIQUE,
    street        VARCHAR(150) NOT NULL,
    city          VARCHAR(100) NOT NULL,
    state         VARCHAR(100) NOT NULL,
    postal_code   VARCHAR(20)  NOT NULL,
    country       VARCHAR(100) NOT NULL,
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by    VARCHAR(20)  NOT NULL,
    updated_at    TIMESTAMP    DEFAULT NULL,
    updated_by    VARCHAR(20)  DEFAULT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE

);


CREATE TABLE IF NOT EXISTS roles (
    role_id     BIGINT AUTO_INCREMENT PRIMARY KEY,

    name        VARCHAR(50) NOT NULL,
    created_at TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP   DEFAULT NULL,
    updated_by VARCHAR(20) DEFAULT NULL,
    UNIQUE KEY unique_name(name);
    );

    create table if not exists customer_roles(
    customer_id bigint not null,
    role_id bigint not null,

    primary key (customer_id,role_id),
    foreign key(customer_id) references customers(customer_id) on delete cascade,
    foreign key(role_id) references roles(role_id) on delete cascade

    );



    CREATE TABLE IF NOT EXISTS order_items
    (
        order_item_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
        order_id        BIGINT NOT NULL,
        product_id      BIGINT NOT NULL,
        quantity        INT NOT NULL,
        price           DECIMAL(10, 2) NOT NULL,
        created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        created_by      VARCHAR(20)    NOT NULL,
        updated_at      TIMESTAMP      DEFAULT NULL,
        updated_by      VARCHAR(20)    DEFAULT NULL,
        FOREIGN KEY (order_id) REFERENCES orders(order_id),
        FOREIGN KEY (product_id) REFERENCES products(product_id)
        );


CREATE TABLE IF NOT EXISTS orders
(
    order_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id    BIGINT NOT NULL,
    total_price    DECIMAL(10, 2)                        NOT NULL,
    payment_id     VARCHAR(200)                          NOT NULL,
    payment_status VARCHAR(50)                           NOT NULL,
    order_status   VARCHAR(50)                           NOT NULL,
    created_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by     VARCHAR(20)                           NOT NULL,
    updated_at     TIMESTAMP   DEFAULT NULL,
    updated_by     VARCHAR(20) DEFAULT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers (customer_id)
    );

    CREATE TABLE custom_stickers (
        id BIGINT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(255),
        image_path VARCHAR(500),
        sticker_type VARCHAR(50),
        created_at TIMESTAMP
    );

