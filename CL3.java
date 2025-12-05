import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
// DO NOT ADD ANY ADDITIONAL IMPORTS ------

public class CL3{
  // TODO: IMPLEMENT main() METHOD LOGIC TO CREATE A GAME AND INTERACT WITH THE USER
  public static void main(String[] args) {
    Game game = new Game();
    boolean quit = false;

    System.out.println("Welcome to Ultimate Battle Simulator! \nLet's get started! \n");
    game.setupGame();

    Scanner scanner = new Scanner(System.in);

    while (!quit) {
      System.out.println("\nWelcome to the Battle Simulator! \nWhat would you like to do? \n\t1. Play a one player game \n\t2. Play a two player game \n\t3. Play a one player game multiverse mayhem \n\t4. Watch a recursive battle \n\t5. Change universe \n\t6. View Game Credits \n\t7. Exit Game");
      String choice = scanner.nextLine(); // read as a string to more easily handle user error

      if (choice.equals("1")) { // one player game 
        game.playOptionOne();
      } else if (choice.equals("2")) { //two player game 
          game.playOptionTwo();
      } else if (choice.equals("3")) { // teleport mode 
          game.playTeleportMode();
      } else if (choice.equals("4")) { // watch a recursive battle 
          game.playOptionFour();
      } else if (choice.equals("5")) { // change universe 
          game.setupGame();
          System.out.println("\nReturning to main menu...");
      } else if (choice.equals("6")) { // game credits 
          System.out.println();
          System.out.println("---------------GAME CREDITS---------------\nCode developed by Arielle Dow \nDecember 5th, 2025 \nSources: CS1301 Unit 1, 2 and 3 Material\n");
          System.out.println("Returning to main menu...");
      } else if (choice.equals("7")) { // exit game 
          System.out.println("Thanks for playing!");
          quit = true;
      } else { // invalid input 
          System.out.println("Invalid input. Please enter a number from 1-7 to make your choice");
      }
    }
  }

  // TODO: Implement any additional methods as needed for CL3 logic.
}