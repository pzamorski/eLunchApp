package com.patzam.elunchapp.model;

import net.karneim.pojobuilder.GeneratePojoBuilder;

import javax.annotation.Nullable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@GeneratePojoBuilder
@Entity
@DiscriminatorValue("deliverer")
public class Deliverer extends Employee {

	@Nullable
	@OneToMany(mappedBy = "deliverer")
	private List<Order> orders;


	@Nullable
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(@Nullable List<Order> orderDTOS) {
		this.orders = orderDTOS;
	}
}
