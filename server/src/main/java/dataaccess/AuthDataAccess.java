package dataaccess;

import datastructures.AuthData;

public interface AuthDataAccess {

    void clear() throws DataAccessException;

    void addAuthData(AuthData authData) throws DataAccessException;

    AuthData getAuthData(String authToken) throws DataAccessException;

    void removeAuthData(String authToken) throws DataAccessException;
}
