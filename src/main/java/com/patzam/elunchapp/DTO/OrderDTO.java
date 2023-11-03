package com.patzam.elunchapp.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.annotation.Nullable;
import javax.persistence.Embedded;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@GeneratePojoBuilder
public class OrderDTO {
	public static class View {
		public interface Basic {}
		public interface Extended extends Basic {}
	}
	public interface OrderValidation {}
	public interface OrderStatusValidation {}

	@JsonView(View.Basic.class)
	@NotNull
	private UUID uuid;

	@JsonView(View.Extended.class)
	@Digits(integer = 10, fraction = 2)
	@Min(0)
	@Null(groups = OrderValidation.class)
	private BigDecimal nettoPrice;

	@JsonView(View.Extended.class)
	@Digits(integer = 10, fraction = 2)
	@Min(0)
	@NotNull
	private BigDecimal bruttoPrice;

	@JsonView(View.Extended.class)
	@Nullable
	private DiscountCodeDTO discountCodeDTO;

	@JsonView(View.Extended.class)
	@Digits(integer = 10, fraction = 2)
	@Min(0)
	@NotNull
	private BigDecimal amountToPayBrutto;

	@JsonView(View.Extended.class)
	@Nullable
	private String note;

	@JsonView(View.Basic.class)
	@Null(groups = OrderValidation.class)
	@NotNull(groups = OrderStatusValidation.class)
	@Embedded
	private OrderStatusDTO orderStatusDTO;

	@JsonView(View.Extended.class)
	@NotNull
	private DeliveryAddressDTO deliveryAddressDTO;

	@JsonView(View.Extended.class)
	@NotNull
	@Size(min = 1)
	private List<OrderItemDTO> orderItemDTOS;

	@JsonView(View.Basic.class)
	@NotNull
	private UserDTO user;

	@JsonView(View.Basic.class)
	@NotNull
	private DelivererDTO delivererDTO;

	@JsonView(View.Basic.class)
	@NotNull
	private RestaurantDTO restaurantDTO;


	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public BigDecimal getNettoPrice() {
		return nettoPrice;
	}

	public void setNettoPrice(BigDecimal nettoPrice) {
		this.nettoPrice = nettoPrice;
	}

	public BigDecimal getBruttoPrice() {
		return bruttoPrice;
	}

	public void setBruttoPrice(BigDecimal bruttoPrice) {
		this.bruttoPrice = bruttoPrice;
	}

	@Nullable
	public DiscountCodeDTO getDiscountCode() {
		return discountCodeDTO;
	}

	public void setDiscountCode(@Nullable DiscountCodeDTO discountCodeDTO) {
		this.discountCodeDTO = discountCodeDTO;
	}

	public BigDecimal getAmountToPayBrutto() {
		return amountToPayBrutto;
	}

	public void setAmountToPayBrutto(BigDecimal amountToPayBrutto) {
		this.amountToPayBrutto = amountToPayBrutto;
	}

	@Nullable
	public String getNote() {
		return note;
	}

	public void setNote(@Nullable String note) {
		this.note = note;
	}

	public OrderStatusDTO getOrderStatusDTO() {
		return orderStatusDTO;
	}

	public void setOrderStatus(OrderStatusDTO orderStatusDTO) {
		this.orderStatusDTO = orderStatusDTO;
	}

	public DeliveryAddressDTO getDeliveryAddressDTO() {
		return deliveryAddressDTO;
	}

	public void setDeliveryAddressDTO(DeliveryAddressDTO deliveryAddressDTO) {
		this.deliveryAddressDTO = deliveryAddressDTO;
	}

	public List<OrderItemDTO> getOrderItems() {
		return orderItemDTOS;
	}

	public void setOrderItems(List<OrderItemDTO> orderItemDTOS) {
		this.orderItemDTOS = orderItemDTOS;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public DelivererDTO getDeliverer() {
		return delivererDTO;
	}

	public void setDeliverer(DelivererDTO delivererDTO) {
		this.delivererDTO = delivererDTO;
	}

	public RestaurantDTO getRestaurant() {
		return restaurantDTO;
	}

	public void setRestaurant(RestaurantDTO restaurantDTO) {
		this.restaurantDTO = restaurantDTO;
	}
}
