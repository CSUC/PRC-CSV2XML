package org.csuc.utils;

import org.apache.commons.lang.StringUtils;

import java.text.MessageFormat;

/**
 *
 * @author amartinez
 *
 */
public enum DocumentTypes {

    JOURNAL_ARTICLE("Journal Article"),
    CHAPTER_IN_BOOK("Chapter in Book"),
    BOOK("Book"),
    PHD_THESIS("Phd Thesis");

    private String value;

    DocumentTypes(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static DocumentTypes convert(String value) {
        for (DocumentTypes inst : values()) {
            if (inst.value().equals(value)
                    || StringUtils.deleteWhitespace(inst.value()).toLowerCase().equals(StringUtils.deleteWhitespace(value).toLowerCase())) {
                return inst;
            }
        }
        return null;
    }
}
