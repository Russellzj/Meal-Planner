package mealplanner;

import java.util.Scanner;
import java.sql.*;


public class Main {

    public static void main(String[] args) throws SQLException {
        Database database = new Database();
        //database.deleteTables();

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
                    database.addMeal(newMeal);
                    break;
                case "show":
                    System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");
                    String catergroy = sc.nextLine();
                    while (!database.getMealOptions().contains(catergroyChoice)) {
                        System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                        catergroy = scanner.nextLine();
                    }
                    database.getMealsByCategory(catergroy);
                    break;
                case "exit":
                    continueInput = false;
                    break;
                default:
                    //System.out.println("Invalid command");
            }
        }
        System.out.println("Bye!");
    }
}