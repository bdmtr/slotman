package com.bdmtr.slotman.model.entity;

import com.bdmtr.slotman.model.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The Transaction class represents the transactions of all users.
 * TransactionType can be "INCOME" or "OUTCOME".
 */
@Entity
@Table(name="transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "Transaction id", example = "1")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Schema(name = "User that made transaction")
    private User user;

    @Min(value = 1, message = "cant send less then 1")
    @Schema(name = "Transaction ammount" , example = "1000")
    private int amount;

    @Enumerated(EnumType.STRING)
    @Schema(name = "Type of transaction" , example = "OUTCOME")
    private TransactionType type;

    @Schema(name = "Date when transaction was made")
    private LocalDateTime timestamp;

    public Transaction() {
    }

    public Transaction(User user, int amount, TransactionType type) {
        this.user = user;
        this.amount = amount;
        this.type = type;
    }

    public Transaction(Long id, User user, int amount, TransactionType type, LocalDateTime timestamp) {
        this.id = id;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        Transaction that = (Transaction) o;
        return amount == that.amount && Objects.equals(id, that.id) && Objects.equals(user, that.user) && type == that.type && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, amount, type, timestamp);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", user=" + user +
                ", amount=" + amount +
                ", type=" + type +
                ", timestamp=" + timestamp +
                '}';
    }
}
