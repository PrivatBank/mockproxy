package com.pb.mockproxy.config;

import com.pb.mockproxy.filter.RayIdLoggingFilter;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Add or remove custom attributes to {@link DefaultErrorAttributes}
 * witch uses Spring Boot {@link org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController}.
 *
 * This need to create custom response when errors happens.
 */
@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {

        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Object rayId = webRequest.getAttribute(
                RayIdLoggingFilter.RAY_ID_REQUEST_ATTR_KEY,
                RequestAttributes.SCOPE_REQUEST
        );

        final Throwable error = super.getError(webRequest);

        if (error instanceof MethodArgumentNotValidException) {
            final MethodArgumentNotValidException argumentNotValidException = (MethodArgumentNotValidException) error;
            errorAttributes.put("errors", argumentNotValidException.getBindingResult().getAllErrors().stream()
                    .map(e -> e.getDefaultMessage()).collect(Collectors.toList()));
            errorAttributes.remove("trace");

        } else if (error instanceof ConstraintViolationException) {
            errorAttributes.remove("trace");
        }

        if (rayId != null) {
            errorAttributes.put("rayId", rayId);
        }
        return errorAttributes;
    }
}
