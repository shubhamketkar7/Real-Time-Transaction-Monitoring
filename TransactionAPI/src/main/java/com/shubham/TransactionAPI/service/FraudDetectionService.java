package com.shubham.TransactionAPI.service;

import com.shubham.TransactionAPI.model.FlaggedTransaction;
import com.shubham.TransactionAPI.model.Transaction;
import com.shubham.TransactionAPI.repository.FlaggedTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FraudDetectionService {
    private final Set<String> blacklistedAccounts = Set.of("1234567890", "9876543210");
    private final Map<String, List<LocalDateTime>> transactionHistory = new HashMap<>();
    private final FlaggedTransactionRepository repository;

    public FraudDetectionService(FlaggedTransactionRepository repository) {
        this.repository = repository;
    }

    public String processTransaction(Transaction transaction) {
        // Rule 1: Amount exceeds limit
        if (transaction.getAmount() > 100000) {
            String reason = "Transaction amount exceeds the limit of \u20B91,00,000.";
            repository.save(new FlaggedTransaction(transaction.getTransactionId(), reason));
            return fraudResponse(reason);
        }

        // Rule 2: More than 3 transactions in 5 minutes
        transactionHistory.putIfAbsent(transaction.getAccountNumber(), new ArrayList<>());
        List<LocalDateTime> times = transactionHistory.get(transaction.getAccountNumber());
        times.add(transaction.getTransactionTime());

        times.removeIf(time -> Duration.between(time, transaction.getTransactionTime()).toMinutes() > 5);
        if (times.size() > 3) {
            String reason = "More than 3 transactions within 5 minutes.";
            repository.save(new FlaggedTransaction(transaction.getTransactionId(), reason));
            return fraudResponse(reason);
        }

        // Rule 3: Blacklisted accounts
        if (blacklistedAccounts.contains(transaction.getAccountNumber())) {
            String reason = "Transaction from a blacklisted account.";
            repository.save(new FlaggedTransaction(transaction.getTransactionId(), reason));
            return fraudResponse(reason);
        }

        // Rule 4: IP address outside India
        if (!"India".equalsIgnoreCase(transaction.getCountry())) {
            String reason = "Transaction initiated from an IP address outside India.";
            repository.save(new FlaggedTransaction(transaction.getTransactionId(), reason));
            return fraudResponse(reason);
        }

        return successResponse();
    }

    public List<FlaggedTransaction> getFlaggedTransactions() {
        return repository.findAll();
    }

    private String fraudResponse(String reason) {
        return String.format("{\"status\": \"Fraud\", \"reason\": \"%s\"}", reason);
    }

    private String successResponse() {
        return "{\"status\": \"Success\", \"message\": \"Transaction is valid.\"}";
    }
}