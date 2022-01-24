package scrabble.model;

import java.util.ArrayList;
import java.util.List;

import scrabble.model.words.AdjacentWordChecker;
import scrabble.model.words.InMemoryScrabbleWordChecker;
import scrabble.model.words.ScrabbleWordChecker;

public class DeadEndChecker {
	
	
	/**
	 * Method for checking whether there is a possible word to be played from the current decks of players.
	 * Should be called near the end of the game, preferably when the bag is empty.
	 * @requires PlayerList to be instantiated
	 * @ensures return of true when no more words can be created
	 * @return true || false
	 * @author Maxim
	 */
	public boolean isDeadEnd(Board board) {

		List<Player> playerList = PlayerList.getInstance().getPlayers();

		ArrayList<String> listOfWords = new ArrayList<>();

		ScrabbleWordChecker checker = new InMemoryScrabbleWordChecker();
		
		ArrayList<String> listWithBlanksReplaced = new ArrayList<>();
		
		AdjacentWordChecker adjacentChecker = new AdjacentWordChecker(board);

		for (int i = 0; i < playerList.size(); i++) {
			Player player = playerList.get(i);
			for (int j = 0; j < player.getLetterDeck().getLettersInDeck().size(); j++) {
				listOfWords.addAll(SmartStrategy.determineWord(SmartStrategy.formString(player.getLetterDeck()),
						SmartStrategy.formString(player.getLetterDeck()).length() - j));
			}
		}
		
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		/** Blank replacer */
		for (int i = 0; i < listOfWords.size(); i++) {
			for (int j = 0; j < alphabet.toCharArray().length; j++) {
				if (SmartStrategy.containsBlank(listOfWords.get(i))) {
					String blankReplaced = listOfWords.get(i).replace('*', alphabet.toCharArray()[j]);
					listWithBlanksReplaced.add(blankReplaced);
				}
			}
		}
		
		listOfWords.addAll(listWithBlanksReplaced);
		
		/** Extending letters */
		for (String word : board.getPlayedWords()) {
			/** Extending from first letter */

			int[] playedWordStartCoords = board.convert(board.getWordCoordinateMap().get(word));

			/** Horizontal from left */
			if (board.getWordDirectionMap().get(word) == "H") {
				for (int i = 0; i < listOfWords.size(); i++) {

					boolean flag = true;

					String generatedWord = listOfWords.get(i);

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

						if (adjacentChecker.areAdjacentWordsValid(board.convert(playedWordStartCoords[0],
								playedWordStartCoords[1] - generatedWord.length()), "H", newWord)) {
							flag = false;
						}
					}

					if (flag == true) {
						return false;
					}
				}
			}
			/** Vertical from top */
			else {
				for (int i = 0; i < listOfWords.size(); i++) {

					boolean flag = true;

					String generatedWord = listOfWords.get(i);

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

						if (adjacentChecker.areAdjacentWordsValid(
								board.convert(playedWordStartCoords[0] - 1 - generatedWord.length(),
										playedWordStartCoords[1]),
								"V", newWord)) {
							flag = false;
						}
					}

					if (flag == true) {
						return false;
					}
				}
			}

