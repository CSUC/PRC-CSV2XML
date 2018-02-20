package org.csuc.typesafe.ruct;

import org.junit.Test;

import static org.junit.Assert.*;

public class RuctTest {

    @Test
    public void isValidRuct() {
        assertTrue(Ruct.isValidRuct("024"));
        assertTrue(Ruct.isValidRuct("043"));
        assertTrue(Ruct.isValidRuct("062"));
        assertFalse(Ruct.isValidRuct("asd"));
    }
}