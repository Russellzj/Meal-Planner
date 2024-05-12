package mealplanner;

import java.util.*;
import java.sql.*;

public class Database implements MealDao {
    private static final String DB_URL = "jdbc:postgresql:meals_db";
    private String DB_USER = "postgres";
    private String DB_PASSWORD = "1111";
    private static final String mealsTable = "CREATE TABLE IF NOT EXISTS meals (" +
            "category VARCHAR(10)," +
            "meal VARCHAR(20)," +
            "meal_id INTEGER PRIMARY KEY)";
    private static final String ingredientsTable = "CREATE TABLE IF NOT EXISTS ingredients (" +
            "ingedient Varchar(10)," +
            "ingredient_id INTEGER PRIMARY KEY," +
            "meal_id INTEGER)";
    private static final List<String> mealOptions = List.of("breakfast", "lunch", "dinner");
    private int totalIngredients = 0;
    private int totalMeals = 0;

    public Database() {
        createTables();
    }

    //Creates connection to the database
    private Statement createConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        connection.setAutoCommit(true);
        Statement stmt = connection.createStatement();
        return stmt;
    }

    //Creates a table in the database
    public void createTables() {
        try (Statement statement = createConnection()) {
            statement.executeUpdate(mealsTable);
            statement.executeUpdate(ingredientsTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Retrieves total meals
    public int getTotalMeals() {
        return totalMeals;
    }

    public List<String> getMealOptions() {
        return mealOptions;
    }

    //Removes a table from the database
    public void deleteTables() {
        try (Statement statement = createConnection();) {
            statement.executeUpdate("DROP TABLE meals");
            statement.executeUpdate("DROP TABLE ingredients");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Tables meals and ingredients deleted.");
    }

    @Deprecated
    public List<String> getIngredientList(int meal_id) throws SQLException {
        List<String> ingredientList = new ArrayList<>();
        Statement statement = createConnection();
        ResultSet ingredients = statement.executeQuery("SELECT * FROM ingredients WHERE meal_id = " + meal_id);
        while (ingredients.next()) {
            ingredientList.add(ingredients.getString("ingredient"));
        }
        return ingredientList;
    }

    @Deprecated
    public List<Meal> getMeals() throws SQLException {
        List<Meal> menu = new ArrayList<>();
        Statement statement = createConnection();
        ResultSet meals = statement.executeQuery("SELECT * FROM meals");
        while (meals.next()) {
            Meal newMeal = new Meal(meals.getString("category"),
                    meals.getString("meal"), getIngredientList(meals.getInt("meal_id")));
            menu.add(newMeal);
        }
        return menu;
    }

    //Retrieves the highest value used from meals - Used mainly for creating the primary key
    public void getLastMealID() throws SQLException {
        Statement statement = createConnection();
        ResultSet lastMealID =
                statement.executeQuery("SELECT MAX(meal_id) AS meal_id_max FROM meals");
        while (lastMealID.next()) {
            totalMeals = lastMealID.getInt("meal_id_max");
        }
    }

    //Retrieves the highest value used from ingredients - Used mainly for creating the primary key
    public void getLastIngredientID() throws SQLException {
        Statement statement = createConnection();
        ResultSet lastIngredientID =
                statement.executeQuery("SELECT MAX(ingredient_id) AS ingredient_id_max FROM ingredients");
        while (lastIngredientID.next()) {
            totalIngredients = lastIngredientID.getInt("ingredient_id_max");
        }
    }

    @Deprecated
    //Adds multiple meals to the database from a List
    public void addMeals(List<Meal> menu) throws SQLException {
        Statement statement = createConnection();
        getLastMealID();
        getLastIngredientID();
        totalMeals++;
        for (Meal meal : menu) {
            statement.executeUpdate("INSERT INTO meals VALUES ('%s', '%s', '%d')".formatted(
                    meal.getCategory(), meal.getMealName(), totalMeals));
            for (String ingredient : meal.getIngredients()) {
                statement.executeUpdate("INSERT INTO ingredients VALUES ('%s', '%d', %d)".formatted(
                        ingredient, ++totalIngredients, totalMeals));
            }
            totalMeals++;

        }
    }

    //Adds a single meal to the database
    public void addMeal(Meal meal) {
        try {
            getLastMealID();
            getLastIngredientID();
            Statement statement = createConnection();
            statement.executeUpdate("INSERT INTO meals VALUES ('%s', '%s', '%d')".formatted(
                    meal.getCategory(), meal.getMealName(), ++totalMeals));
            for (String ingredient : meal.getIngredients()) {
                statement.executeUpdate("INSERT INTO ingredients VALUES ('%s', '%d', %d)".formatted(
                        ingredient, ++totalIngredients, totalMeals));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //returns a list of all the meals in the database
    @Override
    public List<Meal> getAllMeals() {
        List<Meal> meals = new ArrayList<>();
        //Open 2 statements one for querying the meals table and the other for the ingredients table
        try {
            Statement statementForMeals = createConnection();
            Statement statementForIngredients = createConnection();
            ResultSet resultSet = statementForMeals.executeQuery(String.format(
                    "SELECT * FROM meals"));
            while (resultSet.next()) {
                List<String> mealIngredients = new ArrayList<>();
                //retrieves the ingredients of the selected meal
                ResultSet ingredients = statementForIngredients.executeQuery("SELECT * FROM ingredients WHERE meal_id = " +
                        resultSet.getInt("meal_id"));
                while (ingredients.next()) {
                    mealIngredients.add(ingredients.getString("ingredient"));
                }
                //Creates meal from results
                Meal meal = new Meal(
                        resultSet.getString("category"),
                        resultSet.getString("meal"),
                        mealIngredients);

                meals.add(meal);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return meals;
    }

    //Prints meals based on a category chosen by the user
    public List<Meal> getMealsByCategory(String category) {
        List<Meal> meals = new ArrayList<>();
        //Open 2 statements one for querying the meals table and the other for the ingredients table
        try {
            Statement statementForMeals = createConnection();
            Statement statementForIngredients = createConnection();
            ResultSet resultSet = statementForMeals.executeQuery(String.format(
                    "SELECT * FROM meals WHERE category = '%s'", category));
            while (resultSet.next()) {
                List<String> mealIngredients = new ArrayList<>();
                //retrieves the ingredients of the selected meal
                ResultSet ingredients = statementForIngredients.executeQuery("SELECT * FROM ingredients WHERE meal_id = " +
                        resultSet.getInt("meal_id"));
                while (ingredients.next()) {
                    mealIngredients.add(ingredients.getString("ingredient"));
                }
                //Creates meal from results
                Meal meal = new Meal(
                        resultSet.getString("category"),
                        resultSet.getString("meal"),
                        mealIngredients);

                meals.add(meal);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return meals;
    }

}