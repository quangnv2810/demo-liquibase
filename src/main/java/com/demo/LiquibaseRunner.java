package com.demo;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class LiquibaseRunner {

    public static void main(String[] args) {
        String databaseUrl = "jdbc:mysql://54.252.38.59:3306/logs";
        String databaseUser = "ftpuser";
        String databasePassword = "123456aA@";
        String changeLogFile = "db/changelog/db.changelog-master.xml";

        Connection connection = null;
        Liquibase liquibase = null;

        try {
            connection = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            liquibase = new Liquibase(changeLogFile, new ClassLoaderResourceAccessor(), database);

            liquibase.update(new Contexts(), new LabelExpression());

            System.out.println("Database updated successfully!");
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