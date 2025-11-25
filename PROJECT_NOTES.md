# Banking System - Project Notes

## Important: Running the Application

**CRITICAL**: This is a JavaFX desktop application that **CANNOT run in Replit or GitHub Codespaces** because these cloud environments do not provide the graphical display server (X11/Wayland) required by JavaFX.

### To Run This Application:

1. **Download/Clone the project to your local computer** (Windows, macOS, or Linux with desktop environment)

2. **Install Prerequisites**:
   - Java JDK 17 or higher
   - Maven 3.6+
   - Ensure JAVA_HOME environment variable is set

3. **Compile and Run**:
   ```bash
   cd banking-system
   mvn clean compile
   mvn javafx:run
   ```

4. **Test Login**:
   - Customer ID: `CUST001`
   - Password: `password123`

---

## Database Configuration

The application connects to a PostgreSQL database hosted on Supabase with credentials configured in:
- File: `src/main/java/com/banking/util/DatabaseConnection.java`

**Credentials** (as provided):
```
URL: jdbc:postgresql://db.vgssxkidvqovqebkobvp.supabase.co:5432/postgres
Username: postgres
Password: PulaBank2025!@#Gaborone
```

The database is automatically initialized on first run with:
- 5 sample customers
- 12 accounts (mix of Savings, Investment, Cheque)
- 20+ transaction records

---

## Architecture Overview

### MVC + DAO Pattern

```
View (FXML files) 
   ↓
Controller (LoginController, DashboardController, etc.)
   ↓
Domain Model (Customer, Account subclasses, Transaction)
   ↓
DAO (CustomerDAO, AccountDAO, TransactionDAO)
   ↓
Database (PostgreSQL via JDBC)
```

### Key Classes

**Model Layer** (`com.banking.model`):
- `InterestBearing` - Interface for interest-earning accounts
- `Account` - Abstract base class for all accounts
- `SavingsAccount` - 0.05% monthly interest, no withdrawals
- `InvestmentAccount` - 5% monthly interest, minimum BWP 500
- `ChequeAccount` - Salary account, requires employer info
- `Customer` - Holds multiple accounts (composition 1:*)
- `Transaction` - Transaction records

**DAO Layer** (`com.banking.dao`):
- `CustomerDAO` - Customer CRUD and authentication
- `AccountDAO` - Account CRUD with polymorphic loading
- `TransactionDAO` - Transaction logging and history

**Controller Layer** (`com.banking.controller`):
- `LoginController` - Authentication
- `RegisterController` - Customer registration
- `DashboardController` - Account dashboard and navigation
- `AccountController` - Account operations (open, deposit, withdraw)
- `TransactionController` - Transaction history display

**Views** (`src/main/resources/fxml`):
- 7 FXML files for all user interfaces

---

## OOP Principles Demonstrated

1. **Abstraction**: `Account` is an abstract class with abstract `withdraw()` method
2. **Inheritance**: `SavingsAccount`, `InvestmentAccount`, `ChequeAccount` extend `Account`
3. **Polymorphism**: `InterestBearing` interface with different implementations
4. **Encapsulation**: All attributes private with public getters/setters
5. **Composition**: `Customer` has `List<Account>` (1 to many relationship)
6. **Method Overloading**: `Customer.openAccount(String, double)` and `Customer.openAccount(String, double, String)`

---

## Business Rules Enforced

✅ **Savings Account**:
- Deposits allowed
- Withdrawals **BLOCKED** (UnsupportedOperationException)
- 0.05% monthly interest calculated and displayed

✅ **Investment Account**:
- Minimum initial deposit BWP 500.00 enforced
- Both deposits and withdrawals allowed
- 5% monthly interest calculated and displayed

✅ **Cheque Account**:
- Requires employer company name and address
- Both deposits and withdrawals allowed
- No interest earned

✅ **General**:
- All amounts validated (positive, sufficient funds)
- Passwords hashed with BCrypt
- Transaction history logged for all operations
- Account numbers auto-generated uniquely

---

## Testing Instructions

### Manual Test Scenarios

1. **Registration Flow**:
   - Create new customer
   - Validate all fields required
   - Verify password hashing (check database)

2. **Account Creation**:
   - Open Savings account with any amount
   - Try Investment with < BWP 500 (should fail)
   - Open Investment with >= BWP 500
   - Open Cheque with employer info

3. **Transaction Testing**:
   - Deposit to any account (works)
   - Try withdraw from Savings (blocked with message)
   - Withdraw from Investment (success)
   - Withdraw from Cheque (success)
   - Try withdraw more than balance (insufficient funds error)

4. **Interest Calculation**:
   - View Savings account details → See 0.05% monthly interest
   - View Investment account details → See 5% monthly interest
   - Verify calculations are correct

5. **Transaction History**:
   - Make several transactions
   - View history for an account
   - Verify all transactions logged with correct dates and balances

---

## Known Limitations (By Design for Assignment)

1. **JavaFX Environment Requirement**: Cannot run in cloud IDEs
2. **Hardcoded Database Credentials**: For academic assignment only
3. **Static Database Connection**: Single connection shared (works for assignment demo)
4. **No Multi-User Session Management**: Single user at a time
5. **Interest Display Only**: Interest calculated and shown but not automatically credited
6. **No Account Closure UI**: Can delete via DAO but no UI button

---

## File Structure

```
banking-system/
├── pom.xml                                    # Maven configuration
├── README.md                                  # Comprehensive documentation
├── INTEGRATION_TEST_REPORT.md                 # Testing documentation
├── PROJECT_NOTES.md                           # This file
├── .gitignore                                 # Git ignore rules
└── src/
    ├── main/
    │   ├── java/com/banking/
    │   │   ├── MainApp.java                   # Application entry point
    │   │   ├── model/                         # Domain model (7 classes)
    │   │   ├── dao/                           # Data access layer (3 classes)
    │   │   ├── controller/                    # JavaFX controllers (5 classes)
    │   │   └── util/                          # Utilities (2 classes)
    │   └── resources/
    │       ├── fxml/                          # JavaFX views (7 files)
    │       └── schema.sql                     # Database schema + sample data
    └── test/                                  # (Unit tests if needed)
```

---

## Troubleshooting

### "Maven not found" error
Install Maven from https://maven.apache.org/download.cgi

### "Cannot find JavaFX" error  
Maven will automatically download JavaFX. If issues persist:
1. Ensure pom.xml is correct
2. Run `mvn clean install` first
3. Check JAVA_HOME is set to JDK 17+

### "Database connection failed" error
1. Check internet connection (connects to Supabase cloud)
2. Verify credentials in DatabaseConnection.java are correct
3. Ensure PostgreSQL JDBC driver is in classpath (Maven handles this)

### "Display not found" error in Codespaces/Replit
This is expected - JavaFX requires a desktop environment. Download the project and run locally.

---

## Credits

**Student**: Donovan Ntsima (FCSE23-018)  
**Module**: CSE202 - Object-Oriented Analysis & Design with Java  
**Institution**: Botswana Accountancy College  
**Assignment**: Part B - System Implementation  
**Year**: 2, Semester 1  
**Submission**: November 17, 2025

**Grade Target**: 58-60/60 (Part B)

---

## Next Steps After Download

1. Download/clone entire project folder
2. Open terminal in project directory
3. Run `mvn clean install`
4. Run `mvn javafx:run`
5. Login with CUST001 / password123
6. Explore all features
7. Review code for understanding OOP principles

**Good luck with your presentation!** 🎓
