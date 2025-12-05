import java.io.File;
import java.util.Scanner;
import java.util.Random;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
// DO NOT ADD ANY ADDITIONAL IMPORTS ------

public class Game {
	// TODO: Attributes
	File file;
	int numCharacters;
	Character[] characters;
	Character playerOne;
	Character playerTwo;
	
	// Default Constructor - DO NOT MODIFY
	public Game() {};

	// TODO: Implement setupGame() - DO NOT MODIFY METHOD HEADER
	public void setupGame() {
		Scanner input = new Scanner(System.in);
		String fileName = "";

		while(true) {
		System.out.println("Which universe would you like to battle in? \n1. Pokemon \n2. Star Wars \n3. Marvel \n4. DC");
		try { // returns the file name based on what the user inputs 
	      int universe = input.nextInt();
	      if(universe == 1){
	        fileName = "pokemon.txt";
	        break;
	      } else if (universe == 2){
	          fileName = "starwars.txt";
	          break;
	      } else if (universe == 3){
	          fileName = "marvel.txt";
	          break;
	      } else if (universe == 4){
	          fileName = "dc.txt";
	          break;
	      } else {
	        System.out.println("Invalid input. Please enter a number between 1 and 4");
	      }
	    } catch (InputMismatchException e){ // checks if input is an integer or not 
	        String invalid = input.next(); 
	        System.out.println("Invalid input. Please enter a number between 1 and 4");
	      }
	    }

		// These three lines must be included in this method as the last lines. DO NOT DELETE THEM. IF YOU CHOOSE TO CONSOLIDATE THEM INTO A SINGLE METHOD, MODIFY AS NEEDED.
		this.file = new File(fileName); // this.file is the filename based on the user's selection
	    this.countCharactersInFile(); 
	    this.readCharacters();	
	}

	/** TODO: Implement countCharactersInFile() - DO NOT MODIFY METHOD HEADER
  	 ** ALTERNATIVELY, YOU CAN CONSOLIDATE BOTH readCharacters() and countCharactersInFile() methods into a single method. You may also choose
  	 ** to use a LinkedList instead of an array. This modification is optional. If you choose to do this, then you may modify the method header
  	 ** as needed. 
  	**/
	private void countCharactersInFile() {
		int characterNum = 0; 
	    try {
	      Scanner reader = new Scanner(this.file); // scanner which will read the file 

	      // read the file  
	      while (reader.hasNextLine()) {
	        reader.nextLine();
	        characterNum += 1; // after reading the file this will add 1 extra 
	      }
	      reader.close();
	     
	    } catch (FileNotFoundException e) {
	      System.out.println("Error: file not found.");
	    }
	    this.numCharacters = characterNum; // updates the class attribute 
	}

	/** TODO: Implement readCharacters() - DO NOT MODIFY METHOD HEADER
  	 ** ALTERNATIVELY, YOU CAN CONSOLIDATE BOTH readCharacters() and countCharactersInFile() methods into a single method. You may also choose
  	 ** to use a LinkedList instead of an array. This modification is optional. If you choose to do this, then you may modify the method header
  	 ** as needed. 
  	**/
	private void readCharacters() {
		this.characters = new Character[this.numCharacters]; // creates a Character array for the number of character 
		try {
			Scanner reader = new Scanner(this.file);
			for(int i = 0; i < this.numCharacters; i++){
				String line = reader.nextLine(); // each line of the file is a new string
				String[] array = line.split(" "); // split at whitespace into an array 

				String name = array[0];
				int maxHp = Integer.parseInt(array[1]);
				int attack = Integer.parseInt(array[2]);
				int defense = Integer.parseInt(array[3]);

				Character newCharacter = new Character(name, maxHp, attack, defense); // creates a new character

				this.characters[i] = newCharacter; // adds the new character to the character array 
			}
			reader.close();
		} catch (FileNotFoundException e){
      		System.out.println("An error occurred: file not found");
      	}
	}
	
