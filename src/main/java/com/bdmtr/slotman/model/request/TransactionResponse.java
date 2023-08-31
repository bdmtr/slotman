package com.bdmtr.slotman.model.request;

import com.bdmtr.slotman.model.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.Objects;

public class TransactionResponse {
    private Long id;
    private int userId;
    private int amount;
    private TransactionType type;
    private LocalDateTime timestamp;

    public TransactionResponse() {
    }

    public TransactionResponse(Long id, int userId, int amount, TransactionType type, LocalDateTime timestamp) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionResponse that = (TransactionResponse) o;
        return userId == that.userId && amount == that.amount && Objects.equals(id, that.id) && type == that.type && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, amount, type, timestamp);
    }
}
