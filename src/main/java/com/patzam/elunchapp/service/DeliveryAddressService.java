package com.patzam.elunchapp.service;

import com.patzam.elunchapp.DTO.DeliveryAddressDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryAddressService {
	List<DeliveryAddressDTO> getAll();
	void put(UUID uuid, DeliveryAddressDTO deliveryAddressDTO);
	void delete(UUID uuid);
	Optional<DeliveryAddressDTO> getByUuid(UUID uuid);
}
