package com.bdmtr.slotman.model.mapper;

import com.bdmtr.slotman.model.response.TransactionRequest;
import com.bdmtr.slotman.model.request.TransactionResponse;
import com.bdmtr.slotman.model.entity.Transaction;
import com.bdmtr.slotman.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionMapper {
    public Transaction mapToEntity(TransactionRequest request, User user) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setTimestamp(request.getTimestamp());
        return transaction;
    }

    public TransactionResponse mapToResponse(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAmount(transaction.getAmount());
        response.setType(transaction.getType());
        response.setTimestamp(transaction.getTimestamp());
        response.setUserId(transaction.getUser().getId());
        return response;
    }

    public List<TransactionResponse> mapToResponseList(List<Transaction> transactions) {
        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}
