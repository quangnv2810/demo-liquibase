package com.demo;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LiquibaseRunner {

    public static void main(String[] args) {
        String databaseUrl = "jdbc:mysql://54.252.38.59:3306/logs";
        String databaseUser = "fptuser";
        String databasePassword = "123456aA@";
        String changeLogFile = "db.changelog-master.xml";
        if (args != null && args.length > 0) {
            if (args[0].equalsIgnoreCase("dev")) {
                changeLogFile = "dev/db.changelog-dev.xml";
            } else if (args[0].equalsIgnoreCase("prod")) {
                changeLogFile = "prod/db.changelog-prod.xml";
            } else if (args[0].equalsIgnoreCase("stg")) {
                changeLogFile = "stg/db.changelog-stg.xml";
            }
        }

        Connection connection = null;
        Liquibase liquibase = null;

        try {
            connection = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

            // Use the current directory and set it as the FileSystemResourceAccessor root
            File currentDir = new File(System.getProperty("user.dir"));

            // Use FileSystemResourceAccessor with the base directory as the root
            liquibase = new Liquibase(changeLogFile, new FileSystemResourceAccessor(currentDir), database);
            liquibase.update(new Contexts(), new LabelExpression());

        } catch (SQLException | LiquibaseException e) {
            e.printStackTrace();
        } finally {
            if (liquibase != null) {
                try {
                    liquibase.close();
                } catch (LiquibaseException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}