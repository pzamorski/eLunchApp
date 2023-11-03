package com.patzam.elunchapp.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.UUID;

@GeneratePojoBuilder
public class DishDTO {
	public static class View {
		public interface Id {}
		public interface Basic extends Id {}
		public interface Extended extends Basic {}
	}
	public interface DataUpdateValidation {}

	@JsonView(View.Id.class)
	@NotNull
	private UUID uuid;

	@JsonView(View.Extended.class)
	@NotNull
	@Min(1)
	private Integer quantity;

	@JsonView(View.Extended.class)
	@NotNull
	private ProductDTO productDTO;

	@JsonView(View.Extended.class)
	@Nullable
	@Null(groups = DataUpdateValidation.class)
	private List<MenuItemDTO> menuItemDTOS;


	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public ProductDTO getProduct() {
		return productDTO;
	}

	public void setProduct(ProductDTO productDTO) {
		this.productDTO = productDTO;
	}

	@Nullable
	public List<MenuItemDTO> getMenuItems() {
		return menuItemDTOS;
	}

	public void setMenuItems(@Nullable List<MenuItemDTO> menuItemDTOS) {
		this.menuItemDTOS = menuItemDTOS;
	}
}
