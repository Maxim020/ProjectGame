package server.view;

import java.io.PrintWriter;
import java.util.Scanner;

public class ServerTUI {
    private PrintWriter printWriter;
    private Scanner scanner;

    public ServerTUI(){
        printWriter = new PrintWriter(System.out, true);
        scanner = new Scanner(System.in);
    }

    public void showMessage(String msg){
        printWriter.println(msg);
    }

    public int getInt(String question){
        showMessage(question);
        String s = scanner.nextLine();
        if(isNumeric(s)){
            return Integer.parseInt(s);
        }
        else {
            return 0;
        }
    }

    public boolean isNumeric(String string){
        try{
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    public boolean getBoolean(String question){
        showMessage(question);
        showMessage("yes or no?");
        return scanner.nextLine().equalsIgnoreCase("yes");
    }
}
