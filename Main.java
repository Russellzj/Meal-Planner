package mealplanner;

import java.util.List;
import java.util.Scanner;
import java.sql.*;


public class Main {

    public static void main(String[] args) throws SQLException {
        Database database = new Database();
        //database.deleteTables();

        boolean continueInput = true; //continues the loop as long as the user does not want to exit

        while (continueInput) {
            Scanner sc = new Scanner(System.in);
            System.out.println("What would you like to do (add, show, plan, exit)?");
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
                    //Checks user input to make sure it is part of the available options
                    String catergroy = sc.nextLine();
                    while (!database.getCategoryOptions().contains(catergroy)) {
                        System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
                        catergroy = sc.nextLine();
                    }
                    List<Meal> meals = database.getMealsByCategory(catergroy, "meal_id");
                    if (meals.size() == 0) {
                        System.out.println("No meals found.");
                    } else {
                        System.out.println("Category: " + catergroy + "\n");
                    }
                    for (Meal meal : meals) {
                        System.out.println(meal.toStringWOCategory());
                        System.out.println();
                    }
                    break;
                case "plan":
                    database.deletePlan();
                    for (String day : database.getDaysOfTheWeek()) {
                        System.out.println(day);
                        for (String category : database.getCategoryOptions()) {
                            List<Meal> mealOptions = database.getMealsByCategory(category, "meal");
                            for (Meal meal : mealOptions) {
                                System.out.println(meal.getMealName());
                            }
                            System.out.printf("Choose the %s for %s from the list above:\n", category, day);
                            Meal chosenMeal = null;
                            do {
                                chosenMeal = database.getMealByName(sc.nextLine());
                                if (chosenMeal != null) {
                                    database.updatePlan(chosenMeal, day);
                                } else {
                                    System.out.println("This meal doesn’t exist. Choose a meal from the list above.\n");
                                }
                            } while (chosenMeal == null);
                        }
                        System.out.printf("Yeah! We planned the meals for %s.\n\n", day);
                    }
                    for (String day : database.getDaysOfTheWeek()) {
                        System.out.println("\n" + day);
                        for (String category : database.getCategoryOptions()) {
                            Plan plan = database.getPlanByDayAndCategory(day, category);
                            System.out.printf("%s: %s\n", category, plan.getMealName());
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
    }
}