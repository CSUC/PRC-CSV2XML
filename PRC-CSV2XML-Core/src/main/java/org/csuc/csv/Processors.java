package org.csuc.csv;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * @author amartinez
 */
public class Processors {

    /**
     *
     * @return
     */
    public static  CellProcessor[] getProcessorsResearcher() {
        final CellProcessor[] processors = new CellProcessor[] {
                new Optional(),                                  // NAME
                new Optional(new NotNull(new UniqueHashCode())), // ORCID
                new Optional(),                                  // SIGNATURA
                new Optional()                                   // AE
        };
        return processors;
    }

    public static CellProcessor[] getProcessorsDepartment() {
        final CellProcessor[] processors = new CellProcessor[]{
                new Optional(),                                 // NAME
                new Optional(),                                 // SIGLES
                new Optional(),                                 // ADREÇA
                new Optional(),                                 // URL
                new Optional(),                                 // AE
                new Optional(new NotNull(new UniqueHashCode())),// CODI
                new Optional(),                                 // TELF
        };
        return processors;
    }

    /**
     * @return
     */
    public static CellProcessor[] getProcessorsDepartmentRelation() {
        final CellProcessor[] processors = new CellProcessor[]{
                new Optional(),              // CODI
                new Optional(new NotNull()), // ORCID
        };
        return processors;
    }

    /**
     * @return
     */
    public static CellProcessor[] getProcessorsResearchGroup() {
        final CellProcessor[] processors = new CellProcessor[]{
                new Optional(),                                  // NAME
                new Optional(),                                  // SIGLES
                new Optional(),                                  // URL
                new Optional(),                                  // AE
                new Optional(new NotNull(new UniqueHashCode())), // CODI
                new Optional(),                                  // SGR
                new Optional(),                                  // DATE
        };
        return processors;
    }

    /**
     * @return
     */
    public static CellProcessor[] getProcessorsResearchGroupRelation() {
        final CellProcessor[] processors = new CellProcessor[]{
                new Optional(new NotNull()), // CODI
                new Optional(),              // NOM
                new Optional(),              // ORCID
                new Optional(new NotNull()), // IP
        };
        return processors;
    }

    /**
     * @return
     */
    public static CellProcessor[] getProcessorsProject() {
        final CellProcessor[] processors = new CellProcessor[]{
                new Optional(),                                  // TITLE
                new Optional(),                                  // URL
                new Optional(),                                  // OFFICIAL CODE
                new Optional(new NotNull(new UniqueHashCode())), // CODE
                new Optional(),                                  // FUNDING PROGRAM
                new Optional(),                                  // DATE INICI
                new Optional(),                                  // DATE FI
        };
        return processors;
    }

    /**
     * @return
     */
    public static CellProcessor[] getProcessorsProjectRelation() {
        final CellProcessor[] processors = new CellProcessor[]{
                new Optional(new NotNull()), // CODI
                new Optional(),              // NOM
                new Optional(),              // ORCID
                new Optional(new NotNull()), // IP
        };
        return processors;
    }

    /**
     * @return
     */
    public static CellProcessor[] getProcessorsPublication() {
        final CellProcessor[] processors = new CellProcessor[]{
                new Optional(),                                  // TITLE
                new Optional(new NotNull(new UniqueHashCode())), // ID
                new Optional(),                                  // DOI
                new Optional(),                                  // HANDLE
                new Optional(),                                  // NUM
                new Optional(),                                  // VOL
                new Optional(),                                  // START PAGE
                new Optional(),                                  // END PAGE
                new Optional(),                                  // ISBN
                new Optional(),                                  // ISSN
                new Optional(),                                  // DATA
                new Optional(),                                  // PUBLICAT A
                new Optional(),                                  // PUBLICAT PER
                new Optional(),                                  // DOCUMENT TYPES
                new Optional(),                                  // CADENA D'AUTORS
        };
        return processors;
    }

    /**
     * @return
     */
    public static CellProcessor[] getProcessorsPublicationRelation() {
        final CellProcessor[] processors = new CellProcessor[]{
                new Optional(new NotNull()), // ID
                new Optional(),              // NOM
                new Optional(),              // ORCID
                new Optional(new NotNull()), // En cas de tesis, intervé en la seva direcció?
        };
        return processors;
    }
}