			/** Extending from last letter */
			/** Horizontal from right */
			if (board.getWordDirectionMap().get(word) == "H") {
				for (int i = 0; i < listOfWords.size(); i++) {

					boolean flag = true;

					int[] coordsOfLastLetterOfPlayedWord = new int[2];

					coordsOfLastLetterOfPlayedWord[0] = playedWordStartCoords[0];

					coordsOfLastLetterOfPlayedWord[1] = playedWordStartCoords[1] + word.length() - 1;

					String generatedWord = listOfWords.get(i);

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
						return false;
					}
				}
			}
			/** Vertical from bottom */
			else {
				for (int i = 0; i < listOfWords.size(); i++) {

					boolean flag = true;

					int[] coordsOfLastLetterOfPlayedWord = new int[2];

					coordsOfLastLetterOfPlayedWord[0] = playedWordStartCoords[0] + word.length() - 1;

					coordsOfLastLetterOfPlayedWord[1] = playedWordStartCoords[1];

					String generatedWord = listOfWords.get(i);

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
						return false;
					}
				}
			}
		}
		
		/** Cycle through generated list of words */
		for (String word : listOfWords) {
			/** Suitable word found */
			if (checker.isValidWord(word) != null) {
				/** If word was not already played */
				if (!board.getPlayedWords().contains(word)) {
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
									if (board.getWordDirectionMap().get(playedWord) == "H") {
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
											String startCoordinateOfNewWord = board.convert(
													board.convert(coordsOfMatchingLetter)[0] - indexMatchingLetterWord,
													board.convert(coordsOfMatchingLetter)[1]);
											/** How far up does the word go above the matching letter */
											int up = 0 + indexMatchingLetterWord;
											/** How far down does the word go under the matching letter */
											int down = word.length() - 1 - indexMatchingLetterWord;
											/** Converted coordinates of matching letter */
											int[] checkedCoordinate = board.convert(coordsOfMatchingLetter);
											/** Check if fields below are empty and valid */
											for (int j = down; j >= 1; j--) {
												if (!board.isFieldValid(checkedCoordinate[0] + j, checkedCoordinate[1])
														|| !board.isFieldEmpty(checkedCoordinate[0] + j,
																checkedCoordinate[1])) {
													flag = false;
												}
											}
											/** Check if field above are empty and valid */
											for (int j = 1; j <= up; j++) {
												if (!board.isFieldValid(checkedCoordinate[0] - j, checkedCoordinate[1])
														|| !board.isFieldEmpty(checkedCoordinate[0] - j,
																checkedCoordinate[1])) {
													flag = false;
												}
											}
											/**
											 * Check if the newly created letter would be compatible with adjacent words
											 */
											if (!adjacentChecker.areAdjacentWordsValid(startCoordinateOfNewWord, "V",
													word)) {
												flag = false;
											}
											/**
											 * If none of the conditions above changed the flag to false, return a word
											 * to be placed with starting coordinate and direction
											 */
											if (flag == true) {
												return false;
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
											String startCoordinateOfNewWord = board.convert(
													board.convert(coordsOfMatchingLetter)[0],
													board.convert(coordsOfMatchingLetter)[1] - indexMatchingLetterWord);
											/** How far left does the word go left from the matching letter */
											int left = 0 + indexMatchingLetterWord;
											/** How far right does the word go right from the matching letter */
											int right = word.length() - 1 - indexMatchingLetterWord;
											/** Converted coordinates of matching letter */
											int[] checkedCoordinate = board.convert(coordsOfMatchingLetter);
											/** Check if fields below are empty and valid */
											for (int j = right; j >= 0; j--) {
												if (!board.isFieldValid(checkedCoordinate[0], checkedCoordinate[1] + j)
														|| !board.isFieldEmpty(checkedCoordinate[0],
																checkedCoordinate[1] + j)) {
													flag = false;
												}
											}
											/** Check if field above are empty and valid */
											for (int j = 0; j < left; j++) {
												if (!board.isFieldValid(checkedCoordinate[0], checkedCoordinate[1] - j)
														|| !board.isFieldEmpty(checkedCoordinate[0],
																checkedCoordinate[1] - j)) {
													flag = false;
												}
											}
											/**
											 * Check if the newly created letter would be compatible with adjacent words
											 */
											if (!adjacentChecker.areAdjacentWordsValid(startCoordinateOfNewWord, "H",
													word)) {
												flag = false;
											}
											/**
											 * If none of the conditions above changed the flag to false, return a word
											 * to be placed with starting coordinate and direction
											 */
											if (flag == true) {
												return false;
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
		return true;
	}
}
