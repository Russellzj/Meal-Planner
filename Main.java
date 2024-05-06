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

    public static void main(String[] args) throws SQLException {
        Database database = new Database("meals_db", DB_USER, DB_PASSWORD);
        //database.deleteTable("meals");
        //database.deleteTable("ingredients");

        //Creates table for meals and ingredients if none exists
        database.creatTable("IF NOT EXISTS meals (" +
                "category VARCHAR(10)," +
                "meal VARCHAR(20)," +
                "meal_id INTEGER PRIMARY KEY)");
        database.creatTable("IF NOT EXISTS ingredients (" +
                "ingredient VARCHAR(20)," +
                "ingredient_id INTEGER PRIMARY KEY," +
                "meal_id INTEGER)");


        boolean continueInput = true; //continues the loop as long as the user does not want to exit

        while (continueInput) {
            Scanner sc = new Scanner(System.in);
            System.out.println("What would you like to do (add, show, exit)?");
            String userCommand  = sc.nextLine();
            switch (userCommand) {
                //Adds Meal to the menu
                case "add":
                    Meal newMeal = new Meal();
                    newMeal.addInput();
                    database.addSingleMealToDatabase(newMeal);
                    break;
                case "show":
                    database.printMealsByCategory();
                    //if (database.getTotalMeals() == 0)
                    break;
                case "exit":
                    continueInput = false;
                    break;
                default:
                    //System.out.println("Invalid command");
            }
        }
        System.out.println("Bye!");
        //database.addMultipleMealsToDatabase(menu);
    }
}