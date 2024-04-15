package mealplanner;

import java.util.List;
import java.util.Scanner;

public class Menu {
    private String category;
    private String mealName;
    private String[] ingredients;

    public void add() {

        Scanner sc = new Scanner(System.in);
        List<String> mealOptions = List.of("breakfast", "lunch", "dinner");
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String mealChoice;
        do {
            mealChoice = sc.next().toLowerCase();
            if (!mealOptions.contains(mealChoice)) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            } else {
                category = mealChoice;
            }
        } while (!mealOptions.contains(mealChoice));
        category = sc.nextLine().toLowerCase();
        System.out.println("Input the meal's name: ");
        mealName = sc.nextLine();
        System.out.println("Input the ingredients:");
        ingredients = sc.nextLine().split(", ");
        System.out.println("The meal has been added!");
    }

    public void printMeal() {
        System.out.println("Category: " + category);
        System.out.println("Name: " + mealName);
        System.out.println("Ingredients:");
        for (String ingredient : ingredients) {
            System.out.println(ingredient);
        }
    }
}
