package com.patzam.elunchapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.patzam.elunchapp.model.Deliverer;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DelivererRepo extends JpaRepository<Deliverer, Long> {
	Optional<Deliverer> findByUuid(UUID uuid);
}
