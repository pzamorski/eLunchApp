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
import com.patzam.elunchapp.DTO.DishDTO;
import com.patzam.elunchapp.DTO.IngredientDTO;
import com.patzam.elunchapp.DTO.ProductDTO;
import com.patzam.elunchapp.service.ProductService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductController {
	interface ProductListListView extends ProductDTO.View.Basic {}
	interface ProductView extends ProductDTO.View.Extended, IngredientDTO.View.Basic, DishDTO.View.Basic {}

	private final ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@JsonView(ProductListListView.class)
	@GetMapping
	public List<ProductDTO> get() {
		return productService.getAll();
	}

	@JsonView(ProductView.class)
	@GetMapping("/{uuid}")
	public ProductDTO get(@PathVariable UUID uuid) {
		return productService.getByUuid(uuid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@Transactional
	@PutMapping("/{uuid}")
	public void put(@PathVariable UUID uuid, @RequestBody @Valid ProductDTO json) {
		productService.put(uuid, json);
	}

	@Transactional
	@DeleteMapping("/{uuid}")
	public void delete(@PathVariable UUID uuid) {
		productService.delete(uuid);
	}
}
