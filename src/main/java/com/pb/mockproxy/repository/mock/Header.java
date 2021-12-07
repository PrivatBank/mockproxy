package com.pb.mockproxy.repository.mock;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class Header {
    @NotBlank(groups = {Mock.Create.class, Mock.Update.class}, message = "header name is required")
    private String name;
    @NotBlank(groups = {Mock.Create.class, Mock.Update.class}, message = "header value is required")
    private String value;
}