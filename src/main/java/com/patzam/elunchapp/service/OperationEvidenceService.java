package com.patzam.elunchapp.service;

import com.patzam.elunchapp.model.OperationEvidence;
import com.patzam.elunchapp.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface OperationEvidenceService {
	List<OperationEvidence> getAll();
	void add(OperationEvidence operationEvidence);
	void delete(OperationEvidence operationEvidence);
	BigDecimal getUserAccountBalance(User user);
	BigDecimal getAccountBalanceAfterOperation(OperationEvidence operationEvidence);
}
