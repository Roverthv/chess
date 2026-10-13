package DataStructures;

import java.util.ArrayList;

public record UserData(String username, String password, ArrayList<String> gameIDs) {
    public String getUsername(){return username;}
    public boolean checkPassword(String passGiven){return password.equals(passGiven);}
    public ArrayList<String> getGameList(){return gameIDs;}
    public void addGame(String ID){gameIDs.add(ID);}
    public void removeGame(String ID){gameIDs.remove(ID);}
}
