INSERT INTO customer (customer_number, customer_first_name, customer_last_name, customer_contact_number, customer_email, created_datetime, version) VALUES (111, 'Customer', 'One', '0123456789', 'customer.one@example.com', NOW(), 0);
INSERT INTO customer (customer_number, customer_first_name, customer_last_name, customer_contact_number, customer_email, created_datetime, version) VALUES (222, 'Customer', 'Two', '0987654321', 'customer.one@example.com', NOW(), 0);
INSERT INTO customer (customer_number, customer_first_name, customer_last_name, customer_contact_number, customer_email, created_datetime, version) VALUES (333, 'Customer', 'Three', '0654987321', 'customer.three@example.com', NOW(), 0);
-- INSERT INTO customer (customer_number, customer_name, created_datetime) VALUES ('111', 'Customer1', NOW());
-- INSERT INTO customer (customer_number, customer_name, created_datetime) VALUES ('222', 'Customer2', NOW());
-- INSERT INTO customer (customer_number, customer_name, created_datetime) VALUES ('333', 'Customer3', NOW());

INSERT INTO account (account_number, account_status, account_type, account_balance, created_datetime, customer_id, version) VALUES (8872838283, 'ACTIVE', 'SAVING', '10000000000.00', NOW(), 1, 0);
INSERT INTO account (account_number, account_status, account_type, account_balance, created_datetime, customer_id, version) VALUES (8872838299, 'ACTIVE', 'CURRENT', '10000000000.00', NOW(), 2, 0);
INSERT INTO account (account_number, account_status, account_type, account_balance, created_datetime, customer_id, version) VALUES (6872838260, 'ACTIVE', 'SAVING', '10000000000.00', NOW(), 3, 0);
-- INSERT INTO account (account_number, account_name, customer_id, created_datetime) VALUES ('8872838283', 'Customer1', 1, NOW());
-- INSERT INTO account (account_number, account_name, customer_id, created_datetime) VALUES ('8872838299', 'Customer2', 2, NOW());
-- INSERT INTO account (account_number, account_name, customer_id, created_datetime) VALUES ('6872838260', 'Customer3', 3, NOW());