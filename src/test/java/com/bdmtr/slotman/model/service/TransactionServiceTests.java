package com.bdmtr.slotman.model.service;

import com.bdmtr.slotman.model.entity.Transaction;
import com.bdmtr.slotman.model.entity.User;
import com.bdmtr.slotman.model.enums.TransactionType;
import com.bdmtr.slotman.model.mapper.TransactionMapper;
import com.bdmtr.slotman.model.repository.TransactionRepository;
import com.bdmtr.slotman.model.request.TransactionResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTests {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private Transaction transaction;

    @Mock
    private TransactionResponse transactionResponse;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void testGetAllTransactions() {
        List<Transaction> mockTransactions = new ArrayList<>();
        mockTransactions.add(transaction);
        when(transactionRepository.findAll()).thenReturn(mockTransactions);

        List<TransactionResponse> mockResponses = new ArrayList<>();
        mockResponses.add(transactionResponse);
        when(transactionMapper.mapToResponse(any())).thenReturn(mockResponses.get(0), mockResponses.subList(1, mockResponses.size()).toArray(new TransactionResponse[0]));

        List<TransactionResponse> result = transactionService.getAllTransactions();

        assertEquals(mockResponses.size(), result.size());
        verify(transactionRepository, times(1)).findAll();
        verify(transactionMapper, times(mockTransactions.size())).mapToResponse(any());
    }

    @Test
    public void testGetTransactionById() {
        long transactionId = 1L;
        Transaction mockTransaction = new Transaction();
        mockTransaction.setId(transactionId);
        mockTransaction.setAmount(100);
        mockTransaction.setType(TransactionType.INCOME);
        mockTransaction.setTimestamp(LocalDateTime.now());
        User mockUser = new User();
        mockUser.setId(1);
        mockTransaction.setUser(mockUser);

        when(transactionRepository.findById(transactionId)).thenReturn(mockTransaction);

        TransactionResponse mockResponse = new TransactionResponse();
        mockResponse.setId(transactionId);
        mockResponse.setAmount(100);
        mockResponse.setType(TransactionType.INCOME);
        mockResponse.setTimestamp(mockTransaction.getTimestamp());
        mockResponse.setUserId(mockUser.getId());
        when(transactionMapper.mapToResponse(mockTransaction)).thenReturn(mockResponse);

        TransactionResponse result = transactionService.getTransactionById(transactionId);

        assertNotNull(result);
        assertEquals(mockResponse.getId(), result.getId());
        assertEquals(mockResponse.getAmount(), result.getAmount());
        assertEquals(mockResponse.getType(), result.getType());
        assertEquals(mockResponse.getTimestamp(), result.getTimestamp());
        assertEquals(mockResponse.getUserId(), result.getUserId());

        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionMapper, times(1)).mapToResponse(mockTransaction);
    }
}
