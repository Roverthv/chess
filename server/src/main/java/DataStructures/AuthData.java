package DataStructures;

import java.util.Objects;

public record AuthData(int authtoken, String username) {
    public int getAuthToken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthData authData = (AuthData) o;
        return Objects.equals(username, authData.username) && Objects.equals(authtoken, authData.authtoken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authtoken, username);
    }
}
