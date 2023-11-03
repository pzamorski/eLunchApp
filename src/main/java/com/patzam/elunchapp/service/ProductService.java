package com.patzam.elunchapp.service;

import com.patzam.elunchapp.DTO.ProductDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
	List<ProductDTO> getAll();
	void put(UUID uuid, ProductDTO productDTO);
	void delete(UUID uuid);
	Optional<ProductDTO> getByUuid(UUID uuid);
}
