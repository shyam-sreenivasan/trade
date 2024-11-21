
TRUNCATE exchange.trade;
TRUNCATE exchange.wallet;
TRUNCATE exchange.coin;
TRUNCATE exchange.user;

INSERT INTO exchange."user" (id, username, password, email, status, created_at, modified_at, roles) VALUES
(1, 'shyam_sreeni', 'password123', 'shyamsreeni@example.com', 1, NOW(), NOW(), 'admin'),
(2, 'jane_smith', 'password456', 'jane.smith@example.com', 1, NOW(), NOW(), 'user'),
(3, 'john_doe', 'password456', 'john.doe@example.com', 1, NOW(), NOW(), 'user'),
(4, 'alice_wang', 'password789', 'alice.wang@example.com', 1, NOW(), NOW(), 'user'),
(5, 'bob_johnson', 'password321', 'bob.johnson@example.com', 1, NOW(), NOW(), 'user'),
(6, 'charlie_brown', 'password654', 'charlie.brown@example.com', 1, NOW(), NOW(), 'user'),
(7, 'david_white', 'password111', 'david.white@example.com', 1, NOW(), NOW(), 'user'),
(8, 'emma_davis', 'password222', 'emma.davis@example.com', 1, NOW(), NOW(), 'user'),
(9, 'frank_hall', 'password333', 'frank.hall@example.com', 1, NOW(), NOW(), 'user'),
(10, 'grace_moore', 'password444', 'grace.moore@example.com', 1, NOW(), NOW(), 'user');


INSERT INTO exchange."coin" (name, code, status, trade_config, created_at, modified_at)
VALUES
    ('Bitcoin', 'BTC', 1, '{"min_trade_size": "0.001", "max_trade_size": "10"}', NOW(), NOW()),
    ('Ethereum', 'ETH', 1, '{"min_trade_size": "0.01", "max_trade_size": "50"}', NOW(), NOW()),
    ('Tether', 'USDT', 1, '{"min_trade_size": "1", "max_trade_size": "100000"}', NOW(), NOW()),
    ('Ripple', 'XRP', 1, '{"min_trade_size": "1", "max_trade_size": "100000"}', NOW(), NOW());

INSERT INTO exchange.wallet (id, user_id, coin_id, status, balance, created_at, modified_at) VALUES
-- User 1
(1, 1, 1, 1, 0.5, NOW(), NOW()),   
(2, 1, 2, 1, 1.2, NOW(), NOW()),   
(3, 1, 3, 1, 1000, NOW(), NOW()),
(4, 1, 4, 1, 200, NOW(), NOW()),

-- User 2
(5, 2, 1, 1, 0.8, NOW(), NOW()),   
(6, 2, 2, 1, 2.0, NOW(), NOW()),   
(7, 2, 3, 1, 1500, NOW(), NOW()),
(8, 2, 4, 1, 600, NOW(), NOW()),

-- User 3
(9, 3, 1, 1, 0.0, NOW(), NOW()),   
(10, 3, 2, 1, 0.0, NOW(), NOW()),   
(11, 3, 3, 1, 10000, NOW(), NOW()),
(12, 3, 4, 1, 500, NOW(), NOW()),

-- User 4
(13, 4, 1, 1, 0.3, NOW(), NOW()),   
(14, 4, 2, 1, 3.1, NOW(), NOW()),   
(15, 4, 3, 1, 1200, NOW(), NOW()),
(16, 4, 4, 1, 700, NOW(), NOW()),

-- User 5
(17, 5, 1, 1, 1.1, NOW(), NOW()),   
(18, 5, 2, 1, 2.5, NOW(), NOW()),   
(19, 5, 3, 1, 1300, NOW(), NOW()),
(20, 5, 4, 1, 900, NOW(), NOW()),

-- User 6
(21, 6, 1, 1, 0.7, NOW(), NOW()),   
(22, 6, 2, 1, 1.0, NOW(), NOW()),   
(23, 6, 3, 1, 1400, NOW(), NOW()),
(24, 6, 4, 1, 400, NOW(), NOW()),

-- User 7
(25, 7, 1, 1, 0.2, NOW(), NOW()),   
(26, 7, 2, 1, 0.5, NOW(), NOW()),   
(27, 7, 3, 1, 1700, NOW(), NOW()),
(28, 7, 4, 1, 800, NOW(), NOW()),

-- User 8
(29, 8, 1, 1, 0.6, NOW(), NOW()),   
(30, 8, 2, 1, 0.9, NOW(), NOW()),   
(31, 8, 3, 1, 1900, NOW(), NOW()),
(32, 8, 4, 1, 300, NOW(), NOW()),

-- User 9
(33, 9, 1, 1, 1.0, NOW(), NOW()),   
(34, 9, 2, 1, 2.3, NOW(), NOW()),   
(35, 9, 3, 1, 1100, NOW(), NOW()),
(36, 9, 4, 1, 600, NOW(), NOW()),

-- User 10
(37, 10, 1, 1, 0.4, NOW(), NOW()),   
(38, 10, 2, 1, 0.6, NOW(), NOW()),   
(39, 10, 3, 1, 1800, NOW(), NOW()),
(40, 10, 4, 1, 500, NOW(), NOW());


DELETE FROM exchange.trade;

ALTER TABLE exchange.trade ALTER COLUMN coina_value TYPE DECIMAL(40,8);
ALTER TABLE exchange.trade ALTER COLUMN coinb_value TYPE DECIMAL(40,8);
ALTER TABLE exchange.trade ALTER COLUMN price TYPE DECIMAL(40,8);

update exchange.wallet set balance = 0.5 where id = 1;

SELECT 
    t.id,
    t.user_id,
    t.trade_type,
    c1.code AS coin_a_code,
    c2.code AS coin_b_code,
    t.coina_value,
    t.coinb_value,
    t.price,
    t.status,
    t.created_at,
    t.modified_at
FROM 
    exchange.trade t
JOIN 
    exchange.coin c1 ON t.coin_a = c1.id
JOIN 
    exchange.coin c2 ON t.coin_b = c2.id;


SELECT 
    w.id,
    w.user_id,
    u.username,
    c.code AS coin_code,
    w.balance,
    w.status,
    w.created_at,
    w.modified_at
FROM 
    exchange.wallet w
JOIN 
    exchange.coin c ON w.coin_id = c.id
JOIN 
    exchange.user u ON w.user_id = u.id
ORDER BY w.user_id;