	// TODO: Implement playOptionOne() - DO NOT MODIFY METHOD HEADER
	public void playOptionOne() {
		// setup game in main first, file will be chosen and read 
		String playerOneName = "You";
		String playerTwoName = "Computer";
		// start with character selection 
		this.playerOne = selectCharacter();
		System.out.println("You selected: " + playerOne.getName());

		int playerOneChoice = -1; // figures out the index of players choice to compare to computers choice 
		for (int i = 0; i < this.numCharacters; i++) {
			if (this.characters[i] == this.playerOne) {
				playerOneChoice = i;
				break;
			}
		}

		Random random = new Random();
		int computerChoice = random.nextInt(this.numCharacters); // potential numbers will be equivalent to the indexes of the characters array
		boolean invalidRandom = true;
		while (invalidRandom) { // computer must choose a different character 
			if (computerChoice == playerOneChoice) {
				invalidRandom = true;
				computerChoice = random.nextInt(this.numCharacters); // chooses new random number to compare again
			} else {
				invalidRandom = false;
			}
		}

		this.playerTwo = this.characters[computerChoice];
		System.out.println("You will be battling: " + playerTwo.getName());

		// choose difficulty level before beginning battle 
		boolean isSmart = isSmart();
		
		// start battle
		System.out.println("\nGame ready. Let's start!");
		Scanner input = new Scanner(System.in);
		// initialize variables 
		int roundNum = 1;
		boolean validChoice = false;
		boolean forfeited = false;
		
		// battle 
		while (playerOne.getCurrHp() > 0 && playerTwo.getCurrHp() > 0 && !forfeited) {
			roundInfo(roundNum, playerOne, playerTwo, playerOneName, playerTwoName);
			
			// user's turn
			System.out.println("Your turn. What would you like to do? \n\t1. Attack \n\t2. Heal \n\t3. Forfeit Game");
			String turnChoice = input.nextLine();
			if (turnChoice.equals("1")) { // attack
				this.playerOne.attack(playerTwo);
				validChoice = true;
			} else if (turnChoice.equals("2")) { // heal
				this.playerOne.heal(this.numCharacters);
				validChoice = true;
			} else if (turnChoice.equals("3")) { // forfeit
				System.out.println(playerOne.getName() + " has forfeited the game.");
				validChoice = true;
				forfeited = true;
			} else {
				System.out.println("Invalid input, please enter a number between 1 and 3");
				validChoice = false;
			}

			// computers turn
			if (!forfeited && validChoice && playerTwo.getCurrHp() > 0) {
				System.out.println("\nIt is now " + playerTwo.getName() + "'s turn.");
				
				// computer will either 1 attack or 2 heal
				int computerMove;
				if (!isSmart) { // easy mode 
					random = new Random();
					computerMove = random.nextInt(2) + 1; // choose between 1 and 2 
				} else { // medium
					computerMove = playerTwo.calculateBestMove(playerOne);
				}

				if (computerMove == 1) { // attack
					this.playerTwo.attack(playerOne);
				} else { // heal
					this.playerTwo.heal(this.numCharacters);
				}
			}

			if (validChoice) {
				roundNum += 1;
			}
		}

		// winning message 
		if (playerOne.getCurrHp() <= 0) {
			System.out.println("\nSorry, you lost. Better luck next time");
		} else if (playerTwo.getCurrHp() <= 0) {
			System.out.println("\nCongratulations! You won!");
		}

		// reset the current health after the game is over 
		playerOne.setCurrHp(playerOne.getMaxHp());
		playerTwo.setCurrHp(playerOne.getMaxHp());

		System.out.println("\nReturning to main menu..."); 
	}

