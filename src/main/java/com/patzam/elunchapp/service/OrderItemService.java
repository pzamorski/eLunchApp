package com.patzam.elunchapp.service;

import com.patzam.elunchapp.model.DiscountCode;
import com.patzam.elunchapp.model.OrderItem;
import com.patzam.elunchapp.model.enums.PriceType;

import javax.activation.UnsupportedDataTypeException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderItemService {
	List<OrderItem> getAll();
	void add(OrderItem orderItem);
	void delete(OrderItem orderItem);
	Optional<OrderItem> getByUuid(UUID uuid);

	BigDecimal calculatePrice(List<OrderItem> orderItemList, BigDecimal startPrice, PriceType priceType) throws UnsupportedDataTypeException;
	BigDecimal applyDiscount(DiscountCode discountCode, BigDecimal orderBruttoPrice) throws UnsupportedDataTypeException;
}
