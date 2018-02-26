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
public class CSVResearcher implements Read<List<List<Object>>>{

    private static Logger logger = LogManager.getLogger(CSVResearcher.class);

    private List<List<Object>> data;
    private String file;

    public CSVResearcher(String file) throws IOException {
        this.file = file;

        logger.debug("Researcher file:           {}", this.file);
        data = Reading.readWithCsvListReader(this.file, Processors.getProcessorsResearcher());
    }

    @Override
    public List<List<Object>> readCSV(){
        if(Objects.nonNull(data)){
            data.forEach(consumer->{
                try {
                    if(consumer.size() != 4)    throw new Exception(MessageFormat.format("{0} invalid format!", file));
                    logger.debug("name: \"{}\"  orcid:  \"{}\"   signatura: \"{}\"     ae:  \"{}\"", consumer.get(0), consumer.get(1), consumer.get(2), consumer.get(3));
                }catch(Exception e){
                    logger.error(e);
                }
            });
        }
        return data;
    }

    @Override
    public <T> T readCSVRelation() {
        return null;
    }


}
