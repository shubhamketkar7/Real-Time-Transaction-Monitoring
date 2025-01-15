package com.shubham.TransactionAPI.controller;

import com.shubham.TransactionAPI.model.Transaction;
import com.shubham.TransactionAPI.service.FraudDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private FraudDetectionService fraudDetectionService;

    @PostMapping("/process")
    public ResponseEntity<String> processTransaction(@RequestBody Transaction transaction) {
        String response = fraudDetectionService.processTransaction(transaction);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/flagged-transactions")
    public ResponseEntity<Object> getFlaggedTransactions() {
        return ResponseEntity.ok(fraudDetectionService.getFlaggedTransactions());
    }
}