package com.patzam.elunchapp.model;

import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@GeneratePojoBuilder
@Entity
public class DeliveryAddress {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	@NotNull
	private UUID uuid;

	@Nullable
	private String description;

	@NotNull
	private String street;

	@NotNull
	private String streetNumber;

	@NotNull
	private String localNumber;

	@NotNull
	private String postcode;

	@NotNull
	private String city;

	@Nullable
	private String borough;

	@Nullable
	private String county;

	@Nullable
	private String state;

	@NotNull
	@ManyToOne
	private User user;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Nullable
	public String getDescription() {
		return description;
	}

	public void setDescription(@Nullable String description) {
		this.description = description;
	}

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
