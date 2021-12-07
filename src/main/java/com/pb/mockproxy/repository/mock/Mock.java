package com.pb.mockproxy.repository.mock;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@Document("mock")
public class Mock {

    public interface Create {
    }

    public interface Update {
    }

    @Id
    @Null(groups = Mock.Create.class, message = "id must be null")
    @NotBlank(groups = Mock.Update.class, message = "id may not be blank")
    private String id;

    @NotBlank(groups = Mock.Create.class, message = "projectId may not be blank")
    @Null(groups = Mock.Update.class, message = "projectId must be null")
    @Field(targetType = FieldType.OBJECT_ID)
    private String projectId;

    @NotBlank(groups = Mock.Create.class, message = "name may not be blank")
    private String name;
    private String description;

    @Null(groups = {Mock.Create.class, Mock.Update.class}, message = "state must be null")
    private String state;

    @Valid
    @NotNull(groups = Mock.Create.class, message = "response may not be null")
    private Response response;
}
