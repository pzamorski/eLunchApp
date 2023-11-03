package com.patzam.elunchapp.service;

import com.google.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.patzam.elunchapp.DTO.IngredientDTO;
import com.patzam.elunchapp.model.Ingredient;
import com.patzam.elunchapp.model.IngredientBuilder;
import com.patzam.elunchapp.repo.IngredientRepo;
import com.patzam.elunchapp.utils.ConverterUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IngredientServiceImpl implements IngredientService {
	private final IngredientRepo ingredientRepo;

	@Autowired
	public IngredientServiceImpl(IngredientRepo ingredientRepo) {
		this.ingredientRepo = ingredientRepo;
	}


	@Override
	public List<IngredientDTO> getAll() {
		return ingredientRepo.findAll().stream()
			.map(ConverterUtils::convert)
			.collect(Collectors.toList());
	}

	@Override
	public void put(UUID uuid, IngredientDTO ingredientDTO) {
		if (!Objects.equal(ingredientDTO.getUuid(), uuid)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		Ingredient ingredient = ingredientRepo.findByUuid(ingredientDTO.getUuid())
			.orElseGet(() -> newIngredient(uuid));

		ingredient.setName(ingredientDTO.getName());
		ingredient.setAllergen(ingredientDTO.getIsAllergen());

		if (ingredient.getId() == null) {
			ingredientRepo.save(ingredient);
		}
	}

	@Override
	public void delete(UUID uuid) {
		Ingredient ingredient = ingredientRepo.findByUuid(uuid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		ingredientRepo.delete(ingredient);

	}

	@Override
	public Optional<IngredientDTO> getByUuid(UUID uuid) {
		return ingredientRepo.findByUuid(uuid).map(ConverterUtils::convert);
	}


	private Ingredient newIngredient(UUID uuid) {
		return new IngredientBuilder()
			.withUuid(uuid)
			.build();
	}
}
