package com.bdmtr.slotman.controller;

import com.bdmtr.slotman.model.response.TransactionRequest;
import com.bdmtr.slotman.model.request.TransactionResponse;
import com.bdmtr.slotman.model.enums.TransactionType;
import com.bdmtr.slotman.model.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The `TransactionAdminController` class handles HTTP requests related to transactions.
 * This controller provides endpoints for retrieving and managing transactions, such as fetching all transactions,
 * getting a specific transaction by ID, retrieving transactions for a user within a specific date range, and
 * creating new transactions.
 * @see TransactionService
 */
@RestController
@RequestMapping("/api/v1/transactions")
@CrossOrigin("http://localhost:8081")
@Tag(name = "TransactionAdminController", description = "Controller class that handles requests related to transactions")
public class TransactionAdminController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionAdminController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Retrieve a page of transactions.
     *
     * @param page The page number (default is 0).
     * @param size The number of transactions per page (default is 10).
     * @return ResponseEntity containing a page of transaction responses and an HTTP status code.
     */
    @GetMapping("/all")
    @Operation(description = "Retrieve a page of transactions for pagination purpose")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<TransactionResponse> transactionsPage = transactionService.getAllTransactions(page, size);
        return new ResponseEntity<>(transactionsPage, HttpStatus.OK);
    }

    /**
     * Retrieve a transaction by its unique ID.
     *
     * @param id The ID of the transaction to retrieve.
     * @return ResponseEntity containing the requested transaction response and an HTTP status code.
     */
    @GetMapping("/{id}")
    @Operation(description =  "Retrieve a transaction by its id")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable("id") Long id) {
        TransactionResponse transactionResponse = transactionService.getTransactionById(id);
        return new ResponseEntity<>(transactionResponse, HttpStatus.OK);
    }

    /**
     * Retrieve transactions for a user within a specific date range and of a particular type.
     *
     * @param userId The ID of the user for whom transactions are requested.
     * @param type The type of transactions to retrieve (e.g., INCOME or OUTCOME).
     * @param start The start date of the date range.
     * @param end The end date of the date range.
     * @return ResponseEntity containing a list of transaction responses and an HTTP status code.
     */
    @GetMapping("/user")
    @Operation(description = "Retrieve a transaction for user within a specific date range and of a particular type")
    public ResponseEntity<List<TransactionResponse>> getTransactionsForUser(@RequestParam("userId") Integer userId,
                                                                            @RequestParam("type") TransactionType type,
                                                                            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                                                            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<TransactionResponse> transactions = transactionService.getAllByUserIdAndTypeAndTimestampBetween(userId, type, start, end);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    /**
     * Create a new transaction.
     *
     * @param transactionRequest The request body containing transaction details.
     * @return ResponseEntity with an HTTP status code indicating the success of the transaction creation.
     */
    @PostMapping("/create")
    @Operation(description = "Create a new transaction")
    public ResponseEntity<Void> addTransaction(@RequestBody TransactionRequest transactionRequest) {
        transactionService.createTransaction(transactionRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
