import java.util.Random;
// DO NOT ADD ANY ADDITIONAL IMPORTS ------
// NOTE: NO USER INPUT LOGIC SHOULD BE HANDLED IN THIS CLASS AT ALL - DO NOT IMPORT SCANNER.

public class Character {
	// TODO: Attributes
	String name;
	int maxHp;
	int currHp;
	int attack;
	int defense;

	// Default Constructor - DO NOT MODIFY
	public Character() {};

	// Constructor that should take in name, maxHp, attack, and defense, and set all attribute value
	// DO NOT MODIFY PARAMETERS
	public Character(String name, int maxHp, int attack, int defense) {
		this.name = name;
		this.maxHp = maxHp;
		this.currHp = maxHp; // when character is created the current HP is the same as the maxHP
		this.attack = attack;
		this.defense = defense;

	}

	// Implement all of the following getters for all attributes
	// DO NOT DELETE ANY GETTER - MODIFY RETURN AS NEEDED
	public String getName() {
		return this.name;
	}

	public int getMaxHp() {
		return this.maxHp;
	}

	public int getCurrHp() {
		return this.currHp;
		
	}

	public int getAttack() {
		return this.attack;
	}

	public int getDefense() {
		return this.defense;
	}

	// Implement all of the following setters for all attributes
	// DO NOT DELETE ANY SETTER - MODIFY RETURN & PARAMETERS AS NEEDED
	public void setName(String name) {
		this.name = name;
	}

	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}

	public void setCurrHp(int currHp) {
		this.currHp = currHp;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	// TODO: Implement heal() - DO NOT MODIFY METHOD HEADER.
	public void heal(int numCharacters) {
		Random randomNum = new Random();
		int randomNumber = randomNum.nextInt(numCharacters) + 1; // creates bound between 0 and number of characters 
		int healAmount = 4 + (randomNumber % 3);

		// ensure currHp does not exceed maxHp
		if (this.currHp + healAmount > this.maxHp){ 
        healAmount = this.maxHp - this.currHp; 
      	}

      	System.out.println(this.name + " has healed by " + healAmount);

      	this.currHp += healAmount;
	}

	// TODO: Implement attack() - DO NOT MODIFY METHOD HEADER.
	public void attack(Character opponent) {
		// attack message 
    	System.out.println(this.name + " has attacked " + opponent.getName() + " with an attack strength of " + this.attack);

    	// defense message 
    	System.out.println(opponent.getName() + " has defended by " + opponent.getDefense());

    	// updated HP 
    	int totalDamage = this.attack - opponent.getDefense();
    	int updatedHP = opponent.getCurrHp() - totalDamage;
    	System.out.println(opponent.getName() + " now has " + updatedHP + " HP.");

   		opponent.setCurrHp(updatedHP);
	}

	// TODO: Implement calculateBestMove() - DO NOT MODIFY METHOD NAME. YOU MAY MODIFY THE METHOD RETURN & PARAMETERS AS NEEDED
	public int calculateBestMove(Character opponent) {
		// 1 for attack, 2 for heal
	    if ((this.attack - opponent.getDefense()) >= opponent.getCurrHp()){
	      return 1; 
	    }
	    if (this.currHp <= (opponent.getAttack() - this.defense)){
	      return 2; 
	    }
	    if (this.maxHp - this.currHp <= 5){
	      return 1;
	    }
	    if (this.currHp >= opponent.getCurrHp()){
	      return 1;
	    }
	    if ((opponent.getCurrHp() - this.currHp) <= 5){
	      Random randomNum = new Random();
	      double probability = randomNum.nextDouble();
	      if (probability < 0.6){
	        return 1;
	      } else {
	          return 2;
	      }
	    } else {
	      return 2;
	    }	
	}

	// Default toString() method already implemented. You may modify if you'd like, or leave it as is.
	// DO NOT DELETE
	@Override
    public String toString() {
        return this.name + ", HP=" + this.currHp + ", Attack=" + this.attack + ", Defense=" + this.defense;
    }

    // TODO: Implement any additional methods as needed for character logic.
}