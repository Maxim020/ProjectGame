package scrabble.model.letters;

import scrabble.model.PlayerList;

import java.util.ArrayList;
import java.util.Collections;

public class Bag {
	private static ArrayList<Character> letterList;
	private final static String LETTERS = "AAAAAAAAABBCCDDDDEEEEEEEEEEEEFFGGHHIIIIIIIIJJKKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ**";

	private static Bag instance = null;

	/**
	 * Constructor of the Bag class
	 * When Bag object is initialized, the attribute letterList is filled with letters contained in the "letters" String
	 * @requires letters != null;
	 * @ensures letterList != null;
	 * @author Maxim
	 */
	private Bag() {}

	public static Bag getInstance(){
		if (instance == null) {
			instance = new Bag();

			letterList = new ArrayList<>();

			for(int i = 0; i < LETTERS.split("").length; i++) {
				letterList.add(LETTERS.toCharArray()[i]);
			}
		}
		return instance;
	}

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
	 * @param letter
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
	 * Method for pulling a random letter from the bag.
	 * @requires letterList != null;
	 * @ensures Error message || return of random letter
	 * @return char letter
	 * @author Maxim & Yasin
	 */
	public char pull() { //Should an Exception be thrown?
		if(letterList.isEmpty()){System.out.println("The bag is already empty");}
		shuffleBag();
		return letterList.get(0);
	}

	public void add(char tile){
		letterList.add(tile);
	}

	public ArrayList<Character> getLetterList(){
		return letterList;
	}

	public void setLetterList(ArrayList<Character> letterList) {
		this.letterList = letterList;
	}

	public String getLETTERS() {
		return LETTERS;
	}

	public static void setInstance(Bag instance) {
		Bag.instance = instance;
	}
}





