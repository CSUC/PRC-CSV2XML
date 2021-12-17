package org.csuc.marshal;

import xmlns.org.eurocris.cerif_1.CfTransType;

import java.util.Objects;

/**
 * @author amartinez
 */
public class NameOrTitle {

    private String value;
    private String langCode = "und";
    private CfTransType trans = CfTransType.O;

    public NameOrTitle(String value, String langCode, String trans) {
        if (Objects.nonNull(value)) this.value = value;
        if (Objects.nonNull(langCode)) this.langCode = langCode;
        if (Objects.nonNull(trans)) {
            try {
                this.trans = CfTransType.fromValue(trans);
            } catch (Exception e) {

            }
        }
    }

    public String getValue() {
        return value;
    }

    public String getLangCode() {
        return langCode;
    }

    public CfTransType getTrans() {
        return trans;
    }
}
