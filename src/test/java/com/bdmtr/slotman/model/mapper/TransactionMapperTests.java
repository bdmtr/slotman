package com.bdmtr.slotman.model.mapper;

import com.bdmtr.slotman.model.entity.Transaction;
import com.bdmtr.slotman.model.entity.User;
import com.bdmtr.slotman.model.enums.TransactionType;
import com.bdmtr.slotman.model.request.TransactionResponse;
import com.bdmtr.slotman.model.response.TransactionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionMapperTests {

    @InjectMocks
    private TransactionMapper transactionMapper;

    @Mock
    private User mockUser;

    @BeforeEach
    public void setUp() {
        org.mockito.MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMapToEntity() {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(100);
        request.setType(TransactionType.valueOf("INCOME"));
        request.setTimestamp(LocalDateTime.now());

        Transaction transaction = transactionMapper.mapToEntity(request, mockUser);

        assertEquals(request.getAmount(), transaction.getAmount());
        assertEquals(request.getType(), transaction.getType());
        assertEquals(request.getTimestamp(), transaction.getTimestamp());
        assertEquals(mockUser, transaction.getUser());
    }

    @Test
    public void testMapToResponse() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(200);
        transaction.setType(TransactionType.valueOf("OUTCOME"));
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setUser(mockUser);

        TransactionResponse response = transactionMapper.mapToResponse(transaction);

        assertEquals(transaction.getId(), response.getId());
        assertEquals(transaction.getAmount(), response.getAmount());
        assertEquals(transaction.getType(), response.getType());
        assertEquals(transaction.getTimestamp(), response.getTimestamp());
        assertEquals(mockUser.getId(), response.getUserId());
    }

    @Test
    public void testMapToResponseList() {
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        transaction1.setUser(mockUser);
        transaction2.setUser(mockUser);
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        List<TransactionResponse> responseList = transactionMapper.mapToResponseList(transactions);

        assertEquals(transactions.size(), responseList.size());
    }
}