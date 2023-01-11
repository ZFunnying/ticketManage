package com.cmit.kapok.system.controller.login.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
public class SsoLoginParam {
    @NotBlank(message = "token can not be null")
    private String token;
}
