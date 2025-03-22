package sqlQueriesAccountManagerClass;

public class creditclassquery {
    public static final String query1 = "SELECT * FROM accounts WHERE security_pin = ? AND account_number = ?; ";
    public static final String query2 = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?;";
}
