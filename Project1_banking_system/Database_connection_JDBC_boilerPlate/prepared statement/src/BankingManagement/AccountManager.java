package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import sqlQueriesAccountManagerClass.*;

public class AccountManager {
    private final Connection connection;
    private final Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void debitMoney(long accountNumber) throws SQLException {
        System.out.print("Please enter the amount you would like to debit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Please enter your security pin: ");
        String securityPin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            PreparedStatement pstmt = connection.prepareStatement(debitclassQuery.query1);
            pstmt.setLong(1, accountNumber);
            pstmt.setString(2, securityPin);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                if (amount <= currentBalance) {
                    PreparedStatement pstmt1 = connection.prepareStatement(debitclassQuery.query2);
                    pstmt1.setDouble(1, amount);
                    pstmt1.setLong(2, accountNumber);

                    if (pstmt1.executeUpdate() > 0) {
                        System.out.println("Rs. " + amount + " debited successfully!");
                        connection.commit();
                    } else {
                        System.out.println("Transaction failed");
                        connection.rollback();
                    }
                } else {
                    System.out.println("Insufficient Balance!");
                }
            } else {
                System.out.println("Invalid Pin!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void creditMoney(long accountNumber) throws SQLException {
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String securityPin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(creditclassquery.query1);
            preparedStatement.setString(1, securityPin);
            preparedStatement.setLong(2, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                PreparedStatement pstmt1 = connection.prepareStatement(creditclassquery.query2);
                pstmt1.setDouble(1, amount);
                pstmt1.setLong(2, accountNumber);

                if (pstmt1.executeUpdate() > 0) {
                    System.out.println("Rs." + amount + " credited Successfully");
                    connection.commit();
                } else {
                    System.out.println("Transaction Failed!");
                    connection.rollback();
                }
            } else {
                System.out.println("Invalid Security Pin!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public void getBalance(long accountNumber) {
        System.out.print("Enter Security Pin: ");
        String securityPin = scanner.nextLine();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getbalanceclass.query1);
            preparedStatement.setLong(1, accountNumber);
            preparedStatement.setString(2, securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Balance: " + resultSet.getDouble("balance"));
            } else {
                System.out.println("Invalid Pin!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void transferMoney(long senderAccountNumber) throws SQLException {
        System.out.print("Enter Receiver Account Number: ");
        long receiverAccountNumber = scanner.nextLong();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String securityPin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(transfermoneyquery.query3);
            preparedStatement.setLong(1, senderAccountNumber);
            preparedStatement.setString(2, securityPin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double currentBalance = resultSet.getDouble("balance");
                if (amount <= currentBalance) {
                    PreparedStatement debitStmt = connection.prepareStatement(transfermoneyquery.query1);
                    PreparedStatement creditStmt = connection.prepareStatement(transfermoneyquery.query2);

                    debitStmt.setDouble(1, amount);
                    debitStmt.setLong(2, senderAccountNumber);
                    creditStmt.setDouble(1, amount);
                    creditStmt.setLong(2, receiverAccountNumber);

                    if (debitStmt.executeUpdate() > 0 && creditStmt.executeUpdate() > 0) {
                        System.out.println("Rs." + amount + " Transferred Successfully");
                        connection.commit();
                    } else {
                        System.out.println("Transaction Failed");
                        connection.rollback();
                    }
                } else {
                    System.out.println("Insufficient Balance!");
                }
            } else {
                System.out.println("Invalid Security Pin!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            connection.setAutoCommit(true);
        }
    }
}