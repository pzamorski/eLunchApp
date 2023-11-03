package com.patzam.elunchapp.listeners;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.patzam.elunchapp.DTO.UserDTO;
import com.patzam.elunchapp.config.JPAConfiguration;
import com.patzam.elunchapp.controller.UserController;
import com.patzam.elunchapp.model.OperationEvidence;
import com.patzam.elunchapp.model.User;
import com.patzam.elunchapp.model.enums.Archive;
import com.patzam.elunchapp.model.enums.EvidenceType;
import com.patzam.elunchapp.model.enums.Sex;
import com.patzam.elunchapp.repo.OperationEvidenceRepo;
import com.patzam.elunchapp.repo.UserRepo;
import com.patzam.elunchapp.service.OperationEvidenceService;
import com.patzam.elunchapp.service.OperationEvidenceServiceImpl;
import com.patzam.elunchapp.service.UserService;
import com.patzam.elunchapp.service.UserServiceImpl;
import com.patzam.elunchapp.utils.ConverterUtils;
import com.patzam.elunchapp.utils.TestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {
	JPAConfiguration.class,
	OperationEvidenceListenerTest.Config.class
})
class OperationEvidenceListenerTest {
	@Configuration
	public static class Config {
		@Bean
		public OperationEvidenceService operationEvidenceService(OperationEvidenceRepo operationEvidenceRepo) {
			return new OperationEvidenceServiceImpl(operationEvidenceRepo);
		}
		@Bean
		public OperationEvidenceListener operationEvidenceListener(OperationEvidenceService operationEvidenceService, UserRepo userRepo) {
			return new OperationEvidenceListener(operationEvidenceService, userRepo);
		}
		@Bean
		public UserService userService(UserRepo userRepo) {
			return new UserServiceImpl(userRepo);
		}
		@Bean
		public UserController userController(UserService userService, ApplicationEventPublisher applicationEventPublisher) {
			return new UserController(userService, applicationEventPublisher);
		}
	}

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private OperationEvidenceRepo operationEvidenceRepo;

	@Autowired
	private UserController userController;

	private static final String STR_UUID = "2774af04-bdcd-44a8-a648-5b390d818f23";

	@Test
	@Transactional
	public void deposit() {
		User user = TestUtils.user(STR_UUID, TestUtils.personalData("John", "Smith",
				Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);

		UserDTO userJson = ConverterUtils.convert(user);
		userJson.setOperationEvidence(List.of(
			TestUtils.operationEvidenceDTO("2020-01-01T12:00:00Z", EvidenceType.DEPOSIT, new BigDecimal("100.00"), userJson)
		));
		userController.postOperation(UUID.fromString(STR_UUID), userJson);

		BigDecimal userAccountBalance = operationEvidenceRepo.getUserAccountBalance(user);
		Assertions.assertEquals(new BigDecimal("100.00"), userAccountBalance);
	}

	@Test
	@Transactional
	public void withdraw() {
		User user = TestUtils.user(STR_UUID, TestUtils.personalData("John", "Smith",
				Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);
		OperationEvidence operationEvidence = TestUtils.operationEvidence("2020-01-01T12:00:00Z", EvidenceType.DEPOSIT, new BigDecimal("100.00"), user);
		operationEvidenceRepo.save(operationEvidence);

		UserDTO userJson = ConverterUtils.convert(user);
		userJson.setOperationEvidence(List.of(
			TestUtils.operationEvidenceDTO("2020-01-01T12:00:00Z", EvidenceType.WITHDRAW, new BigDecimal("25.00"), userJson)
		));
		userController.postOperation(UUID.fromString(STR_UUID), userJson);

		BigDecimal userAccountBalance = operationEvidenceRepo.getUserAccountBalance(user);
		Assertions.assertEquals(new BigDecimal("75.00"), userAccountBalance);
	}

	@Test
	@Transactional
	public void payment() {
		User user = TestUtils.user(STR_UUID, TestUtils.personalData("John", "Smith",
				Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);
		OperationEvidence operationEvidence = TestUtils.operationEvidence("2020-01-01T12:00:00Z", EvidenceType.DEPOSIT, new BigDecimal("100.00"), user);
		operationEvidenceRepo.save(operationEvidence);

		UserDTO userJson = ConverterUtils.convert(user);
		userJson.setOperationEvidence(List.of(
			TestUtils.operationEvidenceDTO("2020-01-01T12:00:00Z", EvidenceType.PAYMENT, new BigDecimal("25.00"), userJson)
		));
		userController.postOperation(UUID.fromString(STR_UUID), userJson);

		BigDecimal userAccountBalance = operationEvidenceRepo.getUserAccountBalance(user);
		Assertions.assertEquals(new BigDecimal("75.00"), userAccountBalance);
	}

	@Test
	@Transactional
	public void minusBalance() {
		User user = TestUtils.user(STR_UUID, TestUtils.personalData("John", "Smith",
				Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);

		UserDTO userJson = ConverterUtils.convert(user);
		userJson.setOperationEvidence(List.of(
			TestUtils.operationEvidenceDTO("2020-01-01T12:00:00Z", EvidenceType.WITHDRAW, new BigDecimal("100.00"), userJson)
		));
		Assertions.assertThrows(ResponseStatusException.class, () -> userController.postOperation(UUID.fromString(STR_UUID), userJson));
	}
}
