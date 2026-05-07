package com.cajaviva.cajaviva.auth.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AuthResponse(
        UUID userId,
        String email,
        List<String> roles,
        LocalDateTime expiresAt
) {
}
