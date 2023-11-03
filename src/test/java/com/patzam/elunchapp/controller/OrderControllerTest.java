package com.patzam.elunchapp.controller;

import com.google.common.truth.Truth8;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.patzam.elunchapp.DTO.OrderDTO;
import com.patzam.elunchapp.DTO.OrderStatusDTO;
import com.patzam.elunchapp.config.JPAConfiguration;
import com.patzam.elunchapp.listeners.OperationEvidenceListener;
import com.patzam.elunchapp.model.Deliverer;
import com.patzam.elunchapp.model.DeliveryAddress;
import com.patzam.elunchapp.model.Dish;
import com.patzam.elunchapp.model.Ingredient;
import com.patzam.elunchapp.model.MenuItem;
import com.patzam.elunchapp.model.OperationEvidence;
import com.patzam.elunchapp.model.Order;
import com.patzam.elunchapp.model.OrderItem;
import com.patzam.elunchapp.model.Product;
import com.patzam.elunchapp.model.Restaurant;
import com.patzam.elunchapp.model.User;
import com.patzam.elunchapp.model.enums.Archive;
import com.patzam.elunchapp.model.enums.EvidenceType;
import com.patzam.elunchapp.model.enums.Sex;
import com.patzam.elunchapp.model.enums.VatTax;
import com.patzam.elunchapp.repo.DelivererRepo;
import com.patzam.elunchapp.repo.DeliveryAddressRepo;
import com.patzam.elunchapp.repo.DiscountCodeRepo;
import com.patzam.elunchapp.repo.DishRepo;
import com.patzam.elunchapp.repo.IngredientRepo;
import com.patzam.elunchapp.repo.MenuItemRepo;
import com.patzam.elunchapp.repo.OperationEvidenceRepo;
import com.patzam.elunchapp.repo.OrderItemRepo;
import com.patzam.elunchapp.repo.OrderRepo;
import com.patzam.elunchapp.repo.ProductRepo;
import com.patzam.elunchapp.repo.RestaurantRepo;
import com.patzam.elunchapp.repo.UserRepo;
import com.patzam.elunchapp.service.DelivererService;
import com.patzam.elunchapp.service.DelivererServiceImpl;
import com.patzam.elunchapp.service.OperationEvidenceService;
import com.patzam.elunchapp.service.OperationEvidenceServiceImpl;
import com.patzam.elunchapp.service.OrderItemService;
import com.patzam.elunchapp.service.OrderItemServiceImpl;
import com.patzam.elunchapp.service.OrderService;
import com.patzam.elunchapp.service.OrderServiceImpl;
import com.patzam.elunchapp.service.UserService;
import com.patzam.elunchapp.service.UserServiceImpl;
import com.patzam.elunchapp.utils.AssertionUtils;
import com.patzam.elunchapp.utils.ConverterUtils;
import com.patzam.elunchapp.utils.TestUtils;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest(classes = {
	JPAConfiguration.class,
	OrderControllerTest.Config.class
})
class OrderControllerTest {
	@Configuration
	public static class Config {
		@Bean
		public OperationEvidenceService operationEvidenceService(OperationEvidenceRepo operationEvidenceRepo) {
			return new OperationEvidenceServiceImpl(operationEvidenceRepo);
		}
		@Bean
		public OperationEvidenceListener operationEvidenceListener(OperationEvidenceService operationEvidenceService, UserRepo userRepo) {
			return new OperationEvidenceListener(operationEvidenceService, userRepo);
		}
		@Bean
		public OrderItemService orderItemService(OrderItemRepo orderItemRepo) {
			return new OrderItemServiceImpl(orderItemRepo);
		}
		@Bean
		public OrderService orderService(OrderRepo orderRepo,
										 UserRepo userRepo,
										 RestaurantRepo restaurantRepo,
										 DelivererRepo delivererRepo,
										 DeliveryAddressRepo deliveryAddressRepo,
										 MenuItemRepo menuItemRepo,
										 OrderItemRepo orderItemRepo,
										 DiscountCodeRepo discountCodeRepo,
										 OrderItemService orderItemService) {
			return new OrderServiceImpl(orderRepo, userRepo, restaurantRepo, delivererRepo, deliveryAddressRepo, menuItemRepo, discountCodeRepo, orderItemRepo, orderItemService);
		}
		@Bean
		public DelivererService delivererService(DelivererRepo delivererRepo, OrderRepo orderRepo) {
			return new DelivererServiceImpl(delivererRepo, orderRepo);
		}
		@Bean
		public UserService userService(UserRepo userRepo) {
			return new UserServiceImpl(userRepo);
		}
		@Bean
		public OrderController orderController(OrderService orderService,
											   DelivererService delivererService,
											   UserService userService,
											   ApplicationEventPublisher applicationEventPublisher) {
			return new OrderController(orderService, delivererService, userService, applicationEventPublisher);
		}
	}

	@Autowired
	private OrderRepo orderRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private OperationEvidenceRepo operationEvidenceRepo;

	@Autowired
	private RestaurantRepo restaurantRepo;

	@Autowired
	private DelivererRepo delivererRepo;

	@Autowired
	private DeliveryAddressRepo deliveryAddressRepo;

	@Autowired
	private IngredientRepo ingredientRepo;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private DishRepo dishRepo;

	@Autowired
	private MenuItemRepo menuItemRepo;

	@Autowired
	private OrderItemRepo orderItemRepo;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderController orderController;

	@Autowired
	private PlatformTransactionManager txManager;

	private static final String STR_UUID = "34739d73-6523-417a-8f68-263f4bfde58e";

	// add
	@Test
	@Transactional
	public void put1() {
		Restaurant restaurant = TestUtils.restaurant("087c502c-559b-4d81-ae53-e67ab6fdf995", "MakJson",
			TestUtils.logginData("mcJson123", "bigDag123!"),
			TestUtils.companyData("MakJson sp. z zoo",
				TestUtils.address("St. Patric", "152", "00-000", "NY"),
				"123-010-00-11", "123123123", "+48 501 502 503", "MJ@gmail.com"), Archive.CURRENT);
		restaurantRepo.save(restaurant);

		Deliverer deliverer = TestUtils.deliverer("cb093f68-f4f1-4f21-a094-5850150b42dd",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer);

		User user = TestUtils.user("9986208e-961a-48d4-bf7a-112c627779c2",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);

		DeliveryAddress deliveryAddress = TestUtils.deliveryAddress("6d66a218-f493-41ee-8291-a0065fab5cfe", null, "Jana Pawła", "1", "", "00-010", "Warszawa", null, "PL", null, user);
		deliveryAddressRepo.save(deliveryAddress);

		Ingredient ingredient1 = TestUtils.ingredient("ac30a0fe-f97b-4b08-aaac-1309c76c5455", "Kurczak", true);
		ingredientRepo.save(ingredient1);
		Ingredient ingredient2 = TestUtils.ingredient("5632de5e-5c00-463b-b701-152fb76daea3", "Mąka", false);
		ingredientRepo.save(ingredient2);
		Ingredient ingredient3 = TestUtils.ingredient("5a1e100b-b82d-42bb-b34f-fc1c3497f9c8", "Kukurydza", true);
		ingredientRepo.save(ingredient3);
		Ingredient ingredient4 = TestUtils.ingredient("6b1e5fdc-d771-423e-b968-46e5a03a8200", "Ziemniak", false);
		ingredientRepo.save(ingredient4);
		Ingredient ingredient5 = TestUtils.ingredient("e7cec9d4-cdf3-4b32-b2f4-9b90f41a8d05", "Masło", false);
		ingredientRepo.save(ingredient5);
		Ingredient ingredient6 = TestUtils.ingredient("dc0ff645-2596-4c7d-a6fa-6926db65be16", "Marchew", false);
		ingredientRepo.save(ingredient6);
		Ingredient ingredient7 = TestUtils.ingredient("8ede3563-edd0-4d8b-b121-dd21dd11b7b6", "Jabłko", false);
		ingredientRepo.save(ingredient7);

		Product product1 = TestUtils.product("19e1281b-949d-48c3-9486-c0abbf7a0267", "Udko korczaka", ingredient1, ingredient2, ingredient3);
		productRepo.save(product1);
		Product product2 = TestUtils.product("e192ce71-75a9-4778-82c8-0bff97a98879", "Ziemniak", ingredient4, ingredient5);
		productRepo.save(product2);
		Product product3 = TestUtils.product("2af0b1a3-e2be-4e16-bc8b-b5ff4355b44a", "Surówka", ingredient6, ingredient7);
		productRepo.save(product3);

		Dish dish1 = TestUtils.dish("b9820ee0-c4b8-4ed8-90f7-7fa947f72548", 1, product1);
		dishRepo.save(dish1);
		Dish dish2 = TestUtils.dish("74e078d9-f49f-4c53-be67-f9f4cbe5804f", 3, product2);
		dishRepo.save(dish2);
		Dish dish3 = TestUtils.dish("0173a261-be83-4e9c-914f-eacfdaba46a8", 1, product3);
		dishRepo.save(dish3);

		MenuItem menuItem1 = TestUtils.menuItem("3deb5633-e968-4bdc-a114-cd4430a4f003", "Zestaw odbiadowy szefa kuchni", new BigDecimal("20.00"),
			VatTax._23, new BigDecimal("24.60"), restaurant, dish1, dish2, dish3);
		menuItemRepo.save(menuItem1);


		OrderDTO orderJson = TestUtils.orderDTO(STR_UUID, null, "Poproszę bez panierki", ConverterUtils.convert(user), ConverterUtils.convert(deliverer),
			ConverterUtils.convert(deliveryAddress), ConverterUtils.convert(restaurant),
			ConverterUtils.convert(TestUtils.orderItem("1f4fa012-cd0b-4ae0-bbc0-040d73d75b6c", 2, menuItem1)));
		orderController.put(UUID.fromString(STR_UUID), orderJson);

		orderJson.setNettoPrice(menuItem1.getNettoPrice().multiply(new BigDecimal(orderJson.getOrderItems().get(0).getQuantity())));
		orderJson.setBruttoPrice(menuItem1.getBruttoPrice().multiply(new BigDecimal(orderJson.getOrderItems().get(0).getQuantity())));
		orderJson.setAmountToPayBrutto(menuItem1.getBruttoPrice().multiply(new BigDecimal(orderJson.getOrderItems().get(0).getQuantity())));

		OrderDTO orderDB = orderService.getByUuid(UUID.fromString(STR_UUID)).orElseThrow();
		AssertionUtils.assertEquals(orderJson, orderDB, AssertionUtils.OrderStatusType.NEW);
	}

