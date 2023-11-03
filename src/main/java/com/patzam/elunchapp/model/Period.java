package com.patzam.elunchapp.model;

import net.karneim.pojobuilder.GeneratePojoBuilder;
import com.patzam.elunchapp.validator.PeriodConstraint;

import javax.annotation.Nullable;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@GeneratePojoBuilder
@PeriodConstraint
@Embeddable
public class Period {

	@Nullable
	private LocalDateTime begin;

	@Nullable
	private LocalDateTime end;


	@Nullable
	public LocalDateTime getBegin() {
		return begin;
	}

	public void setBegin(@Nullable LocalDateTime begin) {
		this.begin = begin;
	}

	@Nullable
	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(@Nullable LocalDateTime end) {
		this.end = end;
	}
}
