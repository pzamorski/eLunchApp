package com.patzam.elunchapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.patzam.elunchapp.DTO.DeliveryAddressDTO;
import com.patzam.elunchapp.DTO.UserDTO;
import com.patzam.elunchapp.service.DeliveryAddressService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/delivery-address", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeliveryAddressController {
	interface DeliveryAddressListListView extends DeliveryAddressDTO.View.Basic, UserDTO.View.Id {}
	interface DeliveryAddressView extends DeliveryAddressDTO.View.Extended, UserDTO.View.Id {}

	private final DeliveryAddressService deliveryAddressService;

	@Autowired
	public DeliveryAddressController(DeliveryAddressService deliveryAddressService) {
		this.deliveryAddressService = deliveryAddressService;
	}

	@JsonView(DeliveryAddressListListView.class)
	@GetMapping
	public List<DeliveryAddressDTO> get() {
		return deliveryAddressService.getAll();
	}

	@JsonView(DeliveryAddressView.class)
	@GetMapping("/{uuid}")
	public DeliveryAddressDTO get(@PathVariable UUID uuid) {
		return deliveryAddressService.getByUuid(uuid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Transactional
	@PutMapping("/{uuid}")
	public void put(@PathVariable UUID uuid, @RequestBody @Valid DeliveryAddressDTO json) {
		deliveryAddressService.put(uuid, json);
	}

	@Transactional
	@DeleteMapping("/{uuid}")
	public void delete(@PathVariable UUID uuid) {
		deliveryAddressService.delete(uuid);
	}
}
