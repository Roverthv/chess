package client;

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
            String input = scanner.nextLine();
            if (state == State.SIGNEDOUT) {
                handlePreLogin(input);
            } else {
                handlePostLogin(input);
            }
        }
    }

    private void handlePreLogin(String input) {
    }

    private void handlePostLogin(String input) {
    }
}
