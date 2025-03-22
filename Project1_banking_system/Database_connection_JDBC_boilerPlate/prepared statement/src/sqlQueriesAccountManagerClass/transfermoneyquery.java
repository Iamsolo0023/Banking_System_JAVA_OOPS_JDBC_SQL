package sqlQueriesAccountManagerClass;

public class transfermoneyquery {
    public static final String query1 = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?;";
    public static final String query2 = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?;";
    public static final String query3 = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?;";
}
