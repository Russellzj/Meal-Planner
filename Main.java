package mealplanner;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
    String meal = sc.next();
    System.out.println("Input the meal's name: ");
  }
}