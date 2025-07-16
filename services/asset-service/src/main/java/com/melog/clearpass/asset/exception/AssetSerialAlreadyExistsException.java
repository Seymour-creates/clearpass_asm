package com.melog.clearpass.asset.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Thrown when a new Asset’s serial number is already present in the database.
 * Mapped to HTTP 409 (CONFLICT) by extending {@link ResponseStatusException},
 * so the GlobalExceptionHandler can simply forward the ProblemDetail body.
 */
public class AssetSerialAlreadyExistsException extends ResponseStatusException {

    public AssetSerialAlreadyExistsException(String serial) {
        super(HttpStatus.CONFLICT,
              "Asset serial '%s' already exists".formatted(serial));
    }
}
