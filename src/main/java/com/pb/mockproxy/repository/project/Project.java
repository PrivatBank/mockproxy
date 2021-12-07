package com.pb.mockproxy.repository.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pb.mockproxy.validator.ProjectIdExistConstraint;
import com.pb.mockproxy.validator.ProjectStateCreateConstraint;
import com.pb.mockproxy.validator.ProjectStateUpdateConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.util.List;

@Data
@NoArgsConstructor
@Document("project")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {

    public interface Create {
    }

    public interface Update {
    }

    @Id
    @Null(groups = Project.Create.class, message = "Id must be null")
    @NotBlank(groups = Project.Update.class, message = "Id may not be blank")
    @ProjectIdExistConstraint(groups = Project.Update.class, message = "ProjectId is not valid")
    private String id;
    @NotBlank(groups = Project.Create.class, message = "Name may not be blank")
    private String name;
    private String description;
    private List<User> users;
    @ProjectStateCreateConstraint(groups = Project.Create.class, message = "State is not valid")
    @ProjectStateUpdateConstraint(groups = Project.Update.class, message = "State is not valid")
    private String state;
}
