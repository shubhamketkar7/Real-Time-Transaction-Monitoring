package com.shubham.TransactionAPI.model;

public class FlaggedTransaction {
    private String transactionId;
    private String reason;

    public FlaggedTransaction(String transactionId, String reason) {
        this.transactionId = transactionId;
        this.reason = reason;
    }

    // Getters and setters

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}