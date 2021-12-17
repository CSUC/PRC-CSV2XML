package org.csuc.typesafe.semantics;


import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * @author amartinez
 */
public class Semantics {

    public static String getSchemaId(SchemeId id) {
        Config conf = ConfigFactory.load("semantics.conf");
        return conf.getObject("schemeId").toConfig().getString(id.name());
    }

    public static String getClassId(ClassId id) {
        Config conf = ConfigFactory.load("semantics.conf");
        return conf.getObject("classId").toConfig().getString(id.name());
    }
}
