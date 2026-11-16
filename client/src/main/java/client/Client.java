package client;

import ui.EscapeSequences;

import java.util.List;
import java.util.Scanner;

public class Client {
    enum State {
        QUIT, SIGNEDOUT, SIGNEDIN
    }

    State state = State.SIGNEDOUT;
    Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("Welcome to Chess. Type Help to get started.");
        while (!state.equals(State.QUIT)) {
            printHeader();
            String input = scanner.nextLine().toLowerCase();
            if (state == State.SIGNEDOUT) {
                handlePreLogin(input);
            } else {
                handlePostLogin(input);
            }
        }
    }

    private void printHeader() {
        String stateHeader = determineHeader();
        System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        System.out.printf("%s >>>: ", stateHeader);
    }

    private String determineHeader() {
        if (state == State.SIGNEDOUT) {
            return "[SIGNED OUT]";
        } else {
            return "[SIGNED IN]";
        }
    }

    private void handlePreLogin(String input) {
        switch (input) {
            case "help" -> handleHelp(SIGNEDOUTCOMMANDS);
            case "quit" -> state = State.QUIT;
            case "login" -> handleLogin();
            case "register" -> handleRegister();
            default -> handleInvalid(input);
        }
    }

    private void handlePostLogin(String input) {
        switch (input) {
            case "help" -> handleHelp(SIGNEDINCOMMANDS);
            case "logout" -> handleLogout();
            case "create" -> handleCreate();
            case "list" -> handleList();
            case "join" -> handleJoin();
            case "observe" -> handleObserve();
            default -> handleInvalid(input);
        }
    }

    private void handleObserve() {
    }

    private void handleJoin() {
    }

    private void handleList() {
    }

    private void handleCreate() {
    }

    private void handleLogout() {
    }

    private void handleLogin() {
    }

    private void handleRegister() {
    }

    private void handleHelp(String[] commands) {
        for (int i = 0; i < commands.length; i = i + 2) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
            System.out.print(commands[i]);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
            System.out.print(commands[i + 1]);
            System.out.println();
        }
    }

    private void handleInvalid(String input) {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.printf("%s is not a valid command, type 'help' to receive a list of commands", input);
        System.out.println();
    }

    private String[] SIGNEDOUTCOMMANDS = {
            "register", " - create a new account",
            "login", " - play on an existing account",
            "quit", " - close application",
            "help", " - view possible commands"
    };

    private String[] SIGNEDINCOMMANDS = {
            "create", " - create a new game",
            "list", " - list existing games",
            "join", " - play in an existing game",
            "observe", " - watch an existing game",
            "logout", " - sign out of account",
            "help", " - view possible commands"
    };
}
