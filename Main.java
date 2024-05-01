package mealplanner;
import org.postgresql.util.PSQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.*;


public class Main {
    private static String DB_URL = "jdbc:postgresql:meals_db";
    private static String DB_USER = "postgres";
    private static String DB_PASSWORD = "1111";
    private static int ingredientIDCounter = 1;

    public static void main(String[] args) {
        ArrayList<Meal> menu = new ArrayList<>();
        int mealIDCounter = 1;

        //Create connection and make 2 talbes for holding data
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            try (Statement statement = connection.createStatement()) {
                //statement.execute("DROP TABLE meals");
                //statement.execute("DROP TABLE ingredients");
                statement.execute("CREATE TABLE IF NOT EXISTS meals (" +
                        "category VARCHAR(10)," +
                        "meal VARCHAR(20)," +
                        "meal_id INTEGER PRIMARY KEY)");
                statement.execute("CREATE TABLE IF NOT EXISTS ingredients (" +
                        "ingredient VARCHAR(20)," +
                        "ingredient_id INTEGER PRIMARY KEY," +
                        "meal_id INTEGER)");
                try (ResultSet meals = statement.executeQuery("SELECT * FROM meals")) {
                    while (meals.next()) {
                        mealIDCounter++;
                        Meal newMeal = new Meal();
                        menu.add(newMeal);
                        menu.get(menu.size()-1).add(meals.getString("category"),
                                meals.getString("meal"),
                                getIngredientList(meals.getInt("meal_id")));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);
        //ArrayList to hold all meals created
        boolean continueInput = true; //continues the loop as long as the user does not want to exit
        while (continueInput) {
            System.out.println("What would you like to do (add, show, exit)?");
            String userCommand  = sc.nextLine();
            switch (userCommand) {
                //Adds Meal to the menu
                case "add":
                    Meal newMeal = new Meal();
                    menu.add(newMeal);
                    menu.get(menu.size()-1).addInput();
                    break;
                case "show":
                    if (menu.isEmpty()) {
                        System.out.println("No meals saved. Add a meal first.");
                    } else {
                        System.out.println();
                        for (Meal meal : menu) {
                            System.out.println(meal);
                            System.out.println();
                        }
                    }

                    break;
                case "exit":
                    continueInput = false;
                    break;
                default:
                    //System.out.println("Invalid command");
            }
        }

        int mealsAdded = 0;
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement statement = con.createStatement();
            String insert = "INSERT INTO meals (category, meal, meal_id) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insert)) {
                for (Meal meal : menu) {
                    if (meal.isNewData()) {
                        mealsAdded++;
                        //System.out.println("adding meal " + meal.getCategory() + " meal name: " + meal.getMealName());
                        ps.setString(1, meal.getCategory());
                        ps.setString(2, meal.getMealName());
                        ps.setInt(3, mealIDCounter);
                        ps.executeUpdate();
                        String insert2 = "INSERT INTO ingredients (ingredient, meal_id, ingredient_id) VALUES (?, ?, ?)";
                        try (PreparedStatement ps2 = con.prepareStatement(insert2)) {
                            for (String ingredient : meal.getIngredients()) {
                                //System.out.println("Adding ingredient: " + ingredient);
                                ps2.setString(1, ingredient);
                                ps2.setInt(2, mealIDCounter);
                                ps2.setInt(3, ingredientIDCounter++);
                                ps2.executeUpdate();
                            }
                            mealIDCounter++;

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }




        System.out.println("Bye!");
    }

    public static List<String> getIngredientList(int meal_id) {
        List<String> ingredientList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            try (Statement statement = connection.createStatement()) {
                ResultSet ingredients = statement.executeQuery("SELECT * FROM ingredients WHERE meal_id = " + meal_id);
                while (ingredients.next()) {
                    ingredientList.add(ingredients.getString("ingredient"));
                    ingredientIDCounter++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredientList;
    }
}