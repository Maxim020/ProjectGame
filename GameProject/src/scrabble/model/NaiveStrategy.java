package scrabble.model;

import java.util.ArrayList;

import scrabble.model.letters.LetterDeck;
import scrabble.model.words.ScrabbleWordChecker;

public class NaiveStrategy implements Strategy {

	/**
	 * Forms a starting String to be made permutations of
	 * 
	 * @param LetterDeck letterDeck
	 * @requires letterDeck != null
	 * @return String s
	 * @author Maxim
	 */
	public String formString(LetterDeck letterDeck) {
		String s = "";
		for (int i = 0; i < letterDeck.getLettersInDeck().size(); i++) {
			s = s + letterDeck.getLettersInDeck().get(i);
		}
		return s;
	}

	/**
	 * Recursively makes permutations from given string and adds them to the list.
	 * The method calls itself until the String "" is added to the list.
	 * 
	 * @param String s
	 * @requires s != null
	 * @ensures return of word contained in the scrabble dictionary
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

	@Override
	/**
	 * returns a String in form of "WORD {coordinate} {direction} {word}"
	 * 
	 * @param Board               board
	 * @param LetterDeck          letterDeck
	 * @param ScrabbleWordChecker checker
	 * @requires board != null && letterDeck != null && checker != null
	 * @ensures Return of a valid command for the processMove() method
	 * @return String move
	 * @author Maxim
	 */
	public String determineMove(Board board, LetterDeck letterDeck, ScrabbleWordChecker checker) {

		String move = "";

		// WHAT THIS DOES AT THIS POINT:
		// USES TWO ABOVE METHODS TO GET WORDS FROM TILES IN HAND E.G. ADAF AFAF AFFA...
		// FROM THESE METHODS WE GET A LIST FOR THIS METHOD
		// THIS METHOD GOES THROUGH THE LIST TO FIND WORD THAT EXISTS
		// THEN IF THE COMPUTER GOES FIRST IT PLAYS THIS WORD ON TILE H8 HORIZONTALY
		// IF NOT THE METHOD CHECKS IF THE WORD WAS ALREADY PLAYED
		// IF NOT THE METHOD FINDS A LETTER THAT IS CONTAINED IN BOTH THE GENERATED WORD
		// AND PLAYEDWORD AND SAVES IT
		// THEN THE METHOD CHECKS IF THE ALREADY PLAYED WORD WAS PLAYED HORIZONTALLY OR
		// VERTICALLY
		// THEN WE FIND THE COORDINATE OF THIS LETTER ON THE BOARD, WHILE GETTING THE
		// INDEX OF THE MATCHING LETTER WITHIN THE WORD STRING
		// FROM THE WORD STRING INDEX WE KNOW HOW MUCH TO SUBTRACT TO GET THE CORRECT
		// ROW SO THE MATCHING LETTERS OVERLAP
		// CREATES A RESPONSE IN FORM "WORD + COORDINATE + DIRECTION + GENERATEDWORD"
		
		//!!PROBLEM!! IF * IS PART OF THE STRING, CANT CREATE STRING

		// TO BE ADDED APPENDING WORDS FROM START OR FINISH, RIGHT NOW IT ONLY PLAYS
		// VERTICAL WORDS ON HORIZONTALLY PLAYED WORDS AND VICE VERSA

		// TO BE ADDED SWAPPING (HOWEVER PRETTY UNLIKELY THAT A COMPUTER WONT BE ABLE TO
		// FIND A WORD TO CREATE) <- THIS SHOULD BE DONE IN THE COMPUTERPLAYER ITSELF IF
		// THIS METHOD WOULD RETURN AN EMPTY STRING

		// THIS IS A STUPID STRATEGY, DOES NOT LOOK FOR WORDS WITH HIGHEST SCORES, IT
		// ALWAYS TAKES FIRST WORD IT FINDS THAT ACTUALLY CAN BE PLACED

		/** Word to be played */
		ArrayList<String> listOfAllPermutations = new ArrayList<>();

		listOfAllPermutations = determineWord(formString(letterDeck));

		String s = "";

		for (int i = 0; i < formString(letterDeck).length() - 1; i++) {
			s = formString(letterDeck).substring(0, formString(letterDeck).length() - (i + 1));
			listOfAllPermutations.addAll(determineWord(s));
		}

		//for (int i = 0; i < listOfAllPermutations.size(); i++) {
			//System.out.println(listOfAllPermutations.get(i));
		//}

		/** Cycle through generated list of words */
		for (String word : listOfAllPermutations) {
			/** Suitable word found */
			if (checker.isValidWord(word) != null) {
				/** If first turn play from center */
				System.out.println(word);
				if (board.getPlayedWords().isEmpty()) {
					System.out.println("First Word");
					/**We dont have to check if the word will fit here because it will always fit*/
					move = move + "WORD H8 H " + word;
					return move;
				}
				/** If word was not already played */
				else if (!board.getPlayedWords().contains(word)) {
					/** Cycle though words already on board */
					System.out.println("Here1");
					for (String playedWord : board.getPlayedWords()) {
						/** Cycle through letters in played word */
						System.out.println("Here2 " + playedWord);
						for (char playedWordLetter : playedWord.toCharArray()) {
							/** Cycle through letters in generated word */
							System.out.println("Here3 " + playedWordLetter);
							for (char letter : word.toCharArray()) {
								/** Find matching letter */
								System.out.println("Here4 " + letter);
								if (playedWordLetter == letter) {
									/** Stores Matching letters */
									System.out.println("Here5");
									char matchingLetter = letter;
									System.out.println(letter);
									/** If the played word is horizontal */
									if (board.getWordDirectionMap().get(playedWord) == "H") {

										/*
										 * //If the last letter of the word is the first letter of the played word
										 * if(word.charAt(word.length()-1) == playedWord.charAt(0)) { //If the new word
										 * can fit if(board.convert(board.getWordCoordinateMap().get(playedWord))[1] >
										 * word.length()) {
										 * 
										 * move = move + "WORD " +
										 * board.convert(board.convert(board.getWordCoordinateMap().get(playedWord))[0],
										 * board.convert(board.getWordCoordinateMap().get(playedWord))[1] -
										 * word.length() + 1) + " H " + word.toUpperCase(); } } //If the first letter of
										 * the word is the last letter of the played word else if(word.charAt(0) ==
										 * playedWord.charAt(playedWord.length() - 1)) { //If the new word can fit
										 * if(board.convert(board.getWordCoordinateMap().get(playedWord))[1] +
										 * playedWord.length() - 1 + word.length() - 1 <= 15) {
										 * 
										 * move = move + "WORD " +
										 * board.convert(board.convert(board.getWordCoordinateMap().get(playedWord))[0],
										 * board.convert(board.getWordCoordinateMap().get(playedWord))[1] +
										 * word.length() - 1) + " H " + word.toUpperCase(); } }
										 */

										/** If matching letter is not empty */
										if (matchingLetter != ' ') {
											/** Get index of matching letter in playedWord */
											int indexMatchingLetterPlayedWord = playedWord.indexOf(matchingLetter);
											/** Get index of matching letter in word */
											int indexMatchingLetterWord = word.indexOf(matchingLetter);
											/** Get start coordinates of played word */
											int[] playedWordStartCoords = board
													.convert(board.getWordCoordinateMap().get(playedWord));
											/** Get coordinates of matching letter inside of playedWord */
											String coordsOfMatchingLetter = board.convert(playedWordStartCoords[0],
													playedWordStartCoords[1] + indexMatchingLetterPlayedWord);

											move = move + "WORD "
													+ board.convert(
															board.convert(coordsOfMatchingLetter)[0]
																	- indexMatchingLetterWord,
															board.convert(coordsOfMatchingLetter)[1])
													+ " V " + word;
											return move;
										}
									}

									else {

										/** Finds matching letters */
										for (char wordLetters : word.toCharArray()) {
											for (char letters : playedWord.toCharArray()) {
												if (wordLetters == letters) {
													matchingLetter = letters;
												}
											}
										}

										/** If matching letter is not empty */
										if (matchingLetter != ' ') {
											/** Get index of matching letter in playedWord */
											int indexMatchingLetterPlayedWord = playedWord.indexOf(matchingLetter);
											/** Get index of matching letter in word */
											int indexMatchingLetterWord = word.indexOf(matchingLetter);
											/** Get start coordinates of played word */
											int[] playedWordStartCoords = board
													.convert(board.getWordCoordinateMap().get(playedWord));
											/** Get coordinates of matching letter inside of playedWord */
											String coordsOfMatchingLetter = board.convert(
													playedWordStartCoords[0] + indexMatchingLetterPlayedWord,
													playedWordStartCoords[1]);

											move = move + "WORD " + board.convert(
													board.convert(coordsOfMatchingLetter)[0],
													board.convert(coordsOfMatchingLetter)[1] - indexMatchingLetterWord)
													+ " H " + word;
											return move;

										}
									}
								}
							}

						}
					}
				}
			}

		}

		return move;
	}

}
