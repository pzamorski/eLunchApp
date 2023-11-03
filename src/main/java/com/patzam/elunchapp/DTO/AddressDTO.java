package com.patzam.elunchapp.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.annotation.Nullable;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@GeneratePojoBuilder
@Embeddable
public class AddressDTO {
	public static class View {
		public interface Basic {}
		public interface Extended extends Basic {}
	}

	@JsonView(View.Basic.class)
	@NotNull
	private String street;

	@JsonView(View.Basic.class)
	@NotNull
	private String streetNumber;

	@JsonView(View.Basic.class)
	@NotNull
	private String localNumber;

	@JsonView(View.Basic.class)
	@NotNull
	private String postcode;

	@JsonView(View.Basic.class)
	@NotNull
	private String city;

	@JsonView(View.Extended.class)
	@Nullable
	private String borough;

	@JsonView(View.Extended.class)
	@Nullable
	private String county;

	@JsonView(View.Extended.class)
	@Nullable
	private String state;


	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getLocalNumber() {
		return localNumber;
	}

	public void setLocalNumber(String localNumber) {
		this.localNumber = localNumber;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Nullable
	public String getBorough() {
		return borough;
	}

	public void setBorough(@Nullable String borough) {
		this.borough = borough;
	}

	@Nullable
	public String getCounty() {
		return county;
	}

	public void setCounty(@Nullable String county) {
		this.county = county;
	}

	@Nullable
	public String getState() {
		return state;
	}

	public void setState(@Nullable String state) {
		this.state = state;
	}
}
