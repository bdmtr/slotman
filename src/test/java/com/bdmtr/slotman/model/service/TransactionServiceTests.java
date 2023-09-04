package com.bdmtr.slotman.model.service;

import com.bdmtr.slotman.model.entity.Transaction;
import com.bdmtr.slotman.model.entity.User;
import com.bdmtr.slotman.model.enums.TransactionType;
import com.bdmtr.slotman.model.mapper.TransactionMapper;
import com.bdmtr.slotman.model.repository.TransactionRepository;
import com.bdmtr.slotman.model.repository.UserRepository;
import com.bdmtr.slotman.model.request.TransactionResponse;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
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

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

/*    @Test
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
    }*/

    @Test
    public void testGetTransactionById() {
        Transaction mockTransaction = new Transaction();
        long transactionId = 1L;
        mockTransaction.setId(transactionId);
        mockTransaction.setAmount(100);
        mockTransaction.setType(TransactionType.INCOME);
        mockTransaction.setTimestamp(LocalDateTime.now());

        User mockUser = new User();
        mockUser.setId(1);
        mockTransaction.setUser(mockUser);

        when(transactionRepository.findById(transactionId)).thenReturn(mockTransaction);
        when(transactionMapper.mapToResponse(mockTransaction)).thenReturn(transactionResponse);

        TransactionResponse result = transactionService.getTransactionById(transactionId);

        assertNotNull(result);
        assertEquals(transactionResponse.getId(), result.getId());
        assertEquals(transactionResponse.getAmount(), result.getAmount());
        assertEquals(transactionResponse.getType(), result.getType());
        assertEquals(transactionResponse.getTimestamp(), result.getTimestamp());
        assertEquals(transactionResponse.getUserId(), result.getUserId());

        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionMapper, times(1)).mapToResponse(mockTransaction);
    }

    @Test
    public void testGetAllByUserIdAndTypeAndTimestampBetween() {
        LocalDateTime dateTime = LocalDateTime.now();
        User user = new User();
        user.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        Transaction mockTransaction1 = new Transaction();
        mockTransaction1.setId(1L);
        mockTransaction1.setAmount(100);
        mockTransaction1.setType(TransactionType.INCOME);
        mockTransaction1.setTimestamp(dateTime);

        Transaction mockTransaction2 = new Transaction();
        mockTransaction2.setId(1L);
        mockTransaction2.setAmount(200);
        mockTransaction2.setType(TransactionType.INCOME);
        mockTransaction2.setTimestamp(dateTime);

        List<Transaction> transactions = Arrays.asList(mockTransaction1, mockTransaction2);
        when(transactionRepository.findByUserIdAndTypeAndTimestampBetween(eq(1), eq(TransactionType.INCOME), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(transactions);

        TransactionResponse response1 = new TransactionResponse();
        response1.setId(1L);
        response1.setUserId(1);
        response1.setAmount(100);
        response1.setTimestamp(dateTime);
        response1.setType(TransactionType.INCOME);

        TransactionResponse response2 = new TransactionResponse();
        response2.setId(1L);
        response2.setUserId(1);
        response2.setAmount(200);
        response2.setTimestamp(dateTime);
        response2.setType(TransactionType.INCOME);

        when(transactionMapper.mapToResponse(mockTransaction1)).thenReturn(response1);
        when(transactionMapper.mapToResponse(mockTransaction2)).thenReturn(response2);

        List<TransactionResponse> responseList = transactionService.getAllByUserIdAndTypeAndTimestampBetween(1, TransactionType.INCOME, LocalDateTime.now(), LocalDateTime.now());

        assertEquals(2, responseList.size());
        assertSame(response1, responseList.get(0));
        assertSame(response2, responseList.get(1));
    }
}
