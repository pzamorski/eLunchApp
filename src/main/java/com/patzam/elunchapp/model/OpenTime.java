package com.patzam.elunchapp.model;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import com.patzam.elunchapp.model.enums.DayOfWeek;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@GeneratePojoBuilder
@Entity
public class OpenTime {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	@NotNull
	private UUID uuid;

	@NotNull
	@Enumerated(EnumType.STRING)
	private DayOfWeek dayOfWeek;

	@NotNull
	@Embedded
	private PeriodTime periodTime;

	@NotNull
	@ManyToOne
	private Restaurant restaurant;


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

	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public PeriodTime getPeriodTime() {
		return periodTime;
	}

	public void setPeriodTime(PeriodTime periodTime) {
		this.periodTime = periodTime;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
}
