package com.patzam.elunchapp.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import com.patzam.elunchapp.DTO.UserDTO;
import com.patzam.elunchapp.events.OperationEvidenceCreator;
import com.patzam.elunchapp.model.OperationEvidence;
import com.patzam.elunchapp.model.User;
import com.patzam.elunchapp.repo.UserRepo;
import com.patzam.elunchapp.service.OperationEvidenceService;
import com.patzam.elunchapp.utils.ConverterUtils;

import java.math.BigDecimal;

@Component
public class OperationEvidenceListener {

	private final OperationEvidenceService operationEvidenceService;
	private final UserRepo userRepo;

	@Autowired
	public OperationEvidenceListener(OperationEvidenceService operationEvidenceService, UserRepo userRepo) {
		this.operationEvidenceService = operationEvidenceService;
		this.userRepo = userRepo;
	}

	@EventListener
	public void onAddOperation(OperationEvidenceCreator operationEvidenceCreator) {
		UserDTO userDTO = operationEvidenceCreator.getUserDTO();
		OperationEvidence operationEvidence = ConverterUtils.convert(userDTO.getOperationEvidence().stream().findFirst().orElseThrow());
		User user = userRepo.findByUuid(userDTO.getUuid()).orElseThrow();
		operationEvidence.setUser(user);

		validateAccountBalanceAfterOperation(operationEvidence);
		operationEvidenceService.add(operationEvidence);
	}

	private void validateAccountBalanceAfterOperation(OperationEvidence operationEvidence) {
		BigDecimal acconutBalanceAfterOperation = operationEvidenceService.getAccountBalanceAfterOperation(operationEvidence);
		if (acconutBalanceAfterOperation.compareTo(BigDecimal.ZERO) <= 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
}
