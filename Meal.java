package mealplanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Meal {
    private String category;
    private String mealName;
   private final boolean newData;
    private List<String> ingredients;

    Meal(String category, String mealName, List<String> ingredients) {
        this.category = category;
        this.mealName = mealName;
        this.ingredients = ingredients;
        this.newData = false;
    }

    public Meal() {
        this.newData = true;
    }

    public void addInput() {

        Scanner sc = new Scanner(System.in);

        //Chooses meal category
        List<String> mealOptions = List.of("breakfast", "lunch", "dinner");
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        String mealChoice;
        //Makes sure that the category chosen is from the acceptable list mealOptions
        do {
            mealChoice = sc.nextLine().toLowerCase();
            if (!mealOptions.contains(mealChoice)) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            } else {
                category = mealChoice;
            }
        } while (!mealOptions.contains(mealChoice));

        //Adds meal name to meal
        System.out.println("Input the meal's name: ");
        String mealName;
        do {
            mealName = sc.nextLine();
            if (!mealName.matches("[a-zA-Z ]+")) {
                System.out.println("Wrong format. Use letters only!");
            } else {
                this.mealName = mealName;
            }
        } while (!mealName.matches("[a-zA-Z ]+"));
        //Add Ingredients to meal
        String newIngredient;
        System.out.println("Input the ingredients:");
        //Loops makes sure that there is no special characters or numbers in the ingredient list
        String regexIngredient = "[a-zA-Z]+( *, *[a-zA-Z]+( +[a-zA-Z]+)*)*";

        do {
            newIngredient = sc.nextLine();
            if (!newIngredient.matches(regexIngredient))
                System.out.println("Wrong format. Use letters only!");
        } while (!newIngredient.matches(regexIngredient));

        String[] ingredientArray = newIngredient.split(", ");
        ingredients = new ArrayList<>();

        for (String ingredient : ingredientArray) {
            ingredients.add(ingredient);
        }
        System.out.println("The meal has been added!");
    }

    @Override
    public String toString() {
        return String.format("Category: %s\nName: %s\nIngredients:\n%s",
                category, mealName, String.join("\n", ingredients));
    }

    public String getCategory() {
        return category;
    }

    public String getMealName() {
        return mealName;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public boolean isNewData() {
        return newData;
    }

}
