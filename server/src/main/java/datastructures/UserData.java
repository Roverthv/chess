package datastructures;

import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Objects;

public record UserData(String username, String password, String email, ArrayList<Integer> gameIDs) {
    public String getUsername() {
        return username;
    }
    public String getEmail() { return email;}

    public boolean checkPassword(String passGiven) {
        return BCrypt.checkpw(passGiven, password);
    }

    public ArrayList<Integer> getGameList() {
        return gameIDs;
    }

    public void addGame(int gameID) {
        gameIDs.add(gameID);
    }

    public void removeGame(int gameID) {
        gameIDs.remove(Integer.valueOf(gameID));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserData userData = (UserData) o;
        return Objects.equals(username, userData.username) &&
                Objects.equals(password, userData.password) &&
                Objects.equals(gameIDs, userData.gameIDs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, gameIDs);
    }


}
