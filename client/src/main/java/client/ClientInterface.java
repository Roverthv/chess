package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
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
    ServerFacade facade = new ServerFacade(8080);
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
        int id = retrieveGameNumber();

        double gameID = gameList.get(id - 1).getGameID();
        ChessGame game;
        try {
            game = facade.sendObserveGameRequest(gameID);
            printBoardPerspectiveWhite(game);
        } catch (ClientEndException e) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (e.getMessage().equals("bad request")) {
                System.out.print("Chosen Game Does Not Exist.\n");
            } else {
                System.out.print(e.getMessage());
                System.out.println();
            }
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(EscapeSequences.RESET_BG_COLOR);
        }
    }

    private void handleJoin() {
        int id = retrieveGameNumber();

        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.print("Enter the team color you would like play: ");
        String team = scanner.nextLine().toUpperCase();
        if (!Objects.equals(team, "WHITE") && !Objects.equals(team, "BLACK")) {
            while (!Objects.equals(team, "WHITE") && !Objects.equals(team, "BLACK")) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.printf("%s is not a valid team color, please enter 'WHITE' or 'BLACK': ", team);
                team = scanner.nextLine().toUpperCase();
            }
        }
        double gameID = gameList.get(id - 1).getGameID();
        ChessGame game;
        try {
            game = facade.sendJoinGameRequest(gameID, team);
            if (Objects.equals(team, "WHITE")) {
                printBoardPerspectiveWhite(game);
            } else {
                printBoardPerspectiveBlack(game);
            }
        } catch (ClientEndException e) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (e.getMessage().equals("bad request")) {
                System.out.print("Chosen Game or Team Does Not Exist.\n");
            } else {
                System.out.print(e.getMessage());
                System.out.println();
            }
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(EscapeSequences.RESET_BG_COLOR);
        }
    }

    private void handleList() {
        try {
            gameList = facade.sendListGameRequest();
            int count = 1;
            System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
            for (GameStruct i : gameList) {
                System.out.printf("%d. %s: White: %s, Black: %s", count, i.getGameName(), i.getWhiteUsername(), i.getBlackUsername());
                System.out.println();
                count++;
            }
        } catch (ClientEndException e) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (e.getMessage().equals("unauthorized")) {
                System.out.print("Issue with session permissions, please logout and login back in.\n");
            } else {
                System.out.print(e.getMessage());
            }
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(EscapeSequences.RESET_BG_COLOR);
        }
    }

    private void handleCreate() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.print("What would you like to call this game?: ");
        String gameName = scanner.nextLine();
        try {
            facade.sendCreateGameRequest(gameName);
        } catch (ClientEndException e) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (e.getMessage().equals("unauthorized")) {
                System.out.print("Issue with session permissions, please logout and login back in.\n");
            } else if (e.getMessage().equals("bad request")) {
                System.out.print("Invalid name please try again.\n");
            } else {
                System.out.print(e.getMessage());
            }
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(EscapeSequences.RESET_BG_COLOR);
        }
    }

    private void handleLogout() {
        try {
            facade.sendLogoutRequest();
            state = State.SIGNEDOUT;
        } catch (ClientEndException e) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (e.getMessage().equals("unauthorized")) {
                System.out.print("Issue with Connection, Logging user out.\n");
                state = State.SIGNEDOUT;
            } else {
                System.out.print(e.getMessage());
            }
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(EscapeSequences.RESET_BG_COLOR);
        }
    }

    private void handleLogin() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        try {
            facade.sendLoginRequest(username, password);
            state = State.SIGNEDIN;
        } catch (ClientEndException e) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (e.getMessage().equals("bad request")) {
                System.out.print("Invalid username, password, Please try again.\n");
            } else if (e.getMessage().equals("unauthorized")) {
                System.out.print("Incorrect password, Please try again.\n");
            } else {
                System.out.print(e.getMessage());
            }
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(EscapeSequences.RESET_BG_COLOR);
        }
    }

    private void handleRegister() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.print("Enter Desired Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Desired Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        try {
            facade.sendRegisterRequest(username, password, email);
            state = State.SIGNEDIN;
        } catch (ClientEndException e) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(EscapeSequences.SET_BG_COLOR_RED);
            if (e.getMessage().equals("bad request")) {
                System.out.print("Invalid username, password, or email. Please try again.\n");
            } else if (e.getMessage().equals("name taken")) {
                System.out.print("Username is already taken, Please try again.\n");
            } else {
                System.out.print(e.getMessage());
            }
            System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            System.out.print(EscapeSequences.RESET_BG_COLOR);
        }
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

    private static final String[] SIGNEDOUTCOMMANDS = {
            "register", " - create a new account",
            "login", " - play on an existing account",
            "quit", " - close application",
            "help", " - view possible commands"
    };

    private static final String[] SIGNEDINCOMMANDS = {
            "create", " - create a new game",
            "list", " - list existing games",
            "join", " - play in an existing game",
            "observe", " - watch an existing game",
            "logout", " - sign out of account",
            "help", " - view possible commands"
    };

    private void printBoardPerspectiveWhite(ChessGame game) {
        ChessBoard board = game.getBoard();
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        for (String i : WHITEPERSPECTIVEBOARDERROW) {
            System.out.print(i);
        }
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.println();
        for (int i = 8; i > 0; i--) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.printf("\u2003%d ", i);
            for (int j = 1; j < 9; j++) {
                printInnerBoard(i, j, board);
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.printf(" %d\u2003", i);
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.println();
        }
        for (String i : WHITEPERSPECTIVEBOARDERROW) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(i);
        }
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.println();
    }

    private void printBoardPerspectiveBlack(ChessGame game) {
        ChessBoard board = game.getBoard();
        System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
        for (String i : BLACKPERSPECTIVEBOARDERROW) {
            System.out.print(i);
        }
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.println();
        for (int i = 1; i < 9; i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.printf("\u2003%d ", i);
            for (int j = 8; j > 0; j--) {
                printInnerBoard(i, j, board);
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.printf(" %d\u2003", i);
            System.out.print(EscapeSequences.RESET_BG_COLOR);
            System.out.println();
        }
        for (String i : BLACKPERSPECTIVEBOARDERROW) {
            System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLACK);
            System.out.print(i);
        }
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.println();
    }

    private void printInnerBoard(int i, int j, ChessBoard board) {
        ChessPiece piece = board.getPiece(new ChessPosition(i, j));
        if (piece != null) {
            printTile(i, j, piece.getTeamColor(), piece.getPieceType());
        } else {
            printTileNull(i, j);
        }
    }

    private void printTileNull(int i, int j) {
        if ((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
            printBlackTileBlackTeam(EscapeSequences.EMPTY);
        } else {
            printWhiteTileBlackTeam(EscapeSequences.EMPTY);
        }
    }

    private void printTile(int i, int j, ChessGame.TeamColor team, ChessPiece.PieceType piece) {
        if ((i % 2 == 0 && j % 2 == 0) || (i % 2 != 0 && j % 2 != 0)) {
            if (team == ChessGame.TeamColor.WHITE) {
                String character = convertWhitePieceToText(piece);
                printBlackTileWhiteTeam(character);
            } else {
                String character = convertBlackPieceToText(piece);
                printBlackTileBlackTeam(character);
            }
        } else {
            if (team == ChessGame.TeamColor.WHITE) {
                String character = convertWhitePieceToText(piece);
                printWhiteTileWhiteTeam(character);
            } else {
                String character = convertBlackPieceToText(piece);
                printWhiteTileBlackTeam(character);
            }
        }
    }

    private String convertWhitePieceToText(ChessPiece.PieceType piece) {
        String character;
        switch (piece) {
            case ChessPiece.PieceType.KING -> character = EscapeSequences.WHITE_KING;
            case ChessPiece.PieceType.QUEEN -> character = EscapeSequences.WHITE_QUEEN;
            case ChessPiece.PieceType.BISHOP -> character = EscapeSequences.WHITE_BISHOP;
            case ChessPiece.PieceType.KNIGHT -> character = EscapeSequences.WHITE_KNIGHT;
            case ChessPiece.PieceType.ROOK -> character = EscapeSequences.WHITE_ROOK;
            case ChessPiece.PieceType.PAWN -> character = EscapeSequences.WHITE_PAWN;
            default -> throw new IllegalStateException("Unexpected value: " + piece);
        }
        return character;
    }

    private String convertBlackPieceToText(ChessPiece.PieceType piece) {
        String character;
        switch (piece) {
            case ChessPiece.PieceType.KING -> character = EscapeSequences.BLACK_KING;
            case ChessPiece.PieceType.QUEEN -> character = EscapeSequences.BLACK_QUEEN;
            case ChessPiece.PieceType.BISHOP -> character = EscapeSequences.BLACK_BISHOP;
            case ChessPiece.PieceType.KNIGHT -> character = EscapeSequences.BLACK_KNIGHT;
            case ChessPiece.PieceType.ROOK -> character = EscapeSequences.BLACK_ROOK;
            case ChessPiece.PieceType.PAWN -> character = EscapeSequences.BLACK_PAWN;
            default -> throw new IllegalStateException("Unexpected value: " + piece);
        }
        return character;
    }

    private void printBlackTileBlackTeam(String character) {
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(character);
    }

    private void printBlackTileWhiteTeam(String character) {
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print(character);
    }

    private void printWhiteTileBlackTeam(String character) {
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(character);
    }

    private void printWhiteTileWhiteTeam(String character) {
        System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print(character);
    }

    private static final String[] WHITEPERSPECTIVEBOARDERROW = {EscapeSequences.EMPTY, "\u2003a ", "\u2003b ",
            "\u2003c ", "\u2003d ", "\u2003e ", "\u2003f ", "\u2003g ", "\u2003h ", EscapeSequences.EMPTY};
    private static final String[] BLACKPERSPECTIVEBOARDERROW = {EscapeSequences.EMPTY, "\u2003h ", "\u2003g ",
            "\u2003f ", "\u2003e ", "\u2003d ", "\u2003c ", "\u2003b ", "\u2003a ", EscapeSequences.EMPTY};

    private int retrieveGameNumber() {
        if (gameList == null) {
            handleList();
        }
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
            scanner.nextLine();
            if (id < 0 || id > gameList.size()) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.printf("%d is not a game in the list please try again", id);
                System.out.println();
                continue;
            }
            valid = true;
        }
        return id;
    }
}
