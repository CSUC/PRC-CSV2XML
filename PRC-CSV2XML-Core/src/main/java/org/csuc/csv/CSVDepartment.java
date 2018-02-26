package org.csuc.csv;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

/**
 * @author amartinez
 */
public class CSVDepartment implements Read<List<List<Object>>>{

    private static Logger logger = LogManager.getLogger(CSVDepartment.class);

    private List<List<Object>> data;
    private List<List<Object>> dataRelation;

    private String file;
    private String fileRelation;

    public CSVDepartment(String file, String relation) throws IOException {
        this.file = file;
        this.fileRelation = relation;

        logger.debug("Department file:           {}", this.file);
        logger.debug("Department Relation file:  {}", this.fileRelation);

        data = Reading.readWithCsvListReader(this.file, Processors.getProcessorsDepartment());
        dataRelation = Reading.readWithCsvListReader(this.fileRelation, Processors.getProcessorsDepartmentRelation());
    }

    @Override
    public List<List<Object>> readCSV() {
        if(Objects.nonNull(data)){
            data.forEach(consumer -> {
                try {
                    if (consumer.size() != 7) throw new Exception(MessageFormat.format("{0} invalid format!", file));
                    logger.debug("name: \"{}\"  sigles:  \"{}\"   adreça: \"{}\"     url:  \"{}\"     ae:  \"{}\"     codi:  \"{}\"     telf:  \"{}\"",
                            consumer.get(0), consumer.get(1), consumer.get(2), consumer.get(3), consumer.get(4), consumer.get(5), consumer.get(6));
                } catch (Exception e) {
                    logger.error(e);
                }
            });
        }
        return data;
    }

    @Override
    public List<List<Object>> readCSVRelation() {
        if(Objects.nonNull(dataRelation)){
            dataRelation.forEach(consumer -> {
                try {
                    if (consumer.size() != 2) throw new Exception(MessageFormat.format("{0} invalid format!", fileRelation));
                    logger.debug("codi: \"{}\"  orcid:  \"{}\"", consumer.get(0) ,consumer.get(1));
                } catch (Exception e) {
                    logger.error(e);
                }
            });
        }
        return dataRelation;
    }

}
