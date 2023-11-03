package com.patzam.elunchapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.patzam.elunchapp.model.Dish;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DishRepo extends JpaRepository<Dish, Long> {
	Optional<Dish> findByUuid(UUID uuid);
}
