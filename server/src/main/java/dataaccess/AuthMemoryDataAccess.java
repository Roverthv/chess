package dataaccess;


import datastructures.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthMemoryDataAccess implements AuthDataAccess {
    private final Map<String, AuthData> authTokens = new HashMap<>();

    @Override
    public void clear() {
        authTokens.clear();
    }

    @Override
    public void addAuthData(AuthData authData) {
        authTokens.put(authData.getAuthToken(), authData);
    }

    @Override
    public AuthData getAuthData(String authToken) {
        return authTokens.get(authToken);
    }

    @Override
    public void removeAuthData(String authToken) {
        authTokens.remove(authToken);
    }
}
