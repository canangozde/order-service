INSERT INTO customers(username, password, role, external_id)
VALUES
    ('admin', '$2a$10$NfMsX0cAqp3vH5s10VI18e4Xg1oHwOkATn46JUmddCpjsU2P9NXt.', 'ADMIN', 'admin'),
    ('canan', '$2a$10$wgItMK9POw8GQCtPWHgXqe19TeV/e/Io0DhKr8Vko570jX9w9zbmC', 'CUSTOMER', 'canan');

-- şifreler sırasıyla:
-- admin: admin123
-- canan: canan123

INSERT INTO assets (id, customer_id, asset_name, size, usable_size)
VALUES
    (1, 2, 'TRY', 100000, 100000),
    (2, 2, 'ING', 100, 100),
    (3, 1, 'TRY', 100000, 100000),
    (4, 1, 'ING', 100, 100);