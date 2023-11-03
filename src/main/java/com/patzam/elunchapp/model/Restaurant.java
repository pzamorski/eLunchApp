package com.patzam.elunchapp.model;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import com.patzam.elunchapp.model.enums.Archive;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@GeneratePojoBuilder
@Entity
public class Restaurant {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	@NotNull
	private UUID uuid;

	@NotBlank
	private String name;

	@NotNull
	@Embedded
	private LogginData logginData;

	@NotNull
	@Embedded
	private CompanyData companyData;

	@NotNull
	@Size(max = 7)
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OpenTime> openTimes;

	@NotNull
	@OneToMany(mappedBy = "restaurant")
	private List<Order> orderDTOS;

	@NotNull
	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MenuItem> menu;

	@NotNull
	@ManyToMany(mappedBy = "restaurants")
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LogginData getLogginData() {
		return logginData;
	}

	public void setLogginData(LogginData logginData) {
		this.logginData = logginData;
	}

	public CompanyData getCompanyData() {
		return companyData;
	}

	public void setCompanyData(CompanyData companyData) {
		this.companyData = companyData;
	}

	public List<OpenTime> getOpenTimes() {
		return openTimes;
	}

	public void setOpenTimes(List<OpenTime> openTimes) {
		this.openTimes = openTimes;
	}

	public List<Order> getOrders() {
		return orderDTOS;
	}

	public void setOrders(List<Order> orderDTOS) {
		this.orderDTOS = orderDTOS;
	}

	public List<MenuItem> getMenu() {
		return menu;
	}

	public void setMenu(List<MenuItem> menuItems) {
		this.menu = menuItems;
	}

	public List<DiscountCode> getDiscountCodes() {
		return discountCodes;
	}

	public void setDiscountCodes(List<DiscountCode> discountCodes) {
		this.discountCodes = discountCodes;
	}

	public Archive getArchive() {
		return archive;
	}

	public void setArchive(Archive archive) {
		this.archive = archive;
	}
}
