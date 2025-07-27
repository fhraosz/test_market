CREATE TABLE IF NOT EXISTS member (
                                      id BIGINT PRIMARY KEY,
                                      name VARCHAR(100),
    created_at DATETIME,
    updated_at DATETIME
    );

CREATE TABLE IF NOT EXISTS product (
                                       id BIGINT PRIMARY KEY,
                                       name VARCHAR(100),
    price DECIMAL(20,2),
    created_at DATETIME,
    updated_at DATETIME
    );
INSERT INTO member (id, name, created_at, updated_at)
VALUES (1, '홍길동', NOW(), NOW());

INSERT INTO product (id, name, price, created_at, updated_at) VALUES
                                                              (101, '노트북',       1500000.00, NOW(), NOW()),
                                                              (102, '스마트폰',      800000.00, NOW(), NOW()),
                                                              (103, '무선 이어폰',   200000.00, NOW(), NOW());
