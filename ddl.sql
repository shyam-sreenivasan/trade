DROP SCHEMA exchange CASCADE;
CREATE SCHEMA exchange;


-- Ensure schema exists
CREATE SCHEMA IF NOT EXISTS exchange;

-- Create the "user" table
CREATE TABLE exchange."user" (
    id SERIAL PRIMARY KEY,                        
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    status INT DEFAULT 1,                   
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL,
    roles VARCHAR(255) DEFAULT 'user'
);

-- Create the "coin" table
CREATE TABLE exchange."coin" (
    id SERIAL PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    code VARCHAR(10) NOT NULL,
    status INT,
    trade_config TEXT,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL
);

-- Create the "wallet" table
CREATE TABLE exchange."wallet" (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    coin_id INT NOT NULL,
    status INT DEFAULT 1,                
    balance DECIMAL(40,8) DEFAULT 0.0,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES exchange."user"(id),
    FOREIGN KEY (coin_id) REFERENCES exchange."coin"(id)
);

-- Create the "trade" table
CREATE TABLE exchange."trade" (
    id SERIAL PRIMARY KEY,
    user_id INT,
    trade_type int NOT NULL, -- 0 - buy, 1 - sell
    coin_a INT,
    coin_b INT,
    coina_value DECIMAL(40,8),
    coinb_value DECIMAL(40,8),
    price DECIMAL(40,8),
    status INT DEFAULT 1,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES exchange."user"(id),
    FOREIGN KEY (coin_a) REFERENCES exchange."coin"(id),
    FOREIGN KEY (coin_b) REFERENCES exchange."coin"(id)
);

