package scrabble.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Map.Entry;

import scrabble.model.Board;
import scrabble.model.letters.LetterDeck;
import scrabble.model.words.AdjacentWordChecker;
import scrabble.model.words.InMemoryScrabbleWordChecker;
import scrabble.model.words.ScrabbleWordChecker;
import scrabble.model.words.WordScoreCounter;
import scrabble.strategy.Strategy;

public class SmartStrategy implements Strategy {

	/**
	 * Forms a starting String to be made permutations of
	 * 
	 * @param  letterDeck
	 * @requires letterDeck != null
	 * @return String s
	 * @author Maxim
	 */
	public static String formString(LetterDeck letterDeck) {
		String s = "";
		for (int i = 0; i < letterDeck.getLettersInDeck().size(); i++) {
			s = s + letterDeck.getLettersInDeck().get(i);
		}
		return s;
	}

	/**
	 * This method is used to generate all the possible combinations of words from a
	 * letter deck
	 * 
	 * @param  s
	 * @param     counter - necessary for the switch case
	 * @requires s != null && counter starts from the length of the letter deck
	 * @ensures All possible combinations from given letters
	 * @return ArrayList<String> res
	 * @author Maxim
	 */
	public static ArrayList<String> determineWord(String s, int counter) {

		ArrayList<String> res = new ArrayList<>();
		String word = "";
		int sLength = s.length();

		switch (counter) {
		case 7:
			for (int i = 0; i < sLength; i++) {
				for (int j = 0; j < sLength; j++) {
					for (int k = 0; k < sLength; k++) {
						for (int l = 0; l < sLength; l++) {
							for (int m = 0; m < sLength; m++) {
								for (int n = 0; n < sLength; n++) {
									for (int o = 0; o < sLength; o++) {
										if (i != j && i != k && i != l && i != m && i != n && i != o && j != k && j != l
												&& j != m && j != n && j != o && k != l && k != m && k != n && k != o
												&& l != m && l != n && l != o && m != n && m != o && n != o) {
											word = word + s.charAt(i) + s.charAt(j) + s.charAt(k) + s.charAt(l)
													+ s.charAt(m) + s.charAt(n) + s.charAt(o);
											res.add(word);
											word = "";
										}
									}
								}
							}
						}
					}
				}
			}
			return res;
		case 6:
			for (int i = 0; i < sLength; i++) {
				for (int j = 0; j < sLength; j++) {
					for (int k = 0; k < sLength; k++) {
						for (int l = 0; l < sLength; l++) {
							for (int m = 0; m < sLength; m++) {
								for (int n = 0; n < sLength; n++) {
									if (i != j && i != k && i != l && i != m && i != n && j != k && j != l && j != m
											&& j != n && k != l && k != m && k != n && l != m && l != n && m != n) {
										word = word + s.charAt(i) + s.charAt(j) + s.charAt(k) + s.charAt(l)
												+ s.charAt(m) + s.charAt(n);
										res.add(word);
										word = "";
									}
								}
							}
						}
					}
				}
			}
			return res;
		case 5:
			for (int i = 0; i < sLength; i++) {
				for (int j = 0; j < sLength; j++) {
					for (int k = 0; k < sLength; k++) {
						for (int l = 0; l < sLength; l++) {
							for (int m = 0; m < sLength; m++) {
								if (i != j && i != k && i != l && i != m && j != k && j != l && j != m && k != l
										&& k != m && l != m) {
									word = word + s.charAt(i) + s.charAt(j) + s.charAt(k) + s.charAt(l) + s.charAt(m);
									res.add(word);
									word = "";
								}
							}
						}
					}
				}
			}
			return res;
		case 4:
			for (int i = 0; i < sLength; i++) {
				for (int j = 0; j < sLength; j++) {
					for (int k = 0; k < sLength; k++) {
						for (int l = 0; l < sLength; l++) {
							if (i != j && i != k && i != l && j != k && j != l && k != l) {
								word = word + s.charAt(i) + s.charAt(j) + s.charAt(k) + s.charAt(l);
								res.add(word);
								word = "";
							}
						}
					}
				}
			}
			return res;
		case 3:
			for (int i = 0; i < sLength; i++) {
				for (int j = 0; j < sLength; j++) {
					for (int k = 0; k < sLength; k++) {
						if (i != j && i != k && j != k) {
							word = word + s.charAt(i) + s.charAt(j) + s.charAt(k);
							res.add(word);
							word = "";
						}
					}
				}
			}
			return res;
		case 2:
			for (int i = 0; i < sLength; i++) {
				for (int j = 0; j < sLength; j++) {
					if (i != j) {
						word = word + s.charAt(i) + s.charAt(j);
						res.add(word);
						word = "";
					}
				}
			}
			return res;
		case 1:
			for (int i = 0; i < sLength; i++) {
				word = word + s.charAt(i);
				res.add(word);
				word = "";
			}
			return res;
		}

		res.add("");
		System.out.println(res);
		return res;
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
	public String determineMove(Board board, LetterDeck letterDeck) {

		String move = "";

		ArrayList<String> listOfMoves = new ArrayList<>();

		ArrayList<String> listOfExtendingMoves = new ArrayList<>();

		HashMap<String, Integer> scoreMap = new HashMap<>();

		AdjacentWordChecker adjacentChecker = new AdjacentWordChecker(board);

		WordScoreCounter scoreCounter = new WordScoreCounter(board);

		ScrabbleWordChecker checker = new InMemoryScrabbleWordChecker();

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

		// WORKS WITH BLANK LETTERS NOW

		// TO BE ADDED APPENDING WORDS FROM START OR FINISH, RIGHT NOW IT ONLY PLAYS
		// VERTICAL WORDS ON HORIZONTALLY PLAYED WORDS AND VICE VERSA - ADDED

		// TO BE ADDED SWAPPING (HOWEVER PRETTY UNLIKELY THAT A COMPUTER WONT BE ABLE TO
		// FIND A WORD TO CREATE {After many attempts, seems like it's actually very
		// likely}) <- THIS SHOULD BE DONE IN THE COMPUTERPLAYER ITSELF IF
		// THIS METHOD WOULD RETURN AN EMPTY STRING - ADDED

		// THIS IS A SMART STRATEGY. CAN MAKE WORDS PERPENDICULAR AS WELL AS APPEND
		// ALREADY EXISTING WORDS.
		// COLLECTS ALL POSSIBLE WORDS, GETS THEIR SCORES AND PICKS THE WORD THAT NETS
		// THE HIGHEST SCORE

		/** List of all permutations */
		ArrayList<String> listOfAllPermutations = new ArrayList<>();
		/** If '*' is present in String, list with all word possibilities */
		ArrayList<String> listWithBlanksReplaced = new ArrayList<>();
		/** Calls method to get initial list of permuated Strings */

		/**
		 * Calls method to permuate a String again but with size of the initial string
		 * decreased until the word is of length 1
		 */

		for (int i = 0; i < formString(letterDeck).length(); i++) {
			listOfAllPermutations.addAll(determineWord(formString(letterDeck), formString(letterDeck).length() - i));
		}

		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		/** Blank replacer */
		for (int i = 0; i < listOfAllPermutations.size(); i++) {
			for (int j = 0; j < alphabet.toCharArray().length; j++) {
				if (containsBlank(listOfAllPermutations.get(i))) {
					String blankReplaced = listOfAllPermutations.get(i).replace('*', alphabet.toCharArray()[j]);
					listWithBlanksReplaced.add(blankReplaced);
				}
			}
		}

		listOfAllPermutations.addAll(listWithBlanksReplaced);

		/** Extending letters */
		for (String word : board.getPlayedWords()) {
			/** Extending from first letter */

			int[] playedWordStartCoords = board.convert(board.getWordCoordinateMap().get(word));

			/** Horizontal from left */
			if (board.getWordDirectionMap().get(word) == "H") {
				for (int i = 0; i < listOfAllPermutations.size(); i++) {

					boolean flag = true;

					String generatedWord = listOfAllPermutations.get(i);

					String newWord = generatedWord + word;

					for (int j = 0; j < generatedWord.length(); j++) {
						if (board.isFieldValid(playedWordStartCoords[0], playedWordStartCoords[1] - 1 - j) == false
								|| board.isFieldEmpty(playedWordStartCoords[0],
										playedWordStartCoords[1] - 1 - j) == false) {
							flag = false;
						}
					}
					if (flag != false) {
						if (checker.isValidWord(newWord) == null) {
							flag = false;
						}
						if (playedWordStartCoords[1] - generatedWord.length() > 0
								&& playedWordStartCoords[1] - generatedWord.length() < 15) {

							if (adjacentChecker.areAdjacentWordsValid(board.convert(playedWordStartCoords[0],
									playedWordStartCoords[1] - generatedWord.length()), "H", newWord)) {
								flag = false;
							}
						}
					}

					if (flag == true) {
						move = move + "WORD " + board.convert(playedWordStartCoords[0],
								playedWordStartCoords[1] - 1 - generatedWord.length()) + " H " + generatedWord;
						listOfExtendingMoves.add(move);
						scoreMap.put(move, scoreCounter.getTotalWordScoreHorizontal(newWord, playedWordStartCoords[0],
								playedWordStartCoords[1] - 1 - generatedWord.length()));
						move = "";
					}
				}
			}
			/** Vertical from top */
			else {
				for (int i = 0; i < listOfAllPermutations.size(); i++) {

					boolean flag = true;

					String generatedWord = listOfAllPermutations.get(i);

					String newWord = generatedWord + word;

					for (int j = 0; j < generatedWord.length(); j++) {
						if (board.isFieldValid(playedWordStartCoords[0] - 1 - j, playedWordStartCoords[1]) == false
								|| board.isFieldEmpty(playedWordStartCoords[0] - 1 - j,
										playedWordStartCoords[1]) == false) {
							flag = false;
						}
					}
					if (flag != false) {
						if (checker.isValidWord(newWord) == null) {
							flag = false;
						}
						if (playedWordStartCoords[0] - generatedWord.length() > 0
								&& playedWordStartCoords[0] - generatedWord.length() < 15) {

							if (adjacentChecker.areAdjacentWordsValid(
									board.convert(playedWordStartCoords[0] - 1 - generatedWord.length(),
											playedWordStartCoords[1]),
									"V", newWord)) {
								flag = false;
							}
						}
					}

					if (flag == true) {
						move = move + "WORD " + board.convert(playedWordStartCoords[0] - 1 - generatedWord.length(),
								playedWordStartCoords[1]) + " V " + generatedWord;
						listOfExtendingMoves.add(move);
						scoreMap.put(move, scoreCounter.getTotalWordScoreHorizontal(newWord,
								playedWordStartCoords[0] - 1 - generatedWord.length(), playedWordStartCoords[1]));
						move = "";
					}
				}
			}

			/** Extending from last letter */
			/** Horizontal from right */
			if (board.getWordDirectionMap().get(word).equals("H")) {
				for (int i = 0; i < listOfAllPermutations.size(); i++) {

					boolean flag = true;

					int[] coordsOfLastLetterOfPlayedWord = new int[2];

					coordsOfLastLetterOfPlayedWord[0] = playedWordStartCoords[0];

					coordsOfLastLetterOfPlayedWord[1] = playedWordStartCoords[1] + word.length() - 1;

					String generatedWord = listOfAllPermutations.get(i);

					String newWord = word + generatedWord;

					for (int j = 0; j < generatedWord.length(); j++) {
						if (board.isFieldValid(coordsOfLastLetterOfPlayedWord[0],
								coordsOfLastLetterOfPlayedWord[1] + 1 + j) == false
								|| board.isFieldEmpty(coordsOfLastLetterOfPlayedWord[0],
										coordsOfLastLetterOfPlayedWord[1] + 1 + j) == false) {
							flag = false;
						}
					}
					if (flag != false) {
						if (checker.isValidWord(newWord) == null) {
							flag = false;
						}

						if (adjacentChecker.areAdjacentWordsValid(
								board.convert(playedWordStartCoords[0], playedWordStartCoords[1]), "H", newWord)) {
							flag = false;
						}
					}

					if (flag == true) {
						move = move + "WORD "
								+ board.convert(playedWordStartCoords[0], playedWordStartCoords[1] + word.length())
								+ " H " + generatedWord;
						listOfExtendingMoves.add(move);
						scoreMap.put(move, scoreCounter.getTotalWordScoreHorizontal(newWord, playedWordStartCoords[0],
								playedWordStartCoords[1]));
						move = "";
					}
				}
			}
			/** Vertical from bottom */
			else {
				for (int i = 0; i < listOfAllPermutations.size(); i++) {

					boolean flag = true;

					int[] coordsOfLastLetterOfPlayedWord = new int[2];

					coordsOfLastLetterOfPlayedWord[0] = playedWordStartCoords[0] + word.length() - 1;

					coordsOfLastLetterOfPlayedWord[1] = playedWordStartCoords[1];

					String generatedWord = listOfAllPermutations.get(i);

					String newWord = word + generatedWord;

					for (int j = 0; j < generatedWord.length(); j++) {
						if (board.isFieldValid(coordsOfLastLetterOfPlayedWord[0] + 1 + j,
								coordsOfLastLetterOfPlayedWord[1]) == false
								|| board.isFieldEmpty(coordsOfLastLetterOfPlayedWord[0] + 1 + j,
										coordsOfLastLetterOfPlayedWord[1]) == false) {
							flag = false;
						}
					}
					if (flag != false) {
						if (checker.isValidWord(newWord) == null) {
							flag = false;
						}

						if (adjacentChecker.areAdjacentWordsValid(
								board.convert(playedWordStartCoords[0], playedWordStartCoords[1]), "V", newWord)) {
							flag = false;
						}
					}

					if (flag == true) {
						move = move + "WORD " + board.convert(playedWordStartCoords[0] + generatedWord.length(),
								playedWordStartCoords[1]) + " V " + generatedWord;
						listOfExtendingMoves.add(move);
						scoreMap.put(move, scoreCounter.getTotalWordScoreHorizontal(newWord, playedWordStartCoords[0],
								playedWordStartCoords[1]));
						move = "";
					}
				}
			}
		}

		/** Cycle through generated list of words */
		for (String word : listOfAllPermutations) {
			/** Suitable word found */
			if (checker.isValidWord(word) != null) {
				/** If first turn play from center */
				if (board.getPlayedWords().isEmpty()) {
					/**
					 * We dont have to check if the word will fit here because it will always fit
					 */
					move = move + "WORD H8 H " + word;
					listOfMoves.add(move);
					move = "";
				}
				/** If word was not already played */
				else if (!board.getPlayedWords().contains(word)) {
					/** Cycle though words already on board */
					for (String playedWord : board.getPlayedWords()) {
						/** Cycle through letters in played word */
						for (char playedWordLetter : playedWord.toCharArray()) {
							/** Cycle through letters in generated word */
							for (char letter : word.toCharArray()) {
								/** Find matching letter */
								if (playedWordLetter == letter) {
									/** Stores Matching letters */
									char matchingLetter = letter;
									/** If the played word is horizontal */
									if (board.getWordDirectionMap().get(playedWord).equals("H")) {
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
											/**
											 * Flag to tell if fields are not empty or invalid or adjacent words would
											 * not be correct
											 */
											boolean flag = true;
											/** Create Starting coordinate of new word */
											String startCoordinateOfNewWord = "";
											if (playedWordStartCoords[0] - indexMatchingLetterWord < 15
													&& playedWordStartCoords[0] - indexMatchingLetterWord >= 0) {
												startCoordinateOfNewWord = board.convert(
														board.convert(coordsOfMatchingLetter)[0]
																- indexMatchingLetterWord,
														board.convert(coordsOfMatchingLetter)[1]);
											}
											if (!startCoordinateOfNewWord.equals("")) {
												/** How far up does the word go above the matching letter */
												int up = 0 + indexMatchingLetterWord;
												/** How far down does the word go under the matching letter */
												int down = word.length() - 1 - indexMatchingLetterWord;
												/** Converted coordinates of matching letter */
												int[] checkedCoordinate = board.convert(coordsOfMatchingLetter);
												/** Check if fields below are empty and valid */
												for (int j = down; j >= 1; j--) {
													if (!board.isFieldValid(checkedCoordinate[0] + j,
															checkedCoordinate[1])
															|| !board.isFieldEmpty(checkedCoordinate[0] + j,
																	checkedCoordinate[1])) {
														flag = false;
													}
												}
												/** Check if field above are empty and valid */
												for (int j = 1; j <= up; j++) {
													if (!board.isFieldValid(checkedCoordinate[0] - j,
															checkedCoordinate[1])
															|| !board.isFieldEmpty(checkedCoordinate[0] - j,
																	checkedCoordinate[1])) {
														flag = false;
													}
												}
												/**
												 * Check if the newly created letter would be compatible with adjacent
												 * words
												 */
												if (adjacentChecker.areAdjacentWordsValid(startCoordinateOfNewWord, "V",
														word) == false) {
													flag = false;
												}
												/**
												 * If none of the conditions above changed the flag to false, return a
												 * word to be placed with starting coordinate and direction
												 */
												if (flag == true) {
													move = move + "WORD "
															+ board.convert(
																	board.convert(coordsOfMatchingLetter)[0]
																			- indexMatchingLetterWord,
																	board.convert(coordsOfMatchingLetter)[1])
															+ " V " + word;
													listOfMoves.add(move);
													move = "";
												}
											}
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
											/**
											 * Flag to tell if fields are not empty or invalid or adjacent words would
											 * not be correct
											 */
											boolean flag = true;
											/** Create Starting coordinate of new word */
											String startCoordinateOfNewWord = "";
											if (playedWordStartCoords[1] - indexMatchingLetterWord < 15
													&& playedWordStartCoords[1] - indexMatchingLetterWord >= 0) {
												startCoordinateOfNewWord = board.convert(
														board.convert(coordsOfMatchingLetter)[0],
														board.convert(coordsOfMatchingLetter)[1]
																- indexMatchingLetterWord);
											}
											if (!startCoordinateOfNewWord.equals("")) {
												/** How far left does the word go left from the matching letter */
												int left = 0 + indexMatchingLetterWord;
												/** How far right does the word go right from the matching letter */
												int right = word.length() - 1 - indexMatchingLetterWord;
												/** Converted coordinates of matching letter */
												int[] checkedCoordinate = board.convert(coordsOfMatchingLetter);
												/** Check if fields below are empty and valid */
												for (int j = right; j >= 1; j--) {
													if (!board.isFieldValid(checkedCoordinate[0],
															checkedCoordinate[1] + j)
															|| !board.isFieldEmpty(checkedCoordinate[0],
																	checkedCoordinate[1] + j)) {
														flag = false;
													}
												}
												/** Check if field above are empty and valid */
												for (int j = 1; j <= left; j++) {
													if (!board.isFieldValid(checkedCoordinate[0],
															checkedCoordinate[1] - j)
															|| !board.isFieldEmpty(checkedCoordinate[0],
																	checkedCoordinate[1] - j)) {
														flag = false;
													}
												}
												/**
												 * Check if the newly created letter would be compatible with adjacent
												 * words
												 */
												if (adjacentChecker.areAdjacentWordsValid(startCoordinateOfNewWord, "H",
														word) == false) {
													flag = false;
												}
												/**
												 * If none of the conditions above changed the flag to false, return a
												 * word to be placed with starting coordinate and direction
												 */
												if (flag == true) {
													move = move
															+ "WORD " + board
																	.convert(board.convert(coordsOfMatchingLetter)[0],
																			board.convert(coordsOfMatchingLetter)[1]
																					- indexMatchingLetterWord)
															+ " H " + word;
													listOfMoves.add(move);
													move = "";
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

		}

		if (listOfMoves.isEmpty()) {
			return "";
		}

		else {

			for (int i = 0; i < listOfMoves.size(); i++) {
				if (listOfMoves.get(i).split(" ")[2].equals("H")) {
					scoreMap.put(listOfMoves.get(i),
							scoreCounter.getTotalWordScoreHorizontal(listOfMoves.get(i).split(" ")[3],
									board.convert(listOfMoves.get(i).split(" ")[1])[0],
									board.convert(listOfMoves.get(i).split(" ")[1])[1]));
				} else {
					scoreMap.put(listOfMoves.get(i),
							scoreCounter.getTotalWordScoreVertical(listOfMoves.get(i).split(" ")[3],
									board.convert(listOfMoves.get(i).split(" ")[1])[0],
									board.convert(listOfMoves.get(i).split(" ")[1])[1]));
				}

			}

			int max = Collections.max(scoreMap.values());

			for (Entry<String, Integer> entry : scoreMap.entrySet()) {
				if (Objects.equals(max, entry.getValue())) {
					return entry.getKey();
				}
			}
		}

		return "";

	}

	/**
	 * Returns true if a String contains a blank tile
	 * 
	 * @param str
	 * @return true||false
	 * @author Maxim
	 */
	public static boolean containsBlank(String str) {
		for (char c : str.toCharArray()) {
			if (c == '*') {
				return true;
			}
		}
		return false;
	}

	public String swapHand(LetterDeck letterDeck) {

		String lettersInHand = "";

		for (char letter : letterDeck.getLettersInDeck()) {
			lettersInHand = lettersInHand + letter;
		}
		return lettersInHand;
	}
}