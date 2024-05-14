package mealplanner.Enum;

public enum MealCategories {
    BREAKFAST("breakfast"),
    LUNCH("lunch"),
    DINNER("dinner");
    private final String category;

    MealCategories(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public static boolean isCategory(String category) {
        for (MealCategories categoryName : MealCategories.values()) {
            if (categoryName.getCategory().equals(category.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
