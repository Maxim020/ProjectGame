package scrabble.model;

import java.util.ArrayList;

import scrabble.model.letters.Bag;

public class DeadEndChecker {
	
	public static String formStringBag(Bag bag) {
		String s = "";
		for (int i = 0; i < bag.getLetterList().size(); i++) {
			s = s + bag.getLetterList().get(i);
		}
		return s;
	}
	
	/**
	 * Recursively makes permutations from given string and adds them to the list.
	 * The method calls itself until the String "" is added to the list.
	 * 
	 * @param String s
	 * @requires s != null
	 * @ensures return of all possible permutations of a string
	 * @return ArrayList<String> Res
	 * @author Maxim
	 */
	public ArrayList<String> determineWord(String s) {

		if (s.length() == 0) {

			ArrayList<String> empty = new ArrayList<>();
			empty.add("");
			return empty;
		}

		char ch = s.charAt(0);

		String substring = s.substring(1);

		ArrayList<String> prevResult = determineWord(substring);

		ArrayList<String> Res = new ArrayList<>();

		for (String val : prevResult) {
			for (int i = 0; i <= val.length(); i++) {
				Res.add(val.substring(0, i) + ch + val.substring(i));
			}
		}

		return Res;
	}
	
	public boolean isDeadEnd() {
		
		/** Check if there are still valid words in the bag*/
		
		ArrayList<String> permutations = new ArrayList<>();
		
		String bagString = formStringBag(Bag.getInstance());
		
		permutations.addAll(determineWord(bagString));
		
		for(int i = 0; i < bagString.length(); i++) {
			for(int j = 0; j < bagString.length() * (i + 1); j++) {
				permutations.add(bagString.substring(0, bagString.length() - 1 - j) + bagString.substring(0 + bagString.length() - 1 + j, bagString.length() - 1));
			}
			
			
		}
		
		
		
		return false;
	}

}
