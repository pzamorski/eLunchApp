package com.patzam.elunchapp.model;

import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@GeneratePojoBuilder
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	@NotNull
	private UUID uuid;

	@Column(scale = 2, precision = 12)
	@Digits(integer = 10, fraction = 2)
	@Min(0)
	@NotNull
	private BigDecimal nettoPrice;

	@Column(scale = 2, precision = 12)
	@Digits(integer = 10, fraction = 2)
	@Min(0)
	@NotNull
	private BigDecimal bruttoPrice;

	@Nullable
	@ManyToOne
	private DiscountCode discountCode;

	@Column(scale = 2, precision = 12)
	@Digits(integer = 10, fraction = 2)
	@Min(0)
	@NotNull
	private BigDecimal amountToPayBrutto;

	@Nullable
	@Lob
	private String note;

	@NotNull
	@Embedded
	private OrderStatus status;

	@NotNull
	@OneToOne
	private DeliveryAddress deliveryAddress;

	@NotNull
	@Size(min = 1)
	@OneToMany
	private List<OrderItem> orderItems;

	@NotNull
	@ManyToOne
	private User user;

	@NotNull
	@ManyToOne
	private Deliverer deliverer;

	@NotNull
	@ManyToOne
	private Restaurant restaurant;


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

	public BigDecimal getNettoPrice() {
		return nettoPrice;
	}

	public void setNettoPrice(BigDecimal neetoPrice) {
		this.nettoPrice = neetoPrice;
	}

	public BigDecimal getBruttoPrice() {
		return bruttoPrice;
	}

	public void setBruttoPrice(BigDecimal bruttoPrice) {
		this.bruttoPrice = bruttoPrice;
	}

	@Nullable
	public DiscountCode getDiscountCode() {
		return discountCode;
	}

	public void setDiscountCode(@Nullable DiscountCode discountCode) {
		this.discountCode = discountCode;
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

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus orderStatus) {
		this.status = orderStatus;
	}

	public DeliveryAddress getDeliveryAddress() {
		return deliveryAddress;
	}

	public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Deliverer getDeliverer() {
		return deliverer;
	}

	public void setDeliverer(Deliverer deliverer) {
		this.deliverer = deliverer;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
}
