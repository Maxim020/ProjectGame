package server.utils;

public class Protocol {
    public static final char MESSAGE_SEPARATOR = '\u001e';
    public static final char UNIT_SEPARATOR = '\u001f';

    public interface Command {}
    public enum BasicCommand implements Command {
        ANNOUNCE,
        WELCOME,
        REQUESTGAME,
        INFORMQUEUE,
        STARTGAME,
        NOTIFYTURN,
        MAKEMOVE,
        NEWTILES,
        INFORMMOVE,
        ERROR,
        GAMEOVER,
        PLAYERDISCONNECTED
    }

    public enum ChatCommand implements Command {
        SENDCHAT,
        NOTIFYCHAT
    }

    public enum Error {
        E001("Name already taken"),
        E002("Unknown command"),
        E003("Invalid argument"),
        E004("Invalid coordinate"),
        E005("Word does not fit on board"),
        E006("Unknown word"),
        E007("Too few letters left in tilebag to swap"),
        E008("You do not have the required tiles"),
        E009("It is not your turn");

        private final String description;

        Error(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }
    }

}
