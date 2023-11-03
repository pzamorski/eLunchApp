package com.patzam.elunchapp.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import com.patzam.elunchapp.model.enums.VatTax;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@GeneratePojoBuilder
public class MenuItemDTO {
	public static class View {
		public interface Basic {}
		public interface Extended extends Basic {}
	}

	@JsonView(View.Basic.class)
	@NotNull
	private UUID uuid;

	@JsonView(View.Basic.class)
	@NotBlank
	private String name;

	@JsonView(View.Extended.class)
	@Digits(integer = 10, fraction = 2)
	@Min(0)
	@NotNull
	private BigDecimal nettoPrice;

	@JsonView(View.Extended.class)
	@NotNull
	private VatTax vatTax;

	@JsonView(View.Extended.class)
	@Digits(integer = 10, fraction = 2)
	@Min(0)
	@NotNull
	private BigDecimal bruttoPrice;

	@JsonView(View.Extended.class)
	@NotNull
	@Size(min = 1)
	private List<DishDTO> dishDTOS;

	@JsonView(View.Extended.class)
	@NotNull
	private RestaurantDTO restaurantDTO;


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

	public BigDecimal getNettoPrice() {
		return nettoPrice;
	}

	public void setNettoPrice(BigDecimal nettoPrice) {
		this.nettoPrice = nettoPrice;
	}

	public VatTax getVatTax() {
		return vatTax;
	}

	public void setVatTax(VatTax vatTax) {
		this.vatTax = vatTax;
	}

	public BigDecimal getBruttoPrice() {
		return bruttoPrice;
	}

	public void setBruttoPrice(BigDecimal bruttoPrice) {
		this.bruttoPrice = bruttoPrice;
	}

	public List<DishDTO> getDishes() {
		return dishDTOS;
	}

	public void setDishes(List<DishDTO> dishDTOS) {
		this.dishDTOS = dishDTOS;
	}

	public RestaurantDTO getRestaurant() {
		return restaurantDTO;
	}

	public void setRestaurant(RestaurantDTO restaurantDTO) {
		this.restaurantDTO = restaurantDTO;
	}
}
