package com.patzam.elunchapp.utils;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Assertions;
import com.patzam.elunchapp.DTO.DelivererDTO;
import com.patzam.elunchapp.DTO.DeliveryAddressDTO;
import com.patzam.elunchapp.DTO.DiscountCodeDTO;
import com.patzam.elunchapp.DTO.LogginDataDTO;
import com.patzam.elunchapp.DTO.OperationEvidenceDTO;
import com.patzam.elunchapp.DTO.OrderDTO;
import com.patzam.elunchapp.DTO.OrderStatusDTO;
import com.patzam.elunchapp.DTO.PeriodDTO;
import com.patzam.elunchapp.DTO.PersonalDataDTO;
import com.patzam.elunchapp.DTO.RestaurantDTO;
import com.patzam.elunchapp.DTO.UserDTO;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class AssertionUtils {

	public static void assertEquals(DelivererDTO expected, DelivererDTO actual) {
		Assertions.assertEquals(expected.getUuid(), actual.getUuid());
		assertEquals(expected.getPersonalData(), actual.getPersonalData());
		assertEquals(expected.getLogginData(), actual.getLogginData());
		Assertions.assertEquals(expected.getArchive(), actual.getArchive());

		Map<UUID, OrderDTO> expectedOrders = Maps.uniqueIndex(expected.getOrders() != null ? expected.getOrders() : new ArrayList<>(), OrderDTO::getUuid);
		Map<UUID, OrderDTO> actualOrders = Maps.uniqueIndex(actual.getOrders() != null ? actual.getOrders() : new ArrayList<>(), OrderDTO::getUuid);
		Assertions.assertEquals(expectedOrders.keySet(), actualOrders.keySet());
	}

	public static void assertEquals(PersonalDataDTO expected, PersonalDataDTO actual) {
		Assertions.assertEquals(expected.getName(), actual.getName());
		Assertions.assertEquals(expected.getSurname(), actual.getSurname());
		Assertions.assertEquals(expected.getSex(), actual.getSex());
		Assertions.assertEquals(expected.getPhone(), actual.getPhone());
		Assertions.assertEquals(expected.getEmail(), actual.getEmail());
	}

	public static void assertEquals(LogginDataDTO expected, LogginDataDTO actual) {
		Assertions.assertEquals(expected.getLogin(), actual.getLogin());
		Assertions.assertEquals(expected.getPassword(), actual.getPassword());
	}

	public static void assertEquals(DeliveryAddressDTO expected, DeliveryAddressDTO actual) {
		Assertions.assertEquals(expected.getUuid(), actual.getUuid());
		Assertions.assertEquals(expected.getDescription(), actual.getDescription());
		Assertions.assertEquals(expected.getStreet(), actual.getStreet());
		Assertions.assertEquals(expected.getStreetNumber(), actual.getStreetNumber());
		Assertions.assertEquals(expected.getLocalNumber(), actual.getLocalNumber());
		Assertions.assertEquals(expected.getPostcode(), actual.getPostcode());
		Assertions.assertEquals(expected.getCity(), actual.getCity());
		Assertions.assertEquals(expected.getBorough(), actual.getBorough());
		Assertions.assertEquals(expected.getCounty(), actual.getCounty());
		Assertions.assertEquals(expected.getState(), actual.getState());
		assertEqualsId(expected.getUser(), actual.getUser());
	}

	public static void assertEquals(DiscountCodeDTO expected, DiscountCodeDTO actual) {
		Assertions.assertEquals(expected.getUuid(), actual.getUuid());
		Assertions.assertEquals(expected.getCode(), actual.getCode());
		Assertions.assertEquals(expected.getDiscount(), actual.getDiscount());
		Assertions.assertEquals(expected.getDiscount(), actual.getDiscount());
		assertEquals(expected.getPeriod(), actual.getPeriod());
	}

	public static void assertEquals(PeriodDTO expected, PeriodDTO actual) {
		Assertions.assertEquals(expected.getBegin(), actual.getBegin());
		Assertions.assertEquals(expected.getEnd(), actual.getEnd());
	}

	public static void assertEquals(OperationEvidenceDTO expected, OperationEvidenceDTO actual) {
		Assertions.assertEquals(expected.getDate(), actual.getDate());
		Assertions.assertEquals(expected.getType(), actual.getType());
		Assertions.assertEquals(expected.getAmount(), actual.getAmount());
		assertEqualsId(expected.getUser(), actual.getUser());
	}

	public static void assertEquals(OrderDTO expected, OrderDTO actual, OrderStatusType orderStatusType) {
		Assertions.assertEquals(expected.getUuid(), actual.getUuid());
		Assertions.assertEquals(expected.getNettoPrice(), actual.getNettoPrice());
		Assertions.assertEquals(expected.getBruttoPrice(), actual.getBruttoPrice());
		assertEqualsId(expected.getDiscountCode(), actual.getDiscountCode());
		Assertions.assertEquals(expected.getAmountToPayBrutto(), actual.getAmountToPayBrutto());
		Assertions.assertEquals(expected.getNote(), actual.getNote());
		assertEquals(expected.getOrderStatusDTO(), actual.getOrderStatusDTO(), orderStatusType);
		assertEqualsId(expected.getUser(), actual.getUser());
		assertEqualsId(expected.getRestaurant(), actual.getRestaurant());
	}

	public enum OrderStatusType {
		NEW, PAID, GIVED_OUT, DELIVERED;
	}

	private static void assertEquals(OrderStatusDTO expected, OrderStatusDTO actual, OrderStatusType orderStatus) {
		Assertions.assertNotNull(actual.getOrderTime());
		switch (orderStatus) {
			case NEW: break;
			case PAID: assert actual.getIsPaid(); break;
			case GIVED_OUT:
				assert expected != null;
				assert actual.getIsPaid();
				Assertions.assertEquals(actual.getGiveOutTime(), expected.getGiveOutTime());
				break;
			case DELIVERED:
				assert expected != null;
				assert actual.getIsPaid();
				Assertions.assertEquals(actual.getGiveOutTime(), expected.getGiveOutTime());
				Assertions.assertEquals(actual.getDeliveryTime(), expected.getDeliveryTime());
				break;
		}
	}

	private static void assertEqualsId(UserDTO expected, UserDTO actual) {
		Assertions.assertEquals(expected.getUuid(), actual.getUuid());
	}

	private static void assertEqualsId(@Nullable DiscountCodeDTO expected, @Nullable DiscountCodeDTO actual) {
		Assertions.assertEquals(expected != null ? expected.getUuid() : null, actual != null ? actual.getUuid() : null);
	}

	private static void assertEqualsId(RestaurantDTO expected, RestaurantDTO actual) {
		Assertions.assertEquals(expected.getUuid(), actual.getUuid());
	}
}
