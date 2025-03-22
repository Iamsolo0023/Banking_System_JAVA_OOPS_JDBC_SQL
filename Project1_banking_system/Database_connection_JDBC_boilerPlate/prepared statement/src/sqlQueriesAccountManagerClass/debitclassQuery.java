package sqlQueriesAccountManagerClass;

public class debitclassQuery {
    public static final String query1 = "SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?;";
    public static final String query2 = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?;";
}
