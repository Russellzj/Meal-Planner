package mealplanner;
import java.util.List;

public interface MealDao {
    void addMeal(Meal meal);
    //void updateMeal(Meal meal);
    //void deleteMeal(Meal meal);
    void deletePlan();
    List<Meal> getAllMeals();
    List<Meal> getMealsByCategory(String category, String order);
    Meal getMealByName(String name);
    List<String> getIngredientsByMealId(int mealId);
    void updatePlan(Meal meal, String day);

}
