package scrabble.view;

public interface UserInterface {

    public boolean isInputValid(String input);

    public String getInput();

    public void updateBoard();

    public void showRack();

    public void printInstructions();
}
