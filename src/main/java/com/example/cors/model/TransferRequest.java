package com.example.cors.model;

/**
 * Transfer Request Model
 * 
 * Data Flow:
 * ┌──────────────┐
 * │ Client JSON  │
 * │ {           │
 * │  amount: N, │
 * │  toAccount: X│
 * │ }           │
 * └──────────────┘
 *        │
 *        ▼
 * ┌──────────────┐
 * │TransferRequest│
 * └──────────────┘
 */
public class TransferRequest {
    private long amount;
    private String toAccount;

    // Getters and Setters
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }
}
