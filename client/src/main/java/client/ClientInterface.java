package client;

import chess.ChessGame;
import ui.EscapeSequences;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ClientInterface {
    enum State {
        QUIT, SIGNEDOUT, SIGNEDIN
    }

    State state = State.SIGNEDOUT;
    Scanner scanner = new Scanner(System.in);
    ClientBackground facade = new ClientBackground();
    List<GameStruct> gameList;

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
        boolean valid = false;
        int id = 1;
        while (!valid) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
            System.out.print("Enter Game Number: ");
            while (!scanner.hasNextInt()) {
                String invalidInput = scanner.nextLine();
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.printf("%s is not a number, please provide the number of the game you wish to join: ", invalidInput);
            }
            id = scanner.nextInt();
            if (id < 0 || id > gameList.size()) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.printf("%d is not a game in the list please try again", id);
                System.out.println();
                break;
            }
            valid = true;
        }
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.print("Enter the team color you would like play: ");
        String team = scanner.nextLine();

        while (!Objects.equals(team, "white") || !Objects.equals(team, "black")) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
            System.out.printf("%s is not a valid team color, please enter 'white' or 'black': ", team);
            team = scanner.nextLine();
        }
        double gameID = gameList.get(id - 1).getGameID();
        ChessGame game = facade.sendJoinGameRequest(gameID, team);
        //print out board based on what we got back.
    }

    private void handleList() {
        gameList = facade.sendListGameRequest();
        int count = 1;
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        for (GameStruct i : gameList) {
            System.out.printf("%d. %s: White: %s, Black: %s", count, i.getGameName(), i.getWhiteUsername(), i.getBlackUsername());
            System.out.println();
            count++;
        }
    }

    private void handleCreate() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.print("What would you like to call this game?: ");
        String gameName = scanner.nextLine();
        facade.sendCreateGameRequest(gameName);
    }

    private void handleLogout() {
        facade.sendLogoutRequest();
        state = State.SIGNEDOUT;
    }

    private void handleLogin() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        facade.sendLoginRequest(username, password);
        state = State.SIGNEDIN;
    }

    private void handleRegister() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.print("Enter Desired Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Desired Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        facade.sendRegisterRequest(username, password, email);
        state = State.SIGNEDIN;
    }

    private void handleHelp(String[] commands) {
        for (int i = 0; i < commands.length; i = i + 2) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
            System.out.print(commands[i]);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            System.out.print(commands[i + 1]);
            System.out.println();
        }
    }

    private void handleInvalid(String input) {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.printf("%s is not a valid command, type 'help' to receive a list of commands", input);
        System.out.println();
    }

    private final String[] SIGNEDOUTCOMMANDS = {
            "register", " - create a new account",
            "login", " - play on an existing account",
            "quit", " - close application",
            "help", " - view possible commands"
    };

    private final String[] SIGNEDINCOMMANDS = {
            "create", " - create a new game",
            "list", " - list existing games",
            "join", " - play in an existing game",
            "observe", " - watch an existing game",
            "logout", " - sign out of account",
            "help", " - view possible commands"
    };
}
