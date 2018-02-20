package org.csuc.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.csv.*;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author amartinez
 *
 */
public class App {

	private static Logger logger = LogManager.getLogger("PRC-CSV2XML");

    private static ArgsBean bean;

    public static void main(String[] args) {
        new App().doMain(args);
    }

    public void doMain(String[] args)  {
        this.bean = new ArgsBean(args);
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            // parse the arguments.
            parser.parseArgument(args);
        } catch( CmdLineException e ) {
            System.exit(1);
        }

        try {
            new Factory(Arrays.asList(
                    new CSVResearcher(bean.getResearcher().toString()),
                    new CSVDepartment(bean.getDepartment().toString(), bean.getRelationDepartment().toString()),
                    new CSVGroup(bean.getResearcherGroup().toString(), bean.getRelationResearcherGroup().toString()),
                    new CSVProject(bean.getProject().toString(), bean.getRelationProject().toString()),
                    new CSVPublication(bean.getPublication().toString(), bean.getRelationPublication().toString())
            )).XML(bean.getRuct(), "output.xml", StandardCharsets.UTF_8, true, true);

        }catch(Exception e){
            logger.error(e);
        } finally{
            logger.info("Done!");
        }
	}
}