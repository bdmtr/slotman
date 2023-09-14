package com.bdmtr.slotman.model.response;

import com.bdmtr.slotman.model.enums.TransactionType;

import java.time.LocalDateTime;

/**
 * The `TransactionRequest` class represents the request data for a transaction in your application.
 * @see com.bdmtr.slotman.model.entity.Transaction
 */
public class TransactionRequest {
    private int userId;
    private int amount;
    private TransactionType type;
    private LocalDateTime timestamp;

    public TransactionRequest() {
    }

    public TransactionRequest(int userId, int amount, TransactionType type) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;

    }

    public TransactionRequest(int userId, int amount, TransactionType type, LocalDateTime timestamp) {
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "TransactionRequest{" +
                "userId=" + userId +
                ", amount=" + amount +
                ", type=" + type +
                ", timestamp=" + timestamp +
                '}';
    }
}
