package com.patzam.elunchapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.patzam.elunchapp.model.DiscountCode;
import com.patzam.elunchapp.model.OrderItem;
import com.patzam.elunchapp.model.enums.PriceType;
import com.patzam.elunchapp.repo.OrderItemRepo;

import javax.activation.UnsupportedDataTypeException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderItemServiceImpl implements OrderItemService {
	private final OrderItemRepo orderItemRepo;

	@Autowired
	public OrderItemServiceImpl(OrderItemRepo orderItemRepo) {
		this.orderItemRepo = orderItemRepo;
	}


	@Override
	public List<OrderItem> getAll() {
		return orderItemRepo.findAll();
	}

	@Override
	public void add(OrderItem orderItem) {
		orderItemRepo.save(orderItem);
	}

	@Override
	public void delete(OrderItem orderItem) {
		orderItemRepo.delete(orderItem);
	}

	@Override
	public Optional<OrderItem> getByUuid(UUID uuid) {
		return orderItemRepo.findByUuid(uuid);
	}

	@Override
	public BigDecimal calculatePrice(List<OrderItem> orderItemList, BigDecimal startPrice, PriceType priceType) throws UnsupportedDataTypeException {
		BigDecimal orderPrice = startPrice;

		for (OrderItem orderItem : orderItemList) {
			switch (priceType) {
				case NETTO:
					orderPrice = orderPrice.add(
						orderItem.getMenuItem().getNettoPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
					);
					break;
				case BRUTTO:
					orderPrice = orderPrice.add(
						orderItem.getMenuItem().getBruttoPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
					);
					break;
				default:
					throw new UnsupportedDataTypeException();
			}
		}
		return orderPrice;
	}

	@Override
	public BigDecimal applyDiscount(DiscountCode discountCode, BigDecimal orderBruttoPrice) throws UnsupportedDataTypeException {
		if (discountCode == null) {
			return orderBruttoPrice;
		}

		BigDecimal amountToPayBrutto;

		switch (discountCode.getDiscountUnit()) {
			case PLN:
				amountToPayBrutto = orderBruttoPrice.subtract(discountCode.getDiscount());
				break;
			case PERCENT:
				amountToPayBrutto = orderBruttoPrice.multiply(
					BigDecimal.valueOf(100).subtract(discountCode.getDiscount())
				).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
				break;
			default:
				throw new UnsupportedDataTypeException();
		}

		return amountToPayBrutto;
	}
}
