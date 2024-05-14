package mealplanner;

public class Plan {
    private final int mealId;
    private final String day;
    private final String mealName;
    private final String mealCategory;

    Plan(String day, int mealId, String mealCategory, String mealName) {
        this.day = day;
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealCategory = mealCategory;
    }

    public int getMealId() { return mealId; }
    public String getDay() { return day; }
    public String getMealName() { return mealName; }
    public String getMealCategory() { return mealCategory; }



}


