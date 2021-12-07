package com.pb.mockproxy.validator;

import com.pb.mockproxy.repository.project.ProjectState;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ProjectStateUpdateConstraintValidator implements ConstraintValidator<ProjectStateUpdateConstraint, String> {

    private List<String> allowedStates = Arrays.asList(ProjectState.ACTIVE, ProjectState.DELETED);

    @Override
    public boolean isValid(String state, ConstraintValidatorContext constraintValidatorContext) {
        return state == null || allowedStates.contains(state);
    }
}
