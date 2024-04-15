package mealplanner;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Menu newMeal = new Menu();
        newMeal.add();
        newMeal.printMeal();
        boolean continueInput = true; //continues the loop as long as the user does not want to exit
        while (continueInput) {
            System.out.println("Would you like to do(add, show exit)?");
            String userCommand  = sc.next();
            switch (userCommand) {
                case "add":
                    System.out.println("ADD");
                    break;
                case "show":
                    System.out.println("SHOW");
                    break;
                case "exit":
                    continueInput = false;
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }
    }
}