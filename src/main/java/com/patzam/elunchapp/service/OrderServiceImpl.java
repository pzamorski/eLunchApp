package com.patzam.elunchapp.service;

import com.google.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.patzam.elunchapp.DTO.OperationEvidenceDTO;
import com.patzam.elunchapp.DTO.OperationEvidenceDTOBuilder;
import com.patzam.elunchapp.DTO.OrderDTO;
import com.patzam.elunchapp.DTO.OrderItemDTO;
import com.patzam.elunchapp.DTO.OrderStatusDTO;
import com.patzam.elunchapp.DTO.UserDTO;
import com.patzam.elunchapp.model.Deliverer;
import com.patzam.elunchapp.model.DeliveryAddress;
import com.patzam.elunchapp.model.DiscountCode;
import com.patzam.elunchapp.model.MenuItem;
import com.patzam.elunchapp.model.Order;
import com.patzam.elunchapp.model.OrderBuilder;
import com.patzam.elunchapp.model.OrderItem;
import com.patzam.elunchapp.model.OrderItemBuilder;
import com.patzam.elunchapp.model.OrderStatus;
import com.patzam.elunchapp.model.OrderStatusBuilder;
import com.patzam.elunchapp.model.Restaurant;
import com.patzam.elunchapp.model.User;
import com.patzam.elunchapp.model.enums.EvidenceType;
import com.patzam.elunchapp.model.enums.PriceType;
import com.patzam.elunchapp.repo.DelivererRepo;
import com.patzam.elunchapp.repo.DeliveryAddressRepo;
import com.patzam.elunchapp.repo.DiscountCodeRepo;
import com.patzam.elunchapp.repo.MenuItemRepo;
import com.patzam.elunchapp.repo.OrderItemRepo;
import com.patzam.elunchapp.repo.OrderRepo;
import com.patzam.elunchapp.repo.RestaurantRepo;
import com.patzam.elunchapp.repo.UserRepo;
import com.patzam.elunchapp.utils.ConverterUtils;

