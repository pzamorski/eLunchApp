package com.patzam.elunchapp.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import com.patzam.elunchapp.model.enums.DiscountUnit;

import javax.annotation.Nullable;
import javax.persistence.Embedded;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@GeneratePojoBuilder
public class DiscountCodeDTO {
	public static class View {
		public interface Basic {}
		public interface Extended extends Basic {}
	}

	@JsonView(View.Basic.class)
	@NotNull
	private UUID uuid;

	@JsonView(View.Basic.class)
	@NotBlank
	private String code;

	@JsonView(View.Extended.class)
	@Digits(integer = 10, fraction = 2)
	@Min(0)
	@NotNull
	private BigDecimal discount;

	@JsonView(View.Extended.class)
	@NotNull
	private DiscountUnit discountUnit;

	@JsonView(View.Basic.class)
	@NotNull
	@Embedded
	private PeriodDTO period;

	@JsonView(View.Extended.class)
	@Nullable
	private List<UserDTO> users;

	@JsonView(View.Extended.class)
	@Nullable
	private List<RestaurantDTO> restaurantDTOS;


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

	public DiscountUnit getDiscountUnit() {
		return discountUnit;
	}

	public void setDiscountUnit(DiscountUnit discountUnit) {
		this.discountUnit = discountUnit;
	}

	public PeriodDTO getPeriod() {
		return period;
	}

	public void setPeriod(PeriodDTO period) {
		this.period = period;
	}

	@Nullable
	public List<RestaurantDTO> getRestaurantDTOS() {
		return restaurantDTOS;
	}

	public void setRestaurantDTOS(@Nullable List<RestaurantDTO> restaurantDTOS) {
		this.restaurantDTOS = restaurantDTOS;
	}

	@Nullable
	public List<UserDTO> getUsers() {
		return users;
	}

	public void setUsers(@Nullable List<UserDTO> users) {
		this.users = users;
	}

	@Nullable
	public List<RestaurantDTO> getRestaurants() {
		return restaurantDTOS;
	}

	public void setRestaurants(@Nullable List<RestaurantDTO> restaurantDTOS) {
		this.restaurantDTOS = restaurantDTOS;
	}
}
