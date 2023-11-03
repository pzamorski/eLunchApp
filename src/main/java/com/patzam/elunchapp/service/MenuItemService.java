package com.patzam.elunchapp.service;

import com.patzam.elunchapp.DTO.MenuItemDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemService {
	List<MenuItemDTO> getAll();
	void put(UUID uuid, MenuItemDTO menuItemDTO);
	void delete(UUID uuid);
	Optional<MenuItemDTO> getByUuid(UUID uuid);
}
