package org.csuc.cli;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;

import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Objects;

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

    private Path input;
    private Path output;
    private String charset = StandardCharsets.UTF_8.name();
    private Boolean formatted = false;

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

    public String getResearcher() {
        return (Objects.isNull(researcher)) ? null : researcher.toString();
    }

    public void setResearcher(Path researcher) throws FileNotFoundException {
        if(Files.notExists(researcher)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", researcher));
        this.researcher = researcher;
    }

    public String getDepartment() {
        return (Objects.isNull(department)) ? null : department.toString();
    }

    public void setDepartment(Path department) throws FileNotFoundException {
        if(Files.notExists(department)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", department));
        this.department = department;
    }

    public String getResearcherGroup() {
        return (Objects.isNull(researcherGroup)) ? null : researcherGroup.toString();
    }

    public void setResearcherGroup(Path researcherGroup) throws FileNotFoundException {
        if(Files.notExists(researcherGroup)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", researcherGroup));
        this.researcherGroup = researcherGroup;
    }

    public String getProject() {
        return (Objects.isNull(project)) ? null : project.toString();
    }

    public void setProject(Path project) throws FileNotFoundException {
        if(Files.notExists(project)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", project));
        this.project = project;
    }

    public String getPublication() {
        return (Objects.isNull(publication)) ? null : publication.toString();
    }

    public void setPublication(Path publication) throws FileNotFoundException {
        if(Files.notExists(publication)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", publication));
        this.publication = publication;
    }

    public String getRuct() {
        return ruct;
    }

    @Option(name = "-ruct", aliases = "--ruct", usage= "ruct code", required = true, metaVar = "https://www.educacion.gob.es/ruct/home")
    public void setRuct(String ruct) throws Exception {
        //if(!Ruct.isValidRuct(ruct) || !Altres.isValidCode(ruct)) throw new Exception(MessageFormat.format("{0} invalid ruct code!", ruct));
        if(ruct.isEmpty()) throw new Exception(MessageFormat.format("{0} ruct is empty. Invalid ruct code!", ruct));
        this.ruct = ruct;
    }

    public String getRelationDepartment() {
        return (Objects.isNull(relationDepartment)) ? null : relationDepartment.toString();
    }

    public void setRelationDepartment(Path relationDepartment) throws FileNotFoundException {
        if(Files.notExists(relationDepartment)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", relationDepartment));
        this.relationDepartment = relationDepartment;
    }

    public String getRelationResearcherGroup() {
        return (Objects.isNull(relationResearcherGroup)) ? null : relationResearcherGroup.toString();
    }

    public void setRelationResearcherGroup(Path relationResearcherGroup) throws FileNotFoundException {
        if(Files.notExists(relationResearcherGroup)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", relationResearcherGroup));
        this.relationResearcherGroup = relationResearcherGroup;
    }

    public String getRelationProject() {
        return (Objects.isNull(relationProject)) ? null : relationProject.toString();
    }

    public void setRelationProject(Path relationProject) throws FileNotFoundException {
        if(Files.notExists(relationProject)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", relationProject));
        this.relationProject = relationProject;
    }

    public String getRelationPublication() {
        return (Objects.isNull(relationPublication)) ? null : relationPublication.toString();
    }

    public void setRelationPublication(Path relationPublication) throws FileNotFoundException {
        if(Files.notExists(relationPublication)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", relationPublication));
        this.relationPublication = relationPublication;
    }

    public Path getOutput() {
        return output;
    }

    @Option(name = "-o", aliases = "--output", usage= "output file", metaVar = "<Path>")
    public void setOutput(Path output) throws IllegalArgumentException {
        if(!FilenameUtils.getExtension(output.toString()).equalsIgnoreCase("xml"))
            throw new IllegalArgumentException(MessageFormat.format("{0} illegal extension!", FilenameUtils.getExtension(output.toString())));
        this.output = output;
    }

    public Path getInput() {
        return input;
    }

    @Option(name = "-i", aliases = "--input", usage= "input file", metaVar = "<Path>")
    public void setInput(Path input) throws FileNotFoundException {
        if(Files.notExists(input)) throw new FileNotFoundException(MessageFormat.format("{0} File not Found!", input));
        if(!FilenameUtils.getExtension(input.toString()).equalsIgnoreCase("xlsx"))
            throw new IllegalArgumentException(MessageFormat.format("{0} illegal extension!", FilenameUtils.getExtension(input.toString())));
        this.input = input;
    }

    public Charset getCharset() {
        return Charset.forName(charset);
    }

    @Option(name = "-c", aliases = "--charset", usage= "charset output file", metaVar = "[UTF-8, ISO_8859_1, US_ASCII, UTF_16, UTF_16BE, UTF_16LE]")
    public void setCharset(String charset) {
        this.charset = Charset.forName(charset).toString();
    }

    public Boolean getFormatted() {
        return formatted;
    }

    @Option(name = "-f", aliases = "--formatted", handler=BooleanOptionHandler.class, usage= "formatted output file")
    public void setFormatted(Boolean formatted) {
        this.formatted = formatted;
    }

    public void run(){
        logger.info("Ruct                        :   {}", ruct);
        logger.info("Input file                  :   {}", input);
        logger.info("Output file                 :   {}", output);
        logger.info("Charset file                :   {}", charset);
        logger.info("Formatted file              :   {}", formatted);
    }
}
