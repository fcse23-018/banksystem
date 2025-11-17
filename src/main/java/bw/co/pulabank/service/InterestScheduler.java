package bw.co.pulabank.service;

import bw.co.pulabank.util.Config;
import bw.co.pulabank.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InterestScheduler {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void start() {
        Runnable task = () -> {
            if (LocalDate.now().getDayOfMonth() == 1) {
                applyMonthlyInterest();
            }
        };

        // Calculate delay to next 1st of month at 02:00 AM
        LocalDateTime nextFirst = LocalDate.now().withDayOfMonth(1).plusMonths(1).atTime(2, 0);
        long delayMinutes = Duration.between(LocalDateTime.now(), nextFirst).toMinutes();

        scheduler.scheduleAtFixedRate(task, delayMinutes, 1440, TimeUnit.MINUTES); // Daily check
        System.out.println("Interest Scheduler started – next run: " + nextFirst);
    }

    private static void applyMonthlyInterest() {
        double savingsRate = Config.getDouble("interest.savings.monthly", 0.0005);
        double investmentRate = Config.getDouble("interest.investment.monthly", 0.05);

        String sql = "UPDATE accounts SET balance = balance * (1 + ?) WHERE account_type = ? AND status = 'ACTIVE'";

        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            if (conn == null) {
                System.err.println("Failed to get database connection for interest calculation.");
                return;
            }
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDouble(1, savingsRate);
                ps.setString(2, "SAVINGS");
                ps.executeUpdate();

                ps.setDouble(1, investmentRate);
                ps.setString(2, "INVESTMENT");
                ps.executeUpdate();
            }
            conn.commit();
            System.out.println("Monthly interest applied successfully – Pula Bank Botswana");
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}