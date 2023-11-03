package com.patzam.elunchapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.patzam.elunchapp.model.OperationEvidence;
import com.patzam.elunchapp.model.User;
import com.patzam.elunchapp.repo.OperationEvidenceRepo;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OperationEvidenceServiceImpl implements OperationEvidenceService {
	private final OperationEvidenceRepo operationEvidenceRepo;

	@Autowired
	public OperationEvidenceServiceImpl(OperationEvidenceRepo operationEvidenceRepo) {
		this.operationEvidenceRepo = operationEvidenceRepo;
	}


	@Override
	public List<OperationEvidence> getAll() {
		return operationEvidenceRepo.findAll();
	}

	@Override
	public void add(OperationEvidence operationEvidence) {
		operationEvidenceRepo.save(operationEvidence);
	}

	@Override
	public void delete(OperationEvidence operationEvidence) {
		operationEvidenceRepo.delete(operationEvidence);
	}

	@Override
	public BigDecimal getUserAccountBalance(User user) {
		return operationEvidenceRepo.getUserAccountBalance(user);
	}

	@Override
	public BigDecimal getAccountBalanceAfterOperation(OperationEvidence operationEvidence) {
		BigDecimal balanceBefore = getUserAccountBalance(operationEvidence.getUser());
		BigDecimal blanceAfter;

		switch (operationEvidence.getType()) {
			case WITHDRAW:
			case PAYMENT:
				blanceAfter = balanceBefore.subtract(operationEvidence.getAmount());
				break;
			case DEPOSIT:
				blanceAfter = balanceBefore.add(operationEvidence.getAmount());
				break;
			default:
				throw new UnsupportedOperationException();
		}
		return blanceAfter;
	}
}
