import BankingManagement.AccountManager;
import BankingManagement.Accounts;
import BankingManagement.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        try (Connection conn = DatabaseHelper.getConnection()) {
            System.out.println(GREEN + "✅ Connected to the database!" + RESET);
            Scanner scanner = new Scanner(System.in);
            User user = new User(conn, scanner);
            Accounts accounts = new Accounts(conn, scanner);
            AccountManager accountManager = new AccountManager(conn, scanner);

            while (true) {
                displayBanner();
                System.out.println("\n" + BLUE + "*** MAIN MENU ***" + RESET);
                System.out.println(YELLOW + "1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit" + RESET);
                System.out.print("🔷 Enter your choice: ");

                int choice1 = getValidatedInput(scanner, 1, 3);

                switch (choice1) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        String email = user.login();
                        if (email == null) {
                            System.out.println(RED + "❌ Incorrect Email or Password!" + RESET);
                            break;
                        }

                        System.out.println(GREEN + "\n✅ User Logged In Successfully!" + RESET);

                        if (!accounts.accountExist(email)) {
                            System.out.println("\n" + YELLOW + "1. Open a new Bank Account");
                            System.out.println("2. Exit" + RESET);
                            System.out.print("🔷 Enter your choice: ");

                            int openAccChoice = getValidatedInput(scanner, 1, 2);

                            if (openAccChoice == 1) {
                                long accountNumber = accounts.openAccount(email);
                                System.out.println(GREEN + "🎉 Account Created Successfully!");
                                System.out.println("🏦 Your Account Number: " + accountNumber + RESET);
                            } else {
                                break;
                            }
                        }

                        long accountNumber = accounts.getAccountNumber(email);
                        int choice2 = 0;

                        while (choice2 != 5) {
                            System.out.println("\n" + BLUE + "*** ACCOUNT OPERATIONS ***" + RESET);
                            System.out.println(YELLOW + "1. Debit Money");
                            System.out.println("2. Credit Money");
                            System.out.println("3. Transfer Money");
                            System.out.println("4. Check Balance");
                            System.out.println("5. Log Out" + RESET);
                            System.out.print("🔷 Enter your choice: ");

                            choice2 = getValidatedInput(scanner, 1, 5);

                            switch (choice2) {
                                case 1:
                                    accountManager.debitMoney(accountNumber);
                                    break;
                                case 2:
                                    accountManager.creditMoney(accountNumber);
                                    break;
                                case 3:
                                    accountManager.transferMoney(accountNumber);
                                    break;
                                case 4:
                                    accountManager.getBalance(accountNumber);
                                    break;
                                case 5:
                                    System.out.println(GREEN + "👋 Logging Out..." + RESET);
                                    break;
                                default:
                                    System.out.println(RED + "❌ Invalid choice! Please try again." + RESET);
                                    break;
                            }
                        }
                        break;

                    case 3:
                        System.out.println(GREEN + "\n🎉 THANK YOU FOR USING BANKING SYSTEM!");
                        System.out.println("🚪 Exiting System..." + RESET);
                        return;

                    default:
                        System.out.println(RED + "❌ Invalid choice! Please enter a valid option." + RESET);
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println(RED + "❌ Database error: " + e.getMessage() + RESET);
        }
    }

    private static void displayBanner() {
        System.out.println("\n" + BLUE +
                "=======================================\n" +
                "       🏦 WELCOME TO BANKING SYSTEM     \n" +
                "=======================================" + RESET);
    }

    private static int getValidatedInput(Scanner scanner, int min, int max) {
        while (true) {
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (input >= min && input <= max) {
                    return input;
                }
            } else {
                scanner.next(); // Consume invalid input
            }
            System.out.print(RED + "❌ Invalid input. Please enter a number (" + min + "-" + max + "): " + RESET);
        }
    }
}
