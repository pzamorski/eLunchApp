package com.patzam.elunchapp.service;

import com.google.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.patzam.elunchapp.DTO.RestaurantDTO;
import com.patzam.elunchapp.model.Restaurant;
import com.patzam.elunchapp.model.RestaurantBuilder;
import com.patzam.elunchapp.repo.RestaurantRepo;
import com.patzam.elunchapp.utils.ConverterUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.patzam.elunchapp.utils.ConverterUtils.convert;
import static com.patzam.elunchapp.utils.ConverterUtils.convertDiscountCodeDTOS;
import static com.patzam.elunchapp.utils.ConverterUtils.convertMenuItemDTOS;
import static com.patzam.elunchapp.utils.ConverterUtils.convertOpenTimeDTOS;

@Service
public class RestaurantServiceImpl implements RestaurantService {
	private final RestaurantRepo restaurantRepo;

	@Autowired
	public RestaurantServiceImpl(RestaurantRepo restaurantRepo) {
		this.restaurantRepo = restaurantRepo;
	}


	@Override
	public List<RestaurantDTO> getAll() {
		return restaurantRepo.findAll().stream()
			.map(ConverterUtils::convert)
			.collect(Collectors.toList());
	}

	@Override
	public void put(UUID uuid, RestaurantDTO restaurantDTO) {

		if (!Objects.equal(restaurantDTO.getUuid(), uuid)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		Restaurant restaurant = restaurantRepo.findByUuid(restaurantDTO.getUuid())
			.orElseGet(() -> newRestaurant(uuid));

		restaurant.setName(restaurantDTO.getName());
		restaurant.setLogginData(convert(restaurantDTO.getLogginData()));
		restaurant.setCompanyData(convert(restaurantDTO.getCompanyData()));
		restaurant.setOpenTimes(convertOpenTimeDTOS(restaurantDTO.getOpenTimeDTOS()));
		restaurant.setMenu(convertMenuItemDTOS(restaurantDTO.getMenuItemDTOS()));
		restaurant.setDiscountCodes(convertDiscountCodeDTOS(restaurantDTO.getDiscountCodes()));
		restaurant.setArchive(restaurantDTO.getArchive());

		if (restaurant.getId() == null) {
			restaurantRepo.save(restaurant);
		}
	}

	@Override
	public void delete(UUID uuid) {
		Restaurant restaurant = restaurantRepo.findByUuid(uuid)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		restaurantRepo.delete(restaurant);

	}

	@Override
	public Optional<RestaurantDTO> getByUuid(UUID uuid) {
		return restaurantRepo.findByUuid(uuid).map(ConverterUtils::convert);
	}


	private Restaurant newRestaurant(UUID uuid) {
		return new RestaurantBuilder()
			.withUuid(uuid)
			.build();
	}
}
