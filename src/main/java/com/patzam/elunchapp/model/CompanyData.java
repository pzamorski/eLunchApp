package com.patzam.elunchapp.model;

import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;

@GeneratePojoBuilder
@Embeddable
public class CompanyData {

	@NotNull
	@Column(name = "companyName")
	private String name;

	@Embedded
	@NotNull
	private Address address;

	@NotNull
	private String NIP;

	@NotNull
	private String REGON;

	@NotNull
	private String phone;

	@NotNull
	private String email;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getNIP() {
		return NIP;
	}

	public void setNIP(String NIP) {
		this.NIP = NIP;
	}

	public String getREGON() {
		return REGON;
	}

	public void setREGON(String REGON) {
		this.REGON = REGON;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
