package org.csuc.cli;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.csuc.marshal.*;
import org.csuc.typesafe.semantics.ClassId;
import org.csuc.typesafe.semantics.Semantics;
import org.csuc.utils.SHEETS;
import org.csuc.utils.TimeUtils;
import picocli.CommandLine;
import xmlns.org.eurocris.cerif_1.CfOrgUnitType;
import xmlns.org.eurocris.cerif_1.CfPersType;
import xmlns.org.eurocris.cerif_1.CfProjType;
import xmlns.org.eurocris.cerif_1.CfResPublType;

import java.nio.file.Path;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.spark.sql.functions.*;

/**
 * Classe principal que gestiona la conversió de dades CSV a XML CERIF.
 * Aquesta classe utilitza Apache Spark per processar les dades i generar
 * fitxers XML segons l'estàndard CERIF.
 *
 * @author Albert Martínez
 * @version 2.4.19
 */
@CommandLine.Command(
        name = "prc-cerif",
        usageHelpAutoWidth = true,
        version = {"prc-cerif 2.4.19", "CSUC | (c) 2021"},
        mixinStandardHelpOptions = true,
        footerHeading = "System info:",
        footer = {
                "\tPicocli " + CommandLine.VERSION,
                "\tJVM: ${java.version} (${java.vendor} ${java.vm.name} ${java.vm.version})",
                "\tOS: ${os.name} ${os.version} ${os.arch}"
        }
)
public class Cerif implements Runnable {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    /**
     * Fitxer d'entrada amb les dades a processar
     */
    @CommandLine.Option(names = {"-i", "--input"}, required = true, description = "data file", paramLabel = "<PATH>")
    private Path input;

    /**
     * Indica si el fitxer XML de sortida ha de ser formatat
     */
    @CommandLine.Option(names = {"-f", "--formatted"}, description = "formatted output file (default: ${DEFAULT-VALUE})", paramLabel = "<BOOLEAN>")
    private Boolean formatted = false;

    /**
     * Codi RUCT de la institució
     */
    @CommandLine.Option(names = {"-r", "--ruct"}, required = true, description = "ruct code (https://www.educacion.gob.es/ruct/home)", paramLabel = "<STRING>")
    private String ruct;

    /**
     * Fitxer de sortida XML
     */
    @CommandLine.Option(names = {"-o", "--output"}, description = "output file (default: /tmp/`ruct`.xml)", paramLabel = "<PATH>")
    private Path output;

    private Instant inici;

