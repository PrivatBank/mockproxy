package com.pb.mockproxy.repository.mock;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class Response {
    @NotNull(groups = {Mock.Create.class, Mock.Update.class}, message = "code may not be null")
    private Integer code;
    @Valid
    private List<Header> headers;
    private String body;
}
