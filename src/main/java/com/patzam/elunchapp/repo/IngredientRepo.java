package com.patzam.elunchapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.patzam.elunchapp.model.Ingredient;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IngredientRepo extends JpaRepository<Ingredient, Long> {
	Optional<Ingredient> findByUuid(UUID uuid);
}
