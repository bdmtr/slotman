package com.bdmtr.slotman.model.mapper;

import com.bdmtr.slotman.model.response.TransactionRequest;
import com.bdmtr.slotman.model.request.TransactionResponse;
import com.bdmtr.slotman.model.entity.Transaction;
import com.bdmtr.slotman.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
/**
 * The `TransactionMapper` class is responsible for mapping between `Transaction` entities and `TransactionRequest` and `TransactionResponse` DTOs.
 */
@Component
public class TransactionMapper {

    /**
     * Maps a `TransactionRequest` DTO and a `User` entity to a `Transaction` entity.
     *
     * @param request The `TransactionRequest` containing transaction details.
     * @param user    The associated `User` entity.
     * @return A `Transaction` entity representing the transaction.
     */
    public Transaction mapToEntity(TransactionRequest request, User user) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setTimestamp(request.getTimestamp());
        return transaction;
    }

    /**
     * Maps a `Transaction` entity to a `TransactionResponse` DTO.
     *
     * @param transaction The `Transaction` entity to be mapped.
     * @return A `TransactionResponse` DTO representing the transaction details.
     */
    public TransactionResponse mapToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setTimestamp(transaction.getTimestamp());
        response.setUserId(transaction.getUser().getId());
        return response;
    }

    /**
     * Maps a list of `Transaction` entities to a list of `TransactionResponse` DTOs.
     *
     * @param transactions The list of `Transaction` entities to be mapped.
     * @return A list of `TransactionResponse` DTOs representing the transaction details.
     */
    public List<TransactionResponse> mapToResponseList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
