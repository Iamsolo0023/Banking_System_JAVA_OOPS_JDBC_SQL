package BankingManagement;

import java.sql.*;
import java.util.Scanner;
import sqlQueriesAccountClass.*;

public class Accounts {
    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public long openAccount(String email) {
        if (accountExist(email)) {
            throw new RuntimeException("Account already exists!");
        }

        System.out.print("Please enter your full name: ");
        String newFullName = scanner.nextLine();

        System.out.print("Please enter your initial amount: ");
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. Enter a valid initial amount: ");
            scanner.next();
        }
        double newBalance = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Please create your new password: ");
        String newPassword = scanner.nextLine();

        try {
            long accountNumber = generateNewAccountNumber();
            PreparedStatement pstmt = connection.prepareStatement(openAccount.query1);
            pstmt.setLong(1, accountNumber);
            pstmt.setString(2, newFullName);
            pstmt.setString(3, email);
            pstmt.setDouble(4, newBalance);
            pstmt.setString(5, newPassword);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account created successfully! Your account number is: " + accountNumber);
                return accountNumber;
            } else {
                throw new RuntimeException("Account creation failed!");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }

    public long getAccountNumber(String email) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(getAccountNumber.query1);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("account_number");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
        throw new RuntimeException("Account number doesn't exist!");
    }

    public long generateNewAccountNumber() {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(generateAccountNumber.query1);
            if (rs.next()) {
                return rs.getLong("account_number") + 1;
            }
        } catch (SQLException e) {
            System.out.println("Error generating account number: " + e.getMessage());
        }
        return 10000001; // Default starting account number
    }

    public boolean accountExist(String email) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(accountExistQuery.query1);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage());
        }
    }
}
