package mealplanner;

public enum DaysOfTheWeek {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private final String day;

    DaysOfTheWeek(String day) {
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public static boolean isDay(String day) {
        for (DaysOfTheWeek dayName : DaysOfTheWeek.values()) {
            if (dayName.getDay().equals(day.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
