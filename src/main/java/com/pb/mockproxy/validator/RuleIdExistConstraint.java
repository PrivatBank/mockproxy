package com.pb.mockproxy.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RuleIdExistConstraintValidator.class)
public @interface RuleIdExistConstraint {

    String message() default "RuleId is not valid";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
