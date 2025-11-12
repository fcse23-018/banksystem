package controller;

import dao.AccountDAO;
import model.Account;
import view.DashboardView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.util.List;

public class DashboardController {

    private final DashboardView view;
    private final Stage stage;
    private final AccountDAO accountDAO;

    public DashboardController(DashboardView view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.accountDAO = new AccountDAO(); // Instantiate AccountDAO
    }

    /**
     * Loads the customer's accounts into the dashboard view.
     */
    public void loadAccounts() {
        // For now, we'll use dummy data
        // In a real application, you would fetch the accounts from the database
        // List<Account> accounts = accountDAO.getAccountsByCustomerId(customerId); // Uncomment when DAO is implemented

        // Dummy data for demonstration
        ObservableList<Account> accounts = FXCollections.observableArrayList();

        view.getAccountTable().setItems(accounts);
    }
}
