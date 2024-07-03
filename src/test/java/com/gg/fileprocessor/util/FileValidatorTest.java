package com.gg.fileprocessor.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileValidatorTest {

    @Test
    public void testValidSpeeds() {
        String[] validParts = {"18148426-89e1-11ee-b9d1-0242ac120002", "1X1D14", "John Smith", "Likes Apricots", "Rides A Bike", "6.2", "12.1"};
        assertTrue(FileValidator.isValid(validParts));
    }

    @Test
    public void testInvalidAvgSpeed() {
        String[] invalidParts = {"18148426-89e1-11ee-b9d1-0242ac120002", "1X1D14", "John Smith", "Likes Apricots", "Rides A Bike", "invalid", "12.1"};
        assertFalse(FileValidator.isValid(invalidParts));
    }

    @Test
    public void testInvalidTopSpeed() {
        String[] invalidParts = {"18148426-89e1-11ee-b9d1-0242ac120002", "1X1D14", "John Smith", "Likes Apricots", "Rides A Bike", "6.2", "invalid"};
        assertFalse(FileValidator.isValid(invalidParts));
    }

    @Test
    public void testBothSpeedsInvalid() {
        String[] invalidParts = {"18148426-89e1-11ee-b9d1-0242ac120002", "1X1D14", "John Smith", "Likes Apricots", "Rides A Bike", "invalid", "invalid"};
        assertFalse(FileValidator.isValid(invalidParts));
    }

}