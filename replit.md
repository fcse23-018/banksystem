# Banking System - JavaFX Desktop Application (Enhanced Version 2.0)

## Overview

This is a complete banking system desktop application built with JavaFX and PostgreSQL, designed for the CSE202 Object-Oriented Analysis & Design course. The system implements a full-featured banking solution with customer management, multiple account types (Savings, Investment, Cheque), transaction processing, automated interest calculations, **fund transfers with PIN confirmation**, and **admin dashboard**.

**NEW FEATURES (v2.0)**:
- ✅ **Transfer Funds**: Transfer money between accounts with PIN confirmation
- ✅ **PIN Security**: 4-digit PIN required for all transfers
- ✅ **Admin Panel**: Separate admin login with dashboard showing system statistics
- ✅ **Dual Authentication**: Customer and Admin login support

**CRITICAL**: This is a **desktop application** that requires a graphical display environment and **cannot run in cloud-based IDEs** like Replit or GitHub Codespaces. It must be executed on a local machine with Java JDK 17+, Maven 3.6+, and JavaFX support.

The application demonstrates core OOP principles (abstraction, inheritance, polymorphism, encapsulation, composition) through a well-structured MVC + DAO architecture with PostgreSQL persistence.

## User Preferences

Preferred communication style: Simple, everyday language.

## System Architecture

### MVC + DAO Pattern

The application follows a strict Model-View-Controller pattern with Data Access Objects for database operations:

```
FXML Views → Controllers → Domain Model → DAO Layer → PostgreSQL Database
```

**Architectural Layers:**

1. **View Layer** (FXML + JavaFX)
   - 7 FXML view files for user interface
   - Views: Login, Register, Dashboard, OpenAccount, Deposit, Withdraw, TransactionHistory
   - Zero business logic in views - pure presentation layer
   - All views use standard JavaFX controls (TextField, ComboBox, ListView, TableView, Button)

2. **Controller Layer** (`com.banking.controller`)
   - 5 controllers implementing MVC pattern
   - Controllers: LoginController, RegisterController, DashboardController, AccountController, TransactionController
   - Handles user input validation, navigation between scenes, and Alert dialogs
   - Controllers mediate between views and domain model
   - No direct database access - all persistence through DAO layer

3. **Domain Model Layer** (`com.banking.model`)
   - 7 domain classes implementing OOP principles:
     - `Account` (abstract base class) - demonstrates abstraction
     - `SavingsAccount`, `InvestmentAccount`, `ChequeAccount` (concrete implementations) - demonstrates inheritance
     - `InterestBearing` interface - demonstrates polymorphism through `calculateInterest()` method
     - `Customer` class with composition relationship (1:* with Account)
     - `Transaction` class for transaction history
   - Business rules enforced at model level:
     - SavingsAccount: No withdrawals allowed, 0.05% monthly interest
     - InvestmentAccount: Minimum BWP 500 deposit, 5% monthly interest
     - ChequeAccount: Requires employer information for salary payments
   - All attributes private with public getters/setters (encapsulation)

4. **Data Access Layer** (`com.banking.dao`)
   - 3 DAO classes: CustomerDAO, AccountDAO, TransactionDAO
   - Full CRUD operations for all entities
   - Polymorphic account loading (reads correct subclass from database)
   - Zero business logic - pure data persistence

5. **Utility Layer** (`com.banking.util`)
   - `DatabaseConnection`: Manages PostgreSQL connection pooling
   - `PasswordUtil`: Handles password hashing/verification (if implemented)

**Design Decisions:**

- **Abstraction via Abstract Class**: `Account` is abstract because there's no such thing as a generic "account" - every account must be Savings, Investment, or Cheque. This forces proper typing and prevents invalid account creation.

- **Interface for Interest**: `InterestBearing` interface allows different account types to implement `calculateInterest()` differently (0.05% vs 5%), demonstrating polymorphism. This is cleaner than method overriding alone.

- **Composition over Inheritance**: Customer *has* Accounts (List<Account>) rather than extending Account. This models the real-world relationship correctly and allows one customer to have multiple accounts.

- **MVC Separation**: Strict separation ensures views can change without affecting business logic, and business logic can be tested without GUI.

- **DAO Pattern**: Separates data access from business logic, making it easy to switch databases or add caching without changing domain model.

**Database Schema:**

PostgreSQL database with three main tables:
- `CUSTOMERS`: Stores customer information (ID, name, address, password hash)
- `ACCOUNTS`: Stores all account types with discriminator column for polymorphism (account_type: 'SAVINGS', 'INVESTMENT', 'CHEQUE'), includes balance, interest rate, employer info (for Cheque)
- `TRANSACTIONS`: Stores transaction history (type, amount, timestamp, balance_after)

Auto-initialization on first run with sample data: 5 customers, 12 accounts, 20+ transactions.

## External Dependencies

### Database
- **PostgreSQL** hosted on Supabase
- Connection details in `DatabaseConnection.java`
- JDBC driver: `org.postgresql:postgresql`
- Database URL: `jdbc:postgresql://db.vgssxkidvqovqebkobvp.supabase.co:5432/postgres`

### Build System
- **Maven** 3.6+ for dependency management and build lifecycle
- JavaFX Maven Plugin for running the application: `mvn javafx:run`

### Core Frameworks
- **JavaFX 17+** for GUI (desktop application framework)
  - Required modules: javafx-controls, javafx-fxml
  - Cannot run in web-based environments - requires local graphical display (X11/Wayland on Linux, native on Windows/Mac)
- **Java JDK 17** or higher

### Runtime Requirements
- **Local desktop environment** - this application cannot run in Replit, GitHub Codespaces, or any cloud IDE without X11 forwarding
- JAVA_HOME environment variable must be set
- Maven must be available in PATH

### Sample Credentials
**Customer Login**:
- Customer ID: `CUST001` (or CUST002-CUST005)
- Password: `password123`
- PIN: `1234` (for transfers)

**Admin Login**:
- Username: `admin`
- Password: `admin123`