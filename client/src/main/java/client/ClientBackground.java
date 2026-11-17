package client;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ClientBackground {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String urlString = String.format(Locale.getDefault(), "http://%s:%d", "localhost", 8080);
    private String authToken;

    private void test(String host, int port, String path) throws Exception {


        HttpResponse<String> httpResponse = sendRequest(urlString, "POST", "");

        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            System.out.println(httpResponse.body());
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }
    }

    private HttpResponse<String> sendRequest(String url, String method, String body) throws InterruptedException, IOException {
        var request = HttpRequest.newBuilder(URI.create(url))
                .method(method, requestBodyPublisher(body))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendRequest(String url, String header, String method, String body) throws InterruptedException, IOException {
        var request = HttpRequest.newBuilder(URI.create(url))
                .method(method, requestBodyPublisher(body))
                .header("authorization", header)
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpRequest.BodyPublisher requestBodyPublisher(String body) throws IOException {
        if (body != null) {
            return HttpRequest.BodyPublishers.ofString(body);
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private void receiveResponse(HttpResponse<String> response) {
        var statusCode = response.statusCode();

        var responseBody = new Gson().fromJson(response.body(), Map.class);
        System.out.printf("= Response =========\n[%d]\n\n%s\n\n", statusCode, responseBody);
    }

    public void sendRegisterRequest(String username, String password, String email) {
        String url = urlString + "/user";
        String method = "POST";
        String body = new Gson().toJson(Map.of("username", username, "password", password, "email", email));
        HttpResponse<String> response = null;
        try {
            response = sendRequest(url, method, body);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        receiveResponse(response);
        var responseBody = new Gson().fromJson(response.body(), Map.class);
        setAuthToken(responseBody.get("authToken").toString());
    }

    public void sendLoginRequest(String username, String password) {
        String url = urlString + "/session";
        String method = "POST";
        String body = new Gson().toJson(Map.of("username", username, "password", password));
        HttpResponse<String> response = null;
        try {
            response = sendRequest(url, method, body);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        receiveResponse(response);
        var responseBody = new Gson().fromJson(response.body(), Map.class);
        setAuthToken(responseBody.get("authToken").toString());
    }

    public void sendLogoutRequest() {
        String url = urlString + "/session";
        String method = "DELETE";
        String body = "";
        HttpResponse<String> response = null;
        try {
            response = sendRequest(url, authToken, method, body);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        receiveResponse(response);
        setAuthToken("");
    }

    public void sendCreateGameRequest(String gameName) {
        String url = urlString + "/game";
        String method = "POST";
        String body = new Gson().toJson(Map.of("gameName", gameName));
        HttpResponse<String> response = null;
        try {
            response = sendRequest(url, authToken, method, body);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        receiveResponse(response);
    }

    public List<GameStruct> sendListGameRequest() {
        String url = urlString + "/game";
        String method = "GET";
        String body = "";
        HttpResponse<String> response = null;
        try {
            response = sendRequest(url, authToken, method, body);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        receiveResponse(response);
        Type mapType = new TypeToken<Map<String, List<GameStruct>>>() {
        }.getType();
        Map<String, List<GameStruct>> responseBody = new Gson().fromJson(response.body(), mapType);
        return responseBody.get("games");
    }

    public ChessGame sendJoinGameRequest(double gameID, String teamColor) {
        String url = urlString + "/game";
        String method = "PUT";
        String body = new Gson().toJson(Map.of("gameID", gameID, "playerColor", teamColor));
        String url2 = urlString + "/board";
        String method2 = "GET";
        String body2 = new Gson().toJson(Map.of("gameID", gameID));
        HttpResponse<String> response = null;
        HttpResponse<String> response2 = null;
        try {
            response = sendRequest(url, authToken, method, body);
            response2 = sendRequest(url2, authToken, method2, body2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        receiveResponse(response);
        receiveResponse(response2);
        return new Gson().fromJson(response2.body(), ChessGame.class);
    }

    public ChessGame sendObserveGameRequest(double gameID) {
        String url = urlString + "/board";
        String method = "GET";
        String body = new Gson().toJson(Map.of("gameID", gameID));
        HttpResponse<String> response = null;
        try {
            response = sendRequest(url, authToken, method, body);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        receiveResponse(response);
        return new Gson().fromJson(response.body(), ChessGame.class);
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

