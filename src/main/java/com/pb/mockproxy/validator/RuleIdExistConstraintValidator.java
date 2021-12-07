package com.pb.mockproxy.validator;

import com.pb.mockproxy.repository.rule.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RuleIdExistConstraintValidator implements ConstraintValidator<RuleIdExistConstraint, String> {

    @Autowired
    private RuleRepository ruleRepository;

    @Override
    public boolean isValid(String ruleId, ConstraintValidatorContext constraintValidatorContext) {
        return ruleId == null || ruleRepository.get(ruleId) != null;
    }
}
