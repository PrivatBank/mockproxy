package com.pb.mockproxy.validator;

import com.pb.mockproxy.repository.rule.RuleState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class RuleStateUpdateConstraintValidator implements ConstraintValidator<RuleStateUpdateConstraint, String> {

    private List<String> allowedStates = Arrays.asList(RuleState.ACTIVE, RuleState.DISABLED, RuleState.DELETED);

    @Override
    public boolean isValid(String state, ConstraintValidatorContext constraintValidatorContext) {
        return state == null || allowedStates.contains(state);
    }
}
