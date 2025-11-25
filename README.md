# Banking System - JavaFX Application

**CSE202: Object-Oriented Analysis & Design with Java**  
**Assignment Part B - System Implementation**  
**Student:** Donovan Ntsima (FCSE23-018)  
**Programme:** BSc Computer Systems Engineering, Year 2, Semester 1

---

## Project Overview

This is a complete banking system application built with JavaFX and PostgreSQL. The system demonstrates all core OOP principles including abstraction, inheritance, polymorphism, encapsulation, and composition. It implements a full MVC+DAO architecture with zero coupling between layers.

### Key Features

- **Customer Management**: Registration, authentication, and profile management
- **Multiple Account Types**:
  - **Savings Account**: 0.05% monthly interest, no withdrawals allowed
  - **Investment Account**: 5% monthly interest, minimum BWP 500 deposit required
  - **Cheque Account**: Salary payments, requires employer information
- **Transactions**: Deposits, withdrawals (with account type restrictions), full transaction history
- **Interest Calculation**: Automatic monthly interest calculation displayed on dashboard
- **Database Persistence**: PostgreSQL (Supabase) with full CRUD operations

---

## Architecture

### MVC + DAO Pattern

```
View (FXML) → Controller → Domain Model → DAO → PostgreSQL Database
```

- **Model Package** (`com.banking.model`): Domain classes (Account, Customer, Transaction, etc.)
- **Controller Package** (`com.banking.controller`): JavaFX controllers for each view
- **DAO Package** (`com.banking.dao`): Data Access Objects for database operations
- **Util Package** (`com.banking.util`): Database connection and password utilities
- **Resources** (`src/main/resources`): FXML views and database schema

### OOP Principles Demonstrated

1. **Abstraction**: Abstract `Account` class
2. **Inheritance**: `SavingsAccount`, `InvestmentAccount`, `ChequeAccount` extend `Account`
3. **Polymorphism**: `InterestBearing` interface with different implementations
4. **Encapsulation**: Private attributes with public getters/setters
5. **Composition**: Customer has many Accounts (1:*)
6. **Method Overloading**: `Customer.openAccount()` methods

---

## Prerequisites

### Software Requirements

- **Java JDK 17** or higher
- **Maven 3.6+** for dependency management
- **PostgreSQL Database** (Supabase hosted)
- **JavaFX SDK 17+** (automatically managed by Maven)

### Database Configuration

The application connects to a PostgreSQL database hosted on Supabase with the following credentials (hardcoded in `DatabaseConnection.java`):

```
URL: jdbc:postgresql://db.vgssxkidvqovqebkobvp.supabase.co:5432/postgres
Username: postgres
Password: PulaBank2025!@#Gaborone
```

**Note**: In a production environment, these credentials should be stored securely in environment variables or a configuration file.

---

## Installation & Setup

### Option 1: Local Development Environment

#### 1. Clone or Download the Project

```bash
git clone <repository-url>
cd banking-system
```

#### 2. Install Dependencies

Maven will automatically download all required dependencies:

```bash
mvn clean install
```

#### 3. Initialize Database

The application will automatically create the database schema on first run. Alternatively, you can manually run the schema:

```bash
psql -h db.vgssxkidvqovqebkobvp.supabase.co -U postgres -d postgres -f src/main/resources/schema.sql
```

#### 4. Run the Application

Using Maven:

```bash
mvn javafx:run
```

Or compile and run manually:

```bash
mvn clean compile
java --module-path $PATH_TO_JAVAFX_LIB --add-modules javafx.controls,javafx.fxml -cp target/classes com.banking.MainApp
```

### Option 2: GitHub Codespaces (NOT Recommended)

**IMPORTANT**: JavaFX requires a desktop environment with a display server (X11, Wayland, etc.). GitHub Codespaces and Replit do not provide this environment, so the GUI **cannot run** in cloud-based environments.

However, you can still:
1. Compile the project to verify code correctness
2. Run database operations
3. Test business logic

```bash
# Compile only
mvn clean compile

# Run tests (if available)
mvn test
```

To actually run the application, you **must** use a local development environment with a desktop OS (Windows, macOS, or Linux with GUI).

---

## Usage Guide

### First Time Setup

1. **Run the Application**: The database schema will be created automatically with 10+ sample records
2. **Test Login**: Use sample credentials:
   - Customer ID: `CUST001`
   - Password: `password123`

### User Journeys

#### 1. Customer Registration

1. Click "Register" on the login screen
2. Fill in all required fields (First Name, Surname, Address, Customer ID, Password)
3. Click "Register" to create your account
4. Return to login and authenticate

#### 2. Open a Savings Account

1. Log in to the dashboard
2. Click "Open Account"
3. Select "Savings Account"
4. Enter initial deposit amount
5. Click "Open Account"

#### 3. Open an Investment Account

1. Click "Open Account"
2. Select "Investment Account"
3. Enter initial deposit (minimum BWP 500)
4. Click "Open Account"

#### 4. Open a Cheque Account

1. Click "Open Account"
2. Select "Cheque Account"
3. Enter initial deposit
4. Enter employer company name and address
5. Click "Open Account"

#### 5. Deposit Funds

1. Click "Deposit" from the dashboard
2. Select the account to deposit into
3. Enter deposit amount
4. Click "Deposit"
5. View updated balance on dashboard

#### 6. Withdraw Funds

1. Click "Withdraw" from the dashboard
2. Select an Investment or Cheque account (Savings accounts will be rejected)
3. Enter withdrawal amount
4. Click "Withdraw"
5. System validates sufficient funds

#### 7. View Transaction History

1. Select an account from the list
2. Click "Transaction History"
3. View all transactions with dates, types, amounts, and balances

