package org.csuc.typesafe.semantics;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class SemanticsTest {

    private static Logger logger = LogManager.getLogger(SemanticsTest.class);

    @Test
    public void getSchemaId() {
        assertEquals("759af93b-34ae-11e1-b86c-0800200c9a66", Semantics.getSchemaId(SchemeId.FUNDING_SOURCE_TYPES));
        assertEquals("fee53e30-de3a-421b-80e0-9b3fe3a3c170", Semantics.getSchemaId(SchemeId.ORGANISATION_CONTACT_DETAILS));
        assertEquals("6b2b7d26-3491-11e1-b86c-0800200c9a66", Semantics.getSchemaId(SchemeId.ORGANISATION_OUTPUT_CONTRIBUTIONS));
        assertEquals("759af93e-34ae-11e1-b86c-0800200c9a66", Semantics.getSchemaId(SchemeId.ORGANISATION_RESEARCH_INFRASTRUCTURE_ROLES));
    }

    @Test
    public void getClassId() {
        assertEquals("eda2b2f6-34c5-11e1-b86c-0800200c9a66", Semantics.getClassId(ClassId.BOOK));
        assertEquals("e712ef38-03d1-11e4-9c54-b2227cce2b54", Semantics.getClassId(ClassId.DEPARTMENT_CODE_1));
        assertEquals("e7f1d5ee-03c8-11e4-933d-b2227cce2b54", Semantics.getClassId(ClassId.DEPARTMENT_CODE_2));
        assertEquals("e7f1c0f4-03c8-11e4-933d-b2227cce2b54", Semantics.getClassId(ClassId.DEPARTMENT_OR_INSTITUTE));
    }
}