package org.csuc.typesafe.altres;

import org.junit.Test;

import static org.junit.Assert.*;

public class AltresTest {

    @Test
    public void isValidCode() {
        assertTrue(Altres.isValidCode("58863317"));
        assertTrue(Altres.isValidCode("G63687222"));
    }
}