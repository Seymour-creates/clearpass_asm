package com.melog.clearpass.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResourceNotFoundException extends ResponseStatusException {
    public ResourceNotFoundException(String resource, Object key) {
        super(HttpStatus.NOT_FOUND, "%s with id %s not found".formatted(resource, key));
    }
}

