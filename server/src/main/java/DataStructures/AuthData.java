package DataStructures;

public record AuthData(String authtoken, String username) {
    public String getAuthToken(){return authtoken;}
    public String getUsername(){return username;}
}
