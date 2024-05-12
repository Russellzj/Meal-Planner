package mealplanner;
import java.util.List;

public interface MealDao {
    void addMeal(Meal meal);
    //void updateMeal(Meal meal);
    //void deleteMeal(Meal meal);
    List<Meal> getMealsByCategory(String category);
    List<Meal> getAllMeals();
}
