package org.csuc.cli;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.csuc.typesafe.ruct.Ruct;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;

/**
 * @author amartinez
 */
public class ArgsBean {

    private static Logger logger = LogManager.getLogger(ArgsBean.class);

    @Option(name="-h", aliases = "--help", help = true, required = false)
    private boolean help = false;

    private Path researcher;
    private Path department;
    private Path relationDepartment;
    private Path researcherGroup;
    private Path relationResearcherGroup;
    private Path project;
    private Path relationProject;
    private Path publication;
    private Path relationPublication;

    private String ruct;

    public ArgsBean(String[] args){
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.setUsageWidth(500);
            // parse the arguments.
            parser.parseArgument(args);

            if(this.help){
                System.err.println("Usage: ");
                parser.printUsage(System.err);
                System.err.println();
                System.exit(1);
            }

            this.run();
        } catch( CmdLineException e ) {
            if(this.help){
                System.err.println("Usage: ");
                parser.printUsage(System.err);
                System.err.println();
                return;
            }else{
                System.err.println(e.getMessage());
                parser.printUsage(System.err);
                System.err.println();
                return;
            }
        }
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public Path getResearcher() {
        return researcher;
    }

    @Option(name = "-r", aliases = "--researcher", usage= "Researcher file", required = true, metaVar = "<Path>")
    public void setResearcher(Path researcher) throws FileNotFoundException {
        if(Files.notExists(researcher)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", researcher));
        this.researcher = researcher;
    }

    public Path getDepartment() {
        return department;
    }

    @Option(name = "-d", aliases = "--department", usage= "Department file", required = true, metaVar = "<Path>")
    public void setDepartment(Path department) throws FileNotFoundException {
        if(Files.notExists(department)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", department));
        this.department = department;
    }

    public Path getResearcherGroup() {
        return researcherGroup;
    }

    @Option(name = "-rg", aliases = "--researchgroup", usage= "Research Group file", required = true, metaVar = "<Path>")
    public void setResearcherGroup(Path researcherGroup) throws FileNotFoundException {
        if(Files.notExists(researcherGroup)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", researcherGroup));
        this.researcherGroup = researcherGroup;
    }

    public Path getProject() {
        return project;
    }

    @Option(name = "-pj", aliases = "--project", usage= "Project file", required = true, metaVar = "<Path>")
    public void setProject(Path project) throws FileNotFoundException {
        if(Files.notExists(project)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", project));
        this.project = project;
    }

    public Path getPublication() {
        return publication;
    }

    @Option(name = "-p", aliases = "--publication", usage= "Publication file", required = true, metaVar = "<Path>")
    public void setPublication(Path publication) throws FileNotFoundException {
        if(Files.notExists(publication)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", publication));
        this.publication = publication;
    }

    public String getRuct() {
        return ruct;
    }

    @Option(name = "-ruct", aliases = "--ruct", usage= "ruct code", required = true)
    public void setRuct(String ruct) throws Exception {
        if(!Ruct.isValidRuct(ruct)) throw new Exception(MessageFormat.format("{0} invalid ruct code!", ruct));
        this.ruct = ruct;
    }

    public Path getRelationDepartment() {
        return relationDepartment;
    }

    @Option(name = "-rd", aliases = "--relationDepartment", usage= "Relation Department  file", required = true, metaVar = "<Path>")
    public void setRelationDepartment(Path relationDepartment) throws FileNotFoundException {
        if(Files.notExists(relationDepartment)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", relationDepartment));
        this.relationDepartment = relationDepartment;
    }

    public Path getRelationResearcherGroup() {
        return relationResearcherGroup;
    }

    @Option(name = "-rrg", aliases = "--relationResearchGroup", usage= "Relation Research Group file", required = true, metaVar = "<Path>")
    public void setRelationResearcherGroup(Path relationResearcherGroup) throws FileNotFoundException {
        if(Files.notExists(relationResearcherGroup)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", relationResearcherGroup));
        this.relationResearcherGroup = relationResearcherGroup;
    }

    public Path getRelationProject() {
        return relationProject;
    }

    @Option(name = "-rpj", aliases = "--relationProject", usage= "Relation Project file", required = true, metaVar = "<Path>")
    public void setRelationProject(Path relationProject) throws FileNotFoundException {
        if(Files.notExists(relationProject)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", relationProject));
        this.relationProject = relationProject;
    }

    public Path getRelationPublication() {
        return relationPublication;
    }

    @Option(name = "-rp", aliases = "--relationPublication", usage= "Relation Publication file", required = true, metaVar = "<Path>")
    public void setRelationPublication(Path relationPublication) throws FileNotFoundException {
        if(Files.notExists(relationPublication)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", relationPublication));
        this.relationPublication = relationPublication;
    }

    public void run(){
        logger.info("Researcher                  :   {}", researcher);
        logger.info("Department                  :   {}", department);
        logger.info("Relation Department         :   {}", relationDepartment);
        logger.info("Researcher Group            :   {}", researcherGroup);
        logger.info("Relation Researcher Group   :   {}", relationResearcherGroup);
        logger.info("Project                     :   {}", project);
        logger.info("Relation Project            :   {}", relationProject);
        logger.info("Publication                 :   {}", publication);
        logger.info("Relation Publication        :   {}", relationPublication);
        logger.info("Ruct                        :   {}", ruct);
    }
}