	// update order in case is not paid
	@Test
	@Transactional
	public void put2() {
		Restaurant restaurant = TestUtils.restaurant("087c502c-559b-4d81-ae53-e67ab6fdf995", "MakJson",
			TestUtils.logginData("mcJson123", "bigDag123!"),
			TestUtils.companyData("MakJson sp. z zoo",
				TestUtils.address("St. Patric", "152", "00-000", "NY"),
				"123-010-00-11", "123123123", "+48 501 502 503", "MJ@gmail.com"), Archive.CURRENT);
		restaurantRepo.save(restaurant);

		Deliverer deliverer1 = TestUtils.deliverer("cb093f68-f4f1-4f21-a094-5850150b42dd",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer1);
		Deliverer deliverer2 = TestUtils.deliverer("9f15cfc3-2568-4fe3-8f94-034a23c9df6f",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith3", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer2);

		User user = TestUtils.user("9986208e-961a-48d4-bf7a-112c627779c2", TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);

		DeliveryAddress deliveryAddress = TestUtils.deliveryAddress("6d66a218-f493-41ee-8291-a0065fab5cfe", null, "Jana Pawła", "1",
			"", "00-010", "Warszawa", null, "PL", null, user);
		deliveryAddressRepo.save(deliveryAddress);

		Ingredient ingredient1 = TestUtils.ingredient("ac30a0fe-f97b-4b08-aaac-1309c76c5455", "Kurczak", true);
		ingredientRepo.save(ingredient1);
		Ingredient ingredient2 = TestUtils.ingredient("5632de5e-5c00-463b-b701-152fb76daea3", "Mąka", false);
		ingredientRepo.save(ingredient2);
		Ingredient ingredient3 = TestUtils.ingredient("5a1e100b-b82d-42bb-b34f-fc1c3497f9c8", "Kukurydza", true);
		ingredientRepo.save(ingredient3);
		Ingredient ingredient4 = TestUtils.ingredient("6b1e5fdc-d771-423e-b968-46e5a03a8200", "Ziemniak", false);
		ingredientRepo.save(ingredient4);
		Ingredient ingredient5 = TestUtils.ingredient("e7cec9d4-cdf3-4b32-b2f4-9b90f41a8d05", "Masło", false);
		ingredientRepo.save(ingredient5);
		Ingredient ingredient6 = TestUtils.ingredient("dc0ff645-2596-4c7d-a6fa-6926db65be16", "Marchew", false);
		ingredientRepo.save(ingredient6);
		Ingredient ingredient7 = TestUtils.ingredient("8ede3563-edd0-4d8b-b121-dd21dd11b7b6", "Jabłko", false);
		ingredientRepo.save(ingredient7);

		Product product1 = TestUtils.product("19e1281b-949d-48c3-9486-c0abbf7a0267", "Udko korczaka", ingredient1, ingredient2, ingredient3);
		productRepo.save(product1);
		Product product2 = TestUtils.product("e192ce71-75a9-4778-82c8-0bff97a98879", "Ziemniak", ingredient4, ingredient5);
		productRepo.save(product2);
		Product product3 = TestUtils.product("2af0b1a3-e2be-4e16-bc8b-b5ff4355b44a", "Surówka", ingredient6, ingredient7);
		productRepo.save(product3);

		Dish dish1 = TestUtils.dish("b9820ee0-c4b8-4ed8-90f7-7fa947f72548", 1, product1);
		dishRepo.save(dish1);
		Dish dish2 = TestUtils.dish("74e078d9-f49f-4c53-be67-f9f4cbe5804f", 3, product2);
		dishRepo.save(dish2);
		Dish dish3 = TestUtils.dish("0173a261-be83-4e9c-914f-eacfdaba46a8", 1, product3);
		dishRepo.save(dish3);

		MenuItem menuItem1 = TestUtils.menuItem("3deb5633-e968-4bdc-a114-cd4430a4f003", "Zestaw odbiadowy szefa kuchni", new BigDecimal("20.00"),
			VatTax._23, new BigDecimal("24.60"), restaurant, dish1, dish2, dish3);
		menuItemRepo.save(menuItem1);

		MenuItem menuItem2 = TestUtils.menuItem("44b2cd38-69eb-4178-b271-0bdef113eeea", "Powiększenie zestawu", new BigDecimal("8.00"),
			VatTax._23, new BigDecimal("9.84"), restaurant, dish1);
		menuItemRepo.save(menuItem2);

		OrderItem orderItem = TestUtils.orderItem("1f4fa012-cd0b-4ae0-bbc0-040d73d75b6c", 2, menuItem1);
		orderItemRepo.save(orderItem);

		Order order = TestUtils.order(STR_UUID, null, "Poproszę bez panierki", user,
			deliverer1, deliveryAddress, restaurant, new BigDecimal("40.00"), new BigDecimal("49.20"), new BigDecimal("49.20"),
			TestUtils.orderStatus("2020-01-01T15:00:00Z", false, null, null),
			orderItem
		);
		orderRepo.save(order);

		OrderDTO orderJson = TestUtils.orderDTO(STR_UUID, null, "", ConverterUtils.convert(user),
			ConverterUtils.convert(deliverer2), ConverterUtils.convert(deliveryAddress), ConverterUtils.convert(restaurant),
			ConverterUtils.convert(orderItem),
			ConverterUtils.convert(TestUtils.orderItem("a9058ad1-5b4b-4cd1-aec2-fb3bc2b501b7", 2, menuItem2))
		);
		orderController.put(UUID.fromString(STR_UUID), orderJson);

		orderJson.setNettoPrice(
			menuItem1.
				getNettoPrice().multiply(new BigDecimal(orderJson.getOrderItems().get(0).getQuantity()))
				.add(menuItem2
					.getNettoPrice().multiply(new BigDecimal(orderJson.getOrderItems().get(1).getQuantity())))
		);
		orderJson.setBruttoPrice(menuItem1.
			getBruttoPrice().multiply(new BigDecimal(orderJson.getOrderItems().get(0).getQuantity()))
			.add(menuItem2
				.getBruttoPrice().multiply(new BigDecimal(orderJson.getOrderItems().get(1).getQuantity())))
		);
		orderJson.setAmountToPayBrutto(menuItem1.
			getBruttoPrice().multiply(new BigDecimal(orderJson.getOrderItems().get(0).getQuantity()))
			.add(menuItem2
				.getBruttoPrice().multiply(new BigDecimal(orderJson.getOrderItems().get(1).getQuantity())))
		);

		OrderDTO orderDB = orderService.getByUuid(UUID.fromString(STR_UUID)).orElseThrow();
		AssertionUtils.assertEquals(orderJson, orderDB, AssertionUtils.OrderStatusType.NEW);
	}

	// paying for order
	@Test
	@Transactional
	public void setIsPaid1() {
		Restaurant restaurant = TestUtils.restaurant("087c502c-559b-4d81-ae53-e67ab6fdf995", "MakJson",
			TestUtils.logginData("mcJson123", "bigDag123!"),
			TestUtils.companyData("MakJson sp. z zoo",
				TestUtils.address("St. Patric", "152", "00-000", "NY"),
				"123-010-00-11", "123123123", "+48 501 502 503", "MJ@gmail.com"), Archive.CURRENT);
		restaurantRepo.save(restaurant);

		Deliverer deliverer = TestUtils.deliverer("cb093f68-f4f1-4f21-a094-5850150b42dd",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer);

		User user = TestUtils.user("9986208e-961a-48d4-bf7a-112c627779c2", TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);

		DeliveryAddress deliveryAddress = TestUtils.deliveryAddress("6d66a218-f493-41ee-8291-a0065fab5cfe", null, "Jana Pawła", "1",
			"", "00-010", "Warszawa", null, "PL", null, user);
		deliveryAddressRepo.save(deliveryAddress);

		OperationEvidence operationEvidence = TestUtils.operationEvidence("2020-01-01T14:00:15Z", EvidenceType.DEPOSIT, new BigDecimal("50.00"), user);
		operationEvidenceRepo.save(operationEvidence);

		Ingredient ingredient1 = TestUtils.ingredient("ac30a0fe-f97b-4b08-aaac-1309c76c5455", "Kurczak", true);
		ingredientRepo.save(ingredient1);
		Ingredient ingredient2 = TestUtils.ingredient("5632de5e-5c00-463b-b701-152fb76daea3", "Mąka", false);
		ingredientRepo.save(ingredient2);
		Ingredient ingredient3 = TestUtils.ingredient("5a1e100b-b82d-42bb-b34f-fc1c3497f9c8", "Kukurydza", true);
		ingredientRepo.save(ingredient3);
		Ingredient ingredient4 = TestUtils.ingredient("6b1e5fdc-d771-423e-b968-46e5a03a8200", "Ziemniak", false);
		ingredientRepo.save(ingredient4);
		Ingredient ingredient5 = TestUtils.ingredient("e7cec9d4-cdf3-4b32-b2f4-9b90f41a8d05", "Masło", false);
		ingredientRepo.save(ingredient5);
		Ingredient ingredient6 = TestUtils.ingredient("dc0ff645-2596-4c7d-a6fa-6926db65be16", "Marchew", false);
		ingredientRepo.save(ingredient6);
		Ingredient ingredient7 = TestUtils.ingredient("8ede3563-edd0-4d8b-b121-dd21dd11b7b6", "Jabłko", false);
		ingredientRepo.save(ingredient7);

		Product product1 = TestUtils.product("19e1281b-949d-48c3-9486-c0abbf7a0267", "Udko korczaka", ingredient1, ingredient2, ingredient3);
		productRepo.save(product1);
		Product product2 = TestUtils.product("e192ce71-75a9-4778-82c8-0bff97a98879", "Ziemniak", ingredient4, ingredient5);
		productRepo.save(product2);
		Product product3 = TestUtils.product("2af0b1a3-e2be-4e16-bc8b-b5ff4355b44a", "Surówka", ingredient6, ingredient7);
		productRepo.save(product3);

		Dish dish1 = TestUtils.dish("b9820ee0-c4b8-4ed8-90f7-7fa947f72548", 1, product1);
		dishRepo.save(dish1);
		Dish dish2 = TestUtils.dish("74e078d9-f49f-4c53-be67-f9f4cbe5804f", 3, product2);
		dishRepo.save(dish2);
		Dish dish3 = TestUtils.dish("0173a261-be83-4e9c-914f-eacfdaba46a8", 1, product3);
		dishRepo.save(dish3);

		MenuItem menuItem1 = TestUtils.menuItem("3deb5633-e968-4bdc-a114-cd4430a4f003", "Zestaw odbiadowy szefa kuchni", new BigDecimal("20.00"),
			VatTax._23, new BigDecimal("24.60"), restaurant, dish1, dish2, dish3);
		menuItemRepo.save(menuItem1);

		OrderItem orderItem = TestUtils.orderItem("1f4fa012-cd0b-4ae0-bbc0-040d73d75b6c", 2, menuItem1);
		orderItemRepo.save(orderItem);

		Order order = TestUtils.order(STR_UUID, null, "Poproszę bez panierki", user,
			deliverer, deliveryAddress, restaurant, new BigDecimal("40.00"), new BigDecimal("49.20"), new BigDecimal("49.20"),
			TestUtils.orderStatus("2020-01-01T15:00:00Z", false, null, null),
			orderItem
		);
		orderRepo.save(order);

		orderController.patchIsPaid(UUID.fromString(STR_UUID));

		OrderDTO orderDB = orderService.getByUuid(UUID.fromString(STR_UUID)).orElseThrow();
		assert orderDB.getOrderStatusDTO().getIsPaid();
	}

	// error while not enough founds
	@Test
	@Transactional
	public void setIsPaid2() {
		Restaurant restaurant = TestUtils.restaurant("087c502c-559b-4d81-ae53-e67ab6fdf995", "MakJson",
			TestUtils.logginData("mcJson123", "bigDag123!"),
			TestUtils.companyData("MakJson sp. z zoo",
				TestUtils.address("St. Patric", "152", "00-000", "NY"),
				"123-010-00-11", "123123123", "+48 501 502 503", "MJ@gmail.com"), Archive.CURRENT);
		restaurantRepo.save(restaurant);

		Deliverer deliverer = TestUtils.deliverer("cb093f68-f4f1-4f21-a094-5850150b42dd",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer);

		User user = TestUtils.user("9986208e-961a-48d4-bf7a-112c627779c2", TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);

		DeliveryAddress deliveryAddress = TestUtils.deliveryAddress("6d66a218-f493-41ee-8291-a0065fab5cfe", null, "Jana Pawła", "1",
			"", "00-010", "Warszawa", null, "PL", null, user);
		deliveryAddressRepo.save(deliveryAddress);

		OperationEvidence operationEvidence = TestUtils.operationEvidence("2020-01-01T14:00:15Z", EvidenceType.DEPOSIT, new BigDecimal("10.00"), user);
		operationEvidenceRepo.save(operationEvidence);

		Ingredient ingredient1 = TestUtils.ingredient("ac30a0fe-f97b-4b08-aaac-1309c76c5455", "Kurczak", true);
		ingredientRepo.save(ingredient1);
		Ingredient ingredient2 = TestUtils.ingredient("5632de5e-5c00-463b-b701-152fb76daea3", "Mąka", false);
		ingredientRepo.save(ingredient2);
		Ingredient ingredient3 = TestUtils.ingredient("5a1e100b-b82d-42bb-b34f-fc1c3497f9c8", "Kukurydza", true);
		ingredientRepo.save(ingredient3);
		Ingredient ingredient4 = TestUtils.ingredient("6b1e5fdc-d771-423e-b968-46e5a03a8200", "Ziemniak", false);
		ingredientRepo.save(ingredient4);
		Ingredient ingredient5 = TestUtils.ingredient("e7cec9d4-cdf3-4b32-b2f4-9b90f41a8d05", "Masło", false);
		ingredientRepo.save(ingredient5);
		Ingredient ingredient6 = TestUtils.ingredient("dc0ff645-2596-4c7d-a6fa-6926db65be16", "Marchew", false);
		ingredientRepo.save(ingredient6);
		Ingredient ingredient7 = TestUtils.ingredient("8ede3563-edd0-4d8b-b121-dd21dd11b7b6", "Jabłko", false);
		ingredientRepo.save(ingredient7);

		Product product1 = TestUtils.product("19e1281b-949d-48c3-9486-c0abbf7a0267", "Udko korczaka", ingredient1, ingredient2, ingredient3);
		productRepo.save(product1);
		Product product2 = TestUtils.product("e192ce71-75a9-4778-82c8-0bff97a98879", "Ziemniak", ingredient4, ingredient5);
		productRepo.save(product2);
		Product product3 = TestUtils.product("2af0b1a3-e2be-4e16-bc8b-b5ff4355b44a", "Surówka", ingredient6, ingredient7);
		productRepo.save(product3);

		Dish dish1 = TestUtils.dish("b9820ee0-c4b8-4ed8-90f7-7fa947f72548", 1, product1);
		dishRepo.save(dish1);
		Dish dish2 = TestUtils.dish("74e078d9-f49f-4c53-be67-f9f4cbe5804f", 3, product2);
		dishRepo.save(dish2);
		Dish dish3 = TestUtils.dish("0173a261-be83-4e9c-914f-eacfdaba46a8", 1, product3);
		dishRepo.save(dish3);

		MenuItem menuItem1 = TestUtils.menuItem("3deb5633-e968-4bdc-a114-cd4430a4f003", "Zestaw odbiadowy szefa kuchni", new BigDecimal("20.00"),
			VatTax._23, new BigDecimal("24.60"), restaurant, dish1, dish2, dish3);
		menuItemRepo.save(menuItem1);

		OrderItem orderItem = TestUtils.orderItem("1f4fa012-cd0b-4ae0-bbc0-040d73d75b6c", 2, menuItem1);
		orderItemRepo.save(orderItem);

		Order order = TestUtils.order(STR_UUID, null, "Poproszę bez panierki", user,
			deliverer, deliveryAddress, restaurant, new BigDecimal("40.00"), new BigDecimal("49.20"), new BigDecimal("49.20"),
			TestUtils.orderStatus("2020-01-01T15:00:00Z", false, null, null),
			orderItem
		);
		orderRepo.save(order);

		Assertions.assertThrows(ResponseStatusException.class, () -> orderController.patchIsPaid(UUID.fromString(STR_UUID)));
	}

	// giving out
	@Test
	@Transactional
	public void setIsGivedOut1() {
		Restaurant restaurant = TestUtils.restaurant("087c502c-559b-4d81-ae53-e67ab6fdf995", "MakJson",
			TestUtils.logginData("mcJson123", "bigDag123!"),
			TestUtils.companyData("MakJson sp. z zoo",
				TestUtils.address("St. Patric", "152", "00-000", "NY"),
				"123-010-00-11", "123123123", "+48 501 502 503", "MJ@gmail.com"), Archive.CURRENT);
		restaurantRepo.save(restaurant);

		Deliverer deliverer = TestUtils.deliverer("cb093f68-f4f1-4f21-a094-5850150b42dd",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer);

		User user = TestUtils.user("9986208e-961a-48d4-bf7a-112c627779c2", TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);

		DeliveryAddress deliveryAddress = TestUtils.deliveryAddress("6d66a218-f493-41ee-8291-a0065fab5cfe", null, "Jana Pawła", "1",
			"", "00-010", "Warszawa", null, "PL", null, user);
		deliveryAddressRepo.save(deliveryAddress);

		OperationEvidence operationEvidence = TestUtils.operationEvidence("2020-01-01T14:00:15Z", EvidenceType.DEPOSIT, new BigDecimal("50.00"), user);
		operationEvidenceRepo.save(operationEvidence);
		OperationEvidence operationEvidence2 = TestUtils.operationEvidence("2020-01-01T15:00:15Z", EvidenceType.PAYMENT, new BigDecimal("49.20"), user);
		operationEvidenceRepo.save(operationEvidence2);

		Ingredient ingredient1 = TestUtils.ingredient("ac30a0fe-f97b-4b08-aaac-1309c76c5455", "Kurczak", true);
		ingredientRepo.save(ingredient1);
		Ingredient ingredient2 = TestUtils.ingredient("5632de5e-5c00-463b-b701-152fb76daea3", "Mąka", false);
		ingredientRepo.save(ingredient2);
		Ingredient ingredient3 = TestUtils.ingredient("5a1e100b-b82d-42bb-b34f-fc1c3497f9c8", "Kukurydza", true);
		ingredientRepo.save(ingredient3);
		Ingredient ingredient4 = TestUtils.ingredient("6b1e5fdc-d771-423e-b968-46e5a03a8200", "Ziemniak", false);
		ingredientRepo.save(ingredient4);
		Ingredient ingredient5 = TestUtils.ingredient("e7cec9d4-cdf3-4b32-b2f4-9b90f41a8d05", "Masło", false);
		ingredientRepo.save(ingredient5);
		Ingredient ingredient6 = TestUtils.ingredient("dc0ff645-2596-4c7d-a6fa-6926db65be16", "Marchew", false);
		ingredientRepo.save(ingredient6);
		Ingredient ingredient7 = TestUtils.ingredient("8ede3563-edd0-4d8b-b121-dd21dd11b7b6", "Jabłko", false);
		ingredientRepo.save(ingredient7);

		Product product1 = TestUtils.product("19e1281b-949d-48c3-9486-c0abbf7a0267", "Udko korczaka", ingredient1, ingredient2, ingredient3);
		productRepo.save(product1);
		Product product2 = TestUtils.product("e192ce71-75a9-4778-82c8-0bff97a98879", "Ziemniak", ingredient4, ingredient5);
		productRepo.save(product2);
		Product product3 = TestUtils.product("2af0b1a3-e2be-4e16-bc8b-b5ff4355b44a", "Surówka", ingredient6, ingredient7);
		productRepo.save(product3);

		Dish dish1 = TestUtils.dish("b9820ee0-c4b8-4ed8-90f7-7fa947f72548", 1, product1);
		dishRepo.save(dish1);
		Dish dish2 = TestUtils.dish("74e078d9-f49f-4c53-be67-f9f4cbe5804f", 3, product2);
		dishRepo.save(dish2);
		Dish dish3 = TestUtils.dish("0173a261-be83-4e9c-914f-eacfdaba46a8", 1, product3);
		dishRepo.save(dish3);

		MenuItem menuItem1 = TestUtils.menuItem("3deb5633-e968-4bdc-a114-cd4430a4f003", "Zestaw odbiadowy szefa kuchni", new BigDecimal("20.00"),
			VatTax._23, new BigDecimal("24.60"), restaurant, dish1, dish2, dish3);
		menuItemRepo.save(menuItem1);

		OrderItem orderItem = TestUtils.orderItem("1f4fa012-cd0b-4ae0-bbc0-040d73d75b6c", 2, menuItem1);
		orderItemRepo.save(orderItem);

		Order order = TestUtils.order(STR_UUID, null, "Poproszę bez panierki", user,
			deliverer, deliveryAddress, restaurant, new BigDecimal("40.00"), new BigDecimal("49.20"), new BigDecimal("49.20"),
			TestUtils.orderStatus("2020-01-01T15:00:00Z", true, null, null),
			orderItem
		);
		orderRepo.save(order);

		OrderStatusDTO orderStatusJson =
			TestUtils.orderStatusDTO("2020-01-01T15:00:00Z", true, "2020-01-01T15:30:00Z", null);
		orderController.patchIsGivedOut(UUID.fromString(STR_UUID), orderStatusJson);

		OrderDTO orderDB = orderService.getByUuid(UUID.fromString(STR_UUID)).orElseThrow();
		Assertions.assertEquals(orderDB.getOrderStatusDTO().getGiveOutTime(), orderStatusJson.getGiveOutTime());
	}

	// error when setting ordes as gived out when is not paid
	@Test
	@Transactional
	public void setIsGivedOut2() {
		Restaurant restaurant = TestUtils.restaurant("087c502c-559b-4d81-ae53-e67ab6fdf995", "MakJson",
			TestUtils.logginData("mcJson123", "bigDag123!"),
			TestUtils.companyData("MakJson sp. z zoo",
				TestUtils.address("St. Patric", "152", "00-000", "NY"),
				"123-010-00-11", "123123123", "+48 501 502 503", "MJ@gmail.com"), Archive.CURRENT);
		restaurantRepo.save(restaurant);

		Deliverer deliverer = TestUtils.deliverer("cb093f68-f4f1-4f21-a094-5850150b42dd",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer);

		User user = TestUtils.user("9986208e-961a-48d4-bf7a-112c627779c2", TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);

		DeliveryAddress deliveryAddress = TestUtils.deliveryAddress("6d66a218-f493-41ee-8291-a0065fab5cfe", null, "Jana Pawła", "1",
			"", "00-010", "Warszawa", null, "PL", null, user);
		deliveryAddressRepo.save(deliveryAddress);

		OperationEvidence operationEvidence = TestUtils.operationEvidence("2020-01-01T14:00:15Z", EvidenceType.DEPOSIT, new BigDecimal("50.00"), user);
		operationEvidenceRepo.save(operationEvidence);

		Ingredient ingredient1 = TestUtils.ingredient("ac30a0fe-f97b-4b08-aaac-1309c76c5455", "Kurczak", true);
		ingredientRepo.save(ingredient1);
		Ingredient ingredient2 = TestUtils.ingredient("5632de5e-5c00-463b-b701-152fb76daea3", "Mąka", false);
		ingredientRepo.save(ingredient2);
		Ingredient ingredient3 = TestUtils.ingredient("5a1e100b-b82d-42bb-b34f-fc1c3497f9c8", "Kukurydza", true);
		ingredientRepo.save(ingredient3);
		Ingredient ingredient4 = TestUtils.ingredient("6b1e5fdc-d771-423e-b968-46e5a03a8200", "Ziemniak", false);
		ingredientRepo.save(ingredient4);
		Ingredient ingredient5 = TestUtils.ingredient("e7cec9d4-cdf3-4b32-b2f4-9b90f41a8d05", "Masło", false);
		ingredientRepo.save(ingredient5);
		Ingredient ingredient6 = TestUtils.ingredient("dc0ff645-2596-4c7d-a6fa-6926db65be16", "Marchew", false);
		ingredientRepo.save(ingredient6);
		Ingredient ingredient7 = TestUtils.ingredient("8ede3563-edd0-4d8b-b121-dd21dd11b7b6", "Jabłko", false);
		ingredientRepo.save(ingredient7);

		Product product1 = TestUtils.product("19e1281b-949d-48c3-9486-c0abbf7a0267", "Udko korczaka", ingredient1, ingredient2, ingredient3);
		productRepo.save(product1);
		Product product2 = TestUtils.product("e192ce71-75a9-4778-82c8-0bff97a98879", "Ziemniak", ingredient4, ingredient5);
		productRepo.save(product2);
		Product product3 = TestUtils.product("2af0b1a3-e2be-4e16-bc8b-b5ff4355b44a", "Surówka", ingredient6, ingredient7);
		productRepo.save(product3);

		Dish dish1 = TestUtils.dish("b9820ee0-c4b8-4ed8-90f7-7fa947f72548", 1, product1);
		dishRepo.save(dish1);
		Dish dish2 = TestUtils.dish("74e078d9-f49f-4c53-be67-f9f4cbe5804f", 3, product2);
		dishRepo.save(dish2);
		Dish dish3 = TestUtils.dish("0173a261-be83-4e9c-914f-eacfdaba46a8", 1, product3);
		dishRepo.save(dish3);

		MenuItem menuItem1 = TestUtils.menuItem("3deb5633-e968-4bdc-a114-cd4430a4f003", "Zestaw odbiadowy szefa kuchni", new BigDecimal("20.00"),
			VatTax._23, new BigDecimal("24.60"), restaurant, dish1, dish2, dish3);
		menuItemRepo.save(menuItem1);

		OrderItem orderItem = TestUtils.orderItem("1f4fa012-cd0b-4ae0-bbc0-040d73d75b6c", 2, menuItem1);
		orderItemRepo.save(orderItem);

		Order order = TestUtils.order(STR_UUID, null, "Poproszę bez panierki", user,
			deliverer, deliveryAddress, restaurant, new BigDecimal("40.00"), new BigDecimal("49.20"), new BigDecimal("49.20"),
			TestUtils.orderStatus("2020-01-01T15:00:00Z", false, null, null),
			orderItem
		);
		orderRepo.save(order);

		OrderStatusDTO orderStatusJson =
			TestUtils.orderStatusDTO("2020-01-01T15:00:00Z", true, "2020-01-01T15:30:00Z", null);
		Assertions.assertThrows(ResponseStatusException.class, () -> orderController.patchIsGivedOut(UUID.fromString(STR_UUID), orderStatusJson));
	}

	// delivered
	@Test
	@Transactional
	public void setIsDelivered1() {
		Restaurant restaurant = TestUtils.restaurant("087c502c-559b-4d81-ae53-e67ab6fdf995", "MakJson",
			TestUtils.logginData("mcJson123", "bigDag123!"),
			TestUtils.companyData("MakJson sp. z zoo",
				TestUtils.address("St. Patric", "152", "00-000", "NY"),
				"123-010-00-11", "123123123", "+48 501 502 503", "MJ@gmail.com"), Archive.CURRENT);
		restaurantRepo.save(restaurant);

		Deliverer deliverer = TestUtils.deliverer("cb093f68-f4f1-4f21-a094-5850150b42dd",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer);

		User user = TestUtils.user("9986208e-961a-48d4-bf7a-112c627779c2", TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);

		DeliveryAddress deliveryAddress = TestUtils.deliveryAddress("6d66a218-f493-41ee-8291-a0065fab5cfe", null, "Jana Pawła", "1",
			"", "00-010", "Warszawa", null, "PL", null, user);
		deliveryAddressRepo.save(deliveryAddress);

		OperationEvidence operationEvidence = TestUtils.operationEvidence("2020-01-01T14:00:15Z", EvidenceType.DEPOSIT, new BigDecimal("50.00"), user);
		operationEvidenceRepo.save(operationEvidence);
		OperationEvidence operationEvidence2 = TestUtils.operationEvidence("2020-01-01T15:00:15Z", EvidenceType.PAYMENT, new BigDecimal("49.20"), user);
		operationEvidenceRepo.save(operationEvidence2);

		Ingredient ingredient1 = TestUtils.ingredient("ac30a0fe-f97b-4b08-aaac-1309c76c5455", "Kurczak", true);
		ingredientRepo.save(ingredient1);
		Ingredient ingredient2 = TestUtils.ingredient("5632de5e-5c00-463b-b701-152fb76daea3", "Mąka", false);
		ingredientRepo.save(ingredient2);
		Ingredient ingredient3 = TestUtils.ingredient("5a1e100b-b82d-42bb-b34f-fc1c3497f9c8", "Kukurydza", true);
		ingredientRepo.save(ingredient3);
		Ingredient ingredient4 = TestUtils.ingredient("6b1e5fdc-d771-423e-b968-46e5a03a8200", "Ziemniak", false);
		ingredientRepo.save(ingredient4);
		Ingredient ingredient5 = TestUtils.ingredient("e7cec9d4-cdf3-4b32-b2f4-9b90f41a8d05", "Masło", false);
		ingredientRepo.save(ingredient5);
		Ingredient ingredient6 = TestUtils.ingredient("dc0ff645-2596-4c7d-a6fa-6926db65be16", "Marchew", false);
		ingredientRepo.save(ingredient6);
		Ingredient ingredient7 = TestUtils.ingredient("8ede3563-edd0-4d8b-b121-dd21dd11b7b6", "Jabłko", false);
		ingredientRepo.save(ingredient7);

		Product product1 = TestUtils.product("19e1281b-949d-48c3-9486-c0abbf7a0267", "Udko korczaka", ingredient1, ingredient2, ingredient3);
		productRepo.save(product1);
		Product product2 = TestUtils.product("e192ce71-75a9-4778-82c8-0bff97a98879", "Ziemniak", ingredient4, ingredient5);
		productRepo.save(product2);
		Product product3 = TestUtils.product("2af0b1a3-e2be-4e16-bc8b-b5ff4355b44a", "Surówka", ingredient6, ingredient7);
		productRepo.save(product3);

		Dish dish1 = TestUtils.dish("b9820ee0-c4b8-4ed8-90f7-7fa947f72548", 1, product1);
		dishRepo.save(dish1);
		Dish dish2 = TestUtils.dish("74e078d9-f49f-4c53-be67-f9f4cbe5804f", 3, product2);
		dishRepo.save(dish2);
		Dish dish3 = TestUtils.dish("0173a261-be83-4e9c-914f-eacfdaba46a8", 1, product3);
		dishRepo.save(dish3);

		MenuItem menuItem1 = TestUtils.menuItem("3deb5633-e968-4bdc-a114-cd4430a4f003", "Zestaw odbiadowy szefa kuchni", new BigDecimal("20.00"),
			VatTax._23, new BigDecimal("24.60"), restaurant, dish1, dish2, dish3);
		menuItemRepo.save(menuItem1);

		OrderItem orderItem = TestUtils.orderItem("1f4fa012-cd0b-4ae0-bbc0-040d73d75b6c", 2, menuItem1);
		orderItemRepo.save(orderItem);

		Order order = TestUtils.order(STR_UUID, null, "Poproszę bez panierki", user,
			deliverer, deliveryAddress, restaurant, new BigDecimal("40.00"), new BigDecimal("49.20"), new BigDecimal("49.20"),
			TestUtils.orderStatus("2020-01-01T15:00:00Z", true, "2020-01-01T16:00:00Z", null),
			orderItem
		);
		orderRepo.save(order);

		OrderStatusDTO orderStatusJson =
			TestUtils.orderStatusDTO("2020-01-01T15:00:00Z", true, "2020-01-01T16:00:00Z", "2020-01-01T16:30:00Z");
		orderController.patchIsDelivered(UUID.fromString(STR_UUID), orderStatusJson);

		OrderDTO orderDB = orderService.getByUuid(UUID.fromString(STR_UUID)).orElseThrow();
		Assertions.assertEquals(orderDB.getOrderStatusDTO().getDeliveryTime(), orderStatusJson.getDeliveryTime());
	}

	// error when setting order as delivered when is not paid
	@Test
	@Transactional
	public void setIsDelivered2() {
		Restaurant restaurant = TestUtils.restaurant("087c502c-559b-4d81-ae53-e67ab6fdf995", "MakJson",
			TestUtils.logginData("mcJson123", "bigDag123!"),
			TestUtils.companyData("MakJson sp. z zoo",
				TestUtils.address("St. Patric", "152", "00-000", "NY"),
				"123-010-00-11", "123123123", "+48 501 502 503", "MJ@gmail.com"), Archive.CURRENT);
		restaurantRepo.save(restaurant);

		Deliverer deliverer = TestUtils.deliverer("cb093f68-f4f1-4f21-a094-5850150b42dd",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer);

		User user = TestUtils.user("9986208e-961a-48d4-bf7a-112c627779c2", TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);

		DeliveryAddress deliveryAddress = TestUtils.deliveryAddress("6d66a218-f493-41ee-8291-a0065fab5cfe", null, "Jana Pawła", "1",
			"", "00-010", "Warszawa", null, "PL", null, user);
		deliveryAddressRepo.save(deliveryAddress);

		OperationEvidence operationEvidence = TestUtils.operationEvidence("2020-01-01T14:00:15Z", EvidenceType.DEPOSIT, new BigDecimal("50.00"), user);
		operationEvidenceRepo.save(operationEvidence);

		Ingredient ingredient1 = TestUtils.ingredient("ac30a0fe-f97b-4b08-aaac-1309c76c5455", "Kurczak", true);
		ingredientRepo.save(ingredient1);
		Ingredient ingredient2 = TestUtils.ingredient("5632de5e-5c00-463b-b701-152fb76daea3", "Mąka", false);
		ingredientRepo.save(ingredient2);
		Ingredient ingredient3 = TestUtils.ingredient("5a1e100b-b82d-42bb-b34f-fc1c3497f9c8", "Kukurydza", true);
		ingredientRepo.save(ingredient3);
		Ingredient ingredient4 = TestUtils.ingredient("6b1e5fdc-d771-423e-b968-46e5a03a8200", "Ziemniak", false);
		ingredientRepo.save(ingredient4);
		Ingredient ingredient5 = TestUtils.ingredient("e7cec9d4-cdf3-4b32-b2f4-9b90f41a8d05", "Masło", false);
		ingredientRepo.save(ingredient5);
		Ingredient ingredient6 = TestUtils.ingredient("dc0ff645-2596-4c7d-a6fa-6926db65be16", "Marchew", false);
		ingredientRepo.save(ingredient6);
		Ingredient ingredient7 = TestUtils.ingredient("8ede3563-edd0-4d8b-b121-dd21dd11b7b6", "Jabłko", false);
		ingredientRepo.save(ingredient7);

		Product product1 = TestUtils.product("19e1281b-949d-48c3-9486-c0abbf7a0267", "Udko korczaka", ingredient1, ingredient2, ingredient3);
		productRepo.save(product1);
		Product product2 = TestUtils.product("e192ce71-75a9-4778-82c8-0bff97a98879", "Ziemniak", ingredient4, ingredient5);
		productRepo.save(product2);
		Product product3 = TestUtils.product("2af0b1a3-e2be-4e16-bc8b-b5ff4355b44a", "Surówka", ingredient6, ingredient7);
		productRepo.save(product3);

		Dish dish1 = TestUtils.dish("b9820ee0-c4b8-4ed8-90f7-7fa947f72548", 1, product1);
		dishRepo.save(dish1);
		Dish dish2 = TestUtils.dish("74e078d9-f49f-4c53-be67-f9f4cbe5804f", 3, product2);
		dishRepo.save(dish2);
		Dish dish3 = TestUtils.dish("0173a261-be83-4e9c-914f-eacfdaba46a8", 1, product3);
		dishRepo.save(dish3);

		MenuItem menuItem1 = TestUtils.menuItem("3deb5633-e968-4bdc-a114-cd4430a4f003", "Zestaw odbiadowy szefa kuchni", new BigDecimal("20.00"),
			VatTax._23, new BigDecimal("24.60"), restaurant, dish1, dish2, dish3);
		menuItemRepo.save(menuItem1);

		OrderItem orderItem = TestUtils.orderItem("1f4fa012-cd0b-4ae0-bbc0-040d73d75b6c", 2, menuItem1);
		orderItemRepo.save(orderItem);

		Order order = TestUtils.order(STR_UUID, null, "Poproszę bez panierki", user,
			deliverer, deliveryAddress, restaurant, new BigDecimal("40.00"), new BigDecimal("49.20"), new BigDecimal("49.20"),
			TestUtils.orderStatus("2020-01-01T15:00:00Z", false, null, null),
			orderItem
		);
		orderRepo.save(order);

		OrderStatusDTO orderStatusJson =
			TestUtils.orderStatusDTO("2020-01-01T15:00:00Z", true, "2020-01-01T16:00:00Z", "2020-01-01T16:30:00Z");
		Assertions.assertThrows(ResponseStatusException.class, () -> orderController.patchIsDelivered(UUID.fromString(STR_UUID), orderStatusJson));
	}

	// error when setting order as delivered when is not gived out
	@Test
	@Transactional
	public void setIsDelivered3() {
		Restaurant restaurant = TestUtils.restaurant("087c502c-559b-4d81-ae53-e67ab6fdf995", "MakJson",
			TestUtils.logginData("mcJson123", "bigDag123!"),
			TestUtils.companyData("MakJson sp. z zoo",
				TestUtils.address("St. Patric", "152", "00-000", "NY"),
				"123-010-00-11", "123123123", "+48 501 502 503", "MJ@gmail.com"), Archive.CURRENT);
		restaurantRepo.save(restaurant);

		Deliverer deliverer = TestUtils.deliverer("cb093f68-f4f1-4f21-a094-5850150b42dd",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer);

		User user = TestUtils.user("9986208e-961a-48d4-bf7a-112c627779c2", TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);

		DeliveryAddress deliveryAddress = TestUtils.deliveryAddress("6d66a218-f493-41ee-8291-a0065fab5cfe", null, "Jana Pawła", "1",
			"", "00-010", "Warszawa", null, "PL", null, user);
		deliveryAddressRepo.save(deliveryAddress);

		OperationEvidence operationEvidence = TestUtils.operationEvidence("2020-01-01T14:00:15Z", EvidenceType.DEPOSIT, new BigDecimal("50.00"), user);
		operationEvidenceRepo.save(operationEvidence);
		OperationEvidence operationEvidence2 = TestUtils.operationEvidence("2020-01-01T15:00:15Z", EvidenceType.PAYMENT, new BigDecimal("49.20"), user);
		operationEvidenceRepo.save(operationEvidence2);

		Ingredient ingredient1 = TestUtils.ingredient("ac30a0fe-f97b-4b08-aaac-1309c76c5455", "Kurczak", true);
		ingredientRepo.save(ingredient1);
		Ingredient ingredient2 = TestUtils.ingredient("5632de5e-5c00-463b-b701-152fb76daea3", "Mąka", false);
		ingredientRepo.save(ingredient2);
		Ingredient ingredient3 = TestUtils.ingredient("5a1e100b-b82d-42bb-b34f-fc1c3497f9c8", "Kukurydza", true);
		ingredientRepo.save(ingredient3);
		Ingredient ingredient4 = TestUtils.ingredient("6b1e5fdc-d771-423e-b968-46e5a03a8200", "Ziemniak", false);
		ingredientRepo.save(ingredient4);
		Ingredient ingredient5 = TestUtils.ingredient("e7cec9d4-cdf3-4b32-b2f4-9b90f41a8d05", "Masło", false);
		ingredientRepo.save(ingredient5);
		Ingredient ingredient6 = TestUtils.ingredient("dc0ff645-2596-4c7d-a6fa-6926db65be16", "Marchew", false);
		ingredientRepo.save(ingredient6);
		Ingredient ingredient7 = TestUtils.ingredient("8ede3563-edd0-4d8b-b121-dd21dd11b7b6", "Jabłko", false);
		ingredientRepo.save(ingredient7);

		Product product1 = TestUtils.product("19e1281b-949d-48c3-9486-c0abbf7a0267", "Udko korczaka", ingredient1, ingredient2, ingredient3);
		productRepo.save(product1);
		Product product2 = TestUtils.product("e192ce71-75a9-4778-82c8-0bff97a98879", "Ziemniak", ingredient4, ingredient5);
		productRepo.save(product2);
		Product product3 = TestUtils.product("2af0b1a3-e2be-4e16-bc8b-b5ff4355b44a", "Surówka", ingredient6, ingredient7);
		productRepo.save(product3);

		Dish dish1 = TestUtils.dish("b9820ee0-c4b8-4ed8-90f7-7fa947f72548", 1, product1);
		dishRepo.save(dish1);
		Dish dish2 = TestUtils.dish("74e078d9-f49f-4c53-be67-f9f4cbe5804f", 3, product2);
		dishRepo.save(dish2);
		Dish dish3 = TestUtils.dish("0173a261-be83-4e9c-914f-eacfdaba46a8", 1, product3);
		dishRepo.save(dish3);

		MenuItem menuItem1 = TestUtils.menuItem("3deb5633-e968-4bdc-a114-cd4430a4f003", "Zestaw odbiadowy szefa kuchni", new BigDecimal("20.00"),
			VatTax._23, new BigDecimal("24.60"), restaurant, dish1, dish2, dish3);
		menuItemRepo.save(menuItem1);

		OrderItem orderItem = TestUtils.orderItem("1f4fa012-cd0b-4ae0-bbc0-040d73d75b6c", 2, menuItem1);
		orderItemRepo.save(orderItem);

		Order order = TestUtils.order(STR_UUID, null, "Poproszę bez panierki", user,
			deliverer, deliveryAddress, restaurant, new BigDecimal("40.00"), new BigDecimal("49.20"), new BigDecimal("49.20"),
			TestUtils.orderStatus("2020-01-01T15:00:00Z", true, null, null),
			orderItem
		);
		orderRepo.save(order);

		OrderStatusDTO orderStatusJson =
			TestUtils.orderStatusDTO("2020-01-01T15:00:00Z", true, "2020-01-01T16:00:00Z", "2020-01-01T16:30:00Z");
		Assertions.assertThrows(ResponseStatusException.class, () -> orderController.patchIsDelivered(UUID.fromString(STR_UUID), orderStatusJson));
	}

	// delete not paid order
	@Test
	@Transactional
	public void delete1() {
		TransactionStatus status1 = txManager.getTransaction(TransactionDefinition.withDefaults());
		Restaurant restaurant = TestUtils.restaurant("087c502c-559b-4d81-ae53-e67ab6fdf995", "MakJson",
			TestUtils.logginData("mcJson123", "bigDag123!"),
			TestUtils.companyData("MakJson sp. z zoo",
				TestUtils.address("St. Patric", "152", "00-000", "NY"),
				"123-010-00-11", "123123123", "+48 501 502 503", "MJ@gmail.com"), Archive.CURRENT);
		restaurantRepo.save(restaurant);

		Deliverer deliverer1 = TestUtils.deliverer("cb093f68-f4f1-4f21-a094-5850150b42dd",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer1);
		Deliverer deliverer2 = TestUtils.deliverer("9f15cfc3-2568-4fe3-8f94-034a23c9df6f",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith3", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer2);

		User user = TestUtils.user("9986208e-961a-48d4-bf7a-112c627779c2", TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);

		DeliveryAddress deliveryAddress = TestUtils.deliveryAddress("6d66a218-f493-41ee-8291-a0065fab5cfe", null, "Jana Pawła", "1",
			"", "00-010", "Warszawa", null, "PL", null, user);
		deliveryAddressRepo.save(deliveryAddress);

		Ingredient ingredient1 = TestUtils.ingredient("ac30a0fe-f97b-4b08-aaac-1309c76c5455", "Kurczak", true);
		ingredientRepo.save(ingredient1);
		Ingredient ingredient2 = TestUtils.ingredient("5632de5e-5c00-463b-b701-152fb76daea3", "Mąka", false);
		ingredientRepo.save(ingredient2);
		Ingredient ingredient3 = TestUtils.ingredient("5a1e100b-b82d-42bb-b34f-fc1c3497f9c8", "Kukurydza", true);
		ingredientRepo.save(ingredient3);
		Ingredient ingredient4 = TestUtils.ingredient("6b1e5fdc-d771-423e-b968-46e5a03a8200", "Ziemniak", false);
		ingredientRepo.save(ingredient4);
		Ingredient ingredient5 = TestUtils.ingredient("e7cec9d4-cdf3-4b32-b2f4-9b90f41a8d05", "Masło", false);
		ingredientRepo.save(ingredient5);
		Ingredient ingredient6 = TestUtils.ingredient("dc0ff645-2596-4c7d-a6fa-6926db65be16", "Marchew", false);
		ingredientRepo.save(ingredient6);
		Ingredient ingredient7 = TestUtils.ingredient("8ede3563-edd0-4d8b-b121-dd21dd11b7b6", "Jabłko", false);
		ingredientRepo.save(ingredient7);

		Product product1 = TestUtils.product("19e1281b-949d-48c3-9486-c0abbf7a0267", "Udko korczaka", ingredient1, ingredient2, ingredient3);
		productRepo.save(product1);
		Product product2 = TestUtils.product("e192ce71-75a9-4778-82c8-0bff97a98879", "Ziemniak", ingredient4, ingredient5);
		productRepo.save(product2);
		Product product3 = TestUtils.product("2af0b1a3-e2be-4e16-bc8b-b5ff4355b44a", "Surówka", ingredient6, ingredient7);
		productRepo.save(product3);

		Dish dish1 = TestUtils.dish("b9820ee0-c4b8-4ed8-90f7-7fa947f72548", 1, product1);
		dishRepo.save(dish1);
		Dish dish2 = TestUtils.dish("74e078d9-f49f-4c53-be67-f9f4cbe5804f", 3, product2);
		dishRepo.save(dish2);
		Dish dish3 = TestUtils.dish("0173a261-be83-4e9c-914f-eacfdaba46a8", 1, product3);
		dishRepo.save(dish3);

		MenuItem menuItem1 = TestUtils.menuItem("3deb5633-e968-4bdc-a114-cd4430a4f003", "Zestaw odbiadowy szefa kuchni", new BigDecimal("20.00"),
			VatTax._23, new BigDecimal("24.60"), restaurant, dish1, dish2, dish3);
		menuItemRepo.save(menuItem1);

		MenuItem menuItem2 = TestUtils.menuItem("44b2cd38-69eb-4178-b271-0bdef113eeea", "Powiększenie zestawu", new BigDecimal("8.00"),
			VatTax._23, new BigDecimal("9.84"), restaurant, dish1);
		menuItemRepo.save(menuItem2);

		OrderItem orderItem = TestUtils.orderItem("1f4fa012-cd0b-4ae0-bbc0-040d73d75b6c", 2, menuItem1);
		orderItemRepo.save(orderItem);

		Order order = TestUtils.order(STR_UUID, null, "Poproszę bez panierki", user,
			deliverer1, deliveryAddress, restaurant, new BigDecimal("40.00"), new BigDecimal("49.20"), new BigDecimal("49.20"),
			TestUtils.orderStatus("2020-01-01T15:00:00Z", false, null, null),
			orderItem
		);
		orderRepo.save(order);
		txManager.commit(status1);

		TransactionStatus status2 = txManager.getTransaction(TransactionDefinition.withDefaults());
		orderController.delete(UUID.fromString(STR_UUID));
		txManager.commit(status2);

		TransactionStatus status3 = txManager.getTransaction(TransactionDefinition.withDefaults());
		Truth8.assertThat(orderService.getByUuid(UUID.fromString(STR_UUID))).isEmpty();
		txManager.commit(status3);
	}

	// error when delete paid order
	@Test
	@Transactional
	public void delete2() {
		TransactionStatus status1 = txManager.getTransaction(TransactionDefinition.withDefaults());
		Restaurant restaurant = TestUtils.restaurant("087c502c-559b-4d81-ae53-e67ab6fdf995", "MakJson",
			TestUtils.logginData("mcJson123", "bigDag123!"),
			TestUtils.companyData("MakJson sp. z zoo",
				TestUtils.address("St. Patric", "152", "00-000", "NY"),
				"123-010-00-11", "123123123", "+48 501 502 503", "MJ@gmail.com"), Archive.CURRENT);
		restaurantRepo.save(restaurant);

		Deliverer deliverer1 = TestUtils.deliverer("cb093f68-f4f1-4f21-a094-5850150b42dd",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer1);
		Deliverer deliverer2 = TestUtils.deliverer("9f15cfc3-2568-4fe3-8f94-034a23c9df6f",
			TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"),
			TestUtils.logginData("jSmith3", "I@mIronM@n12"), Archive.CURRENT);
		delivererRepo.save(deliverer2);

		User user = TestUtils.user("9986208e-961a-48d4-bf7a-112c627779c2", TestUtils.personalData("John", "Smith", Sex.MALE, "501-501-501", "jh512@gmail.com"), null,
			TestUtils.logginData("jSmith1", "I@mIronM@n121"), Archive.CURRENT);
		userRepo.save(user);

		DeliveryAddress deliveryAddress = TestUtils.deliveryAddress("6d66a218-f493-41ee-8291-a0065fab5cfe", null, "Jana Pawła", "1",
			"", "00-010", "Warszawa", null, "PL", null, user);
		deliveryAddressRepo.save(deliveryAddress);

		Ingredient ingredient1 = TestUtils.ingredient("ac30a0fe-f97b-4b08-aaac-1309c76c5455", "Kurczak", true);
		ingredientRepo.save(ingredient1);
		Ingredient ingredient2 = TestUtils.ingredient("5632de5e-5c00-463b-b701-152fb76daea3", "Mąka", false);
		ingredientRepo.save(ingredient2);
		Ingredient ingredient3 = TestUtils.ingredient("5a1e100b-b82d-42bb-b34f-fc1c3497f9c8", "Kukurydza", true);
		ingredientRepo.save(ingredient3);
		Ingredient ingredient4 = TestUtils.ingredient("6b1e5fdc-d771-423e-b968-46e5a03a8200", "Ziemniak", false);
		ingredientRepo.save(ingredient4);
		Ingredient ingredient5 = TestUtils.ingredient("e7cec9d4-cdf3-4b32-b2f4-9b90f41a8d05", "Masło", false);
		ingredientRepo.save(ingredient5);
		Ingredient ingredient6 = TestUtils.ingredient("dc0ff645-2596-4c7d-a6fa-6926db65be16", "Marchew", false);
		ingredientRepo.save(ingredient6);
		Ingredient ingredient7 = TestUtils.ingredient("8ede3563-edd0-4d8b-b121-dd21dd11b7b6", "Jabłko", false);
		ingredientRepo.save(ingredient7);

		Product product1 = TestUtils.product("19e1281b-949d-48c3-9486-c0abbf7a0267", "Udko korczaka", ingredient1, ingredient2, ingredient3);
		productRepo.save(product1);
		Product product2 = TestUtils.product("e192ce71-75a9-4778-82c8-0bff97a98879", "Ziemniak", ingredient4, ingredient5);
		productRepo.save(product2);
		Product product3 = TestUtils.product("2af0b1a3-e2be-4e16-bc8b-b5ff4355b44a", "Surówka", ingredient6, ingredient7);
		productRepo.save(product3);

		Dish dish1 = TestUtils.dish("b9820ee0-c4b8-4ed8-90f7-7fa947f72548", 1, product1);
		dishRepo.save(dish1);
		Dish dish2 = TestUtils.dish("74e078d9-f49f-4c53-be67-f9f4cbe5804f", 3, product2);
		dishRepo.save(dish2);
		Dish dish3 = TestUtils.dish("0173a261-be83-4e9c-914f-eacfdaba46a8", 1, product3);
		dishRepo.save(dish3);

		MenuItem menuItem1 = TestUtils.menuItem("3deb5633-e968-4bdc-a114-cd4430a4f003", "Zestaw odbiadowy szefa kuchni", new BigDecimal("20.00"),
			VatTax._23, new BigDecimal("24.60"), restaurant, dish1, dish2, dish3);
		menuItemRepo.save(menuItem1);

		MenuItem menuItem2 = TestUtils.menuItem("44b2cd38-69eb-4178-b271-0bdef113eeea", "Powiększenie zestawu", new BigDecimal("8.00"),
			VatTax._23, new BigDecimal("9.84"), restaurant, dish1);
		menuItemRepo.save(menuItem2);

		OrderItem orderItem = TestUtils.orderItem("1f4fa012-cd0b-4ae0-bbc0-040d73d75b6c", 2, menuItem1);
		orderItemRepo.save(orderItem);

		Order order = TestUtils.order(STR_UUID, null, "Poproszę bez panierki", user,
			deliverer1, deliveryAddress, restaurant, new BigDecimal("40.00"), new BigDecimal("49.20"), new BigDecimal("49.20"),
			TestUtils.orderStatus("2020-01-01T15:00:00Z", true, null, null),
			orderItem
		);
		orderRepo.save(order);
		txManager.commit(status1);

		TransactionStatus status2 = txManager.getTransaction(TransactionDefinition.withDefaults());
		Assertions.assertThrows(ResponseStatusException.class, () -> orderController.delete(UUID.fromString(STR_UUID)));
		txManager.commit(status2);
	}
}
