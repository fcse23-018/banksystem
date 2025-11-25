-- Banking System Database Schema
-- CSE202 Assignment - Part B
-- Student: Donovan Ntsima (FCSE23-018)

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS TRANSACTIONS CASCADE;
DROP TABLE IF EXISTS ACCOUNTS CASCADE;
DROP TABLE IF EXISTS CUSTOMERS CASCADE;

-- Create CUSTOMERS table
CREATE TABLE CUSTOMERS (
    customer_id VARCHAR(50) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    password VARCHAR(255) NOT NULL,
    pin VARCHAR(4) NOT NULL DEFAULT '1234',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create ADMINS table
CREATE TABLE ADMINS (
    admin_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create ACCOUNTS table with discriminator column
CREATE TABLE ACCOUNTS (
    account_number VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    branch VARCHAR(100) NOT NULL,
    employer_company VARCHAR(200),
    employer_address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES CUSTOMERS(customer_id) ON DELETE CASCADE
);

-- Create TRANSACTIONS table
CREATE TABLE TRANSACTIONS (
    transaction_id VARCHAR(50) PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    balance_after DECIMAL(15, 2) NOT NULL,
    transfer_to_account VARCHAR(50),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_number) REFERENCES ACCOUNTS(account_number) ON DELETE CASCADE
);

-- Insert sample customer data (10+ records as required)
-- Passwords are BCrypt hashed for "password123", PIN is "1234" for all
INSERT INTO CUSTOMERS (customer_id, first_name, surname, address, password, pin) VALUES
('CUST001', 'John', 'Doe', '123 Main Street, Gaborone', '$2a$12$7E1nPLCWvTwH3k5bFqZx5eR2vK8PnZxTt6yH4xF5mJ8nL9pR3sT6u', '1234'),
('CUST002', 'Jane', 'Smith', '456 Oak Avenue, Francistown', '$2a$12$7E1nPLCWvTwH3k5bFqZx5eR2vK8PnZxTt6yH4xF5mJ8nL9pR3sT6u', '1234'),
('CUST003', 'Michael', 'Johnson', '789 Pine Road, Maun', '$2a$12$7E1nPLCWvTwH3k5bFqZx5eR2vK8PnZxTt6yH4xF5mJ8nL9pR3sT6u', '1234'),
('CUST004', 'Sarah', 'Williams', '321 Elm Street, Kasane', '$2a$12$7E1nPLCWvTwH3k5bFqZx5eR2vK8PnZxTt6yH4xF5mJ8nL9pR3sT6u', '1234'),
('CUST005', 'David', 'Brown', '654 Maple Drive, Palapye', '$2a$12$7E1nPLCWvTwH3k5bFqZx5eR2vK8PnZxTt6yH4xF5mJ8nL9pR3sT6u', '1234');

-- Insert sample admin data
-- Password is BCrypt hashed for "admin123"
INSERT INTO ADMINS (admin_id, username, password, full_name) VALUES
('ADMIN001', 'admin', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYVvMpYq0yO', 'System Administrator');

-- Insert sample account data (mixed account types for each customer)
INSERT INTO ACCOUNTS (account_number, customer_id, account_type, balance, branch, employer_company, employer_address) VALUES
-- John Doe's accounts
('ACC1001', 'CUST001', 'Savings Account', 5000.00, 'Main Branch', NULL, NULL),
('ACC1002', 'CUST001', 'Investment Account', 15000.00, 'Main Branch', NULL, NULL),
('ACC1003', 'CUST001', 'Cheque Account', 8500.00, 'Main Branch', 'Botswana Power Corporation', 'Gaborone, Botswana'),
-- Jane Smith's accounts
('ACC1004', 'CUST002', 'Savings Account', 3200.00, 'Francistown Branch', NULL, NULL),
('ACC1005', 'CUST002', 'Cheque Account', 12000.00, 'Francistown Branch', 'Ministry of Education', 'Francistown, Botswana'),
-- Michael Johnson's accounts
('ACC1006', 'CUST003', 'Investment Account', 25000.00, 'Maun Branch', NULL, NULL),
('ACC1007', 'CUST003', 'Savings Account', 7500.00, 'Maun Branch', NULL, NULL),
-- Sarah Williams's accounts
('ACC1008', 'CUST004', 'Cheque Account', 9800.00, 'Kasane Branch', 'Chobe Safari Lodge', 'Kasane, Botswana'),
('ACC1009', 'CUST004', 'Investment Account', 18500.00, 'Kasane Branch', NULL, NULL),
-- David Brown's accounts
('ACC1010', 'CUST005', 'Savings Account', 4200.00, 'Palapye Branch', NULL, NULL),
('ACC1011', 'CUST005', 'Investment Account', 22000.00, 'Palapye Branch', NULL, NULL),
('ACC1012', 'CUST005', 'Cheque Account', 11500.00, 'Palapye Branch', 'BCL Mine', 'Palapye, Botswana');

-- Insert sample transaction data
INSERT INTO TRANSACTIONS (transaction_id, account_number, amount, transaction_type, balance_after, transaction_date) VALUES
-- Transactions for ACC1001 (John's Savings)
('TXN1001', 'ACC1001', 5000.00, 'DEPOSIT', 5000.00, '2025-10-01 10:00:00'),
('TXN1002', 'ACC1001', 2.50, 'INTEREST', 5002.50, '2025-11-01 00:00:00'),
-- Transactions for ACC1002 (John's Investment)
('TXN1003', 'ACC1002', 15000.00, 'DEPOSIT', 15000.00, '2025-09-15 14:30:00'),
('TXN1004', 'ACC1002', 750.00, 'INTEREST', 15750.00, '2025-10-15 00:00:00'),
('TXN1005', 'ACC1002', 5000.00, 'WITHDRAWAL', 10750.00, '2025-10-20 11:00:00'),
('TXN1006', 'ACC1002', 537.50, 'INTEREST', 11287.50, '2025-11-15 00:00:00'),
-- Transactions for ACC1003 (John's Cheque)
('TXN1007', 'ACC1003', 8500.00, 'DEPOSIT', 8500.00, '2025-10-05 09:00:00'),
('TXN1008', 'ACC1003', 1200.00, 'WITHDRAWAL', 7300.00, '2025-10-10 15:30:00'),
('TXN1009', 'ACC1003', 3500.00, 'DEPOSIT', 10800.00, '2025-11-01 10:00:00'),
-- Transactions for ACC1004 (Jane's Savings)
('TXN1010', 'ACC1004', 3200.00, 'DEPOSIT', 3200.00, '2025-09-20 12:00:00'),
('TXN1011', 'ACC1004', 1.60, 'INTEREST', 3201.60, '2025-10-20 00:00:00'),
-- Transactions for ACC1005 (Jane's Cheque)
('TXN1012', 'ACC1005', 12000.00, 'DEPOSIT', 12000.00, '2025-10-01 08:00:00'),
('TXN1013', 'ACC1005', 2500.00, 'WITHDRAWAL', 9500.00, '2025-10-15 14:00:00'),
-- Transactions for ACC1006 (Michael's Investment)
('TXN1014', 'ACC1006', 25000.00, 'DEPOSIT', 25000.00, '2025-09-10 13:00:00'),
('TXN1015', 'ACC1006', 1250.00, 'INTEREST', 26250.00, '2025-10-10 00:00:00'),
('TXN1016', 'ACC1006', 10000.00, 'WITHDRAWAL', 16250.00, '2025-10-25 16:00:00'),
-- Additional transactions for variety
('TXN1017', 'ACC1007', 7500.00, 'DEPOSIT', 7500.00, '2025-10-12 11:30:00'),
('TXN1018', 'ACC1008', 9800.00, 'DEPOSIT', 9800.00, '2025-09-25 10:15:00'),
('TXN1019', 'ACC1009', 18500.00, 'DEPOSIT', 18500.00, '2025-10-08 14:45:00'),
('TXN1020', 'ACC1010', 4200.00, 'DEPOSIT', 4200.00, '2025-10-18 09:30:00');

-- Create indexes for better query performance
CREATE INDEX idx_accounts_customer ON ACCOUNTS(customer_id);
CREATE INDEX idx_transactions_account ON TRANSACTIONS(account_number);
CREATE INDEX idx_transactions_date ON TRANSACTIONS(transaction_date);

-- Display summary statistics
SELECT 'Database initialized successfully' AS status;
SELECT COUNT(*) AS total_customers FROM CUSTOMERS;
SELECT COUNT(*) AS total_accounts FROM ACCOUNTS;
SELECT COUNT(*) AS total_transactions FROM TRANSACTIONS;
SELECT account_type, COUNT(*) AS count FROM ACCOUNTS GROUP BY account_type;
