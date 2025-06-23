package com.example.userservice.util;

import java.util.UUID;

public class IdGenerator {

    public static String generateId(String prefix) {
        String uniquePart = UUID.randomUUID().toString().replace("_", "").substring(0, 10 - prefix.length()).toUpperCase();
        return prefix + uniquePart;
    }
}
