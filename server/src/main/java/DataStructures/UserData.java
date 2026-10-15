package DataStructures;

import java.util.ArrayList;
import java.util.Objects;

public record UserData(String username, String password, ArrayList<String> gameIDs) {
    public String getUsername(){return username;}
    public boolean checkPassword(String passGiven){return password.equals(passGiven);}
    public ArrayList<String> getGameList(){return gameIDs;}
    public void addGame(String ID){gameIDs.add(ID);}
    public void removeGame(String ID){gameIDs.remove(ID);}

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserData userData = (UserData) o;
        return Objects.equals(username, userData.username) && Objects.equals(password, userData.password) && Objects.equals(gameIDs, userData.gameIDs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, gameIDs);
    }
}
