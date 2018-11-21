package com.company.appname.common.config.exception.model;

import com.company.appname.common.util.EventMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ValidationException extends Exception {

    private static final long serialVersionUID = 1L;
    
    private Object body;
    private EventMessage eventMessage;

    public ValidationException(EventMessage eventMessage, Object... args) {
        super(String.format(eventMessage.getMessage(), args));
        this.eventMessage = eventMessage;
    }

    public ValidationException(EventMessage eventMessage) {
        super(eventMessage.getMessage());
        this.eventMessage = eventMessage;
    }

    public ValidationException(String message, Object body) {
        super(message);
        setBody(body);
    }

}

