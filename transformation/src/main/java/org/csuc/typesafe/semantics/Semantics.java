package org.csuc.typesafe.semantics;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Classe d'utilitats per a la gestió de la semàntica CERIF.
 * Proporciona mètodes per obtenir identificadors de classes i esquemes
 * a partir del fitxer de configuració semantics.conf.
 *
 * @author Albert Martínez
 */
public class Semantics {

    /**
     * Obté l'identificador d'esquema CERIF per al tipus especificat.
     *
     * @param id Tipus d'esquema
     * @return Identificador de l'esquema
     */
    public static String getSchemaId(SchemeId id) {
        Config conf = ConfigFactory.load("semantics.conf");
        return conf.getObject("schemeId").toConfig().getString(id.name());
    }

    /**
     * Obté l'identificador de classe CERIF per al tipus especificat.
     *
     * @param id Tipus de classe
     * @return Identificador de la classe
     */
    public static String getClassId(ClassId id) {
        Config conf = ConfigFactory.load("semantics.conf");
        return conf.getObject("classId").toConfig().getString(id.name());
    }
}
