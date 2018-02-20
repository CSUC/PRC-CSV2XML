package org.csuc.global;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeTest {

    @Test
    public void formatDateTime() {
        String d = "2018-02-20T16:39:13";
        String d2 = "2015-01-30";

        assertEquals(d, Time.formatDateTime(d).toString());
        assertEquals(d2, Time.formatDate(d2).toString());
    }
}