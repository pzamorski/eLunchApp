package com.patzam.elunchapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.patzam.elunchapp.model.OrderItem;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderItemRepo extends JpaRepository<OrderItem, Long> {
	Optional<OrderItem> findByUuid(UUID uuid);
}
