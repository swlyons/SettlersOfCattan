package server.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.receiver.*;
import server.receiver.AllOfOurInformation;
import shared.data.*;
import shared.model.*;
import shared.locations.*;
import client.communication.CookieModel;
import client.managers.GameManager;
import java.io.BufferedReader;
import java.net.URLEncoder;
import java.util.Scanner;

public class Server {

    /**
     * index on a phone key pad (default port used if none specified
     */
    private static int SERVER_PORT_NUMBER = 8081;
    private static final int MAX_WAITING_CONNECTIONS = 10;

    private HttpServer server;
    private Gson model = new GsonBuilder().create();
    private static boolean isMock = false;

    /**
     * Server constructor
     */
    private Server() {

    }

    /**
     * Initializes the Database
     */
    private void run() {
        try {

            Database.initialize();
        } catch (ServerException e) {
            System.out.println("Could not initialize database: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER), MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            System.out.println("Could not create HTTP server: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        server.setExecutor(null); // use the default executor

        // UserReceiver contexts
        server.createContext("/user/login", loginHandler);// worked on
        server.createContext("/user/register", registerHandler);// worked on

        // Games contexts
        server.createContext("/games/list", listHandler);// worked on
        server.createContext("/games/create", createHandler);// worked on
        server.createContext("/games/join", joinHandler);// worked on

        // Game contexts
        server.createContext("/game/model", modelHandler);// worked on
        server.createContext("/game/listAI", listAIHandler);// worked on
        server.createContext("/game/addAI", addAIHandler);// worked on

        // Moves contexts
        server.createContext("/moves/sendChat", sendChatHandler);// worked on
        server.createContext("/moves/rollNumber", rollNumberHandler);// worked
        // on
        server.createContext("/moves/robPlayer", robPlayerHandler);// worked on
        server.createContext("/moves/finishTurn", finishTurnHandler);// worked
        // on
        server.createContext("/moves/buyDevCard", buyDevCardHandler);// worked
        // on
        server.createContext("/moves/Year_of_Plenty", year_of_plentyHandler);
        server.createContext("/moves/Road_Building", road_buildingHandler);
        server.createContext("/moves/Soldier", soldierHandler);// worked on
        server.createContext("/moves/Monument", monumentHandler);// worked on
        server.createContext("/moves/Monopoly", monopolyHandler);
        server.createContext("/moves/offerTrade", offerTradeHandler);
        server.createContext("/moves/acceptTrade", acceptTradeHandler);
        server.createContext("/moves/buildSettlement", buildSettlementHandler);// worked
        // on
        server.createContext("/moves/buildCity", buildCityHandler);// worked on
        server.createContext("/moves/buildRoad", buildRoadHandler);// worked on
        server.createContext("/moves/maritimeTrade", maritimeTradeHandler);
        server.createContext("/moves/discardCards", discardCardsHandler);// worked
        // on

        // swagger
        server.createContext("/docs/api/data", new Handlers.JSONAppender(""));
        server.createContext("/docs/api/view", new Handlers.BasicFile(""));

        // Empty (good for testing if service is working)
        server.createContext("/", downloadHandler);
        // start the server
        server.start();
    }

    private HttpHandler downloadHandler = new HttpHandler() {
        /**
         * Handler to download a file from the server
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            String path = new File(".").getCanonicalPath(); // + File.separator;
            // + "importdata";
            for (String part : pathParts) {
                if (!part.equals("")) {
                    path += (File.separator + part);
                }
            }
            System.out.println("Server: " + path);
            File file = new File(path);
            byte[] bytearray = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytearray, 0, bytearray.length);

            // ok, we are ready to send the response.
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, file.length());
            OutputStream os = exchange.getResponseBody();
            os.write(bytearray, 0, bytearray.length);
            os.close();
            bis.close();
            exchange.getResponseBody().close();

        }
    };

    /**
     * Handler to login a user
     */
    private HttpHandler loginHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {

                // un-package the data
                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                User user = model.fromJson(reader, User.class);

                if (user.getUsername() == null || user.getPassword() == null) {
                    String error = "Username and password can't be null";
                    System.out.println(error);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                    exchange.getResponseBody().write(error.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                // call the appropriate fascade (real or mock)
                if (isMock) {
                    MockServerFascade.getSingleton().login(user);
                    String message = "Success";
                    System.out.println("Login successful:" + user.getUsername());
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.getResponseHeaders().set("Set-Cookie", "cookies");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                int id = ServerFascade.getSingleton().getLoginId(user);

                // re-package and return the data
                if (id != -1) {
                    String cookie = "catan.user=";
                    String cookieModel = "{name:" + AllOfOurInformation.getSingleton().getUsers().get(id).getUsername()
                            + ",";
                    cookieModel += "password:" + AllOfOurInformation.getSingleton().getUsers().get(id).getPassword()
                            + ",";
                    cookieModel += "playerID:" + id + "}";

                    cookie += URLEncoder.encode(cookieModel, "UTF-8") + ";Path=/;";
                    // String message = model.toJson("Success", String.class);
                    String message = "Success";
                    System.out.println("Login successful:" + user.getUsername());
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.getResponseHeaders().set("Set-Cookie", cookie);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    String message = "Failed to login - bad username or password.";
                    System.out.println(message);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                }
            } catch (Exception e) {
                String error = "ERROR in logging in";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };

    /**
     * Handler to register a user
     */
    private HttpHandler registerHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "text/html");

            // un-package the data
            Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
            User user = model.fromJson(reader, User.class);

            if (user.getUsername() == null || user.getPassword() == null) {
                String error = "Username and password can't be null";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            // call the appropriate fascade (real or mock)
            boolean result = false;
            if (isMock) {
                try {
                    result = MockServerFascade.getSingleton().register(user);
                } catch (ServerException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                result = ServerFascade.getSingleton().register(user);
            } catch (ServerException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

            // re-package and return the data
            if (result) {
                String message = "Success";
                System.out.println(message);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, message.length());
                exchange.getResponseBody().write(message.getBytes());
                exchange.getResponseBody().close();
            } else {
                String message = "Failed to register - bad username or password.";
                System.out.println(message);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                exchange.getResponseBody().write(message.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };

    /**
     * Handler to list all games
     */
    private HttpHandler listHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                ArrayList<GameInfo> gamesList = new ArrayList<GameInfo>();
                if (isMock) {
                    gamesList = MockServerFascade.getSingleton().listGames();
                } else {
                    gamesList = ServerFascade.getSingleton().listGames();
                }

                String response = "[";

                if (!gamesList.isEmpty()) {
                    for (GameInfo gameInfo : gamesList) {
                        response += "{\"title\":\"" + gameInfo.getTitle() + "\",";
                        response += "\"id\":" + gameInfo.getId() + ",";
                        response += "\"players\": [";

                        for (int i = 0; i < gameInfo.getPlayers().size(); i++) {
                            if (gameInfo.getPlayers().get(i) != null) {
                                response += "{";
                                response += "\"color\":\"" + gameInfo.getPlayers().get(i).getColor() + "\",";
                                response += "\"name\":\"" + gameInfo.getPlayers().get(i).getName() + "\",";
                                response += "\"id\":" + gameInfo.getPlayers().get(i).getId();
                                response += (i == (gameInfo.getPlayers().size() - 1)) ? "}" : "},";
                            } else {
                                response += (i == (gameInfo.getPlayers().size() - 1)) ? "{}" : "{},";
                            }
                        }
                        response += "]},";
                    }
                    response = response.substring(0, response.length() - 1);
                }
                response += "]";

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
            } catch (Exception e) {
                e.printStackTrace();
                String error = "ERROR in list games";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }

        }
    };

    /**
     * Handler to create a game
     */
    private HttpHandler createHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                String messageBody = "";

                // check that the booleans are actually booleans for the swagger
                // <editor-fold desc="Check Booleans">
                BufferedReader br = new BufferedReader(reader);
                Scanner sc = new Scanner(br);
                boolean validRandomValues = true;
                while (sc.hasNextLine()) {
                    String line = sc.next();
                    if (line.contains(",")) {
                        if (!line.contains("true") && !line.contains("false")) {
                            // invalid request
                            validRandomValues = false;
                            break;
                        }
                    }
                    messageBody += line;
                }
                // </editor-fold>
                GameInfo gameInfo = null;
                if (validRandomValues) {
                    CreateGameRequest createGame = model.fromJson(messageBody, CreateGameRequest.class);

                    if (createGame.isRandomNumbers() == null || createGame.isRandomPorts() == null
                            || createGame.isRandomTiles() == null) {
                        String error = "Create game booleans can't be null";
                        System.out.println(error);
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                        exchange.getResponseBody().write(error.getBytes());
                        exchange.getResponseBody().close();
                        return;
                    }

                    if (createGame.getName() == null) {
                        String error = "Name can't be null";
                        System.out.println(error);
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                        exchange.getResponseBody().write(error.getBytes());
                        exchange.getResponseBody().close();
                        return;
                    }

                    try {
                        if (isMock) {
                            gameInfo = MockServerFascade.getSingleton().createGame(createGame);
                        } else {
                            gameInfo = ServerFascade.getSingleton().createGame(createGame);
                        }
                    } catch (Exception e) {
                        String error = "Create game error with fascade trying to create game";
                        System.out.println(error);
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                        exchange.getResponseBody().write(error.getBytes());
                        exchange.getResponseBody().close();
                        return;
                    }

                }

                if (gameInfo != null && validRandomValues) {
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    String message = "{";
                    message += "\"title\"" + ":\"" + gameInfo.getTitle() + "\",";
                    message += "\"id\"" + ":" + gameInfo.getId() + ",";
                    message += "\"players\"" + ": [{},{},{},{}]";
                    message += "}";

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");

                    String message = validRandomValues ? "Failed to createGame - need a name."
                            : "Failed to create game - all random values (i.e. randomTiles) must be \"true\" or \"false\"";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in create game";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };

    /**
     * Handler to join a game
     */
    private HttpHandler joinHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // un-package the data
            try {
                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                JoinGameRequest joinGameRequest = model.fromJson(reader, JoinGameRequest.class);
                exchange.getResponseHeaders().set("Content-Type", "text/html");

                String userCookie = exchange.getRequestHeaders().getFirst("Cookie");

                if (userCookie == null || userCookie.equals("")) {
                    String message = "Need to login first.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (joinGameRequest.getId() == null) {
                    String message = "Need a Game Id.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (joinGameRequest.getId() < 0
                        || AllOfOurInformation.getSingleton().getGames().size() <= joinGameRequest.getId()) {
                    String message = "Invalid Game Id.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                String decodedCookie = URLDecoder.decode(userCookie.split("catan.user=")[1], "UTF-8");
                if (decodedCookie.indexOf(";") != -1) {
                    decodedCookie = decodedCookie.split(";")[0];
                }

                Integer playerIdThisOne = model.fromJson(decodedCookie, CookieModel.class).getPlayerID();
                String name = model.fromJson(decodedCookie, CookieModel.class).getName();

                boolean found = false;
                boolean finished = false;
                String takenColors = "";
                boolean colorTaken = false;

                for (int i = 0; i < 4; i++) {
                    if (AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame()
                            .getPlayers().get(i) != null) {
                        takenColors += AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId())
                                .getGame().getPlayers().get(i).getColor();
                    }
                    if (AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame()
                            .getPlayers().get(i) == null) {
                        // dont allow the player to join the game with the same
                        // color as another player
                        if (takenColors.contains(joinGameRequest.getColor())) {
                            colorTaken = true;
                        }
                        break;
                    }

                    if (AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame()
                            .getPlayers().get(i).getId() == playerIdThisOne) {
                        found = true;
                        AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame()
                                .getPlayers().get(i).setColor(joinGameRequest.getColor().toLowerCase());
                        break;
                    }
                    if (i == 3) {
                        finished = true;
                    }
                }
                if (isMock) {
                    found = MockServerFascade.getSingleton().joinGame(joinGameRequest);
                }
                if (found) {
                    String cookie = "catan.game=";
                    cookie += joinGameRequest.getId();
                    cookie += ";Path=/;";
                    String message = "Success";
                    exchange.getResponseHeaders().set("Set-Cookie", cookie);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    if (finished || colorTaken) {
                        String message = colorTaken ? "Color already taken." : "Already full.";
                        System.out.println(message);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                        exchange.getResponseBody().write(message.getBytes());
                        exchange.getResponseBody().close();
                    } else {
                        PlayerInfo player = new PlayerInfo();
                        player.setId(playerIdThisOne);
                        player.setPlayerID(playerIdThisOne);
                        player.setName(name);
                        player.setColor(joinGameRequest.getColor());

                        for (int i = 0; i < 4; i++) {
                            if (AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame()
                                    .getPlayers().get(i) == null) {
                                player.setPlayerIndex(i);
                                AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame()
                                        .getPlayers().set(i, player);
                                break;
                            }

                        }

                        int spot = AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame()
                                .getPlayers().indexOf(player);

                        if (spot == -1 || 3 < spot) {
                            AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame()
                                    .getPlayers().remove(player);
                            String message = "Already full.";
                            System.out.println(message);
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                        } else {
                            String cookie = "catan.game=";
                            cookie += joinGameRequest.getId();
                            cookie += ";Path=/;";
                            String message = "Success";
                            exchange.getResponseHeaders().set("Set-Cookie", cookie);
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                            // Error, someone joining the same game twice
                            // simeltaneously. (Twice at the same time)
                        }
                    }
                }

            } catch (Exception e) {
                String error = "ERROR in join game";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };

    /**
     * Handler to get a the game model
     */
    private HttpHandler modelHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {

                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                Integer version = exchange.getRequestURI().toString().indexOf("version=");

                if (version == -1) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to provide a version parameter.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                try {
                    version = Integer.parseInt(exchange.getRequestURI().toString().substring(version + 8));
                } catch (Exception e) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Had trouble parsing version parameter.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                // call the appropriate fascade (real or mock)
                String result;
                if (version < AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame()
                        .getVersion()) {
                    result = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame()
                            .toString();
                } else {
                    result = "\"true\"";
                }
                if (isMock) {
                    result = MockServerFascade.getSingleton().getModel(version + "");
                }
                // re-package and return the data
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                exchange.getResponseBody().write(result.getBytes());
                exchange.getResponseBody().close();
            } catch (Exception e) {
                e.printStackTrace();
                String error = "ERROR in grabbing model";
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                System.out.println(error);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };

    /**
     * Handler to get a the listAI model
     */
    private HttpHandler listAIHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            String message = "[\"DO NOT ADD A COMPUTER PLAYER\"]";
            if (isMock) {
                try {
                    message = ServerFascade.getSingleton().listAITypesInGame().toString();
                } catch (ServerException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, message.length());
            exchange.getResponseBody().write(message.getBytes());
            exchange.getResponseBody().close();
        }
    };

    private HttpHandler addAIHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {

                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                for (int i = 0; i < AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId())
                        .getGame().getPlayers().size(); i++) {
                    if (AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame()
                            .getPlayers().get(i) == null) {
                        PlayerInfo aiPlayer = new PlayerInfo();

                        aiPlayer.setId(-1 - i);
                        aiPlayer.setPlayerID(-1 - i);
                        aiPlayer.setName("AI " + i);
                        if (i == 1) {
                            aiPlayer.setColor("red");
                        }
                        if (i == 2) {
                            aiPlayer.setColor("blue");
                        }
                        if (i == 3) {
                            aiPlayer.setColor("yellow");
                        }
                        AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame()
                                .getPlayers().set(i, aiPlayer);
                        aiPlayer.setPlayerIndex(i);
                        break;
                    }
                }
                String message = "Success";
                if (isMock) {
                    if (!MockServerFascade.getSingleton().addAIToGame(null)) {
                        message = "Failed";
                    }

                }

                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, message.length());
                exchange.getResponseBody().write(message.getBytes());
                exchange.getResponseBody().close();

            } catch (Exception e) {
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                String message = "ERROR in adding AI.";
                System.out.println(message);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                exchange.getResponseBody().write(message.getBytes());
                exchange.getResponseBody().close();
            }

        }
    };

    /**
     * Handler to send a chat message
     */
    private HttpHandler sendChatHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                boolean invalidIndex = false;
                boolean notLoggedIn = false;

                // make sure the player index is valid (a number that gson can
                // parse)
                SendChat sendChatRequest = null;
                try {
                    sendChatRequest = model.fromJson(reader, SendChat.class);
                } catch (com.google.gson.JsonSyntaxException jse) {
                    invalidIndex = true;
                }

                if (sendChatRequest.getPlayerIndex() == null) {
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                // get the cookie from the header
                String userCookie = exchange.getRequestHeaders().getFirst("Cookie");

                // make sure the user is logged in
                if (userCookie == null || userCookie.equals("")) {
                    notLoggedIn = true;
                }

                // pass in the game id
                GameIdPlayerIdAndPlayerIndex gameAndPlayerId = verifyPlayer(exchange);
                if ((gameAndPlayerId != null) && (sendChatRequest != null)) {

                    sendChatRequest.setContent(sendChatRequest.getContent() + ";" + gameAndPlayerId.getGameId());
                } else {
                    notLoggedIn = true;
                }

                // make sure there is a cookie
                if (invalidIndex || notLoggedIn) {
                    String message = invalidIndex ? "Invalid command.  ['playerIndex' field has an invalid value.]"
                            : "Need to login and join a game first.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    // call the appropriate facade
                    GameInfo result = null;
                    try {
                        if (isMock) {
                            result = MockServerFascade.getSingleton().sendChat(sendChatRequest);
                        } else {
                            result = ServerFascade.getSingleton().sendChat(sendChatRequest);
                        }
                    } catch (NullPointerException npe) {
                        String message = "For the specified game there is no player with the index: "
                                + sendChatRequest.getPlayerIndex();
                        System.out.println(message);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                        exchange.getResponseBody().write(message.getBytes());
                        exchange.getResponseBody().close();
                        return;
                    }

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.toString().length());
                    exchange.getResponseBody().write(result.toString().getBytes());
                    exchange.getResponseBody().close();
                }
            } catch (Exception e) {
                String error = "ERROR in sending chat";
                System.out.println(error);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }

        }
    };
    /**
     * Handler to get roll a number
     */
    private HttpHandler rollNumberHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                RollNumber rollNumber = model.fromJson(reader, RollNumber.class);
                if (rollNumber.getType() == null || !rollNumber.getType().equals("rollNumber")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (rollNumber.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (rollNumber.getPlayerIndex() != gameAndPlayer.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (rollNumber.getNumber() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need a number.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameInfo gi = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame();

                if (gameAndPlayer.getPlayerIndex() != gi.getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (!gi.getTurnTracker().getStatus().equals("Rolling")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not Rolling status in turn tracker. Status is actually: " + gi.getTurnTracker().getStatus();
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (rollNumber.getNumber() < 2 || 12 < rollNumber.getNumber()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Invalid number to roll.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                rollNumber.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().rollNumber(rollNumber);
                    } else {
                        game = ServerFascade.getSingleton().rollNumber(rollNumber);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in rolling number";
                    System.out.println(result);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in rolling number";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();

            }
        }
    };
    /**
     * Handler to rob a player
     */
    private HttpHandler robPlayerHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                RobPlayer robPlayer = model.fromJson(reader, RobPlayer.class);

                if (robPlayer.getType() == null || !robPlayer.getType().equals("robPlayer")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (robPlayer.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != robPlayer.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (robPlayer.getVictimIndex() == null || robPlayer.getVictimIndex() < -1
                        || 3 < robPlayer.getVictimIndex() || robPlayer.getVictimIndex() == robPlayer.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Victim index isn't valid.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (!gm.getGame().getTurnTracker().getStatus().equals("Robbing")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Turn tracker status must be \"Robbing\".";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

//				if (!gm.canPlaceRobber(robPlayer.getLocation())) {
//					exchange.getResponseHeaders().set("Content-Type", "text/html");
//					String message = "Cannot place robber there";
//					System.out.println(message);
//					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
//					exchange.getResponseBody().write(message.getBytes());
//					exchange.getResponseBody().close();
//					return;
//				}
                robPlayer.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().robPlayer(robPlayer);
                    } else {
                        game = ServerFascade.getSingleton().robPlayer(robPlayer);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in rob player";
                    System.out.println(result);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in rob player";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };
    /**
     * Handler to finish a turn
     */
    private HttpHandler finishTurnHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }
                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                FinishMove finishMove = model.fromJson(reader, FinishMove.class);

                if (finishMove.getType() == null || !finishMove.getType().equals("finishTurn")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                if (finishMove.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != finishMove.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                GameInfo gi = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame();
                if (gameAndPlayer.getPlayerIndex() != gi.getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                String status = gi.getTurnTracker().getStatus();
                if (status.equals("Rolling")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Must Roll first.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }
                if (status.equals("Robbing")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Must rob first.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }
                if (status.equals("Discarding")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Must discard first.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                PlayerInfo currentPlayer = gi.getPlayers().get(gameAndPlayer.getPlayerIndex());
                if (status.equals("FirstRound")) {
                    if (currentPlayer.getSettlements() != 4 || currentPlayer.getRoads() != 14) {
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        String message = "Must play only 1 road and 1 settlement in first round.";
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                        exchange.getResponseBody().write(message.getBytes());
                        exchange.getResponseBody().close();
                        System.out.println(message);
                        return;
                    }
                }

                if (status.equals("SecondRound")) {
                    if (currentPlayer.getSettlements() != 3 || currentPlayer.getRoads() != 13) {
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        String message = "Must play only 1 road and 1 settlement in second round.";
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                        exchange.getResponseBody().write(message.getBytes());
                        exchange.getResponseBody().close();
                        System.out.println(message);
                        return;
                    }
                }

                finishMove.setGameId(gameAndPlayer.getGameId());
                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().finishMove(finishMove);
                    } else {
                        game = ServerFascade.getSingleton().finishMove(finishMove);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in finish turn";
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    System.out.println(result);
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in finishing turn";
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
                System.out.println(error);
            }
        }
    };
    /**
     * Handler to buy a development card
     */
    private HttpHandler buyDevCardHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                BuyDevCard buyDevCard = model.fromJson(reader, BuyDevCard.class);

                if (buyDevCard.getType() == null || !buyDevCard.getType().equals("buyDevCard")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (buyDevCard.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != buyDevCard.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                String status = gm.getGame().getTurnTracker().getStatus();

                if (!status.equals("Playing")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Turn tracker status must be in \"Playing\".";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

//				if (!gm.canBuyCard()) {
//					exchange.getResponseHeaders().set("Content-Type", "text/html");
//					String message = "Not enough resources.";
//					System.out.println(message);
//					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
//					exchange.getResponseBody().write(message.getBytes());
//					exchange.getResponseBody().close();
//					return;
//				}
                buyDevCard.setGameId(gameAndPlayer.getGameId());
                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().buyDevCard(buyDevCard);
                    } else {
                        game = ServerFascade.getSingleton().buyDevCard(buyDevCard);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in buy dev card";
                    System.out.println(result);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in buy dev card";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };
    /**
     * Handler to use a year of plenty card
     */
    private HttpHandler year_of_plentyHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                Year_Of_Plenty yearOfPlenty = model.fromJson(reader, Year_Of_Plenty.class);

                if (yearOfPlenty.getType() == null || !yearOfPlenty.getType().equals("Year_of_Plenty")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (yearOfPlenty.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != yearOfPlenty.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (!gm.getGame().getTurnTracker().getStatus().equals("Playing")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Turn tracker status must be \"Playing\".";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

//				if (!gm.canUseYearOfPlenty()) {
//					exchange.getResponseHeaders().set("Content-Type", "text/html");
//					String message = "Cannot play year of plenty card";
//					System.out.println(message);
//					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
//					exchange.getResponseBody().write(message.getBytes());
//					exchange.getResponseBody().close();
//					return;
//				}
                if (yearOfPlenty.getResource1() == null || yearOfPlenty.getResource2() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "The two requested resources cannot be null";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                yearOfPlenty.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().year_Of_Plenty(yearOfPlenty);
                    } else {
                        game = ServerFascade.getSingleton().year_Of_Plenty(yearOfPlenty);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in year of plenty";
                    System.out.println(result);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in yearOfPlenty";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };
    /**
     * Handler to use amonopoly card
     */
    private HttpHandler monopolyHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                Monopoly monopoly = model.fromJson(reader, Monopoly.class);

                if (monopoly.getType() == null || !monopoly.getType().equals("Monopoly")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (monopoly.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != monopoly.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (!gm.getGame().getTurnTracker().getStatus().equals("Playing")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Turn tracker status must be \"Playing\".";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

//				if (!gm.canUseMonopoly()) {
//					exchange.getResponseHeaders().set("Content-Type", "text/html");
//					String message = "Cannot play monopoly card";
//					System.out.println(message);
//					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
//					exchange.getResponseBody().write(message.getBytes());
//					exchange.getResponseBody().close();
//					return;
//				}
                if (monopoly.getResource() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "The requested resource cannot be null";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                monopoly.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().monopoly(monopoly);
                    } else {
                        game = ServerFascade.getSingleton().monopoly(monopoly);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in monopoly";
                    System.out.println(result);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in monopoly";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };
    /**
     * Handler to use a road building card
     */
    private HttpHandler road_buildingHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                Road_Building roadBuilding = model.fromJson(reader, Road_Building.class);

                if (roadBuilding.getType() == null || !roadBuilding.getType().equals("Road_Building")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (roadBuilding.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != roadBuilding.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (!gm.getGame().getTurnTracker().getStatus().equals("Playing")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Turn tracker status must be \"Playing\".";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

//				if (!gm.canUseRoadBuilding()) {
//					exchange.getResponseHeaders().set("Content-Type", "text/html");
//					String message = "Cannot play road building card";
//					System.out.println(message);
//					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
//					exchange.getResponseBody().write(message.getBytes());
//					exchange.getResponseBody().close();
//					return;
//				}
                if (roadBuilding.getSpot1() == null || roadBuilding.getSpot2() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "The two road building locations can't be null";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                roadBuilding.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().roadBuilding(roadBuilding);
                    } else {
                        game = ServerFascade.getSingleton().roadBuilding(roadBuilding);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in road building";
                    System.out.println(result);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in roadBuilding";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };
    /**
     * Handler to use a soldier card
     */
    private HttpHandler soldierHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                Soldier soldier = model.fromJson(reader, Soldier.class);

                if (soldier.getType() == null || !soldier.getType().equals("Soldier")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (soldier.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != soldier.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (soldier.getVictimIndex() == null || soldier.getVictimIndex() < -1 || 3 < soldier.getVictimIndex()
                        || soldier.getVictimIndex() == soldier.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Victim index isn't valid.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (!gm.getGame().getTurnTracker().getStatus().equals("Playing")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Turn tracker status must be \"Playing\".";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

//				if (!gm.canUseSoldier()) {
//					exchange.getResponseHeaders().set("Content-Type", "text/html");
//					String message = "Cannot play soldier card";
//					System.out.println(message);
//					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
//					exchange.getResponseBody().write(message.getBytes());
//					exchange.getResponseBody().close();
//					return;
//				}
//				if (!gm.canPlaceRobber(soldier.getLocation())) {
//					exchange.getResponseHeaders().set("Content-Type", "text/html");
//					String message = "Cannot place robber there";
//					System.out.println(message);
//					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
//					exchange.getResponseBody().write(message.getBytes());
//					exchange.getResponseBody().close();
//					return;
//				}
//				if (!gm.canRobPlayer(soldier.getLocation(), soldier.getVictimIndex())) {
//					exchange.getResponseHeaders().set("Content-Type", "text/html");
//					String message = "Cannot rob that player";
//					System.out.println(message);
//					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
//					exchange.getResponseBody().write(message.getBytes());
//					exchange.getResponseBody().close();
//					return;
//				}
                soldier.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().soldier(soldier);
                    } else {
                        game = ServerFascade.getSingleton().soldier(soldier);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in soldier";
                    System.out.println(result);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in soldier";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };
    /**
     * Handler to user a monument card
     */
    private HttpHandler monumentHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                Monument monument = model.fromJson(reader, Monument.class);

                if (monument.getType() == null || !monument.getType().equals("Monument")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (monument.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != monument.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

//				if (!gm.canUseMonument()) {
//					exchange.getResponseHeaders().set("Content-Type", "text/html");
//					String message = "You do not have a monument.";
//					System.out.println(message);
//					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
//					exchange.getResponseBody().write(message.getBytes());
//					exchange.getResponseBody().close();
//					return;
//				}
                monument.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().monument(monument);
                    } else {
                        game = ServerFascade.getSingleton().monument(monument);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in monument";
                    System.out.println(result);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in soldier";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };
    /**
     * Handler to offer a trade
     */
    private HttpHandler offerTradeHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                OfferTrade offerTrade = model.fromJson(reader, OfferTrade.class);

                if (offerTrade.getType() == null || !offerTrade.getType().equals("offerTrade")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (offerTrade.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != offerTrade.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (!gm.getGame().getTurnTracker().getStatus().equals("Playing")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Turn tracker status must be \"Playing\".";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (offerTrade.getOffer() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Trade offer cannot be null";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (offerTrade.getReceiver() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Trade receiver cannot be null";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                offerTrade.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().offerTrade(offerTrade);
                    } else {
                        game = ServerFascade.getSingleton().offerTrade(offerTrade);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in offer trade";
                    System.out.println(result);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in offerTrade";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };
    /**
     * Handler to accept a trade
     */
    private HttpHandler acceptTradeHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                AcceptTrade acceptTrade = model.fromJson(reader, AcceptTrade.class);

                if (acceptTrade.getType() == null || !acceptTrade.getType().equals("acceptTrade")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (acceptTrade.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != acceptTrade.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (!gm.getGame().getTurnTracker().getStatus().equals("Playing")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Turn tracker status must be \"Playing\".";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (acceptTrade.isWillAccept() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Trade willAccept boolean cannot be null";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                acceptTrade.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().acceptTrade(acceptTrade);
                    } else {
                        game = ServerFascade.getSingleton().acceptTrade(acceptTrade);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in accept trade";
                    System.out.println(result);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in acceptTrade";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to build a settlement
     */
    private HttpHandler buildSettlementHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                BuildSettlement buildSettlement = model.fromJson(reader, BuildSettlement.class);

                if (buildSettlement.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != buildSettlement.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                if (buildSettlement.isFree() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need free boolean.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                if (buildSettlement.getVertexLocation() == null || buildSettlement.getVertexLocation().getX() == null
                        || buildSettlement.getVertexLocation().getY() == null
                        || buildSettlement.getVertexLocation().getDirection() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need a proper settlementLocation.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                String status = gm.getGame().getTurnTracker().getStatus();

                if (status.equals("FirstRound") || status.equals("SecondRound")) {

                    if (!buildSettlement.isFree()) {
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        String message = "Building a settlement must be free in the first two rounds.";
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                        exchange.getResponseBody().write(message.getBytes());
                        exchange.getResponseBody().close();
                        System.out.println(message);
                        return;
                    }

                    if (status.equals("FirstRound")) {
                        for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
                            if (gm.getGame().getPlayers().get(i) == null) {
                                exchange.getResponseHeaders().set("Content-Type", "text/html");
                                String message = "Need 4 players first.";
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                                exchange.getResponseBody().write(message.getBytes());
                                exchange.getResponseBody().close();
                                System.out.println(message);
                                return;
                            }
                        }

                        if (gm.getGame().getPlayers().get(gameAndPlayer.getPlayerIndex()).getSettlements() != 5) {
                            exchange.getResponseHeaders().set("Content-Type", "text/html");
                            String message = "Incorrect number of settlements.";
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                            System.out.println(message);
                            return;
                        }

                    }

                    if (status.equals("SecondRound")) {
                        if (gm.getGame().getPlayers().get(gameAndPlayer.getPlayerIndex()).getSettlements() != 4) {
                            exchange.getResponseHeaders().set("Content-Type", "text/html");
                            String message = "Incorrect number of settlements.";
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                            System.out.println(message);
                            return;
                        }

                    }

                }

                if (!(status.equals("Playing") || status.equals("FirstRound") || status.equals("SecondRound"))) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Cannot place settlement in this turn tracker status.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

//				if (!buildSettlement.isFree()) {
//					if (!gm.canBuildSettlement()) {
//						exchange.getResponseHeaders().set("Content-Type", "text/html");
//						String message = "Not enough resouces.";
//						exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
//						exchange.getResponseBody().write(message.getBytes());
//						exchange.getResponseBody().close();
//						System.out.println(message);
//						return;
//					}
//				}
                HexLocation hexSpot = new HexLocation(buildSettlement.getVertexLocation().getX(),
                        buildSettlement.getVertexLocation().getY());
                VertexDirection vertexDirection = buildSettlement.getVertexLocation().getDirection();
                VertexLocation vertexLocation = new VertexLocation(hexSpot, vertexDirection);

                buildSettlement.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().buildSettlement(buildSettlement);
                    } else {
                        game = ServerFascade.getSingleton().buildSettlement(buildSettlement);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in build settlement";
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(result);
                }

            } catch (Exception e) {
                String error = "ERROR in build settlement";
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
                System.out.println(error);
            }
        }
    };
    /**
     * Handler to build a city
     */
    private HttpHandler buildCityHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                BuildCity buildCity = model.fromJson(reader, BuildCity.class);

                if (buildCity.getType() == null || !buildCity.getType().equals("buildCity")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (buildCity.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != buildCity.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (buildCity.getVertexLocation() == null || buildCity.getVertexLocation().getX() == null
                        || buildCity.getVertexLocation().getY() == null
                        || buildCity.getVertexLocation().getDirection() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need a proper cityLocation.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                String status = gm.getGame().getTurnTracker().getStatus();

                if (!(status.equals("Playing"))) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Cannot place city in this turn tracker status.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

//				if (!gm.canBuildCity()) {
//					exchange.getResponseHeaders().set("Content-Type", "text/html");
//					String message = "Not enough resouces.";
//					System.out.println(message);
//					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
//					exchange.getResponseBody().write(message.getBytes());
//					exchange.getResponseBody().close();
//					return;
//				}
                HexLocation hexSpot = new HexLocation(buildCity.getVertexLocation().getX(),
                        buildCity.getVertexLocation().getY());
                VertexDirection vertexDirection = buildCity.getVertexLocation().getDirection();
                VertexLocation vertexLocation = new VertexLocation(hexSpot, vertexDirection);

//				if (!gm.canPlaceCity(vertexLocation)) {
//					exchange.getResponseHeaders().set("Content-Type", "text/html");
//					String message = "Cannot place city there.";
//					System.out.println(message);
//					exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
//					exchange.getResponseBody().write(message.getBytes());
//					exchange.getResponseBody().close();
//					return;
//				}
                buildCity.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().buildCity(buildCity);
                    } else {
                        game = ServerFascade.getSingleton().buildCity(buildCity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in build city";
                    System.out.println(result);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in build city";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };
    /**
     * Handler to build a road
     */
    private HttpHandler buildRoadHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                BuildRoad buildRoad = model.fromJson(reader, BuildRoad.class);

                if (buildRoad.getType() == null || !buildRoad.getType().equals("buildRoad")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                if (buildRoad.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != buildRoad.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                if (buildRoad.isFree() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need free boolean.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                if (buildRoad.getRoadLocation() == null || buildRoad.getRoadLocation().getX() == null
                        || buildRoad.getRoadLocation().getY() == null
                        || buildRoad.getRoadLocation().getDirection() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need a proper roadLocation.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                String status = gm.getGame().getTurnTracker().getStatus();

                if (!(status.equals("Playing") || status.equals("FirstRound") || status.equals("SecondRound"))) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Cannot place road in this turn tracker status.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(message);
                    return;
                }

                if (status.equals("FirstRound") || status.equals("SecondRound")) {
                    if (!buildRoad.isFree()) {
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        String message = "Building a road must be free in the first two rounds.";
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                        exchange.getResponseBody().write(message.getBytes());
                        exchange.getResponseBody().close();
                        System.out.println(message);
                        return;
                    }

                    if (status.equals("FirstRound")) {
                        if (gm.getGame().getPlayers().get(gameAndPlayer.getPlayerIndex()).getSettlements() != 4) {
                            exchange.getResponseHeaders().set("Content-Type", "text/html");

                            String message = "Place exactly 1 settlement first. You placed : " + (5
                                    - gm.getGame().getPlayers().get(gameAndPlayer.getPlayerIndex()).getSettlements());
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                            System.out.println(message);
                            return;
                        }

                        if (gm.getGame().getPlayers().get(gameAndPlayer.getPlayerIndex()).getRoads() != 15) {
                            exchange.getResponseHeaders().set("Content-Type", "text/html");
                            String message = "Incorrect number of roads";
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                            System.out.println(message);
                            return;
                        }

                    }

                    if (status.equals("SecondRound")) {
                        if (gm.getGame().getPlayers().get(gameAndPlayer.getPlayerIndex()).getSettlements() != 3) {
                            exchange.getResponseHeaders().set("Content-Type", "text/html");
                            String message = "Place exactly 1 settlement first in the second round (you should have 3).";
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                            System.out.println(message);
                            return;
                        }

                        if (gm.getGame().getPlayers().get(gameAndPlayer.getPlayerIndex()).getRoads() != 14) {
                            exchange.getResponseHeaders().set("Content-Type", "text/html");
                            String message = "Incorrect number of roads";
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                            System.out.println(message);
                            return;
                        }
                    }
                }

//				if (!buildRoad.isFree()) {
//					if (!gm.canBuildRoad()) {
//						exchange.getResponseHeaders().set("Content-Type", "text/html");
//						String message = "Not enough resouces.";
//						exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
//						exchange.getResponseBody().write(message.getBytes());
//						exchange.getResponseBody().close();
//						System.out.println(message);
//						return;
//					}
//				}
                HexLocation hexSpot = new HexLocation(buildRoad.getRoadLocation().getX(),
                        buildRoad.getRoadLocation().getY());
                EdgeDirection edgeDirection = buildRoad.getRoadLocation().getDirection();
                EdgeLocation edgeLocation = new EdgeLocation(hexSpot, edgeDirection);

                buildRoad.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().buildRoad(buildRoad);
                    } else {
                        game = ServerFascade.getSingleton().buildRoad(buildRoad);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in build road";
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                    System.out.println(result);
                }
            } catch (Exception e) {
                String error = "ERROR in build road";
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
                System.out.println(error);
            }
        }
    };
    /**
     * Handler to do a maritime trade
     */
    private HttpHandler maritimeTradeHandler = new HttpHandler() {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                MaritimeTrade maritimeTrade = model.fromJson(reader, MaritimeTrade.class);

                if (maritimeTrade.getType() == null || !maritimeTrade.getType().equals("maritimeTrade")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (maritimeTrade.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != maritimeTrade.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                String status = gm.getGame().getTurnTracker().getStatus();

                if (!status.equals("Playing")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Turn tracker status must be in \"Playing\".";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                maritimeTrade.setGameId(gameAndPlayer.getGameId());
                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().maritimeTrade(maritimeTrade);
                    } else {
                        game = ServerFascade.getSingleton().maritimeTrade(maritimeTrade);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in maritime trade";
                    System.out.println(result);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in maritime trade";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };
    /**
     * Handler to discard cards
     */
    private HttpHandler discardCardsHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                DiscardCards discardCards = model.fromJson(reader, DiscardCards.class);

                if (discardCards.getType() == null || !discardCards.getType().equals("discardCards")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (discardCards.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != discardCards.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (discardCards.getDiscardedCards() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Discarded cards can't be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                ResourceList resourcesToDiscard = discardCards.getDiscardedCards();

                if (resourcesToDiscard.getBrick() == null || resourcesToDiscard.getOre() == null
                        || resourcesToDiscard.getSheep() == null || resourcesToDiscard.getWheat() == null
                        || resourcesToDiscard.getWood() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "All Resources need to not be null.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());

                if (gm.getGame().getPlayers().get(discardCards.getPlayerIndex()).isDiscarded()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Player already discarded.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gm.getGame().getPlayers().get(discardCards.getPlayerIndex()).getResources()
                        .getTotalResources() < 7) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Player doesn't need to discard.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                boolean negativeResource = false;
                if (resourcesToDiscard.getBrick() < 0) {
                    negativeResource = true;
                }
                if (resourcesToDiscard.getOre() < 0) {
                    negativeResource = true;
                }
                if (resourcesToDiscard.getSheep() < 0) {
                    negativeResource = true;
                }
                if (resourcesToDiscard.getWheat() < 0) {
                    negativeResource = true;
                }
                if (resourcesToDiscard.getWood() < 0) {
                    negativeResource = true;
                }

                if (negativeResource) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Can't discard a negative number of a resource.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                boolean playerHasEnough = true;
                if (gm.getResourceManager().getGameBanks().get(gameAndPlayer.getPlayerIndex()).getResourcesCards()
                        .getBrick() < resourcesToDiscard.getBrick()) {
                    playerHasEnough = false;
                }
                if (gm.getResourceManager().getGameBanks().get(gameAndPlayer.getPlayerIndex()).getResourcesCards()
                        .getOre() < resourcesToDiscard.getOre()) {
                    playerHasEnough = false;
                }
                if (gm.getResourceManager().getGameBanks().get(gameAndPlayer.getPlayerIndex()).getResourcesCards()
                        .getSheep() < resourcesToDiscard.getSheep()) {
                    playerHasEnough = false;
                }
                if (gm.getResourceManager().getGameBanks().get(gameAndPlayer.getPlayerIndex()).getResourcesCards()
                        .getWheat() < resourcesToDiscard.getWheat()) {
                    playerHasEnough = false;
                }
                if (gm.getResourceManager().getGameBanks().get(gameAndPlayer.getPlayerIndex()).getResourcesCards()
                        .getWood() < resourcesToDiscard.getWood()) {
                    playerHasEnough = false;
                }

                if (!playerHasEnough) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Player doesn't have enough of those resources.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gm.getResourceManager().getGameBanks().get(gameAndPlayer.getPlayerIndex()).getResourcesCards()
                        .getTotalResources() / 2 != resourcesToDiscard.getTotalResources()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect number of resources to discard.";
                    System.out.println(message);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                discardCards.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    if (isMock) {
                        game = MockServerFascade.getSingleton().discardCards(discardCards);
                    } else {
                        game = ServerFascade.getSingleton().discardCards(discardCards);
                    }
                } catch (Exception e) {
                    game = null;
                }
                String result;
                if (game != null) {
                    result = game.toString();
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    result = "Couldn't grab game in discardCards";
                    System.out.println(result);
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }
            } catch (Exception e) {
                String error = "ERROR in discard cards";
                System.out.println(error);
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };

    private GameIdPlayerIdAndPlayerIndex verifyPlayer(HttpExchange exchange) {
        try {
            String userCookie = exchange.getRequestHeaders().getFirst("Cookie");
            GameIdPlayerIdAndPlayerIndex gameAndPlayer = null;
            if (userCookie == null || userCookie.equals("")) {
                return gameAndPlayer;
            }

            String decodedCookie = URLDecoder.decode(userCookie.split(";")[0].split("=")[1], "UTF-8");
            Integer playerIdThisOne = model.fromJson(decodedCookie, CookieModel.class).getPlayerID();
            Integer gameId = Integer
                    .parseInt(URLDecoder.decode(userCookie.split(";")[1].split("catan.game=")[1], "UTF-8"));
            int playerIndex = -1;
            boolean found = false;
            for (GameManager gm : AllOfOurInformation.getSingleton().getGames()) {
                if (gm.getGame().getId() == gameId) {

                    for (int i = 0; i < gm.getGame().getPlayers().size(); i++) {
                        if (gm.getGame().getPlayers().get(i).getId() == playerIdThisOne) {
                            found = true;
                            playerIndex = i;
                            break;
                        }
                    }
                    break;
                }
            }

            if (found) {
                gameAndPlayer = new GameIdPlayerIdAndPlayerIndex(gameId, playerIdThisOne, playerIndex);
            }
            return gameAndPlayer;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @param args the port number to run the server on
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            if (!args[0].equals("")) {
                SERVER_PORT_NUMBER = Integer.parseInt(args[0]);
            }
        }
        //Only for Phase 3 Dependency Injection
        if (args.length > 1) {
            if (args[1].equals("true")) {
                isMock = true;
            }
        }
        new Server().run();
    }

}
