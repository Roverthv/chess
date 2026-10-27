package dataaccess;

import datastructures.AuthData;

public interface AuthDataAccess {

    void clear();

    void addAuthData(AuthData authData);

    AuthData getAuthData(String authToken);

    void removeAuthData(String authToken);
}
