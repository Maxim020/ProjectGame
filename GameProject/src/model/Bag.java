package model;

import java.util.ArrayList;
import java.util.Collections;

public class Bag {
	
	//Attributes
	private ArrayList<Character> letterList;
	private final String LETTERS = "AAAAAAAAABBCCDDDDEEEEEEEEEEEEFFGGHHIIIIIIIIJJKKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ**";
	
	//Constructor
	/**
	 * Constructor of the Bag class
	 * When Bag object is initialized, the attribute letterList is filled with letters contained in the "letters" String
	 * @requires letters != null;
	 * @ensures letterList != null;
	 * @author Maxim
	 */
	public Bag() {
		
		letterList = new ArrayList<>();
		
		for(int i = 0; i < LETTERS.split("").length; i++) {
		letterList.add(LETTERS.toCharArray()[i]);
		}
		
	}
	
	//Methods
	/**
	 * Using the Collections.shuffle method, shuffles (randomizes) the list of letters. Note: I don't know how useful this is, because it could be done with Math.random when player from Player class is first pulling from the bag, but it is a nice touch
	 * @ensures letterList != (old)letterList;
	 * @author Maxim
	 */
	public void shuffleBag() {
		Collections.shuffle(letterList);
	}
	
	/**
	 * Removes a letter from the bag. This should be used everytime a letter is pulled from the bag
	 * @param char letter
	 * @requires letter instanceOf char && letterList.contains(letter)
	 * @exception IllegalArgumentException
	 * @ensures letterList.size() - 1
	 * @author Maxim
	 */
	public void removeFromBag(char letter) {
		if(!letterList.contains(letter)) {throw new IllegalArgumentException("Letter is already not present in the bag");}
		else {
			
			letterList.remove(letter);
		}
	}
	
	/**
	 * Method for pulling a random letter from the bag. Should have additional protection in case of return 0.
	 * @requires letterList != null;
	 * @ensures Error message || return of random letter
	 * @return char letter
	 * @author Maxim
	 */
	public char pull() {
		if(letterList.isEmpty()){System.out.println("The bag is already empty");}
		else if(letterList.size() == 1) {
			char letter = letterList.get(0);
			letterList.remove(0);
			return letter;
		}
		else {
			int range = letterList.size();
			char letter = letterList.get( (int) (Math.random()*range));
			letterList.remove(letter);
			return letter;
		}
		return 0;
	}
	
	public ArrayList<Character> getLetterList(){
		return letterList;
	}
	
}





