package com.patzam.elunchapp.model;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import com.patzam.elunchapp.model.enums.Archive;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@GeneratePojoBuilder
@Entity
public class User {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	@NotNull
	private UUID uuid;

	@NotNull
	@Embedded
	private PersonalData personalData;

	@Nullable
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DeliveryAddress> deliveryAddress;

	@NotNull
	@Embedded
	private LogginData logginData;

	@Nullable
	@OneToMany(mappedBy = "user")
	private List<Order> orders;

	@NotNull
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OperationEvidence> operationEvidence;

	@Nullable
	@ManyToMany(mappedBy = "users")
	private List<DiscountCode> discountCodes;

	@NotNull
	@Enumerated(EnumType.STRING)
	private Archive archive;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public PersonalData getPersonalData() {
		return personalData;
	}

	public void setPersonalData(PersonalData personalData) {
		this.personalData = personalData;
	}

	@Nullable
	public List<DeliveryAddress> getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(@Nullable List<DeliveryAddress> addresses) {
		this.deliveryAddress = addresses;
	}

	public LogginData getLogginData() {
		return logginData;
	}

	public void setLogginData(LogginData logginData) {
		this.logginData = logginData;
	}

	@Nullable
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(@Nullable List<Order> orderDTOS) {
		this.orders = orderDTOS;
	}

	public List<OperationEvidence> getOperationEvidence() {
		return operationEvidence;
	}

	public void setOperationEvidence(List<OperationEvidence> operationEvidences) {
		this.operationEvidence = operationEvidences;
	}

	@Nullable
	public List<DiscountCode> getDiscountCodes() {
		return discountCodes;
	}

	public void setDiscountCodes(@Nullable List<DiscountCode> discountCodes) {
		this.discountCodes = discountCodes;
	}

	public Archive getArchive() {
		return archive;
	}

	public void setArchive(Archive archive) {
		this.archive = archive;
	}
}
