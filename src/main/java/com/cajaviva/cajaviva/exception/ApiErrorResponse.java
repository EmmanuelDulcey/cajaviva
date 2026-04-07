package com.cajaviva.cajaviva.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(
    String message,
    String code,
    LocalDateTime timestamp
) {
}
