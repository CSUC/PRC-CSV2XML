package org.csuc.global;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeTest {

    @Test
    public void formatDateTime() {
        System.out.println(Time.formatDateTime("2009-07-03", null));
    }
}