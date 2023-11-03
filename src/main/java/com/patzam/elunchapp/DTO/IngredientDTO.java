package com.patzam.elunchapp.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@GeneratePojoBuilder
public class IngredientDTO {
	public static class View {
		public interface Basic {}
	}

	@JsonView(View.Basic.class)
	@NotNull
	private UUID uuid;

	@JsonView(View.Basic.class)
	@NotBlank
	private String name;

	@JsonView(View.Basic.class)
	@NotNull
	private Boolean isAllergen;


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

	public Boolean getIsAllergen() {
		return isAllergen;
	}

	public void setAllergen(Boolean allergen) {
		isAllergen = allergen;
	}
}
