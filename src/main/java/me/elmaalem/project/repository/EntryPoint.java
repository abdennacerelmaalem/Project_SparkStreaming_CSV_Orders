package me.elmaalem.project.repository;

import me.elmaalem.project.model.Orders;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

public class EntryPoint {

    public EntryPoint() { }

    private static SparkSession sparkSession(){
        return SparkSession
                .builder()
                .appName(" Application with Spark Streaming and Java")
                .master("local[*]")
                .getOrCreate();
    }

    private static StructType customSchema(){
        return new StructType(new StructField[] {
                new StructField("orderId", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("date", DataTypes.DateType, true, Metadata.empty()),
                new StructField("quantity", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("sales", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("mode", DataTypes.StringType, true, Metadata.empty()),
                new StructField("profit", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("unitPrice", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("customerName", DataTypes.StringType, true, Metadata.empty()),
                new StructField("customerSegment", DataTypes.StringType, true, Metadata.empty()),
                new StructField("productCategory", DataTypes.StringType, true, Metadata.empty())
        });
    }

    public static Dataset<Orders> getDataset(){
        Encoder<Orders> orderEncoder = Encoders.bean(Orders.class);

        return sparkSession()
                .readStream()
                .option("header","true")
                .option("treatEmptyValuesAsNulls", "true")
                .option("dateFormat", "MM-dd-yyyy")
                .option("delimiter",",")
                .schema(customSchema())
                .csv("src/main/resources/data/Orders*.csv")
                .as(orderEncoder);
    }
}
