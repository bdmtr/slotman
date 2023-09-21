package com.bdmtr.slotman.model.request;

import com.bdmtr.slotman.model.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The `TransactionResponse` class represents the response data for a transaction in your application.
 * @see com.bdmtr.slotman.model.entity.Transaction
 */
public class TransactionResponse {
    @Schema(name = "Transaction id" , example = "1")
    private Long id;
    @Schema(name = "User id" , example = "1")
    private int userId;
    @Schema(name = "Sum of transaction" , example = "100")
    private int amount;
    @Schema(name = "Transaction type" , example = "OUTCOME")
    private TransactionType type;
    @Schema(name = "Time when transaction was done")
    private LocalDateTime timestamp;

    public TransactionResponse() {
    }

    /**
     * Constructs a `TransactionResponse` object with the specified attributes.
     *
     * @param id        The unique identifier of the transaction.
     * @param userId    The user ID associated with the transaction.
     * @param amount    The transaction amount.
     * @param type      The type of transaction (e.g., income or outcome).
     * @param timestamp The timestamp indicating when the transaction occurred.
     */
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
