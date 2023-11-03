package com.patzam.elunchapp.model;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import com.patzam.elunchapp.model.enums.EvidenceType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@GeneratePojoBuilder
@Entity
public class OperationEvidence {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	private Instant date;

	@NotNull
	@Enumerated(EnumType.STRING)
	private EvidenceType type;

	@NotNull
	@Column(scale = 2, precision = 12)
	@Digits(integer = 10, fraction = 2)
	@Min(0)
	private BigDecimal amount;

	@NotNull
	@ManyToOne
	private User user;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public EvidenceType getType() {
		return type;
	}

	public void setType(EvidenceType type) {
		this.type = type;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
