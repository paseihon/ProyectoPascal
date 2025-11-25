package org.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import static org.apache.spark.sql.functions.*;

public class BankETL {

    public static void main(String[] args) {

        SparkSession spark = SparkSession.builder()
                .appName("Bank ETL")
                .master("local[*]")
                .getOrCreate();

        // Cargar CSV
        Dataset<Row> df = spark.read()
                .option("header", "true")
                .option("inferSchema", "true")
                .csv("src/main/java/org/example/bank.csv");

        df.cache();

        //What is the age range, which contract the most loans?
        System.out.println("\n1. Rango de edad con más préstamos:");
        Dataset<Row> Prestamos = df.filter(col("loan").equalTo("yes"));
        Prestamos.groupBy("age")
                .count()
                .orderBy(desc("count"))
                .show();

        //What is the age range and marital status that has more money in the accounts?
        System.out.println("\n2. Edad + estado civil con mayor saldo:");
        df.groupBy("age", "marital")
                .agg(max("balance").alias("max_balance"))
                .orderBy(desc("max_balance"))
                .show();

        // What is the most common way of contacting clients, between 25-35 years old?
        System.out.println("\n3. Forma de contacto más común (25-35):");
        df.filter(col("age").between(25, 35))
                .groupBy("contact")
                .count()
                .orderBy(desc("count"))
                .show();

        //What is the average, maximum and minimum balance for each type of campaign, taking
        //into account their marital status and profession?
        System.out.println("\n4. Stats de saldo por campaña , marital y job:");
        df.groupBy("campaign", "marital", "job")
                .agg(
                        avg("balance").alias("avg_saldo"),
                        max("balance").alias("max_saldo"),
                        min("balance").alias("min_saldo")
                )
                .orderBy("campaign")
                .show();

        //What is the most common type of job, among those who are married (job=married), who
        //have their own house (housing=yes), and who have more than 1,200€ in the account and
        //who are from campaign 3?
        System.out.println("\n5. Trabajo más común (casados, vivienda sí, saldo >1200, campaña 3):");
        df.filter(col("marital").equalTo("married")
                        .and(col("housing").equalTo("yes"))
                        .and(col("balance").gt(1200))
                        .and(col("campaign").equalTo(3)))
                .groupBy("job")
                .count()
                .orderBy(desc("count"))
                .show();

        spark.stop();
    }
}
