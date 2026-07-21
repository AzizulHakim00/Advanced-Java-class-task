package com.example.bankmangament.Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUtilsTest {
    @Test
    void hashesAndVerifiesPasswords() {
        String hash = PasswordUtils.hashPassword("Secure123!");

        assertNotEquals("Secure123!", hash);
        assertTrue(PasswordUtils.isHashed(hash));
        assertTrue(PasswordUtils.matches("Secure123!", hash));
        assertFalse(PasswordUtils.matches("wrong", hash));
    }

    @Test
    void supportsLegacyPlainTextPasswords() {
        assertTrue(PasswordUtils.matches("legacy", "legacy"));
        assertFalse(PasswordUtils.matches("other", "legacy"));
    }
}
