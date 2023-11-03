package com.patzam.elunchapp.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@GeneratePojoBuilder
public class OrderItemDTO {
	public static class View {
		public interface Basic {}
		public interface Extended extends Basic {}
	}

	@JsonView(View.Basic.class)
	@NotNull
	private UUID uuid;

	@JsonView(View.Extended.class)
	@NotNull
	@Min(1)
	private Integer quantity;

	@JsonView(View.Extended.class)
	@NotNull
	private MenuItemDTO menuItemDTO;


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

	public MenuItemDTO getMenuItem() {
		return menuItemDTO;
	}

	public void setMenuItem(MenuItemDTO menuItemDTO) {
		this.menuItemDTO = menuItemDTO;
	}
}
