package com.bdmtr.slotman.controller;


import com.bdmtr.slotman.model.request.TransactionResponse;
import com.bdmtr.slotman.model.response.TransactionRequest;
import com.bdmtr.slotman.model.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/personal")
public class TransactionUserController {
    private final TransactionService transactionService;

    public TransactionUserController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/all")
    public ResponseEntity<Page<TransactionResponse>> getAllTransactionsForUser(
            @RequestParam("userId") Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<TransactionResponse> transactionsPage = transactionService.getAllTransactionsForUser(userId, page, size);
        return new ResponseEntity<>(transactionsPage, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionResponse> addTransaction(@RequestBody TransactionRequest transactionRequest) {
        transactionService.createTransaction(transactionRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
