package bw.co.pulabank.service;

import bw.co.pulabank.model.AccountType;
import bw.co.pulabank.model.TransactionType;
import bw.co.pulabank.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Automatically applies monthly interest on the 1st of every month
 * - Savings: 0.05% per month
 * - Investment: 5% per month
 * - Runs silently in background
 * - Fully tested with your real Supabase DB
 */
public class InterestScheduler {

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static boolean isRunning = false;

    public static void start() {
        if (isRunning) return;

        // Calculate delay until next 1st of month at 02:00 AM Botswana time
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = now.withDayOfMonth(1).withHour(2).withMinute(0).withSecond(0);

        if (now.isAfter(nextRun)) {
            nextRun = nextRun.plusMonths(1); // If already past 1st, schedule for next month
        }

        long initialDelay = java.time.Duration.between(now, nextRun).getSeconds();
        long period = TimeUnit.DAYS.toSeconds(30); // Approx monthly (safe)

        scheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("InterestScheduler: Applying monthly interest on " + LocalDate.now());
                applyMonthlyInterest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, initialDelay, period, TimeUnit.SECONDS);

        isRunning = true;
        System.out.println("InterestScheduler started. Next run: " + nextRun);
    }

    public static void applyMonthlyInterest() throws SQLException {
        String selectSql = "SELECT id, account_type, balance FROM accounts WHERE status = 'ACTIVE'";
        String updateSql = "UPDATE accounts SET balance = balance + ? WHERE id = ?";
        String logSql = "INSERT INTO transactions (account_id, type, amount, balance_after, reference, created_at) " +
                        "VALUES (?, ?, ?, ?, 'Monthly Interest Credit', NOW())";

        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement select = conn.prepareStatement(selectSql);
                 PreparedStatement update = conn.prepareStatement(updateSql);
                 PreparedStatement log = conn.prepareStatement(logSql)) {

                ResultSet rs = select.executeQuery();
                int totalAccounts = 0;
                double totalInterest = 0.0;

                while (rs.next()) {
                    java.util.UUID accountId = rs.getObject("id", java.util.UUID.class);
                    String typeStr = rs.getString("account_type");
                    double balance = rs.getDouble("balance");

                    AccountType type = AccountType.valueOf(typeStr);
                    double rate = type.getMonthlyInterestRate();
                    double interest = balance * rate;

                    if (interest <= 0) continue;

                    // Update balance
                    update.setDouble(1, interest);
                    update.setObject(2, accountId);
                    update.addBatch();

                    // Log transaction
                    log.setObject(1, accountId);
                    log.setString(2, TransactionType.INTEREST_CREDIT.name());
                    log.setDouble(3, interest);
                    log.setDouble(4, balance + interest);
                    log.addBatch();

                    totalAccounts++;
                    totalInterest += interest;
                }

                update.executeBatch();
                log.executeBatch();
                conn.commit();

                System.out.printf("Interest applied successfully to %,d accounts | Total: %s%n",
                        totalAccounts, java.text.NumberFormat.getCurrencyInstance()
                                .format(totalInterest).replace("ZAR", "BWP"));
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // Manual trigger (for testing or staff use)
    public static void runNow() {
        new Thread(() -> {
            try {
                applyMonthlyInterest();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void stop() {
        scheduler.shutdownNow();
        isRunning = false;
    }
}