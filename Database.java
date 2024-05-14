package mealplanner;

import java.util.*;
import java.sql.*;


public class Database implements MealDao {
    private static final String DB_URL = "jdbc:postgresql:meals_db";
    private final String DB_USER = "postgres";
    private final String DB_PASSWORD = "1111";
    private static final String mealsTable = "CREATE TABLE IF NOT EXISTS meals (" +
            "category VARCHAR(10)," +
            "meal VARCHAR(20)," +
            "meal_id INTEGER PRIMARY KEY)";
    private static final String ingredientsTable = "CREATE TABLE IF NOT EXISTS ingredients (" +
            "ingredient Varchar(30)," +
            "ingredient_id INTEGER PRIMARY KEY," +
            "meal_id INTEGER)";
    public static final String planTable = "CREATE TABLE IF NOT EXISTS plan (" +
            "day VARCHAR(10)," +
            "meal_category VARCHAR(10)," +
            "meal_id INTEGER)";

    public Database() {
        createTables();
    }

    //Creates connection to the database
    private Statement createConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement stmt = connection.createStatement();
        return stmt;
    }

    private void runDatabase (String command) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement stmt = connection.createStatement();
            stmt.execute(command);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Creates a table in the database
    private void createTables() {
        runDatabase(mealsTable);
        runDatabase(ingredientsTable);
        runDatabase(planTable);
    }

    //Removes a table from the database
    public void deleteTables() {
        runDatabase("DROP TABLE meals");
        runDatabase("DROP TABLE ingredients");
        System.out.println("Tables meals and ingredients deleted.");
    }

    //Deletes and recreates plan table
    public void resetPlan() {
        runDatabase("DROP TABLE plan");
        runDatabase(planTable);
    }

    private List<Meal> queryMeals(String query) {
        List<Meal> meals = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM meals " + query);
            while (rs.next()) {
                Meal meal = new Meal(
                        rs.getInt("meal_id"),
                        rs.getString("category"),
                        rs.getString("meal"),
                        getIngredientsByMealId(rs.getInt("meal_id")));
                meals.add(meal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return meals;
    }

    private Meal queryMealsSingleResult(String query) {
        Meal meal = null;
        int results = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM meals " + query);
            while (rs.next()) {
                meal = new Meal(
                        rs.getInt("meal_id"),
                        rs.getString("category"),
                        rs.getString("meal"),
                        getIngredientsByMealId(rs.getInt("meal_id")));
                results++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (results > 1) {
            throw new RuntimeException("There were more than one meals returned.");
        }
        return meal;
    }



    private Plan queryPlan(String query)  {
        Plan plan = null;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM plan WHERE " + query);
            while (rs.next()) {
                Meal meal = queryMealsSingleResult("WHERE meal_id = %d".formatted(rs.getInt("meal_id")));
                plan = new Plan(
                        rs.getString("day"),
                        rs.getInt("meal_id"),
                        rs.getString("meal_category"),
                        meal.getMealName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plan;
    }

    //Retrieves the highest value used from meals - Used mainly for creating the primary key
    public int getLastMealID() {
        int lastID = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT MAX(meal_id) AS meal_id_max FROM meals");
            while (rs.next()) {
                lastID = rs.getInt("meal_id_max");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastID;
    }

    //Retrieves the highest value used from ingredients - Used mainly for creating the primary key
    public int getLastIngredientID() {
        int lastIngredientID = 0;
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement stmt = connection.createStatement();
            ResultSet rs =
                    stmt.executeQuery("SELECT MAX(ingredient_id) AS ingredient_id_max FROM ingredients");
            while (rs.next()) {
                lastIngredientID = rs.getInt("ingredient_id_max");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lastIngredientID;
    }

    //Adds a meal to the database including its ingredients
    public void addMeal(Meal meal) {
        int lastIngredientId = getLastIngredientID();
        int lastMealId = getLastMealID();
        runDatabase("INSERT INTO meals VALUES ('%s', '%s', '%d')".formatted(
                meal.getCategory(), meal.getMealName(), ++lastMealId));
        for (String ingredient : meal.getIngredients()) {
            runDatabase("INSERT INTO ingredients VALUES ('%s', '%d', %d)".formatted(
                    ingredient, ++lastIngredientId, lastMealId));
        }
    }

    //returns a list of all the meals in the database
    @Override
    public List<Meal> getAllMeals() {
        List<Meal> meals = new ArrayList<>();
        meals = queryMeals("");
        return meals;
    }

    //Prints meals based on a category chosen by the user
    public List<Meal> getMealsByCategory(String category, String order) {
        List<Meal> meals = new ArrayList<>();
        //Open 2 statements one for querying the meals table and the other for the ingredients table
        String query = "WHERE category = '%s' ORDER BY %s".formatted(category, order);
        meals = queryMeals(query);
        return meals;
    }



    //retrieves meal by meal name
    public Meal getMealByName(String mealName) {
        return queryMealsSingleResult("WHERE meal = '%s'".formatted(mealName));
    }

    public Plan getPlanByDayAndCategory(String day, String category){
        Plan plan = null;
        plan = queryPlan("day = '%s' AND meal_category = '%s'".formatted(day, category));
        return plan;
    }

    @Override
    public List<String> getIngredientsByMealId(int mealId) {
        List<String> ingredients = new ArrayList<>();
        try (Statement statement = createConnection()) {
            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM ingredients WHERE meal_id = '%s'",
                    mealId));
            while (rs.next()) {
                ingredients.add(rs.getString("ingredient"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    public void updatePlan(Meal meal, String day) {
        runDatabase("INSERT INTO plan VALUES ('%s', '%s', '%d')".formatted(
                day, meal.getCategory(), meal.getMealId()));
    }
}