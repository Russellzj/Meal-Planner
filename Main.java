package mealplanner;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        //ArrayList to hold all meals created
        ArrayList<Meal> menu = new ArrayList<>();
        boolean continueInput = true; //continues the loop as long as the user does not want to exit
        while (continueInput) {
            System.out.println("Would you like to do(add, show exit)?");
            String userCommand  = sc.next();
            switch (userCommand) {
                //Adds Meal to the menu
                case "add":
                    Meal newMeal = new Meal();
                    menu.add(newMeal);
                    menu.get(menu.size()-1).add();
                    break;
                case "show":
                    System.out.println();
                    for (Meal meal : menu) {
                        System.out.println(meal);
                        System.out.println();
                    }
                    break;
                case "exit":
                    continueInput = false;
                    break;
                default:
                    System.out.println("Invalid command");
            }
        } System.out.println("Bye!");
    }
}