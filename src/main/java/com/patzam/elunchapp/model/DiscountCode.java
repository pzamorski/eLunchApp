package com.patzam.elunchapp.model;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import com.patzam.elunchapp.model.enums.DiscountUnit;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@GeneratePojoBuilder
@Entity
public class DiscountCode {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	@NotNull
	private UUID uuid;

	@NotBlank
	private String code;

	@Column(scale = 2, precision = 12)
	@Digits(integer = 10, fraction = 2)
	@Min(0)
	@NotNull
	private BigDecimal discount;

	@NotNull
	@Enumerated(EnumType.STRING)
	private DiscountUnit discountUnit;

	@NotNull
	@Embedded
	private Period period;

	@Nullable
	@ManyToMany
	private List<User> users;

	@Nullable
	@ManyToMany
	private List<Restaurant> restaurants;


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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public DiscountUnit getDiscountUnit() {
		return discountUnit;
	}

	public void setDiscountUnit(DiscountUnit discountUnit) {
		this.discountUnit = discountUnit;
	}

	@Nullable
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(@Nullable List<User> users) {
		this.users = users;
	}

	@Nullable
	public List<Restaurant> getRestaurants() {
		return restaurants;
	}

	public void setRestaurants(@Nullable List<Restaurant> restaurants) {
		this.restaurants = restaurants;
	}
}
