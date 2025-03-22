package db_credentials;

public class credentials {
    private final String dbUrl = "jdbc:mysql://127.0.0.1:3306/bank";
    private final String dbUsername = "root";
    private final String dbPassword = "Shaswat@1234";

    public String getDbUrl() {
        return dbUrl;
    }
    public String getDbUsername() {
        return dbUsername;
    }
    public String getDbPassword() {
        return dbPassword;  // If hashed, return the hashed version here
    }
}
