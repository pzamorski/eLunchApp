package com.patzam.elunchapp.utils;


import com.patzam.elunchapp.DTO.AddressDTO;
import com.patzam.elunchapp.DTO.AddressDTOBuilder;
import com.patzam.elunchapp.DTO.CompanyDataDTO;
import com.patzam.elunchapp.DTO.CompanyDataDTOBuilder;
import com.patzam.elunchapp.DTO.DelivererDTO;
import com.patzam.elunchapp.DTO.DelivererDTOBuilder;
import com.patzam.elunchapp.DTO.DeliveryAddressDTO;
import com.patzam.elunchapp.DTO.DeliveryAddressDTOBuilder;
import com.patzam.elunchapp.DTO.DiscountCodeDTO;
import com.patzam.elunchapp.DTO.DiscountCodeDTOBuilder;
import com.patzam.elunchapp.DTO.DishDTO;
import com.patzam.elunchapp.DTO.DishDTOBuilder;
import com.patzam.elunchapp.DTO.EmployeeDTO;
import com.patzam.elunchapp.DTO.EmployeeDTOBuilder;
import com.patzam.elunchapp.DTO.IngredientDTO;
import com.patzam.elunchapp.DTO.IngredientDTOBuilder;
import com.patzam.elunchapp.DTO.LogginDataDTO;
import com.patzam.elunchapp.DTO.LogginDataDTOBuilder;
import com.patzam.elunchapp.DTO.MenuItemDTO;
import com.patzam.elunchapp.DTO.MenuItemDTOBuilder;
import com.patzam.elunchapp.DTO.OpenTimeDTO;
import com.patzam.elunchapp.DTO.OpenTimeDTOBuilder;
import com.patzam.elunchapp.DTO.OperationEvidenceDTO;
import com.patzam.elunchapp.DTO.OperationEvidenceDTOBuilder;
import com.patzam.elunchapp.DTO.OrderDTO;
import com.patzam.elunchapp.DTO.OrderDTOBuilder;
import com.patzam.elunchapp.DTO.OrderItemDTO;
import com.patzam.elunchapp.DTO.OrderItemDTOBuilder;
import com.patzam.elunchapp.DTO.OrderStatusDTO;
import com.patzam.elunchapp.DTO.OrderStatusDTOBuilder;
import com.patzam.elunchapp.DTO.PeriodDTO;
import com.patzam.elunchapp.DTO.PeriodDTOBuilder;
import com.patzam.elunchapp.DTO.PeriodTimeDTO;
import com.patzam.elunchapp.DTO.PeriodTimeDTOBuilder;
import com.patzam.elunchapp.DTO.PersonalDataDTO;
import com.patzam.elunchapp.DTO.PersonalDataDTOBuilder;
import com.patzam.elunchapp.DTO.ProductDTO;
import com.patzam.elunchapp.DTO.ProductDTOBuilder;
import com.patzam.elunchapp.DTO.RestaurantDTO;
import com.patzam.elunchapp.DTO.RestaurantDTOBuilder;
import com.patzam.elunchapp.DTO.UserDTO;
import com.patzam.elunchapp.DTO.UserDTOBuilder;
import com.patzam.elunchapp.model.Address;
import com.patzam.elunchapp.model.AddressBuilder;
import com.patzam.elunchapp.model.CompanyData;
import com.patzam.elunchapp.model.CompanyDataBuilder;
import com.patzam.elunchapp.model.Deliverer;
import com.patzam.elunchapp.model.DelivererBuilder;
import com.patzam.elunchapp.model.DeliveryAddress;
import com.patzam.elunchapp.model.DeliveryAddressBuilder;
import com.patzam.elunchapp.model.DiscountCode;
import com.patzam.elunchapp.model.DiscountCodeBuilder;
import com.patzam.elunchapp.model.Dish;
import com.patzam.elunchapp.model.DishBuilder;
import com.patzam.elunchapp.model.Employee;
import com.patzam.elunchapp.model.EmployeeBuilder;
import com.patzam.elunchapp.model.Ingredient;
import com.patzam.elunchapp.model.IngredientBuilder;
import com.patzam.elunchapp.model.LogginData;
import com.patzam.elunchapp.model.LogginDataBuilder;
import com.patzam.elunchapp.model.MenuItem;
import com.patzam.elunchapp.model.MenuItemBuilder;
import com.patzam.elunchapp.model.OpenTime;
import com.patzam.elunchapp.model.OpenTimeBuilder;
import com.patzam.elunchapp.model.OperationEvidence;
import com.patzam.elunchapp.model.OperationEvidenceBuilder;
import com.patzam.elunchapp.model.Order;
import com.patzam.elunchapp.model.OrderBuilder;
import com.patzam.elunchapp.model.OrderItem;
import com.patzam.elunchapp.model.OrderItemBuilder;
import com.patzam.elunchapp.model.OrderStatus;
import com.patzam.elunchapp.model.OrderStatusBuilder;
import com.patzam.elunchapp.model.Period;
import com.patzam.elunchapp.model.PeriodBuilder;
import com.patzam.elunchapp.model.PeriodTime;
import com.patzam.elunchapp.model.PeriodTimeBuilder;
import com.patzam.elunchapp.model.PersonalData;
import com.patzam.elunchapp.model.PersonalDataBuilder;
import com.patzam.elunchapp.model.Product;
import com.patzam.elunchapp.model.ProductBuilder;
import com.patzam.elunchapp.model.Restaurant;
import com.patzam.elunchapp.model.RestaurantBuilder;
import com.patzam.elunchapp.model.User;
import com.patzam.elunchapp.model.UserBuilder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ConverterUtils {

	public static DelivererDTO convert(Deliverer deliverer) {
		return new DelivererDTOBuilder()
			.withUuid(deliverer.getUuid())
			.withPersonalData(convert(deliverer.getPersonalData()))
			.withLogginData(convert(deliverer.getLogginData()))
			.withArchive(deliverer.getArchive())
			.withOrders(convertOrders(deliverer.getOrders()))
			.build();
	}

	public static Deliverer convert(DelivererDTO delivererDTO) {
		return new DelivererBuilder()
			.withUuid(delivererDTO.getUuid())
			.withPersonalData(convert(delivererDTO.getPersonalData()))
			.withLogginData(convert(delivererDTO.getLogginData()))
			.withArchive(delivererDTO.getArchive())
			.withOrders(convertOrdersDTO(delivererDTO.getOrders()))
			.build();
	}


	public static PersonalData convert(PersonalDataDTO personalDataDTO) {
		return new PersonalDataBuilder()
			.withName(personalDataDTO.getName())
			.withSurname(personalDataDTO.getSurname())
			.withSex(personalDataDTO.getSex())
			.withPhone(personalDataDTO.getPhone())
			.withEmail(personalDataDTO.getEmail())
			.build();
	}

	public static PersonalDataDTO convert(PersonalData personalData) {
		return new PersonalDataDTOBuilder()
			.withName(personalData.getName())
			.withSurname(personalData.getSurname())
			.withSex(personalData.getSex())
			.withPhone(personalData.getPhone())
			.withEmail(personalData.getEmail())
			.build();
	}


	public static LogginData convert(LogginDataDTO logginDataDTO) {
		return new LogginDataBuilder()
			.withLogin(logginDataDTO.getLogin())
			.withPassword(logginDataDTO.getPassword())
			.build();
	}

	public static LogginDataDTO convert(LogginData logginData) {
		return new LogginDataDTOBuilder()
			.withLogin(logginData.getLogin())
			.withPassword(logginData.getPassword())
			.build();
	}


	public static List<Order> convertOrdersDTO(List<OrderDTO> orderDTOS) {
		if (orderDTOS == null) {
			return new ArrayList<>();
		}
		ArrayList<Order> orders = new ArrayList<>();
		for (OrderDTO dto : orderDTOS) {
			orders.add(convert(dto));
		}
		return orders;
	}

	public static Order convert(OrderDTO orderDTO) {
		return new OrderBuilder()
			.withUuid(orderDTO.getUuid())
			.withNettoPrice(orderDTO.getNettoPrice())
			.withBruttoPrice(orderDTO.getBruttoPrice())
			.withDiscountCode(convert(orderDTO.getDiscountCode()))
			.withAmountToPayBrutto(orderDTO.getAmountToPayBrutto())
			.withNote(orderDTO.getNote())
			.withStatus(convert(orderDTO.getOrderStatusDTO()))
			.withUser(convert(orderDTO.getUser()))
			.withDeliverer(convert(orderDTO.getDeliverer()))
			.withDeliveryAddress(convert(orderDTO.getDeliveryAddressDTO()))
			.withOrderItems(convertOrderItems(orderDTO.getOrderItems()))
			.withRestaurant(convert(orderDTO.getRestaurant()))
			.build();
	}

	public static List<OrderDTO> convertOrders(List<Order> orders) {
		if (orders == null) {
			return new ArrayList<>();
		}
		ArrayList<OrderDTO> orderDTOS = new ArrayList<>();
		for (Order dto : orders) {
			orderDTOS.add(convert(dto));
		}
		return orderDTOS;
	}

	public static OrderDTO convert(Order order) {
		return new OrderDTOBuilder()
			.withUuid(order.getUuid())
			.withNettoPrice(order.getNettoPrice())
			.withBruttoPrice(order.getBruttoPrice())
			.withDiscountCode(convert(order.getDiscountCode()))
			.withAmountToPayBrutto(order.getAmountToPayBrutto())
			.withNote(order.getNote())
			.withOrderStatus(convert(order.getStatus()))
			.withUser(convert(order.getUser()))
			.withDeliverer(convert(order.getDeliverer()))
			.withDeliveryAddressDTO(convert(order.getDeliveryAddress()))
			.withOrderItems(convertOrderItemDTOS(order.getOrderItems()))
			.withRestaurant(convert(order.getRestaurant()))
			.build();
	}


	public static List<DiscountCode> convertDiscountCodeDTOS(List<DiscountCodeDTO> discountCodeDTOS) {
		if (discountCodeDTOS == null) {
			return new ArrayList<>();
		}
		ArrayList<DiscountCode> discountCodes = new ArrayList<>();
		for (DiscountCodeDTO dto : discountCodeDTOS) {
			discountCodes.add(convert(dto));
		}
		return discountCodes;
	}

	public static DiscountCode convert(@Nullable DiscountCodeDTO discountCodeDTO) {
		if (discountCodeDTO == null) {
			return null;
		}
		return new DiscountCodeBuilder()
			.withUuid(discountCodeDTO.getUuid())
			.withCode(discountCodeDTO.getCode())
			.withDiscount(discountCodeDTO.getDiscount())
			.withDiscountUnit(discountCodeDTO.getDiscountUnit())
			.withPeriod(convert(discountCodeDTO.getPeriod()))
			.withUsers(convertUsers(discountCodeDTO.getUsers()))
			.withRestaurants(convertRestaurants(discountCodeDTO.getRestaurants()))
			.build();
	}

	public static List<DiscountCodeDTO> convertDiscountCodes(List<DiscountCode> discountCodes) {
		if (discountCodes == null) {
			return new ArrayList<>();
		}
		ArrayList<DiscountCodeDTO> discountCodeDTOS = new ArrayList<>();
		for (DiscountCode dto : discountCodes) {
			discountCodeDTOS.add(convert(dto));
		}
		return discountCodeDTOS;
	}

	public static DiscountCodeDTO convert(@Nullable DiscountCode discountCode) {
		if (discountCode == null) {
			return null;
		}
		return new DiscountCodeDTOBuilder()
			.withUuid(discountCode.getUuid())
			.withCode(discountCode.getCode())
			.withDiscount(discountCode.getDiscount())
			.withDiscountUnit(discountCode.getDiscountUnit())
			.withPeriod(convert(discountCode.getPeriod()))
			.withUsers(convertUserDTOS(discountCode.getUsers()))
			.withRestaurants(convertRestaurantDTOS(discountCode.getRestaurants()))
			.build();
	}


	public static Period convert(PeriodDTO periodDTO) {
		return new PeriodBuilder()
			.withBegin(periodDTO.getBegin())
			.withEnd(periodDTO.getEnd())
			.build();
	}

	public static PeriodDTO convert(Period period) {
		return new PeriodDTOBuilder()
			.withBegin(period.getBegin())
			.withEnd(period.getEnd())
			.build();
	}


	public static PeriodTime convert(PeriodTimeDTO periodTimeDTO) {
		return new PeriodTimeBuilder()
			.withBegin(periodTimeDTO.getBegin())
			.withEnd(periodTimeDTO.getEnd())
			.build();
	}

	public static PeriodTimeDTO convert(PeriodTime periodTime) {
		return new PeriodTimeDTOBuilder()
			.withBegin(periodTime.getBegin())
			.withEnd(periodTime.getEnd())
			.build();
	}


	public static OrderStatus convert(OrderStatusDTO orderStatusDTO) {
		return new OrderStatusBuilder()
			.withIsPaid(orderStatusDTO.getIsPaid())
			.withOrderTime(orderStatusDTO.getOrderTime())
			.withGiveOutTime(orderStatusDTO.getGiveOutTime())
			.withDeliveryTime(orderStatusDTO.getDeliveryTime())
			.build();
	}

	public static OrderStatusDTO convert(OrderStatus orderStatus) {
		return new OrderStatusDTOBuilder()
			.withPaid(orderStatus.getIsPaid())
			.withOrderTime(orderStatus.getOrderTime())
			.withGiveOutTime(orderStatus.getGiveOutTime())
			.withDeliveryTime(orderStatus.getDeliveryTime())
			.build();
	}


	public static List<User> convertUsers(List<UserDTO> userDTOS) {
		if (userDTOS == null) {
			return new ArrayList<>();
		}
		ArrayList<User> users = new ArrayList<>();
		for (UserDTO dto : userDTOS) {
			users.add(convert(dto));
		}
		return users;
	}

	public static User convert(UserDTO userDTO) {
		return new UserBuilder()
			.withUuid(userDTO.getUuid())
			.withPersonalData(convert(userDTO.getPersonalData()))
			.withDeliveryAddress(convertAddresses(userDTO.getDeliveryAddress()))
			.withLogginData(convert(userDTO.getLogginData()))
			.withOrders(convertOrdersDTO(userDTO.getOrders()))
			.withOperationEvidence(convertOperationEvidenceDTOS(userDTO.getOperationEvidence()))
			.withDiscountCodes(convertDiscountCodeDTOS(userDTO.getDiscountCodes()))
			.withArchive(userDTO.getArchive())
			.build();
	}

	public static User convertNoEvidence(UserDTO userDTO) {
		return new UserBuilder()
			.withUuid(userDTO.getUuid())
			.build();
	}

	public static List<UserDTO> convertUserDTOS(List<User> users) {
		if (users == null) {
			return new ArrayList<>();
		}
		ArrayList<UserDTO> userDTOS = new ArrayList<>();
		for (User dto : users) {
			userDTOS.add(convert(dto));
		}
		return userDTOS;
	}

	public static UserDTO convert(User user) {
		return new UserDTOBuilder()
			.withUuid(user.getUuid())
			.withPersonalData(convert(user.getPersonalData()))
			.withDeliveryAddress(convertAddressDTOS(user.getDeliveryAddress()))
			.withLogginData(convert(user.getLogginData()))
			.withOrders(convertOrders(user.getOrders()))
			.withOperationEvidence(convertOperationEvidences(user.getOperationEvidence()))
			.withDiscountCodes(convertDiscountCodes(user.getDiscountCodes()))
			.withArchive(user.getArchive())
			.build();
	}


	public static Address convert(AddressDTO DeliveryAddresDTO) {
		return new AddressBuilder()
			.withStreet(DeliveryAddresDTO.getStreet())
			.withStreetNumber(DeliveryAddresDTO.getStreetNumber())
			.withLocalNumber(DeliveryAddresDTO.getLocalNumber())
			.withPostcode(DeliveryAddresDTO.getPostcode())
			.withCity(DeliveryAddresDTO.getCity())
			.withBorough(DeliveryAddresDTO.getBorough())
			.withState(DeliveryAddresDTO.getState())
			.build();
	}

	public static AddressDTO convert(Address address) {
		return new AddressDTOBuilder()
			.withStreet(address.getStreet())
			.withStreetNumber(address.getStreetNumber())
			.withLocalNumber(address.getLocalNumber())
			.withPostcode(address.getPostcode())
			.withCity(address.getCity())
			.withBorough(address.getBorough())
			.withState(address.getState())
			.build();
	}


	public static List<DeliveryAddress> convertAddresses(List<DeliveryAddressDTO> deliveryAddressDTOS) {
		if (deliveryAddressDTOS == null) {
			return new ArrayList<>();
		}
		ArrayList<DeliveryAddress> deliveryAddresses = new ArrayList<>();
		for (DeliveryAddressDTO dto : deliveryAddressDTOS) {
			deliveryAddresses.add(convert(dto));
		}
		return deliveryAddresses;
	}

	public static DeliveryAddress convert(DeliveryAddressDTO DeliveryAddressDTO) {
		return new DeliveryAddressBuilder()
			.withUuid(DeliveryAddressDTO.getUuid())
			.withDescription(DeliveryAddressDTO.getDescription())
			.withStreet(DeliveryAddressDTO.getStreet())
			.withStreetNumber(DeliveryAddressDTO.getStreetNumber())
			.withLocalNumber(DeliveryAddressDTO.getLocalNumber())
			.withPostcode(DeliveryAddressDTO.getPostcode())
			.withCity(DeliveryAddressDTO.getCity())
			.withBorough(DeliveryAddressDTO.getBorough())
			.withCounty(DeliveryAddressDTO.getCounty())
			.withState(DeliveryAddressDTO.getState())
			.withUser(convert(DeliveryAddressDTO.getUser()))
			.build();
	}

	public static List<DeliveryAddressDTO> convertAddressDTOS(List<DeliveryAddress> deliveryAddresses) {
		if (deliveryAddresses == null) {
			return new ArrayList<>();
		}
		ArrayList<DeliveryAddressDTO> deliveryAddressDTOS = new ArrayList<>();
		for (DeliveryAddress dto : deliveryAddresses) {
			deliveryAddressDTOS.add(convert(dto));
		}
		return deliveryAddressDTOS;
	}

	public static DeliveryAddressDTO convert(DeliveryAddress deliveryAddress) {
		return new DeliveryAddressDTOBuilder()
			.withUuid(deliveryAddress.getUuid())
			.withDescription(deliveryAddress.getDescription())
			.withStreet(deliveryAddress.getStreet())
			.withStreetNumber(deliveryAddress.getStreetNumber())
			.withLocalNumber(deliveryAddress.getLocalNumber())
			.withPostcode(deliveryAddress.getPostcode())
			.withCity(deliveryAddress.getCity())
			.withBorough(deliveryAddress.getBorough())
			.withCounty(deliveryAddress.getCounty())
			.withState(deliveryAddress.getState())
			.withUser(convert(deliveryAddress.getUser()))
			.build();
	}


	public static List<OrderItem> convertOrderItems(List<OrderItemDTO> orderItemDTOS) {
		if (orderItemDTOS == null) {
			return new ArrayList<>();
		}
		ArrayList<OrderItem> orderItems = new ArrayList<>();
		for (OrderItemDTO dto : orderItemDTOS) {
			orderItems.add(convert(dto));
		}
		return orderItems;
	}

	public static OrderItem convert(OrderItemDTO orderItemDTO) {
		return new OrderItemBuilder()
			.withUuid(orderItemDTO.getUuid())
			.withQuantity(orderItemDTO.getQuantity())
			.withMenuItem(convert(orderItemDTO.getMenuItem()))
			.build();
	}

	public static List<OrderItemDTO> convertOrderItemDTOS(List<OrderItem> orderItems) {
		if (orderItems == null) {
			return new ArrayList<>();
		}
		ArrayList<OrderItemDTO> orderItemDTOS = new ArrayList<>();
		for (OrderItem dto : orderItems) {
			orderItemDTOS.add(convert(dto));
		}
		return orderItemDTOS;
	}

	public static OrderItemDTO convert(OrderItem orderItem) {
		return new OrderItemDTOBuilder()
			.withUuid(orderItem.getUuid())
			.withQuantity(orderItem.getQuantity())
			.withMenuItem(convert(orderItem.getMenuItem()))
			.build();
	}


	public static List<Restaurant> convertRestaurants(List<RestaurantDTO> restaurantDTOS) {
		if (restaurantDTOS == null) {
			return new ArrayList<>();
		}
		ArrayList<Restaurant> restaurants = new ArrayList<>();
		for (RestaurantDTO dto : restaurantDTOS) {
			restaurants.add(convert(dto));
		}
		return restaurants;
	}

	public static Restaurant convert(RestaurantDTO restaurantDTO) {
		return new RestaurantBuilder()
			.withUuid(restaurantDTO.getUuid())
			.withName(restaurantDTO.getName())
			.withLogginData(convert(restaurantDTO.getLogginData()))
			.withCompanyData(convert(restaurantDTO.getCompanyData()))
			.withOpenTimes(convertOpenTimeDTOS(restaurantDTO.getOpenTimeDTOS()))
			.withOrders(convertOrdersDTO(restaurantDTO.getOrders()))
			.withMenu(convertMenuItemDTOS(restaurantDTO.getMenuItemDTOS()))
			.withDiscountCodes(convertDiscountCodeDTOS(restaurantDTO.getDiscountCodes()))
			.withArchive(restaurantDTO.getArchive())
			.build();
	}

	public static List<RestaurantDTO> convertRestaurantDTOS(List<Restaurant> restaurantDTOS) {
		if (restaurantDTOS == null) {
			return new ArrayList<>();
		}
		ArrayList<RestaurantDTO> restaurants = new ArrayList<>();
		for (Restaurant dto : restaurantDTOS) {
			restaurants.add(convert(dto));
		}
		return restaurants;
	}

	public static RestaurantDTO convert(Restaurant restaurant) {
		return new RestaurantDTOBuilder()
			.withUuid(restaurant.getUuid())
			.withName(restaurant.getName())
			.withLogginData(convert(restaurant.getLogginData()))
			.withCompanyData(convert(restaurant.getCompanyData()))
			.withOpenTimes(convertOpenTimes(restaurant.getOpenTimes()))
			.withOrders(convertOrders(restaurant.getOrders()))
			.withMenuItems(convertMenuItems(restaurant.getMenu()))
			.withDiscountCodes(convertDiscountCodes(restaurant.getDiscountCodes()))
			.withArchive(restaurant.getArchive())
			.build();
	}


	public static List<OperationEvidence> convertOperationEvidenceDTOS(List<OperationEvidenceDTO> operationEvidenceDTOS) {
		if (operationEvidenceDTOS == null) {
			return new ArrayList<>();
		}
		ArrayList<OperationEvidence> operationEvidences = new ArrayList<>();
		for (OperationEvidenceDTO dto : operationEvidenceDTOS) {
			operationEvidences.add(convert(dto));
		}
		return operationEvidences;
	}

	public static OperationEvidence convert(OperationEvidenceDTO operationEvidenceDTO) {
		return new OperationEvidenceBuilder()
			.withDate(operationEvidenceDTO.getDate())
			.withType(operationEvidenceDTO.getType())
			.withAmount(operationEvidenceDTO.getAmount())
			.withUser(convertNoEvidence(operationEvidenceDTO.getUser()))
			.build();
	}

	public static List<OperationEvidenceDTO> convertOperationEvidences(List<OperationEvidence> operationEvidences) {
		if (operationEvidences == null) {
			return new ArrayList<>();
		}
		ArrayList<OperationEvidenceDTO> operationEvidenceDTOS = new ArrayList<>();
		for (OperationEvidence dto : operationEvidences) {
			operationEvidenceDTOS.add(convert(dto));
		}
		return operationEvidenceDTOS;
	}

	public static OperationEvidenceDTO convert(OperationEvidence operationEvidence) {
		return new OperationEvidenceDTOBuilder()
			.withDate(operationEvidence.getDate())
			.withType(operationEvidence.getType())
			.withAmount(operationEvidence.getAmount())
			.withUser(convert(operationEvidence.getUser()))
			.build();
	}


	public static List<MenuItem> convertMenuItemDTOS(List<MenuItemDTO> menuItemDTOS) {
		if (menuItemDTOS == null) {
			return new ArrayList<>();
		}
		ArrayList<MenuItem> menuItems = new ArrayList<>();
		for (MenuItemDTO dto : menuItemDTOS) {
			menuItems.add(convert(dto));
		}
		return menuItems;
	}

	public static MenuItem convert(MenuItemDTO menuItemDTO) {
		return new MenuItemBuilder()
			.withUuid(menuItemDTO.getUuid())
			.withName(menuItemDTO.getName())
			.withNettoPrice(menuItemDTO.getNettoPrice())
			.withVatTax(menuItemDTO.getVatTax())
			.withBruttoPrice(menuItemDTO.getBruttoPrice())
			.withDishes(convertDishDTOS(menuItemDTO.getDishes()))
			.withRestaurant(convert(menuItemDTO.getRestaurant()))
			.build();
	}

	public static List<MenuItemDTO> convertMenuItems(List<MenuItem> menuItems) {
		if (menuItems == null) {
			return new ArrayList<>();
		}
		ArrayList<MenuItemDTO> menuItemDTOS = new ArrayList<>();
		for (MenuItem dto : menuItems) {
			menuItemDTOS.add(convert(dto));
		}
		return menuItemDTOS;
	}

	public static MenuItemDTO convert(MenuItem menuItem) {
		return new MenuItemDTOBuilder()
			.withUuid(menuItem.getUuid())
			.withName(menuItem.getName())
			.withNettoPrice(menuItem.getNettoPrice())
			.withVatTax(menuItem.getVatTax())
			.withBruttoPrice(menuItem.getBruttoPrice())
			.withDishes(convertDishes(menuItem.getDishes()))
			.withRestaurant(convert(menuItem.getRestaurant()))
			.build();
	}


	public static CompanyData convert(CompanyDataDTO companyDataDTO) {
		return new CompanyDataBuilder()
			.withName(companyDataDTO.getName())
			.withAddress(convert(companyDataDTO.getAddress()))
			.withNIP(companyDataDTO.getNIP())
			.withPhone(companyDataDTO.getPhone())
			.withEmail(companyDataDTO.getEmail())
			.build();
	}

	public static CompanyDataDTO convert(CompanyData companyData) {
		return new CompanyDataDTOBuilder()
			.withName(companyData.getName())
			.withAddress(convert(companyData.getAddress()))
			.withNIP(companyData.getNIP())
			.withPhone(companyData.getPhone())
			.withEmail(companyData.getEmail())
			.build();
	}


	public static List<OpenTime> convertOpenTimeDTOS(List<OpenTimeDTO> openTimeDTOS) {
		if (openTimeDTOS == null) {
			return new ArrayList<>();
		}
		ArrayList<OpenTime> openTimes = new ArrayList<>();
		for (OpenTimeDTO dto : openTimeDTOS) {
			openTimes.add(convert(dto));
		}
		return openTimes;
	}

	public static OpenTime convert(OpenTimeDTO openTimeDTO) {
		return new OpenTimeBuilder()
			.withUuid(openTimeDTO.getUuid())
			.withDayOfWeek(openTimeDTO.getDayOfWeek())
			.withPeriodTime(convert(openTimeDTO.getPeriodTimeDTO()))
			.withRestaurant(convert(openTimeDTO.getRestaurantDTO()))
			.build();
	}

	public static List<OpenTimeDTO> convertOpenTimes(List<OpenTime> openTimes) {
		if (openTimes == null) {
			return new ArrayList<>();
		}
		ArrayList<OpenTimeDTO> openTimeDTOS = new ArrayList<>();
		for (OpenTime dto : openTimes) {
			openTimeDTOS.add(convert(dto));
		}
		return openTimeDTOS;
	}

	public static OpenTimeDTO convert(OpenTime openTime) {
		return new OpenTimeDTOBuilder()
			.withUuid(openTime.getUuid())
			.withDayOfWeek(openTime.getDayOfWeek())
			.withPeriodTime(convert(openTime.getPeriodTime()))
			.withRestaurant(convert(openTime.getRestaurant()))
			.build();
	}


	public static List<Dish> convertDishDTOS(List<DishDTO> dishDTOS) {
		if (dishDTOS == null) {
			return new ArrayList<>();
		}
		ArrayList<Dish> dishes = new ArrayList<>();
		for (DishDTO dto : dishDTOS) {
			dishes.add(convert(dto));
		}
		return dishes;
	}

	public static Dish convert(@Nullable DishDTO dishDTO) {
		if (dishDTO == null) {
			return null;
		}
		return new DishBuilder()
			.withUuid(dishDTO.getUuid())
			.withQuantity(dishDTO.getQuantity())
			.withProduct(convert(dishDTO.getProduct()))
			.withMenuItems(convertMenuItemDTOS(dishDTO.getMenuItems()))
			.build();
	}

	public static List<DishDTO> convertDishes(List<Dish> dishes) {
		if (dishes == null) {
			return new ArrayList<>();
		}
		ArrayList<DishDTO> dishDTOS = new ArrayList<>();
		for (Dish dto : dishes) {
			dishDTOS.add(convert(dto));
		}
		return dishDTOS;
	}

	public static DishDTO convert(@Nullable Dish dish) {
		if (dish == null) {
			return null;
		}
		return new DishDTOBuilder()
			.withUuid(dish.getUuid())
			.withQuantity(dish.getQuantity())
			.withProduct(convert(dish.getProduct()))
			.withMenuItems(convertMenuItems(dish.getMenuItems()))
			.build();
	}


	public static Product convert(ProductDTO productDTO) {
		return new ProductBuilder()
			.withUuid(productDTO.getUuid())
			.withName(productDTO.getName())
			.withIngredients(convertIngredientDTOS(productDTO.getIngredients()))
			.withDish(convert(productDTO.getDish()))
			.build();
	}

	public static ProductDTO convert(Product product) {
		return new ProductDTOBuilder()
			.withUuid(product.getUuid())
			.withName(product.getName())
			.withIngredients(convertIngredients(product.getIngredients()))
			.withDish(convert(product.getDish()))
			.build();
	}


	public static List<Ingredient> convertIngredientDTOS(List<IngredientDTO> ingredientDTOS) {
		if (ingredientDTOS == null) {
			return new ArrayList<>();
		}
		ArrayList<Ingredient> ingredients = new ArrayList<>();
		for (IngredientDTO dto : ingredientDTOS) {
			ingredients.add(convert(dto));
		}
		return ingredients;
	}

	public static Ingredient convert(IngredientDTO ingredientDTO) {
		return new IngredientBuilder()
			.withUuid(ingredientDTO.getUuid())
			.withName(ingredientDTO.getName())
			.withAllergen(ingredientDTO.getIsAllergen())
			.build();
	}

	public static List<IngredientDTO> convertIngredients(List<Ingredient> ingredients) {
		if (ingredients == null) {
			return new ArrayList<>();
		}
		ArrayList<IngredientDTO> ingredientDTOS = new ArrayList<>();
		for (Ingredient dto : ingredients) {
			ingredientDTOS.add(convert(dto));
		}
		return ingredientDTOS;
	}

	public static IngredientDTO convert(Ingredient ingredient) {
		return new IngredientDTOBuilder()
			.withUuid(ingredient.getUuid())
			.withName(ingredient.getName())
			.withAllergen(ingredient.getIsAllergen())
			.build();
	}


	public static Employee convert(EmployeeDTO employeeDTO) {
		return new EmployeeBuilder()
			.withUuid(employeeDTO.getUuid())
			.withPersonalData(convert(employeeDTO.getPersonalData()))
			.withLogginData(convert(employeeDTO.getLogginData()))
			.withArchive(employeeDTO.getArchive())
			.build();
	}

	public static EmployeeDTO convert(Employee employee) {
		return new EmployeeDTOBuilder()
			.withUuid(employee.getUuid())
			.withPersonalData(convert(employee.getPersonalData()))
			.withLogginData(convert(employee.getLogginData()))
			.withArchive(employee.getArchive())
			.build();
	}
}
