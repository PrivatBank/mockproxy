package com.pb.mockproxy.validator;

import com.pb.mockproxy.repository.project.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ProjectIdExistConstraintValidator implements ConstraintValidator<ProjectIdExistConstraint, String> {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public boolean isValid(String projectId, ConstraintValidatorContext constraintValidatorContext) {
        return projectId == null || projectRepository.get(projectId) != null;
    }
}
