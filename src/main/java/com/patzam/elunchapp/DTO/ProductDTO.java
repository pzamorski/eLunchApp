package com.patzam.elunchapp.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@GeneratePojoBuilder
public class ProductDTO {
	public static class View {
		public interface Basic {}
		public interface Extended extends Basic {}
	}

	@JsonView(View.Basic.class)
	@NotNull
	private UUID uuid;

	@JsonView(View.Basic.class)
	@NotBlank
	private String name;

	@JsonView(View.Extended.class)
	@NotNull
	private List<IngredientDTO> ingredientDTOS;

	@JsonView(View.Extended.class)
	@Nullable
	private DishDTO dishDTO;


	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IngredientDTO> getIngredients() {
		return ingredientDTOS;
	}

	public void setIngredients(List<IngredientDTO> ingredientDTOS) {
		this.ingredientDTOS = ingredientDTOS;
	}

	@Nullable
	public DishDTO getDish() {
		return dishDTO;
	}

	public void setDish(@Nullable DishDTO dishDTO) {
		this.dishDTO = dishDTO;
	}
}
