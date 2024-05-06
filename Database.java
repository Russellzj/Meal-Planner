package mealplanner;

import java.util.*;
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

    //Retrieves total meals
    public int getTotalMeals(){
        return totalMeals;
    }

    //Creates a table in the database
    public void creatTable(String tableSpecs) throws SQLException {
        Statement statement = createConnection();
        statement.executeUpdate("CREATE TABLE " + tableSpecs);
        //System.out.println("Creating table");
    }

    //Removes a table from the database
    public void deleteTable(String table) throws SQLException {
        Statement statement = createConnection();
        statement.executeUpdate("DROP TABLE " + table);
        System.out.println("Deleting table " + table);
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
        while (lastIngredientID.next()){
            totalIngredients = lastIngredientID.getInt("ingredient_id_max");
        }
    }

    @Deprecated
    //Adds multiple meals to the database from a List
    public void addMultipleMealsToDatabase(List<Meal> menu) throws SQLException {
        Statement statement = createConnection();
        getLastMealID();
        getLastIngredientID();
        totalMeals++;
        for (Meal meal : menu) {
            statement.executeUpdate("INSERT INTO meals VALUES ('%s', '%s', '%d')".formatted(
                    meal.getCategory(), meal.getMealName(), totalMeals));
            for(String ingredient : meal.getIngredients()) {
                statement.executeUpdate("INSERT INTO ingredients VALUES ('%s', '%d', %d)".formatted(
                        ingredient, ++totalIngredients, totalMeals));
            }
            totalMeals++;

        }
    }

    //Adds a single meal to the database
    public void addSingleMealToDatabase(Meal meal) throws SQLException {
        getLastMealID();
        getLastIngredientID();
        Statement statement = createConnection();
            statement.executeUpdate("INSERT INTO meals VALUES ('%s', '%s', '%d')".formatted(
                    meal.getCategory(), meal.getMealName(), ++totalMeals));
            for(String ingredient : meal.getIngredients()) {
                statement.executeUpdate("INSERT INTO ingredients VALUES ('%s', '%d', %d)".formatted(
                        ingredient, ++totalIngredients, totalMeals));
            }
    }

    //Prints all meals
    public void printAllMeals() throws SQLException {
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

    //Prints meals based on a category chosen by the user
    public void printMealsByCategory() throws SQLException {
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
        List<String> mealOptions = List.of("breakfast", "lunch", "dinner");
        Scanner scanner = new Scanner(System.in);
        String category = scanner.nextLine();
        //Makes sure that the input from the user matches the available categories
        while (!mealOptions.contains(category)) {
            System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            category = scanner.nextLine();
        }
        //
        int numberOfResults = 0;
        //Open 2 statements one for querying the meals table and the other for the ingredients table
        Statement statementForMeals = createConnection();
        Statement statementForIngredients = createConnection();
        ResultSet resultSet = statementForMeals.executeQuery(String.format(
                "SELECT * FROM meals WHERE category = '%s'", category));
        while (resultSet.next()) {
            //prints Category only if this is the start of the ResultSet
            if (numberOfResults == 0) {
                System.out.println("Category: " + category);
            }
            numberOfResults++;
            System.out.println();
            System.out.printf("Name: %s\n",
                    resultSet.getString("meal"));
            //Retrieves and prints the meal's ingredients based on its meal ID
            System.out.println("Ingredients: ");
            ResultSet ingredients = statementForIngredients.executeQuery("SELECT * FROM ingredients WHERE meal_id = " +
                    resultSet.getInt("meal_id"));
            while (ingredients.next()) {
                System.out.println(ingredients.getString("ingredient"));
            }
            System.out.println();
        }
        if (numberOfResults == 0) {
            System.out.println("No meals found.");
        }
    }
}
