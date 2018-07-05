package org.csuc.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author amartinez
 */
public enum SHEETS {

    researchers("Investigadors"),
    departments("Departaments,Instituts,Escoles"),
    departments_relations("Dep,Ins,Escoles-Investigadors"),
    research_groups("Grups_recerca"),
    research_groups_relations("Grups_recerca-Investigadors"),
    projects("Projectes_recerca"),
    projects_relations("Projectes_recerca-Investigadors"),
    publications("Publicacions"),
    publication_relations("Publicacions-Autors");

    private String value;

    SHEETS(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static SHEETS convert(String value) throws Exception {
        for (SHEETS inst : values()) {
            if (inst.value().equals(value)
                    || StringUtils.deleteWhitespace(inst.value()).toLowerCase().equals(StringUtils.deleteWhitespace(value).toLowerCase())) {
                return inst;
            }
        }
        throw new Exception(String.format("Invalid sheet name: %s", value));
    }
}
