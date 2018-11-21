package com.company.appname.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventMessage {

    UNEXPECTED_SERVER_ERROR(1, "Unexpected server error. Call technical support."),
    INVALID_METHOD_ARGUMENTS(2, "The given arguments are not in the correct form.");

    private Integer code;
    private String message;

}