#### 8. View Interest Calculation

1. Select a Savings or Investment account
2. View the "Account Details" panel
3. See the calculated monthly interest:
   - Savings: 0.05% of balance
   - Investment: 5% of balance

---

## Database Schema

### CUSTOMERS Table

| Column | Type | Description |
|--------|------|-------------|
| customer_id | VARCHAR(50) | Primary key |
| first_name | VARCHAR(100) | Customer's first name |
| surname | VARCHAR(100) | Customer's surname |
| address | TEXT | Customer's address |
| password | VARCHAR(255) | BCrypt hashed password |
| created_at | TIMESTAMP | Registration timestamp |

### ACCOUNTS Table

| Column | Type | Description |
|--------|------|-------------|
| account_number | VARCHAR(50) | Primary key |
| customer_id | VARCHAR(50) | Foreign key to CUSTOMERS |
| account_type | VARCHAR(20) | Discriminator: "Savings Account", "Investment Account", or "Cheque Account" |
| balance | DECIMAL(15,2) | Current account balance |
| branch | VARCHAR(100) | Branch name |
| employer_company | VARCHAR(200) | For Cheque accounts only |
| employer_address | TEXT | For Cheque accounts only |
| created_at | TIMESTAMP | Account creation timestamp |

### TRANSACTIONS Table

| Column | Type | Description |
|--------|------|-------------|
| transaction_id | VARCHAR(50) | Primary key |
| account_number | VARCHAR(50) | Foreign key to ACCOUNTS |
| amount | DECIMAL(15,2) | Transaction amount |
| transaction_type | VARCHAR(20) | "DEPOSIT", "WITHDRAWAL", or "INTEREST" |
| balance_after | DECIMAL(15,2) | Balance after transaction |
| transaction_date | TIMESTAMP | Transaction timestamp |

---

## Sample Data

The database is pre-populated with:

- **5 customers** with varying account portfolios
- **12 accounts** across all three types
- **20+ transactions** showing deposits, withdrawals, and interest payments

Login credentials for testing:
- CUST001 / password123
- CUST002 / password123
- CUST003 / password123
- CUST004 / password123
- CUST005 / password123

---

## Testing

### Manual Testing Checklist

- [x] Customer registration with validation
- [x] Customer login with correct/incorrect credentials
- [x] Open Savings account
- [x] Open Investment account with minimum deposit validation
- [x] Open Cheque account with employer information
- [x] Deposit to all account types
- [x] Withdraw from Investment account
- [x] Withdraw from Cheque account
- [x] Attempt to withdraw from Savings account (should be blocked)
- [x] View transaction history
- [x] View calculated interest for Savings account (0.05%)
- [x] View calculated interest for Investment account (5%)
- [x] Refresh dashboard to reload accounts

### Known Limitations

1. **Environment**: JavaFX GUI cannot run in cloud environments (Replit, Codespaces) - requires local desktop OS
2. **Database**: Using hardcoded credentials (not production-ready)
3. **Error Handling**: Basic error messages (could be enhanced with more detailed logging)
4. **Security**: Passwords are hashed with BCrypt, but additional security measures (MFA, rate limiting) could be added

---

## Project Structure

```
banking-system/
├── pom.xml                                 # Maven configuration
├── README.md                               # This file
├── INTEGRATION_TEST_REPORT.md              # Testing documentation
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── banking/
│   │   │           ├── MainApp.java        # Application entry point
│   │   │           ├── model/              # Domain model classes
│   │   │           │   ├── InterestBearing.java
│   │   │           │   ├── Account.java (abstract)
│   │   │           │   ├── SavingsAccount.java
│   │   │           │   ├── InvestmentAccount.java
│   │   │           │   ├── ChequeAccount.java
│   │   │           │   ├── Customer.java
│   │   │           │   └── Transaction.java
│   │   │           ├── dao/                # Data Access Objects
│   │   │           │   ├── CustomerDAO.java
│   │   │           │   ├── AccountDAO.java
│   │   │           │   └── TransactionDAO.java
│   │   │           ├── controller/         # JavaFX controllers
│   │   │           │   ├── LoginController.java
│   │   │           │   ├── RegisterController.java
│   │   │           │   ├── DashboardController.java
│   │   │           │   ├── AccountController.java
│   │   │           │   └── TransactionController.java
│   │   │           └── util/               # Utility classes
│   │   │               ├── DatabaseConnection.java
│   │   │               └── PasswordUtil.java
│   │   └── resources/
│   │       ├── fxml/                       # JavaFX views
│   │       │   ├── LoginView.fxml
│   │       │   ├── RegisterView.fxml
│   │       │   ├── DashboardView.fxml
│   │       │   ├── OpenAccountView.fxml
│   │       │   ├── DepositView.fxml
│   │       │   ├── WithdrawView.fxml
│   │       │   └── TransactionHistoryView.fxml
│   │       └── schema.sql                  # Database schema + sample data
│   └── test/                               # Unit tests (if available)
└── target/                                 # Compiled classes (generated)
```

---

## Technologies Used

- **Java 17**: Core programming language
- **JavaFX 17**: GUI framework
- **Maven**: Build and dependency management
- **PostgreSQL**: Relational database
- **Supabase**: Database hosting
- **BCrypt**: Password hashing
- **JDBC**: Database connectivity

---

## Credits

**Developed by:** Donovan Ntsima (FCSE23-018)  
**Institution:** Botswana Accountancy College  
**Module:** CSE202 - Object-Oriented Analysis & Design with Java  
**Assignment:** Part B - System Implementation  
**Submission Date:** November 17, 2025

---

## License

This project is submitted as part of academic coursework for CSE202 at Botswana Accountancy College. All rights reserved.