    /**
     * Punt d'entrada principal de l'aplicació
     *
     * @param args Arguments de la línia de comandes
     */
    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new Cerif());
        if (args.length == 0) cmd.usage(System.out);
        else cmd.execute(args);
    }

    /**
     * Executa el procés de conversió de dades
     */
    @Override
    public void run() {
        inici = Instant.now();

        SparkSession sparkSession = SparkSession.builder()
                .appName("prc-csv2xml")
                .getOrCreate();

        try {
            sparkSession.sparkContext().setLogLevel("WARN");

            String inputPath = input.toString();

            Dataset<Row> researchers = readSheet(sparkSession, inputPath, SHEETS.researchers)
                            .toDF("_c0", "_c1", "_c2", "_c3")
                            .na().drop("all")
                            .withColumn("_c1", regexp_replace(col("_c1"), "\\s+", ""))
                            .withColumn("uuid", expr("uuid()"))
                            .alias("researchers")
                            .cache();

            Dataset<Row> departments = readSheet(sparkSession, inputPath, SHEETS.departments)
                            .toDF("_c0", "_c1", "_c2", "_c3", "_c4", "_c5", "_c6")
                            .na().drop("all")
                            .withColumn("_c5", regexp_replace(col("_c5"), "\\s+", ""))
                            .withColumn("uuid", expr("uuid()"))
                            .alias("departments");

            Dataset<Row> departments_relations = readSheet(sparkSession, inputPath, SHEETS.departments_relations)
                            .toDF("_c0", "_c1")
                            .na().drop("all")
                            .withColumn("_c0", regexp_replace(col("_c0"), "\\s+", ""))
                            .alias("departments_relations")
                            .join(
                                    researchers.select(col("_c1"), col("uuid")),
                                    col("departments_relations._c1").equalTo(col("researchers._c1")),
                                    "left"
                            )
                            .groupBy("departments_relations._c0")
                            .agg(
                                    collect_set(struct(col("departments_relations._c1"), col("uuid"))).as("relation")
                            );

            Dataset<Row> research_groups = readSheet(sparkSession, inputPath, SHEETS.research_groups)
                            .toDF("_c0", "_c1", "_c2", "_c3", "_c4", "_c5", "_c6")
                            .na().drop("all")
                            .withColumn("_c4", regexp_replace(col("_c4"), "\\s+", ""))
                            .withColumn("uuid", expr("uuid()"))
                            .alias("research_groups");

            Dataset<Row> research_groups_relations = readSheet(sparkSession, inputPath, SHEETS.research_groups_relations)
                            .toDF("_c0", "_c1", "_c2", "_c3")
                            .na().drop("all")
                            .withColumn("_c0", regexp_replace(col("_c0"), "\\s+", ""))
                            .withColumn("_c2", regexp_replace(col("_c2"), "\\s+", ""))
                            .alias("research_groups_relations")
                            .join(
                                    researchers.select(col("_c1"), col("uuid")),
                                    col("research_groups_relations._c2").equalTo(col("researchers._c1")),
                                    "left"
                            )
                            .groupBy("research_groups_relations._c0")
                            .agg(
                                    collect_set(struct(col("research_groups_relations._c1"), col("research_groups_relations._c2"), col("research_groups_relations._c3"), col("uuid"))).as("relation")
                            )
                            .alias("research_groups_relations");

            Dataset<Row> projects = readSheet(sparkSession, inputPath, SHEETS.projects)
                            .toDF("_c0", "_c1", "_c2", "_c3", "_c4", "_c5", "_c6")
                            .na().drop("all")
                            .withColumn("_c3", regexp_replace(col("_c3"), "\\s+", ""))
                            .withColumn("uuid", expr("uuid()"))
                            .alias("projects");

            Dataset<Row> projects_relations = readSheet(sparkSession, inputPath, SHEETS.projects_relations)
                            .toDF("_c0", "_c1", "_c2", "_c3")
                            .na().drop("all")
                            .withColumn("_c2", regexp_replace(col("_c2"), "\\s+", ""))
                            .withColumn("_c0", regexp_replace(col("_c0"), "\\s+", ""))
                            .alias("projects_relations")
                            .join(
                                    researchers.select(col("_c1"), col("uuid")),
                                    col("projects_relations._c2").equalTo(col("researchers._c1")),
                                    "left"
                            )
                            .groupBy("projects_relations._c0")
                            .agg(
                                    collect_set(struct(col("projects_relations._c1"), col("projects_relations._c2"), col("projects_relations._c3"), col("uuid"))).as("relation")
                            );

            Dataset<Row> publications = readSheet(sparkSession, inputPath, SHEETS.publications)
                            .toDF("_c0", "_c1", "_c2", "_c3", "_c4", "_c5", "_c6", "_c7", "_c8", "_c9", "_c10", "_c11", "_c12", "_c13", "_c14")
                            .na().drop("all")
                            .withColumn("_c1", regexp_replace(col("_c1"), "\\s+", ""))
                            .alias("publications");

            Dataset<Row> publication_relations = readSheet(sparkSession, inputPath, SHEETS.publication_relations)
                            .toDF("_c0", "_c1", "_c2", "_c3")
                            .na().drop("all")
                            .withColumn("_c0", regexp_replace(col("_c0"), "\\s+", ""))
                            .withColumn("_c2", regexp_replace(col("_c2"), "\\s+", ""))
                            .alias("publication_relations")
                            .join(
                                    researchers.select(col("_c1"), col("uuid")),
                                    col("publication_relations._c2").equalTo(col("researchers._c1")),
                                    "left"
                            )
                            .groupBy("publication_relations._c0")
                            .agg(
                                    collect_set(struct(col("publication_relations._c1"), col("publication_relations._c2"), col("publication_relations._c3"), col("uuid"))).as("relation")
                            );

            Dataset<Row> projects_join = projects.join(projects_relations, col("projects._c3").equalTo(col("projects_relations._c0")), "left").drop(col("projects_relations._c0"));
            Dataset<Row> departments_join = departments.join(departments_relations, col("departments._c5").equalTo(col("departments_relations._c0")), "left").drop(col("departments_relations._c0"));
            Dataset<Row> research_groups_join = research_groups.join(research_groups_relations, col("research_groups._c4").equalTo(col("research_groups_relations._c0")), "left").drop(col("research_groups_relations._c0"));
            Dataset<Row> publication_join = publications.join(publication_relations, col("publications._c1").equalTo(col("publication_relations._c0")), "left").drop(col("publication_relations._c0"));

            Marshaller marshaller = new Marshaller(ruct);

            ConcurrentHashMap<String, CfPersType> cfPersTypeList = new ConcurrentHashMap<>();
            List<CfOrgUnitType> cfOrgUnitTypeList = new ArrayList<>();
            List<CfProjType> cfProjTypeList = new ArrayList<>();
            List<CfResPublType> cfResPublTypeList = new ArrayList<>();

            String checkedClassId = Semantics.getClassId(ClassId.CHECKED);
            String departmentClassId = Semantics.getClassId(ClassId.DEPARTMENT_OR_INSTITUTE);
            String researchGroupClassId = Semantics.getClassId(ClassId.RESEARCH_GROUP);

            List<Row> researcherRows = researchers.collectAsList();
            researcherRows.forEach(row -> {
                cfPersTypeList.computeIfAbsent(
                        Objects.isNull(row.getAs(1)) ? UUID.randomUUID().toString() : row.getAs(1),
                        k -> new Researcher(row, checkedClassId));
            });

            departments_join.collectAsList().forEach(row -> {
                cfOrgUnitTypeList.add(new Department(row, departmentClassId));
            });

            research_groups_join.collectAsList().forEach(row -> {
                cfOrgUnitTypeList.add(new ResearchGroup(row, researchGroupClassId, cfPersTypeList));
            });

            projects_join.collectAsList().forEach(row -> {
                cfProjTypeList.add(new Project(row, cfPersTypeList));
            });

            publication_join.collectAsList().forEach(row -> {
                cfResPublTypeList.add(new Publication(row, cfPersTypeList));
            });

            String outputPath = Objects.isNull(output) ? String.format("/tmp/%s.xml", ruct) : output.toString();

            marshaller.build(outputPath, formatted,
                    new ArrayList<>(cfPersTypeList.values()), cfOrgUnitTypeList, cfProjTypeList, cfResPublTypeList);

            sparkSession.log().info("Saved output {}", outputPath);
            sparkSession.log().info("Duration {}", TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME));

        } catch (Exception e) {
            System.err.println("Error durant la conversió: " + e.getMessage());
            e.printStackTrace(System.err);
        } finally {
            sparkSession.close();
        }
    }

    /**
     * Llegeix un full d'Excel des del fitxer d'entrada
     *
     * @param spark Sessió Spark
     * @param path Ruta al fitxer Excel
     * @param sheet Full a llegir
     * @return Dataset amb les dades del full
     */
    private Dataset<Row> readSheet(SparkSession spark, String path, SHEETS sheet) {
        return spark
                .read()
                .format("com.crealytics.spark.excel")
                .option("dataAddress", String.format("'%s'!A1", sheet.value()))
                .option("treatEmptyValuesAsNulls", "false")
                .option("maxRowsInMemory", 20)
                .option("header", "true")
                .load(path);
    }
}
