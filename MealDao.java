package mealplanner;

public interface MealDao {
    void addMeal(Meal meal);
    //void updateMeal(Meal meal);
    //void deleteMeal(Meal meal);
    void getMealsByCategory(String category);
    void getAllMeals();
}