	// TODO: Implement playOptionTwo() - DO NOT MODIFY METHOD HEADER
	public void playOptionTwo() {
		// ask users for their names first 
		Scanner input = new Scanner(System.in);
		System.out.println("Enter player one's name: ");
		String playerOneName = input.nextLine();
		System.out.println("Enter player two's name: ");
		String playerTwoName = input.nextLine();

		// character selection
		System.out.println("\n" + playerOneName + ", choose your character.");
		this.playerOne = selectCharacter();
		System.out.println(playerOneName + " selected: " + playerOne.getName());

		System.out.println("\n" + playerTwoName + ", choose your character.");
		this.playerTwo = selectCharacter();

		while (this.playerTwo == this.playerOne) { // make sure they don't choose the same character 
			System.out.println("\n" + playerOneName + " already selected that character, please choose a different character");
			this.playerTwo = selectCharacter(); // prompts player 2 to choose another character
		}

		System.out.println(playerTwoName + " selected: " + playerTwo.getName());

		// start battle 
		System.out.println("\nGame ready. Let's start!");
		input = new Scanner(System.in);

		// initialize variables 
		int roundNum = 1;
		boolean forfeited = false;
		boolean validChoiceOne = false;
		boolean validChoiceTwo = false;

		while (playerOne.getCurrHp() > 0 && playerTwo.getCurrHp() > 0 && !forfeited) {
			roundInfo(roundNum, playerOne, playerTwo, playerOneName, playerTwoName);

			// player one's turn 
			while (!validChoiceOne) {
				System.out.println("\n" + playerOneName + "'s turn. What would you like to do? \n\t1. Attack \n\t2. Heal \n\t3. Forfeit Game");
				String turnOne = input.nextLine();

				if (turnOne.equals("1")) { // attack
					this.playerOne.attack(playerTwo);
					validChoiceOne = true;
				} else if (turnOne.equals("2")) { // heal
					this.playerOne.heal(this.numCharacters);
					validChoiceOne = true;
				} else if (turnOne.equals("3")) { // forfeit 
					System.out.println(playerOne.getName() + " has forfeited the game.");
					validChoiceOne = true;
					forfeited = true;
				} else {
					System.out.println("Invalid input, please enter a number between 1 and 3");
					validChoiceOne = false;
				}
			}

			// player two's turn
			while (!validChoiceTwo && !forfeited) {
				System.out.println("\n" + playerTwoName + "'s turn. What would you like to do? \n\t1. Attack \n\t2. Heal \n\t3. Forfeit Game");
				String turnTwo = input.nextLine();

				if (turnTwo.equals("1")) { // attack
					this.playerTwo.attack(playerOne);
					validChoiceTwo = true;
				} else if (turnTwo.equals("2")) { // heal
					this.playerTwo.heal(this.numCharacters);
					validChoiceTwo = true;
				} else if (turnTwo.equals("3")) { // forfeit 
					System.out.println(playerTwo.getName() + " has forfeited the game.");
					validChoiceTwo = true;
					forfeited = true;
				} else {
					System.out.println("Invalid input, please enter a number between 1 and 3");
					validChoiceTwo = false;
				}
			}
			validChoiceOne = false; // resets valid choice to false to recheck next input
			validChoiceTwo = false;
			roundNum += 1; 
		}

		// winning messages 
		if (playerOne.getCurrHp() <= 0) {
			System.out.println("\n" + playerTwoName + " won!");
		} else if (playerTwo.getCurrHp() <= 0) {
			System.out.println("\n" + playerOneName + " won!");
		}

		// reset the current health after the game is over 
		playerOne.setCurrHp(playerOne.getMaxHp());
		playerTwo.setCurrHp(playerOne.getMaxHp());

		System.out.println("\n Returning to main menu...");
	}

