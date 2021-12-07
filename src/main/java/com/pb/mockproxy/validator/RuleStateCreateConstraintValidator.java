package com.pb.mockproxy.validator;

import com.pb.mockproxy.repository.rule.RuleState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class RuleStateCreateConstraintValidator implements ConstraintValidator<RuleStateCreateConstraint, String> {

    private List<String> allowedStates = Arrays.asList(RuleState.ACTIVE);

    @Override
    public boolean isValid(String state, ConstraintValidatorContext constraintValidatorContext) {
        return state == null || allowedStates.contains(state);
    }
}
