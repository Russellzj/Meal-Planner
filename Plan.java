package mealplanner;

import mealplanner.Enum.DaysOfTheWeek;
import mealplanner.Enum.MealCategories;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Plan {
    private final int mealId;
    private final String day;
    private final String mealName;
    private final String mealCategory;


    Plan(String day, String mealCategory, int mealId, String mealName) {
        this.day = day;
        this.mealCategory = mealCategory;
        this.mealId = mealId;
        this.mealName = mealName;

    }

    public void setPlan() {
    }

    //Getters for plan
    public int getMealId() { return mealId; }
    public String getDay() { return day; }
    public String getMealName() { return mealName; }
    public String getMealCategory() { return mealCategory; }

    @Override
    public String toString() {
        String category = mealCategory;
        category = category.substring(0, 1).toUpperCase() + category.substring(1);
        return ("%s: %s").formatted(category, mealName);
    }

    public static List<Plan> createPlan() {
        List<Plan> plans = new ArrayList<Plan>();
        Database db = new Database();
        Scanner sc = new Scanner(System.in);
        for (DaysOfTheWeek day : DaysOfTheWeek.values()) {
            System.out.println(day.getDay());
            for (MealCategories category : MealCategories.values()) {
                for (Meal mealOption : db.getMealsByCategory(category.getCategory(), "meal")) {
                    System.out.println(mealOption.getMealName());
                }
                System.out.printf("Choose the %s for %s from the list above:\n", category, day.getDay());
                Meal chosenMeal = null;
                do {
                    chosenMeal = db.getMealByNameAndCategory(sc.nextLine(), category.getCategory());
                    if (chosenMeal != null)
                        plans.add(new Plan(day.getDay(), chosenMeal.getCategory(), chosenMeal.getMealId(), chosenMeal.getMealName()));
                    else
                        System.out.println("This meal doesn’t exist. Choose a meal from the list above.");
                } while (chosenMeal == null);
            }
            System.out.printf("Yeah! We planned the meals for %s.\n", day.getDay());
        }
        return plans;
    }

    public static void printPlans(List<Plan> plans) {
        String day = "";
        for (Plan plan : plans) {
            if (!plan.getDay().equals(day)) {
                day = plan.getDay();
                System.out.println(day);
            }
            System.out.println(plan);
        }

    }
}


