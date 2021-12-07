package com.pb.mockproxy.repository.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @NotBlank(message = "Login may not be blank")
    private String login;
    private String name;
    private String photoUrl;
}
