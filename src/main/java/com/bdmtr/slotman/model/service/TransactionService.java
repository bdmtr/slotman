package com.bdmtr.slotman.model.service;

import com.bdmtr.slotman.exception.UserNotFoundException;
import com.bdmtr.slotman.model.response.TransactionRequest;
import com.bdmtr.slotman.model.request.TransactionResponse;
import com.bdmtr.slotman.model.entity.Transaction;
import com.bdmtr.slotman.model.entity.User;
import com.bdmtr.slotman.model.enums.TransactionType;
import com.bdmtr.slotman.model.mapper.TransactionMapper;
import com.bdmtr.slotman.model.repository.TransactionRepository;
import com.bdmtr.slotman.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(transactionMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public TransactionResponse getTransactionById(Long id) {
        Optional<Transaction> transactionOptional = Optional.ofNullable(transactionRepository.findById(id));
        return transactionOptional.map(transactionMapper::mapToResponse).orElse(null);
    }

    public List<Transaction> getAllByUserIdAndTypeAndTimestampBetween(Integer userId, TransactionType type, LocalDateTime start, LocalDateTime end) {
        Optional<User> existingUserOptional = Optional.ofNullable(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Cant find transactions for user: " + userId)));
        List<Transaction> transactions = transactionRepository.findByUserIdAndTypeAndTimestampBetween(userId, type, start, end);
        return transactions;
    }

    public void createTransaction(TransactionRequest transactionRequest) {
        int transactionUserId = transactionRequest.getUserId();
        Optional<User> existingUser = Optional.ofNullable(userRepository.findById(transactionUserId).orElseThrow(() -> new UserNotFoundException("Cant find user: " + transactionUserId)));
        User user = existingUser.get();

        Transaction transaction = transactionMapper.mapToEntity(transactionRequest, user);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        int balanceChanges = 0;
        if (transaction.getType().equals(TransactionType.INCOME)) {
            balanceChanges = user.getIncome() + transaction.getAmount();
            userRepository.updateIncomeById(transactionUserId, balanceChanges);
        }
        if (transaction.getType().equals(TransactionType.OUTCOME)) {
            balanceChanges = user.getOutcome() + transaction.getAmount();
            userRepository.updateOutcomeById(transactionUserId, balanceChanges);
        }
    }
}
