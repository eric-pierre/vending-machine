package com.ai.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.ai.domain.Location;
import com.ai.exception.InvalidLocationException;

class LocationTest {

    @Test
    void testGetCharForNumber() {

        assertEquals("A1", Location.builder().row(0).column(0).build().toString());
        assertEquals("B5", Location.builder().row(4).column(1).build().toString());
        assertEquals(Location.builder().column(2).row(3).build(), Location.fromString("C4"));
        assertEquals(Location.builder().row(98).column(25).build(), Location.fromString("Z99"));

        Exception exception = assertThrows(InvalidLocationException.class, () -> Location.fromString("AA4"));
        assertEquals("Location AA4 is not valid", exception.getMessage());

    }
}
