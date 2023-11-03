package com.patzam.elunchapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.patzam.elunchapp.model.DeliveryAddress;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryAddressRepo extends JpaRepository<DeliveryAddress, Long> {
	Optional<DeliveryAddress> findByUuid(UUID uuid);
}
