package com.patzam.elunchapp.model;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import com.patzam.elunchapp.validator.PeriodTimeConstraint;

import javax.annotation.Nullable;
import javax.persistence.Embeddable;
import java.time.LocalTime;

@GeneratePojoBuilder
@PeriodTimeConstraint
@Embeddable
public class PeriodTime {

	@Nullable
	private LocalTime begin;

	@Nullable
	private LocalTime end;


	@Nullable
	public LocalTime getBegin() {
		return begin;
	}

	public void setBegin(@Nullable LocalTime begin) {
		this.begin = begin;
	}

	@Nullable
	public LocalTime getEnd() {
		return end;
	}

	public void setEnd(@Nullable LocalTime end) {
		this.end = end;
	}
}
