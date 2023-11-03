package com.patzam.elunchapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.patzam.elunchapp.model.OperationEvidence;
import com.patzam.elunchapp.model.User;

import java.math.BigDecimal;

@Repository
public interface OperationEvidenceRepo extends JpaRepository<OperationEvidence, Long> {

	@Query("SELECT COALESCE(SUM(" +
		"CASE " +
		"WHEN e.type = com.patzam.elunchapp.model.enums.EvidenceType.DEPOSIT THEN e.amount " +
		"WHEN e.type = com.patzam.elunchapp.model.enums.EvidenceType.WITHDRAW " +
		"or e.type = com.patzam.elunchapp.model.enums.EvidenceType.PAYMENT THEN -e.amount " +
		"ELSE 0 " +
		"END" +
		"), 0) from OperationEvidence e where e.user = :user")
	BigDecimal getUserAccountBalance(@Param("user") User user);
}