import javax.activation.UnsupportedDataTypeException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
	private final OrderRepo orderRepo;
	private final UserRepo userRepo;
	private final RestaurantRepo restaurantRepo;
	private final DelivererRepo delivererRepo;
	private final DeliveryAddressRepo deliveryAddressRepo;
	private final MenuItemRepo menuItemRepo;
	private final DiscountCodeRepo discountCodeRepo;
	private final OrderItemRepo orderItemRepo;
	private final OrderItemService orderItemService;

	@Autowired
	public OrderServiceImpl(OrderRepo orderRepo,
							UserRepo userRepo,
							RestaurantRepo restaurantRepo,
							DelivererRepo delivererRepo,
							DeliveryAddressRepo deliveryAddressRepo, MenuItemRepo menuItemRepo,
							DiscountCodeRepo discountCodeRepo,
							OrderItemRepo orderItemRepo,
							OrderItemService orderItemService) {
		this.orderRepo = orderRepo;
		this.userRepo = userRepo;
		this.restaurantRepo = restaurantRepo;
		this.delivererRepo = delivererRepo;
		this.deliveryAddressRepo = deliveryAddressRepo;
		this.menuItemRepo = menuItemRepo;
		this.discountCodeRepo = discountCodeRepo;
		this.orderItemRepo = orderItemRepo;
		this.orderItemService = orderItemService;
	}


	@Override
	public List<OrderDTO> getAll() {
		return orderRepo.findAll().stream()
			.map(ConverterUtils::convert)
			.collect(Collectors.toList());
	}

	@Override
	public void put(UUID uuid, OrderDTO orderDTO) {
		if (!Objects.equal(orderDTO.getUuid(), uuid)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		User user = userRepo.findByUuid(orderDTO.getUser().getUuid())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
		Restaurant restaurant = restaurantRepo.findByUuid(orderDTO.getRestaurant().getUuid())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
		Deliverer deliverer = delivererRepo.findByUuid(orderDTO.getDeliverer().getUuid())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
		DeliveryAddress deliveryAddress = deliveryAddressRepo.findByUuid(orderDTO.getDeliveryAddressDTO().getUuid())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
		Order order = orderRepo.findByUuid(orderDTO.getUuid())
			.orElseGet(() -> newOrder(uuid, user, restaurant));

		if (!Objects.equal(order.getUser().getUuid(), orderDTO.getUser().getUuid())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (!Objects.equal(order.getRestaurant().getUuid(), orderDTO.getRestaurant().getUuid())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		if (order.getStatus().getDeliveryTime() != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		List<OrderItem> orderItems = putOrderItems(orderDTO);
		DiscountCode discountCode = putDiscountCode(orderDTO);

		BigDecimal orderNettoPrice;
		BigDecimal orderBruttoPrice;
		BigDecimal amountToPayBrutto;
		try {
			orderNettoPrice = orderItemService.calculatePrice(orderItems, BigDecimal.ZERO, PriceType.NETTO);
			orderBruttoPrice = orderItemService.calculatePrice(orderItems, BigDecimal.ZERO, PriceType.BRUTTO);
			amountToPayBrutto = orderItemService.applyDiscount(discountCode, orderBruttoPrice);
		} catch (UnsupportedDataTypeException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		order.setNote(orderDTO.getNote());
		order.setNettoPrice(orderNettoPrice);
		order.setBruttoPrice(orderBruttoPrice);
		order.setAmountToPayBrutto(amountToPayBrutto);
		order.setDiscountCode(discountCode);
		order.setOrderItems(orderItems);
		order.setDeliverer(deliverer);
		order.setDeliveryAddress(deliveryAddress);

		if (order.getId() == null) {
			orderRepo.save(order);
		}
	}

	@Override
	public void delete(UUID uuid) {
		Order order = orderRepo.findByUuid(uuid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (order.getStatus().getIsPaid()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		orderRepo.delete(order);

	}

	@Override
	public Optional<OrderDTO> getByUuid(UUID uuid) {
		return orderRepo.findByUuid(uuid).map(ConverterUtils::convert);
	}

	@Override
	public void setIsPaid(OrderDTO orderDTO) {
		Order order = orderRepo.findByUuid(orderDTO.getUuid())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (!order.getStatus().getIsPaid()) {
			order.getStatus().setIsPaid(true);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public void setIsGivedOut(UUID uuid, OrderStatusDTO orderStatusDTO) {
		Order order = orderRepo.findByUuid(uuid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (!order.getStatus().getIsPaid() || order.getStatus().getDeliveryTime() != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		order.getStatus().setGiveOutTime(orderStatusDTO.getGiveOutTime());
	}

	@Override
	public void setIsDelivered(UUID uuid, OrderStatusDTO orderStatusDTO) {
		Order order = orderRepo.findByUuid(uuid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if (!order.getStatus().getIsPaid() || order.getStatus().getGiveOutTime() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}

		order.getStatus().setDeliveryTime(orderStatusDTO.getDeliveryTime());
	}

	@Override
	public UserDTO newOperationForPaidOrder(OrderDTO orderDTO) {
		User user = userRepo.findByUuid(orderDTO.getUser().getUuid())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		UserDTO userDTO = ConverterUtils.convert(user);
		userDTO.setOperationEvidence(List.of(newEwidenceForOrderPayment(orderDTO)));
		return userDTO;
	}

	private OperationEvidenceDTO newEwidenceForOrderPayment(OrderDTO orderDTO) {
		return new OperationEvidenceDTOBuilder()
			.withDate(Instant.now())
			.withUser(orderDTO.getUser())
			.withAmount(orderDTO.getAmountToPayBrutto())
			.withType(EvidenceType.PAYMENT)
			.build();
	}


	private DiscountCode putDiscountCode(OrderDTO orderDTO) {
		DiscountCode discountCode = null;
		if (orderDTO.getDiscountCode() != null) {
			discountCode = discountCodeRepo.findByUuid(orderDTO.getDiscountCode().getUuid())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

			if (discountCode.getRestaurants() != null) {
				discountCode.getRestaurants().stream()
					.filter(r -> r.getUuid().equals(orderDTO.getRestaurant().getUuid()))
					.findFirst()
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
			}
			if (discountCode.getUsers() != null) {
				discountCode.getUsers().stream()
					.filter(u -> u.getUuid().equals(orderDTO.getUser().getUuid()))
					.findFirst()
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
			}
		}
		return discountCode;
	}

	private List<OrderItem> putOrderItems(OrderDTO orderDTO) {
		List<OrderItem> orderItems = new ArrayList<>();
		for (OrderItemDTO orderItemDTO : orderDTO.getOrderItems()) {
			MenuItem menuItem = menuItemRepo.findByUuid(orderItemDTO.getMenuItem().getUuid())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

			OrderItem orderItem = orderItemService.getByUuid(orderItemDTO.getUuid())
				.orElseGet(() -> newOrderItem(orderItemDTO.getUuid()));
			orderItem.setQuantity(orderItemDTO.getQuantity());
			orderItem.setMenuItem(menuItem);
			if (orderItem.getId() == null) {
				orderItemRepo.save(orderItem);
			}
			orderItems.add(orderItem);
		}
		return orderItems;
	}

	private OrderItem newOrderItem(UUID uuid) {
		return new OrderItemBuilder()
			.withUuid(uuid)
			.build();
	}

	private Order newOrder(UUID uuid, User user, Restaurant restaurant) {
		return new OrderBuilder()
			.withUuid(uuid)
			.withUser(user)
			.withRestaurant(restaurant)
			.withStatus(newOrderStatus())
			.build();
	}

	private OrderStatus newOrderStatus() {
		return new OrderStatusBuilder()
			.withOrderTime(Instant.now())
			.withIsPaid(false)
			.build();
	}
}
