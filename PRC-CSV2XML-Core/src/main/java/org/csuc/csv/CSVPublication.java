package org.csuc.csv;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

/**
 * @author amartinez
 */
public class CSVPublication implements Read<List<List<Object>>> {

    private static Logger logger = LogManager.getLogger(CSVPublication.class);

    private List<List<Object>> data;
    private List<List<Object>> dataRelation;

    private String file;
    private String fileRelation;


    public CSVPublication(String file, String relation) throws IOException {
        this.file = file;
        this.fileRelation = relation;

        logger.debug("Publication file:           {}", this.file);
        logger.debug("Publication Relation file:  {}", this.fileRelation);

        data = Reading.readWithCsvListReader(this.file, Processors.getProcessorsPublication());
        dataRelation = Reading.readWithCsvListReader(this.fileRelation, Processors.getProcessorsPublicationRelation());
    }

    @Override
    public List<List<Object>> readCSV() {
        if (Objects.nonNull(data)) {
            data.forEach(consumer -> {
                try {
                    if (consumer.size() != 15) throw new Exception(MessageFormat.format("{0} invalid format!", file));
                    logger.debug("title: \"{}\"  ID:  \"{}\"   DOI: \"{}\"     Handle:  \"{}\"     Num:  \"{}\"     Vol:  \"{}\"     startPage:  \"{}\"" +
                                    "endPage:  \"{}\"     isbn:  \"{}\"     issn:  \"{}\"     Data publicació:  \"{}\"     Publicat a:  \"{}\"" +
                                    "     Publicat per:  \"{}\"     Tipus document:  \"{}\"     Cadena d'autors:  \"{}\"",
                            consumer.get(0), consumer.get(1), consumer.get(2), consumer.get(3), consumer.get(4), consumer.get(5), consumer.get(6), consumer.get(7),
                            consumer.get(8), consumer.get(9), consumer.get(10), consumer.get(11), consumer.get(12), consumer.get(13), consumer.get(14));
                } catch (Exception e) {
                    logger.error(e);
                }
            });
        }
        return data;
    }

    @Override
    public List<List<Object>> readCSVRelation() {
        if (Objects.nonNull(dataRelation)) {
            dataRelation.forEach(consumer -> {
                try {
                    if (consumer.size() != 4)
                        throw new Exception(MessageFormat.format("{0} invalid format!", fileRelation));
                    String codi = StringEscapeUtils.escapeXml11((String) consumer.get(0));
                    String name = StringEscapeUtils.escapeXml11((String) consumer.get(1));
                    String orcid = StringEscapeUtils.escapeXml11((String) consumer.get(2));
                    String ip = StringEscapeUtils.escapeXml11((String) consumer.get(3));


                    logger.debug("codi: \"{}\"  name:  \"{}\"  orcid:  \"{}\"  ip:  \"{}\"", codi, name, orcid, ip);
                } catch (Exception e) {
                    logger.error(e);
                }
            });
        }
        return dataRelation;
    }
}