package bw.co.pulabank.service;

import bw.co.pulabank.util.Config;
import bw.co.pulabank.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
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

        long initialDelay = LocalDate.now().withDayOfMonth(1).plusMonths(1).atStartOfDay()
                .minusHours(LocalDate.now().getHour()).minusMinutes(LocalDate.now().getMinute())
                .toEpochSecond(java.time.ZoneOffset.UTC) / 60;

        scheduler.scheduleAtFixedRate(task, initialDelay, 1440, TimeUnit.MINUTES); // Daily check
        System.out.println("Interest Scheduler started – next run: 1st of next month");
    }

    private static void applyMonthlyInterest() {
        double savingsRate = Config.getDouble("interest.savings.monthly");
        double investmentRate = Config.getDouble("interest.investment.monthly");

        String sql = "UPDATE accounts SET balance = balance * (1 + ?) WHERE account_type = ? AND status = 'ACTIVE'";

        try (Connection conn = DatabaseUtil.getConnection()) {
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
            e.printStackTrace();
        }
    }
}