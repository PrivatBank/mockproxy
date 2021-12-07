package com.pb.mockproxy.validator;

import com.pb.mockproxy.repository.project.ProjectState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ProjectStateCreateConstraintValidator implements ConstraintValidator<ProjectStateCreateConstraint, String> {

    private List<String> allowedStates = Arrays.asList(ProjectState.ACTIVE);

    @Override
    public boolean isValid(String state, ConstraintValidatorContext constraintValidatorContext) {
        return state == null || allowedStates.contains(state);
    }
}
