package com.patzam.elunchapp.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PeriodTimeConstraintValidator.class)
@Documented
public @interface PeriodTimeConstraint {
	String message() default "{com.patzam.elunchapp.validator.PeriodTimeConstraint}";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
