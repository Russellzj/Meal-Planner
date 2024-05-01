package mealplanner;

import java.sql.*;

public class Database {
    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;
    private int totalIngredients = 0;
    private int totalMeals = 0;

    public Database(String databaseName, String DB_USER, String DB_PASSWORD) throws SQLException {
        this.DB_URL = "jdbc:postgresql:" + databaseName;
        this.DB_USER = DB_USER;
        this.DB_PASSWORD = DB_PASSWORD;
    }

    private Statement createConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        connection.setAutoCommit(true);
        Statement stmt = connection.createStatement();
        return stmt;
    }






    public void creatTable(String tableSpecs) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        connection.setAutoCommit(true);
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE TABLE " + tableSpecs);
        connection.close();
        System.out.println("Creating table");
    }

    public void deleteTable(String table) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        connection.setAutoCommit(true);
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP TABLE " + table);
        connection.close();
        System.out.println("Deleting table " + table);
    }



    public void readTable(String table) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        connection.setAutoCommit(true);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
    }
}
