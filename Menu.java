package mealplanner;

import java.util.Scanner;

public class Menu {
    private String meal;
    private String mealName;
    private String[] ingredients;

    public void add() {

        Scanner sc = new Scanner(System.in);
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        meal = sc.nextLine();
        System.out.println("Input the meal's name: ");
        mealName = sc.nextLine();
        System.out.println("Input the ingredients:");
        ingredients = sc.nextLine().split(",");
        printMeal();
        System.out.println("The meal has been added!");
    }

    public void printMeal() {
        System.out.println("Category: " + meal);
        System.out.println("Name: " + mealName);
        System.out.println("Ingredients:");
        for (String ingredient : ingredients) {
            System.out.println(ingredient);
        }
    }
}
