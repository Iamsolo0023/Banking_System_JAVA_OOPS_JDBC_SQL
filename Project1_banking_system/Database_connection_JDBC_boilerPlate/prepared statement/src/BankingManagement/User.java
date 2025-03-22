package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import sqlQueries.register_query;
import sqlQueries.selectQueryUserExistWithEmail;
import sqlQueries.loginQuery;

public class User {
    private Connection connection;
    private Scanner scanner;

    public User(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    // Register new user
    public void register() {
        System.out.print("Please enter your full name: ");
        String fullName = scanner.nextLine();

        System.out.print("Please enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Please create a strong password: ");
        String password = scanner.nextLine();

        if (userAlreadyExist(email)) {
            System.out.println("User already exists with this email address!");
            return;
        }

        String registerQuery = register_query.query1;
        try (PreparedStatement pstmt = connection.prepareStatement(registerQuery)) {
            pstmt.setString(1, fullName);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Congratulations! Registration successful.");
            } else {
                System.out.println("Registration failed.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Login Function
    public String login() {
        System.out.print("Please enter your email: ");
        String email = scanner.nextLine();

        System.out.print("Please enter the password: ");
        String password = scanner.nextLine();

        String loginQueryStr = loginQuery.query1;
        try (PreparedStatement pstmt = connection.prepareStatement(loginQueryStr)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful!");
                return email;
            } else {
                System.out.println("Invalid email or password.");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public boolean userAlreadyExist(String email) {
        String query = selectQueryUserExistWithEmail.query1;
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }
}
