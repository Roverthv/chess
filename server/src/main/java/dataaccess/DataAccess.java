package dataaccess;

public interface DataAccess {

    void clear();
    void register(UserData userdata, AuthData authData);
    UserData getUser(String username);
}
