package com.patzam.elunchapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.patzam.elunchapp.model.MenuItem;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuItemRepo extends JpaRepository<MenuItem, Long> {
	Optional<MenuItem> findByUuid(UUID uuid);
}
