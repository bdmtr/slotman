package com.bdmtr.slotman.model.service;

import com.bdmtr.slotman.exception.TransactionNotFoundException;
import com.bdmtr.slotman.exception.UserNotFoundException;
import com.bdmtr.slotman.model.response.TransactionRequest;
import com.bdmtr.slotman.model.request.TransactionResponse;
import com.bdmtr.slotman.model.entity.Transaction;
import com.bdmtr.slotman.model.entity.User;
import com.bdmtr.slotman.model.enums.TransactionType;
import com.bdmtr.slotman.model.mapper.TransactionMapper;
import com.bdmtr.slotman.model.repository.TransactionRepository;
import com.bdmtr.slotman.model.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
public class TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Autowired
    public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    public Page<TransactionResponse> getAllTransactions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactionsPage = transactionRepository.findAll(pageable);

        return transactionsPage.map(transactionMapper::mapToResponse);
    }

    public Page<TransactionResponse> getAllTransactionsForUser(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactionsPage = transactionRepository.findAllByUserName(username, pageable);
        return transactionsPage.map(transactionMapper::mapToResponse);
    }


    public TransactionResponse getTransactionById(Long id) {
        Optional<Transaction> transactionOptional = Optional.ofNullable(transactionRepository.findById(id));

        log.info("Attempting to retrieve transaction with id: {}", id);

        Transaction transaction = transactionOptional.orElseThrow(() -> new TransactionNotFoundException("Cant find transactions with id: " + id));
        return transactionMapper.mapToResponse(transaction);
    }

    public List<TransactionResponse> getAllByUserIdAndTypeAndTimestampBetween(Integer userId, TransactionType type, LocalDateTime start, LocalDateTime end) {
        userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Cant find transactions for user: " + userId));

        List<Transaction> transactions = transactionRepository.findByUserIdAndTypeAndTimestampBetween(userId, type, start, end);
        return transactions.stream()
                .map(transactionMapper::mapToResponse)
                .collect(Collectors.toList());
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
