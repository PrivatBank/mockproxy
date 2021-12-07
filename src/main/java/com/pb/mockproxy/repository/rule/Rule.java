package com.pb.mockproxy.repository.rule;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pb.mockproxy.validator.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@Document("rule")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rule {

    public interface Create {
    }

    public interface Update {
    }

    @Id
    @Null(groups = Rule.Create.class, message = "Id must be null")
    @NotBlank(groups = Rule.Update.class, message = "Id may not be blank")
    @RuleIdExistConstraint(groups = Rule.Update.class)
    private String id;
    @NotBlank(groups = Rule.Create.class, message = "Name may not be blank")
    private String name;
    private String description;
    @RuleStateCreateConstraint(groups = Rule.Create.class, message = "State is not valid")
    @RuleStateUpdateConstraint(groups = Rule.Update.class, message = "State is not valid")
    private String state;
    @PositiveOrZero(groups = Rule.Create.class, message = "TimeWait is not valid")
    private Long timeWait;              // Время ожидания ответа
    @NotBlank(groups = Rule.Create.class, message = "RequestMethod may not be blank")
    @HttpMethodsConstraint(groups = Rule.Create.class, message = "RequestMethod is not valid")
    private String requestMethod;       // Правило соответствия
    @NotBlank(groups = Rule.Create.class, message = "RequestUrl may not be blank")
    private String requestUrl;          // Правило соответствия
    private Boolean fullBodyFit;        // Правило соответствия
    private String requestBody;
    @Field(targetType = FieldType.OBJECT_ID)
    private String mockId;
    @NotNull(groups = Rule.Create.class, message = "ProjectId is not valid")
    @ProjectIdExistConstraint(groups = Rule.Create.class, message = "ProjectId is not valid")
    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;
}
