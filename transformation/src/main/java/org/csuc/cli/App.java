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
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.apache.spark.sql.functions.*;

@CommandLine.Command(
        name = "prc-cerif",
        usageHelpAutoWidth = true,
        version = {"prc-cerif 2.4.17", "CSUC | (c) 2021"},
        mixinStandardHelpOptions = true,
        footerHeading = "System info:",
        footer = {
                "\tPicocli " + CommandLine.VERSION,
                "\tJVM: ${java.version} (${java.vendor} ${java.vm.name} ${java.vm.version})",
                "\tOS: ${os.name} ${os.version} ${os.arch}"
        }
)
public class App implements Runnable {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(names = {"-i", "--input"}, required = true, description = "data file", paramLabel = "<PATH>")
    private Path input;

    @CommandLine.Option(names = {"-f", "--formatted"}, description = "formatted output file (default: ${DEFAULT-VALUE})", paramLabel = "<BOOLEAN>")
    private Boolean formatted = false;

    @CommandLine.Option(names = {"-r", "--ruct"}, required = true, description = "ruct code (https://www.educacion.gob.es/ruct/home)", paramLabel = "<STRING>")
    private String ruct;

    @CommandLine.Option(names = {"-o", "--output"}, description = "output file (default: /tmp/`ruct`.xml)", paramLabel = "<PATH>")
    private Path output;

    private Instant inici;

    public static void main(String[] args) {
        CommandLine cmd = new CommandLine(new App());
        if (args.length == 0) cmd.usage(System.out);
        else cmd.execute(args);
    }