	// TODO: Implement the new game mode logic
	// Implement this method as described in the instructions.
	// COME UP WITH YOUR OWN METHOD NAME, RETURN, AND PARAMETERS
	public void playTeleportMode() { // multiverse mayhem (same as one player option with an extra choice)
		// setup game in main first, file will be chosen and read 
		String playerOneName = "You";
		String playerTwoName = "Computer";
		// start with character selection 
		this.playerOne = selectCharacter();
		System.out.println("You selected: " + playerOne.getName());

		int playerOneChoice = -1; // figures out the index of players choice to compare to computers choice 
		for (int i = 0; i < this.numCharacters; i++) {
			if (this.characters[i] == this.playerOne) {
				playerOneChoice = i;
				break;
			}
		}

		Random random = new Random();
		int computerChoice = random.nextInt(this.numCharacters); // potential numbers will be equivalent to the indexes of the characters array
		boolean invalidRandom = true;
		while (invalidRandom) { // computer must choose a different character 
			if (computerChoice == playerOneChoice) {
				invalidRandom = true;
				computerChoice = random.nextInt(this.numCharacters); // chooses new random number to compare again
			} else {
				invalidRandom = false;
			}
		}

		this.playerTwo = this.characters[computerChoice];
		System.out.println("You will be battling: " + playerTwo.getName());

		// choose difficulty level before beginning battle 
		boolean isSmart = isSmart();
		
		// start battle
		System.out.println("\nGame ready. Let's start!");
		Scanner input = new Scanner(System.in);
		// initialize variables 
		int roundNum = 1;
		boolean validChoice = false;
		boolean forfeited = false;
		
		// battle 
		while (playerOne.getCurrHp() > 0 && playerTwo.getCurrHp() > 0 && !forfeited) {
			roundInfo(roundNum, playerOne, playerTwo, playerOneName, playerTwoName);
			
			// user's turn
			System.out.println("Your turn. What would you like to do? \n\t1. Attack \n\t2. Heal \n\t3. Teleport to Another Universe \n\t4. Forfeit Game");
			String turnChoice = input.nextLine();
			if (turnChoice.equals("1")) { // attack
				this.playerOne.attack(playerTwo);
				validChoice = true;
			} else if (turnChoice.equals("2")) { // heal
				this.playerOne.heal(this.numCharacters);
				validChoice = true;
			} else if (turnChoice.equals("3")) { // teleport
				teleport();
				validChoice = true;
			} else if (turnChoice.equals("4")) { // forfeit
				System.out.println(playerOne.getName() + " has forfeited the game.");
				validChoice = true;
				forfeited = true;
			} else {
				System.out.println("Invalid input, please enter a number between 1 and 3");
				validChoice = false;
			}

			// computers turn
			if (!forfeited && validChoice && playerTwo.getCurrHp() > 0) {
				System.out.println("\nIt is now " + playerTwo.getName() + "'s turn.");
				
				// computer will either 1 attack or 2 heal
				int computerMove;
				if (!isSmart) { // easy mode 
					random = new Random();
					computerMove = random.nextInt(2) + 1; // choose between 1 and 2 
				} else { // medium
					computerMove = playerTwo.calculateBestMove(playerOne);
				}

				if (computerMove == 1) { // attack
					this.playerTwo.attack(playerOne);
				} else { // heal
					this.playerTwo.heal(this.numCharacters);
				}
			}

			if (validChoice) {
				roundNum += 1;
			}
		}

		// winning message 
		if (playerOne.getCurrHp() <= 0) {
			System.out.println("\nSorry, you lost. Better luck next time");
		} else if (playerTwo.getCurrHp() <= 0) {
			System.out.println("\nCongratulations! You won!");
		}

		// reset the current health after the game is over 
		playerOne.setCurrHp(playerOne.getMaxHp());
		playerTwo.setCurrHp(playerOne.getMaxHp());

		System.out.println("\nReturning to main menu..."); 
	}

