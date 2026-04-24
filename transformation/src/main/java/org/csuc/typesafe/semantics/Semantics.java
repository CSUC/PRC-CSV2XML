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

    private static final Config CONF = ConfigFactory.load("semantics.conf");
    private static final Config SCHEME_ID_CONF = CONF.getObject("schemeId").toConfig();
    private static final Config CLASS_ID_CONF = CONF.getObject("classId").toConfig();

    /**
     * Obté l'identificador d'esquema CERIF per al tipus especificat.
     *
     * @param id Tipus d'esquema
     * @return Identificador de l'esquema
     */
    public static String getSchemaId(SchemeId id) {
        return SCHEME_ID_CONF.getString(id.name());
    }

    /**
     * Obté l'identificador de classe CERIF per al tipus especificat.
     *
     * @param id Tipus de classe
     * @return Identificador de la classe
     */
    public static String getClassId(ClassId id) {
        return CLASS_ID_CONF.getString(id.name());
    }
}
