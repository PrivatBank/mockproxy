package com.pb.mockproxy.validator;

import com.pb.mockproxy.repository.rule.HttpMethods;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class HttpMethodsConstraintValidator implements ConstraintValidator<HttpMethodsConstraint, String> {

    private List<String> allowedMethods = Arrays.asList(HttpMethods.GET, HttpMethods.POST, HttpMethods.PUT, HttpMethods.DELETE);

    @Override
    public boolean isValid(String method, ConstraintValidatorContext constraintValidatorContext) {
        return method == null || allowedMethods.contains(method);
    }
}
