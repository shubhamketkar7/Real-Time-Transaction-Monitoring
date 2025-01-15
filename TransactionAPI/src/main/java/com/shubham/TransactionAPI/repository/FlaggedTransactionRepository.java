package com.shubham.TransactionAPI.repository;

import com.shubham.TransactionAPI.model.FlaggedTransaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FlaggedTransactionRepository {
    private final List<FlaggedTransaction> flaggedTransactions = new ArrayList<>();

    public void save(FlaggedTransaction flaggedTransaction) {
        flaggedTransactions.add(flaggedTransaction);
    }

    public List<FlaggedTransaction> findAll() {
        return flaggedTransactions;
    }
}