package com.patzam.elunchapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.patzam.elunchapp.DTO.DelivererDTO;
import com.patzam.elunchapp.DTO.LogginDataDTO;
import com.patzam.elunchapp.DTO.OrderDTO;
import com.patzam.elunchapp.DTO.PersonalDataDTO;
import com.patzam.elunchapp.service.DelivererService;

import javax.validation.Valid;
import javax.validation.groups.Default;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping(path = "/api/deliverers", produces = MediaType.APPLICATION_JSON_VALUE)
public class DelivererController {
	interface DelivererListView extends DelivererDTO.View.Basic, PersonalDataDTO.View.Basic {}
	interface DelivererView extends DelivererDTO.View.Extended, PersonalDataDTO.View.Extended, LogginDataDTO.View.Basic, OrderDTO.View.Extended {}

	interface NewDelivererValidation extends Default, DelivererDTO.NewDelivererValidation {}

	private final DelivererService delivererService;

	@Autowired
	public DelivererController(DelivererService delivererService) {
		this.delivererService = delivererService;
	}

	@JsonView(DelivererListView.class)
	@GetMapping
	public List<DelivererDTO> get() {
		return delivererService.getAll();
	}

	@JsonView(DelivererView.class)
	@GetMapping("/{uuid}")
	public DelivererDTO get(@PathVariable UUID uuid) {
		return delivererService.getByUuid(uuid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Transactional
	@Validated(NewDelivererValidation.class)
	@PutMapping("/{uuid}")
	public void put(@PathVariable UUID uuid, @RequestBody @Valid DelivererDTO json) {
		delivererService.put(uuid, json);
	}

	@Transactional
	@DeleteMapping("/{uuid}")
	public void delete(@PathVariable UUID uuid) {
		delivererService.delete(uuid);
	}
}
