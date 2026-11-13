package com.yuvaintern.taskmanagement.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private final JwtService jwtService =
        new JwtService("test-secret-key-that-is-at-least-32-bytes-long", 3600000);

    @Test
    void createsAndValidatesToken() {
        String token = jwtService.generateToken("user@example.com", "USER");
        assertEquals("user@example.com", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, "user@example.com"));
        assertFalse(jwtService.isTokenValid(token, "other@example.com"));
    }

    @Test
    void rejectsTamperedToken() {
        String token = jwtService.generateToken("user@example.com", "USER");
        String tampered = token.substring(0, token.length() - 2) + "xx";
        assertThrows(io.jsonwebtoken.JwtException.class,
            () -> jwtService.extractUsername(tampered));
    }
}
