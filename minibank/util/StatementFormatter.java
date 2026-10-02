package util;

import model.Account;

public class StatementFormatter {

    private StatementFormatter() {
    }

    public static String buildStatement(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append("========== ACCOUNT STATEMENT ==========\n");
        sb.append("Account Number : ").append(account.getAccountNumber()).append("\n");
        sb.append("Owner Name     : ").append(account.getOwnerName()).append("\n");
        sb.append("Balance        : ").append(account.getBalance()).append("\n");
        sb.append("Status         : ").append(account.isActive() ? "ACTIVE" : "INACTIVE").append("\n");
        sb.append("========================================");
        return sb.toString();
    }
}
