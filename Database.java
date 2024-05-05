package mealplanner;

import java.util.*;
import java.sql.*;

public class Database {
    private String DB_URL;
    private String DB_USER;
    private String DB_PASSWORD;
    private int totalIngredients = 1;
    private int totalMeals = 1;

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
        Statement statement = createConnection();
        statement.executeUpdate("CREATE TABLE " + tableSpecs);
        //System.out.println("Creating table");
    }

    public void deleteTable(String table) throws SQLException {
        Statement statement = createConnection();
        statement.executeUpdate("DROP TABLE " + table);
        System.out.println("Deleting table " + table);
    }

    public void readTable(String table) throws SQLException {
        Statement statement = createConnection();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + table);
        while (resultSet.next()) {
            System.out.println(resultSet.getString(1));
        }
    }

    public List<String> getIngredientList(int meal_id) throws SQLException {
        List<String> ingredientList = new ArrayList<>();
        Statement statement = createConnection();
        ResultSet ingredients = statement.executeQuery("SELECT * FROM ingredients WHERE meal_id = " + meal_id);
        while (ingredients.next()) {
            ingredientList.add(ingredients.getString("ingredient"));
            totalIngredients++;
        }
        return ingredientList;
    }

    public List<Meal> getMeals() throws SQLException {
        List<Meal> menu = new ArrayList<>();
        Statement statement = createConnection();
        ResultSet meals = statement.executeQuery("SELECT * FROM meals");
        while (meals.next()) {
            Meal newMeal = new Meal(meals.getString("category"),
                    meals.getString("meal"), getIngredientList(meals.getInt("meal_id")));
            totalMeals++;
            menu.add(newMeal);
        }
        return menu;
    }

    public void addDataToDatabase(List<Meal> menu) throws SQLException {
        Statement statement = createConnection();
        for (Meal meal : menu) {
            if (meal.isNewData()) {
                statement.executeUpdate("INSERT INTO meals VALUES ('%s', '%s', '%d')".formatted(
                        meal.getCategory(), meal.getMealName(), totalMeals));
                for(String ingredient : meal.getIngredients()) {
                    statement.executeUpdate("INSERT INTO ingredients VALUES ('%s', '%d', %d)".formatted(
                            ingredient, totalIngredients++, totalMeals));
                }
                totalMeals++;
            }
        }
    }

    public void printAllMeals() throws SQLException {
        //List<Meal> menu = new ArrayList<>();
        Statement statementForMeals = createConnection();
        Statement statementForIngredients = createConnection();
        ResultSet resultSet = statementForMeals.executeQuery("SELECT * FROM meals");
        while (resultSet.next()) {
            System.out.printf("Category: %s\nName: %s\n",
                    resultSet.getString("category"), resultSet.getString("meal"));
            //Retrieves and prints the meal's ingredients based on its meal ID
            System.out.println("Ingredients: ");
            ResultSet ingredients = statementForIngredients.executeQuery("SELECT * FROM ingredients WHERE meal_id = " +
                    resultSet.getInt("meal_id"));
            while (ingredients.next()) {
                System.out.println(ingredients.getString("ingredient"));
            }
            System.out.println();
        }
    }
}
