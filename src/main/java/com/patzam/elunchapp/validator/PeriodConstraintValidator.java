package com.patzam.elunchapp.validator;

import com.patzam.elunchapp.model.Period;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PeriodConstraintValidator implements ConstraintValidator<PeriodConstraint, Period> {
	@Override
	public void initialize(PeriodConstraint constraintAnnotation) {
	}

	@Override
	public boolean isValid(Period period, ConstraintValidatorContext context) {
		try {
			return period.getBegin() == null || period.getEnd() == null || period.getBegin().isBefore(period.getEnd());
		} catch (Exception e) {
			return false;
		}
	}
}
