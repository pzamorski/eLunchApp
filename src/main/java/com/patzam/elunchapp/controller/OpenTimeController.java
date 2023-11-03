package com.patzam.elunchapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.patzam.elunchapp.DTO.OpenTimeDTO;
import com.patzam.elunchapp.DTO.RestaurantDTO;
import com.patzam.elunchapp.service.OpenTimeService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/open-times", produces = MediaType.APPLICATION_JSON_VALUE)
public class OpenTimeController {
	interface OpenTimeListListView extends OpenTimeDTO.View.Basic {}
	interface OpenTimeView extends OpenTimeDTO.View.Extended, RestaurantDTO.View.Id {}

	private final OpenTimeService openTimeService;

	@Autowired
	public OpenTimeController(OpenTimeService openTimeService) {
		this.openTimeService = openTimeService;
	}

	@JsonView(OpenTimeListListView.class)
	@GetMapping
	public List<OpenTimeDTO> get() {
		return openTimeService.getAll();
	}

	@Transactional
	@PostMapping
	public void post(@RequestBody List<@Valid OpenTimeDTO> openTimesJson) {
		for (OpenTimeDTO openTimeDTO : openTimesJson) {
			put(openTimeDTO.getUuid(), openTimeDTO);
		}
	}

	@JsonView(OpenTimeView.class)
	@GetMapping("/{uuid}")
	public OpenTimeDTO get(@PathVariable UUID uuid) {
		return openTimeService.getByUuid(uuid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Transactional
	@PutMapping("/{uuid}")
	public void put(@PathVariable UUID uuid, @RequestBody @Valid OpenTimeDTO json) {
		openTimeService.put(uuid, json);
	}

	@Transactional
	@DeleteMapping("/{uuid}")
	public void delete(@PathVariable UUID uuid) {
		openTimeService.delete(uuid);
	}
}