    @Override
    public void run() {
        try {
            inici = Instant.now();

            SparkSession sparkSession = SparkSession.builder().appName("prc-csv2xml").getOrCreate();
            //sparkSession.sparkContext().setLogLevel("WARN");

            Dataset<Row> researchers =
                    sparkSession
                            .read()
                            .format("com.crealytics.spark.excel") // Or .format("excel") for V2 implementation
                            .option("dataAddress", String.format("'%s'!A1", SHEETS.researchers.value())) // Optional, default: "A1"
                            .option("treatEmptyValuesAsNulls", "false") // Optional, default: true
                            .option("maxRowsInMemory", 20)
                            .option("header", "true")
                            .load(input.toString())
                            .toDF("_c0", "_c1", "_c2", "_c3")
                            .na().drop("all")
                            .withColumn("_c1", regexp_replace(col("_c1"), "\\s+", ""))
                            .withColumn("uuid", expr("uuid()"))
                            .alias("researchers");

            Dataset<Row> departments =
                    sparkSession
                            .read()
                            .format("com.crealytics.spark.excel") // Or .format("excel") for V2 implementation
                            .option("dataAddress", String.format("'%s'!A1", SHEETS.departments.value())) // Optional, default: "A1"
                            .option("treatEmptyValuesAsNulls", "false") // Optional, default: true
                            .option("maxRowsInMemory", 20)
                            .option("header", "true")
                            .load(input.toString())
                            .toDF("_c0", "_c1", "_c2", "_c3", "_c4", "_c5", "_c6")
                            .na().drop("all")
                            .withColumn("_c5", regexp_replace(col("_c5"), "\\s+", ""))
                            .withColumn("uuid", expr("uuid()"))
                            .alias("departments");

            Dataset<Row> departments_relations =
                    sparkSession
                            .read()
                            .format("com.crealytics.spark.excel") // Or .format("excel") for V2 implementation
                            .option("dataAddress", String.format("'%s'!A1", SHEETS.departments_relations.value())) // Optional, default: "A1"
                            .option("treatEmptyValuesAsNulls", "false") // Optional, default:
                            .option("maxRowsInMemory", 20)
                            .option("header", "true")
                            .load(input.toString())
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

            Dataset<Row> research_groups =
                    sparkSession
                            .read()
                            .format("com.crealytics.spark.excel") // Or .format("excel") for V2 implementation
                            .option("dataAddress", String.format("'%s'!A1", SHEETS.research_groups.value())) // Optional, default: "A1"
                            .option("treatEmptyValuesAsNulls", "false") // Optional, default: true
                            .option("maxRowsInMemory", 20)
                            .option("header", "true")
                            .load(input.toString())
                            .toDF("_c0", "_c1", "_c2", "_c3", "_c4", "_c5", "_c6")
                            .na().drop("all")
                            .withColumn("_c4", regexp_replace(col("_c4"), "\\s+", ""))
                            .withColumn("uuid", expr("uuid()"))
                            .alias("research_groups");

            Dataset<Row> research_groups_relations =
                    sparkSession
                            .read()
                            .format("com.crealytics.spark.excel") // Or .format("excel") for V2 implementation
                            .option("dataAddress", String.format("'%s'!A1", SHEETS.research_groups_relations.value())) // Optional, default: "A1"
                            .option("treatEmptyValuesAsNulls", "false") // Optional, default: true
                            .option("maxRowsInMemory", 20)
                            .option("header", "true")
                            .load(input.toString())
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

            Dataset<Row> projects =
                    sparkSession
                            .read()
                            .format("com.crealytics.spark.excel") // Or .format("excel") for V2 implementation
                            .option("dataAddress", String.format("'%s'!A1", SHEETS.projects.value())) // Optional, default: "A1"
                            .option("treatEmptyValuesAsNulls", "false") // Optional, default: true
                            .option("maxRowsInMemory", 20)
                            .option("header", "true")
                            .load(input.toString())
                            .toDF("_c0", "_c1", "_c2", "_c3", "_c4", "_c5", "_c6")
                            .na().drop("all")
                            .withColumn("_c3", regexp_replace(col("_c3"), "\\s+", ""))
                            .withColumn("uuid", expr("uuid()"))
                            .alias("projects");

            Dataset<Row> projects_relations =
                    sparkSession
                            .read()
                            .format("com.crealytics.spark.excel") // Or .format("excel") for V2 implementation
                            .option("dataAddress", String.format("'%s'!A1", SHEETS.projects_relations.value())) // Optional, default: "A1"
                            .option("treatEmptyValuesAsNulls", "false") // Optional, default: true
                            .option("maxRowsInMemory", 20)
                            .option("header", "true")
                            .load(input.toString())
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

            Dataset<Row> publications =
                    sparkSession
                            .read()
                            .format("com.crealytics.spark.excel") // Or .format("excel") for V2 implementation
                            .option("dataAddress", String.format("'%s'!A1", SHEETS.publications.value())) // Optional, default: "A1"
                            .option("treatEmptyValuesAsNulls", "false") // Optional, default: true
                            .option("maxRowsInMemory", 20)
                            .option("header", "true")
                            .load(input.toString())
                            .toDF("_c0", "_c1", "_c2", "_c3", "_c4", "_c5", "_c6", "_c7", "_c8", "_c9", "_c10", "_c11", "_c12", "_c13", "_c14")
                            .na().drop("all")
                            .withColumn("_c1", regexp_replace(col("_c1"), "\\s+", ""))
                            //.withColumn("uuid", expr("uuid()"))
                            .alias("publications");

            Dataset<Row> publication_relations =
                    sparkSession
                            .read()
                            .format("com.crealytics.spark.excel") // Or .format("excel") for V2 implementation
                            .option("dataAddress", String.format("'%s'!A1", SHEETS.publication_relations.value())) // Optional, default: "A1"
                            .option("treatEmptyValuesAsNulls", "false") // Optional, default: true
                            .option("maxRowsInMemory", 20)
                            .option("header", "true")
                            .load(input.toString())
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

            //CERIF
            Marshaller marshaller = new Marshaller(ruct);

            CopyOnWriteArrayList<CfPersType> cfPersTypeList = new CopyOnWriteArrayList<>();
            CopyOnWriteArrayList<CfOrgUnitType> cfOrgUnitTypeList = new CopyOnWriteArrayList<>();
            CopyOnWriteArrayList<CfOrgUnitType> cfOrgUnitTypeList_2 = new CopyOnWriteArrayList<>();
            CopyOnWriteArrayList<CfProjType> cfProjTypeList = new CopyOnWriteArrayList<>();
            CopyOnWriteArrayList<CfResPublType> cfResPublTypeList = new CopyOnWriteArrayList<>();

            if (researchers.count() > 0) {
                researchers.collectAsList().forEach(row -> {
                    cfPersTypeList.add(new Researcher(row, Semantics.getClassId(ClassId.CHECKED)));
                });
            }

            if (departments_join.count() > 0) {
                departments_join.collectAsList().forEach(row -> {
                    cfOrgUnitTypeList.add(new Department(row, Semantics.getClassId(ClassId.DEPARTMENT_OR_INSTITUTE)));
                });
            }

            if (research_groups_join.count() > 0) {
                research_groups_join.collectAsList().forEach(row -> {
                    cfOrgUnitTypeList_2.add(new ResearchGroup(row, Semantics.getClassId(ClassId.RESEARCH_GROUP), cfPersTypeList));
                });
            }

            if (projects_join.count() > 0) {
                projects_join.collectAsList().forEach(row -> {
                    cfProjTypeList.add(new Project(row, cfPersTypeList));
                });
            }

            if (publication_join.count() > 0) {
                publication_join.collectAsList().forEach(row -> {
                    cfResPublTypeList.add(new Publication(row, cfPersTypeList));
                });
            }

            if (Objects.isNull(output))
                marshaller.build(String.format("/tmp/%s.xml", ruct), formatted, cfPersTypeList, cfOrgUnitTypeList, cfProjTypeList, cfResPublTypeList);
            else
                marshaller.build(output.toString(), formatted, cfPersTypeList, cfOrgUnitTypeList, cfProjTypeList, cfResPublTypeList);


            sparkSession.log().info("Saved output {}", Objects.isNull(output) ? String.format("/tmp/%s.xml", ruct) : output);
            sparkSession.log().info("Duration {}", TimeUtils.duration(inici, DateTimeFormatter.ISO_TIME));

            sparkSession.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
