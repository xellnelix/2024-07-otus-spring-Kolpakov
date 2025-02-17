--liquibase formatted sql

--changeset xellnelix:test-initialization
INSERT INTO data.categories (name) VALUES ('TestCategoryFirst');
INSERT INTO data.categories (name) VALUES ('TestCategorySecond');
INSERT INTO data.categories (name) VALUES ('TestCategoryThird');

INSERT INTO data.users(login, password, role) VALUES('admin', 'adminpass', 'ROLE_ADMIN');
INSERT INTO data.users(login, password, role) VALUES('user', 'userpass', 'ROLE_USER');

INSERT INTO data.accounts(user_id, name, balance, "date") VALUES (1, 'AdminDemoAccount', 999999.99, '2025-02-01'::date);
INSERT INTO data.accounts(user_id, name, balance, "date") VALUES (2, 'UserDemoAccount', 1.00, '2025-02-02'::date);

INSERT INTO data.transactions(account_id, type, category_id, amount, "date") VALUES(1, 'INCOME', 1, 1000000.00, '2025-02-03'::date);
INSERT INTO data.transactions(account_id, type, category_id, amount, "date") VALUES(1, 'EXPENSE', 1, 0.01, '2025-02-04'::date);
INSERT INTO data.transactions(account_id, type, category_id, amount, "date") VALUES(1, 'INCOME', 2, 900.00, '2025-02-05'::date);
INSERT INTO data.transactions(account_id, type, category_id, amount, "date") VALUES(2, 'INCOME', 3, 100.00, '2025-02-06'::date);
INSERT INTO data.transactions(account_id, type, category_id, amount, "date") VALUES(2, 'INCOME', 3, 1900.00, '2025-02-07'::date);
INSERT INTO data.transactions(account_id, type, category_id, amount, "date") VALUES(2, 'INCOME', 2, 2000000.00, '2025-02-08'::date);
INSERT INTO data.transactions(account_id, type, category_id, amount, "date") VALUES(2, 'EXPENSE', 2, 1000000.00, '2025-02-09'::date);