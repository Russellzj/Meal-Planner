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
        database.creatTable("IF NOT EXISTS meals (" +
                "category VARCHAR(10)," +
                "meal VARCHAR(20)," +
                "meal_id INTEGER PRIMARY KEY)");
        database.creatTable("IF NOT EXISTS ingredients (" +
                "ingredient VARCHAR(20)," +
                "ingredient_id INTEGER PRIMARY KEY," +
                "meal_id INTEGER)");
        List<Meal> menu = database.getMeals();

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
        System.out.println("Bye!");
        database.addDataToDatabase(menu);
    }
}