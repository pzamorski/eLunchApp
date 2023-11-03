package com.patzam.elunchapp.DTO;

import com.fasterxml.jackson.annotation.JsonView;
import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;

@GeneratePojoBuilder
@Embeddable
public class CompanyDataDTO {
	public static class View {
		public interface Basic {}
		public interface Extended extends Basic {}
	}

	@JsonView(View.Basic.class)
	@NotNull
	private String name;

	@JsonView(View.Extended.class)
	@Embedded
	@NotNull
	private AddressDTO addressDTO;

	@JsonView(View.Extended.class)
	@NotNull
	private String NIP;

	@JsonView(View.Extended.class)
	@NotNull
	private String REGON;

	@JsonView(View.Extended.class)
	@NotNull
	private String phone;

	@JsonView(View.Extended.class)
	@NotNull
	private String email;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AddressDTO getAddress() {
		return addressDTO;
	}

	public void setAddress(AddressDTO addressDTO) {
		this.addressDTO = addressDTO;
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