	public void playOptionFour() { // watch a recursive battle
		Random random = new Random();
		// player one
		int computerChoice = random.nextInt(this.numCharacters);
		this.playerOne = this.characters[computerChoice];

		//player two
		int computerChoiceTwo = random.nextInt(this.numCharacters); // potential numbers will be equivalent to the indexes of the characters array
		boolean invalidRandom = true;
		while (invalidRandom) { // computer must choose a different character 
		  if (computerChoiceTwo == computerChoice) {
		    invalidRandom = true;
		    computerChoiceTwo = random.nextInt(this.numCharacters); // chooses new random number to compare again
		  } else {
		    invalidRandom = false;
		  }
		}
		this.playerTwo = this.characters[computerChoiceTwo];

		int result = playRecursively(50);

          if (result == 0){
            System.out.println("\nPlayer One (medium mode) won!");
          } else if (result == 1){
              System.out.println("\nPlayer Two (easy computer) won!");
          } else if (result == -1){
              System.out.println("\nIt was a draw!");
          } 
	}

	// TODO: Implement playRecursively() - DO NOT MODIFY METHOD NAME, YOU MAY MODIFY RETURN & PARAMETERS AS NEEDED
	private int playRecursively(int maxRounds) {
		// playerOne = smart computer (medium)
	    // playerTwo = random computer (easy)
	    if (playerOne.getCurrHp() <= 0){ // easy mode won
	      // reset the current health after the game is over 
	      playerOne.setCurrHp(playerOne.getMaxHp());
	      playerTwo.setCurrHp(playerOne.getMaxHp());
	      return 1; 
	    }
	    if (playerTwo.getCurrHp() <= 0){ // smart computer won
	      // reset the current health after the game is over 
	      playerOne.setCurrHp(playerOne.getMaxHp());
	      playerTwo.setCurrHp(playerOne.getMaxHp());
	      return 0;
	    }
	    if (maxRounds <= 0){ // tie
	      // reset the current health after the game is over 
	      playerOne.setCurrHp(playerOne.getMaxHp());
	      playerTwo.setCurrHp(playerOne.getMaxHp());
	      return -1; 
	    }

	    // player one's turn (medium level)
	    int playerOneMove = playerOne.calculateBestMove(playerTwo);

	    if (playerOneMove == 1){ 
	    	playerOne.attack(playerTwo);
	    } else {
	        playerOne.heal(numCharacters);
	    }

	    if (playerTwo.getCurrHp() <= 0){ // check if smart computer won after turn 
	      // reset the current health after the game is over 
	      playerOne.setCurrHp(playerOne.getMaxHp());
	      playerTwo.setCurrHp(playerOne.getMaxHp());
	      return 0;
	    } 

	    // player two's turn (easy level)
	    Random randomTurn = new Random();
	    int playerTwoMove = randomTurn.nextInt(2) + 1; 

	    if (playerTwoMove == 1){
	    	playerTwo.attack(playerOne);
	    } else {
	        playerTwo.heal(numCharacters);
	    }
	    return playRecursively(maxRounds-1); // max rounds ends up reaching zero 
	}

	// TODO: Implement selectCharacter() - DO NOT MODIFY METHOD NAME, MUST REMAIN PRIVATE, YOU MAY MODIFY RETURN & PARAMETERS AS NEEDED
	private Character selectCharacter() {
		System.out.println("Select your character: ");
	    System.out.println("----------Characters to chooses from----------");
	    for (int i = 0; i < this.numCharacters; i++) {
	    	System.out.println(i+1 + ". " + this.characters[i].toString()); // use the toString method to print it out nicely 
	    }

	    Scanner input = new Scanner(System.in);
	    int characterChoice;

	    while (true) {
	        System.out.println("Enter the number for the character you would like to choose: "); // prompts user input 

	        try {
	          characterChoice = input.nextInt(); // reading what the user inputs 
	          // check if the number is in the valid range 
	          if (characterChoice >= 1 && characterChoice <= this.numCharacters) {
	            break;
	          } else {
	            System.out.println("Invalid choice. Please enter a number between 1 and " + this.numCharacters + "."); 
	          }
	        } catch (InputMismatchException e){ // checks if input is an integer or not 
	            String invalid = input.next(); 
	            System.out.println("Invalid input. Please enter a number between 1 and " + this.numCharacters + ".");
	        }
	      }
	    return this.characters[characterChoice - 1]; // minus one to return the index 
  	}

