package com.bdmtr.slotman.controller;


import com.bdmtr.slotman.model.request.TransactionResponse;
import com.bdmtr.slotman.model.response.TransactionRequest;
import com.bdmtr.slotman.model.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The `TransactionUserController` class is a controller responsible for handling transaction-related requests
 * for a specific user in the application.
 */
@RestController
@RequestMapping("/api/v1/personal")
@CrossOrigin("http://localhost:8081")
@Tag(name = "TransactionUserController", description = "Controller responsible for handling transaction-related requests for a specific user in the application without admin rights.")
public class TransactionUserController {
    private final TransactionService transactionService;

    public TransactionUserController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Handles the HTTP GET request to retrieve all transactions for a specific user.
     *
     * @param username The username of the user for whom transactions are requested.
     * @param page     The page number for paginated results (default is 0).
     * @param size     The number of transactions per page (default is 10).
     * @return A `ResponseEntity` containing a paginated list of transaction responses and an HTTP status code.
     */
    @GetMapping("/all")
    @Operation(description = "Get all transactions for user")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactionsForUser(
            @RequestParam("username") String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<TransactionResponse> transactionsPage = transactionService.getAllTransactionsForUser(username, page, size);
        return new ResponseEntity<>(transactionsPage, HttpStatus.OK);
    }

    /**
     * Handles the HTTP POST request to create a new transaction for the current user.
     *
     * @param transactionRequest The transaction request containing transaction details.
     * @return A `ResponseEntity` with an HTTP status code (CREATED) indicating a successful transaction creation.
     */
    @PostMapping("/create")
    @Operation(description = "Create a new transaction")
    public ResponseEntity<TransactionResponse> addTransaction(@RequestBody TransactionRequest transactionRequest) {
        transactionService.createTransaction(transactionRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
