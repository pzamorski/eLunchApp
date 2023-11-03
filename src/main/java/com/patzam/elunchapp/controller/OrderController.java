package com.patzam.elunchapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.patzam.elunchapp.DTO.DelivererDTO;
import com.patzam.elunchapp.DTO.OrderDTO;
import com.patzam.elunchapp.DTO.OrderStatusDTO;
import com.patzam.elunchapp.DTO.RestaurantDTO;
import com.patzam.elunchapp.DTO.UserDTO;
import com.patzam.elunchapp.events.OperationEvidenceCreator;
import com.patzam.elunchapp.service.DelivererService;
import com.patzam.elunchapp.service.OrderService;
import com.patzam.elunchapp.service.UserService;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping(path = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {
	interface OrderListListView extends OrderDTO.View.Basic, UserDTO.View.Id, DelivererDTO.View.Id, RestaurantDTO.View.Id {}
	interface OrderView extends OrderDTO.View.Extended, UserDTO.View.Id, DelivererDTO.View.Id, RestaurantDTO.View.Id {}

	interface OrderDataUpdateValidation extends Default, OrderDTO.OrderValidation {}
	interface OrderStatusValidation extends Default, OrderDTO.OrderStatusValidation {}
	interface GiveOutValidation extends Default, OrderDTO.OrderStatusValidation, OrderStatusDTO.GiveOutStatusValidation {}
	interface DeliveryValidation extends Default, OrderDTO.OrderStatusValidation, OrderStatusDTO.DeliveryValidation {}

	private final OrderService orderService;
	private final DelivererService delivererService;
	private final UserService userService;
	private final ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	public OrderController(OrderService orderService, DelivererService delivererService, UserService userService, ApplicationEventPublisher applicationEventPublisher) {
		this.orderService = orderService;
		this.delivererService = delivererService;
		this.userService = userService;
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@JsonView(OrderListListView.class)
	@GetMapping
	public List<OrderDTO> get() {
		return orderService.getAll();
	}

	@JsonView(OrderListListView.class)
	@GetMapping(params = {"user"})
	public List<OrderDTO> getByUser(@RequestParam("user") UUID userUuid) {
		UserDTO user = userService.getByUuid(userUuid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return user.getOrders();
	}

	@JsonView(OrderListListView.class)
	@GetMapping(params = {"deliverer"})
	public List<OrderDTO> getByDeliverer(@RequestParam("delivererUuid") UUID delivererUuid) {
		DelivererDTO delivererDTO = delivererService.getByUuid(delivererUuid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return delivererDTO.getOrders();
	}

	@JsonView(OrderView.class)
	@GetMapping("/{uuid}")
	public OrderDTO get(@PathVariable UUID uuid) {
		return orderService.getByUuid(uuid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Transactional
	@Validated(OrderDataUpdateValidation.class)
	@PutMapping("/{uuid}")
	public void put(@PathVariable UUID uuid, @RequestBody @Valid OrderDTO json) {
		orderService.put(uuid, json);
	}

	@Transactional
	@DeleteMapping("/{uuid}")
	public void delete(@PathVariable UUID uuid) {
		orderService.delete(uuid);
	}

	@Transactional
	@Validated(OrderStatusValidation.class)
	@PatchMapping("/{uuid}/paid")
	public void patchIsPaid(@PathVariable UUID uuid) {
		OrderDTO orderDTO = orderService.getByUuid(uuid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		orderService.setIsPaid(orderDTO);

		OperationEvidenceCreator operationEvidenceCreator = new OperationEvidenceCreator(this, orderService.newOperationForPaidOrder(orderDTO));
		applicationEventPublisher.publishEvent(operationEvidenceCreator);
	}

	@Transactional
	@Validated(GiveOutValidation.class)
	@PatchMapping("/{uuid}/gived-out")
	public void patchIsGivedOut(@PathVariable UUID uuid, @RequestBody @Valid OrderStatusDTO orderStatusJson) {
		orderService.setIsGivedOut(uuid, orderStatusJson);
	}

	@Transactional
	@Validated(DeliveryValidation.class)
	@PatchMapping("/{uuid}/delivered")
	public void patchIsDelivered(@PathVariable UUID uuid, @RequestBody @Valid OrderStatusDTO orderStatusJson) {
		orderService.setIsDelivered(uuid, orderStatusJson);
	}
}