	// TODO: Implement any additional methods as needed for game logic.

	// choose difficulty level
	private boolean isSmart() {
		Scanner input = new Scanner(System.in);
		System.out.println();
		while(true) {
			System.out.println("Select difficulty: \n\t1. Easy \n\t2. Medium");
			String difficultyLevel = input.nextLine();

			if (difficultyLevel.equals("1")){
				return false; // easy mode
			} else if (difficultyLevel.equals("2")){
				return true;
			} else {
				System.out.println("Invalid input. Please input the number 1 or 2 to make your choice");
			}
		}
	}

	private void roundInfo(int roundNum, Character playerOne, Character playerTwo, String playerOneName, String playerTwoName){
		System.out.println("\n------------Round " + roundNum + "------------"); // print the current round being played 
		System.out.print(playerOneName + ": ");
		System.out.println(playerOne.toString());
		System.out.print(playerTwoName + ": ");
		System.out.println(playerTwo.toString());
	}

	private void teleport() {
		// heal 
		int healAmount = playerOne.getMaxHp() / 2; 

		if ((playerOne.getCurrHp() + healAmount) > playerOne.getMaxHp()) {
			playerOne.setCurrHp(playerOne.getMaxHp());
		} else {
			playerOne.setCurrHp(playerOne.getCurrHp() + healAmount);
		}

		// different universes 
		String[] files = {"pokemon.txt", "starwars.txt", "marvel.txt", "dc.txt"};
		String[] universeNames = {"pokemon", "starwars", "marvel", "dc"};
		Random random = new Random();
		int newUniverse = random.nextInt(files.length); // will give the index of the new file 

		// randomly pick a new universe
		String newFile;
		String currentFile = this.file.getName();
		while (true) {
			newFile = files[newUniverse];
			if (newFile.equals(currentFile)) {
				newUniverse = random.nextInt(files.length);
			} else {
				break;
			}
		}
		System.out.println("\n Teleporting to the " + universeNames[newUniverse] + " universe");

		System.out.println(playerOne.getName() + " has healed! New HP is: " + playerOne.getCurrHp());

		File teleportFile = new File(newFile);

		// count characters in file
		int newCharacterNum = 0; 
	    try {
	      Scanner reader = new Scanner(teleportFile); // scanner which will read the file 

	      // read the file  
	      while (reader.hasNextLine()) {
	        reader.nextLine();
	        newCharacterNum += 1; // after reading the file this will add 1 extra 
	      }
	      reader.close();
	     
	    } catch (FileNotFoundException e) {
	      System.out.println("Error: file not found.");
	    }
	    
	    // read characters in file 
	    Character[] newCharacters = new Character[newCharacterNum]; // creates a Character array for the number of character 
		try {
			Scanner reader = new Scanner(teleportFile);
			for(int i = 0; i < newCharacterNum; i++){
				String line = reader.nextLine(); // each line of the file is a new string
				String[] array = line.split(" "); // split at whitespace into an array 

				String name = array[0];
				int maxHp = Integer.parseInt(array[1]);
				int attack = Integer.parseInt(array[2]);
				int defense = Integer.parseInt(array[3]);

				Character newCharacter = new Character(name, maxHp, attack, defense); // creates a new character
				newCharacters[i] = newCharacter; // adds the new character to the character array 
			}
			reader.close();
		} catch (FileNotFoundException e){
      		System.out.println("An error occurred: file not found");
      	}

      	// update info 
      	this.file = teleportFile;
      	this.numCharacters = newCharacterNum;
      	this.characters = newCharacters;

      	// choose new computer character 
      	random = new Random();
		int computerChoice = random.nextInt(newCharacterNum); // potential numbers will be equivalent to the indexes of the characters array
		this.playerTwo = newCharacters[computerChoice];
		System.out.println("You will be battling: " + playerTwo.getName());
	}
}