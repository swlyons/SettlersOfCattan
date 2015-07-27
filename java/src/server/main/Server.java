package server.main;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

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
import java.util.Scanner;

public class Server {

    /**
     * index on a phone key pad (default port used if none specified
     */
    private static int SERVER_PORT_NUMBER = 8081;
    private static final int MAX_WAITING_CONNECTIONS = 10;

    private HttpServer server;
    private Gson model = new GsonBuilder().create();
    Map<String, String> games;

    /**
     * Server constructor
     */
    private Server() {
        games = new HashMap<>();
    }

    /**
     * Initializes the Database
     */
    private void run() {
        try {

            Database.initialize();
        } catch (ServerException e) {
            System.out.println("Could not initialize database: "
                    + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            server = HttpServer.create(
                    new InetSocketAddress(SERVER_PORT_NUMBER),
                    MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            System.out.println("Could not create HTTP server: "
                    + e.getMessage());
            e.printStackTrace();
            return;
        }

        server.setExecutor(null); // use the default executor

        // UserReceiver contexts
        server.createContext("/user/login", loginHandler);//worked on
        server.createContext("/user/register", registerHandler);//worked on

        // Games contexts
        server.createContext("/games/list", listHandler);//worked on
        server.createContext("/games/create", createHandler);//worked on
        server.createContext("/games/join", joinHandler);//worked on

        // Game contexts
        server.createContext("/game/model", modelHandler);//worked on
        server.createContext("/game/listAI", listAIHandler);//worked on

        // Moves contexts
        server.createContext("/moves/sendChat", sendChatHandler);//worked on
        server.createContext("/moves/rollNumber", rollNumberHandler);//worked on
        server.createContext("/moves/robPlayer", robPlayerHandler);
        server.createContext("/moves/finishTurn", finishTurnHandler);// worked on
        server.createContext("/moves/buyDevCard", buyDevCardHandler);
        server.createContext("/moves/Year_Of_Plenty", year_of_plentyHandler);
        server.createContext("/moves/Road_Building", road_buildingHandler);
        server.createContext("/moves/Soldier", soldierHandler);
        server.createContext("/moves/Monument", monumentHandler);
        server.createContext("/moves/offerTrade", offerTradeHandler);
        server.createContext("/moves/acceptTrade", acceptTradeHandler);
        server.createContext("/moves/buildSettlement", buildSettlementHandler);//worked on
        server.createContext("/moves/buildCity", buildCityHandler);//worked on
        server.createContext("/moves/buildRoad", buildRoadHandler);// worked on
        server.createContext("/moves/maritimeTrade", maritimeTradeHandler);
        server.createContext("/moves/discardCards", discardCardsHandler);

        //swagger
        server.createContext("/docs/api/data", new Handlers.JSONAppender(""));
        server.createContext("/docs/api/view", new Handlers.BasicFile(""));

        // Empty (good for testing if service is working)
        server.createContext("/", downloadHandler);
        //start the server
        server.start();
    }

    private HttpHandler downloadHandler = new HttpHandler() {
        /**
         * Handler to download a file from the server
         */
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            String path = new File(".").getCanonicalPath(); //+ File.separator;
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
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
                    file.length());
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
                //un-package the data
                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                User user = model.fromJson(reader, User.class);

                if (user.getUsername() == null || user.getPassword() == null) {
                    String error = "Username and password can't be null";
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                    exchange.getResponseBody().write(error.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                //call the appropriate fascade (real or mock)
                int id = ServerFascade.getSingleton().getLoginId(user);
                //result = MockServerFascade.getSingleton().login(user);

                //re-package and return the data
                if (id != -1) {
                    String cookie = "catan.user=";
//                cookie+="{\"name\":\""+AllOfOurInformation.getSingleton().getUsers().get(id).getUsername()+"\",";
//                cookie+="\"password\":\""+AllOfOurInformation.getSingleton().getUsers().get(id).getPassword()+"\",";
//                cookie+="\"playerID\":" +id+"}";
                    cookie += "{name:" + AllOfOurInformation.getSingleton().getUsers().get(id).getUsername() + ",";
                    cookie += "password:" + AllOfOurInformation.getSingleton().getUsers().get(id).getPassword() + ",";
                    cookie += "playerID:" + id + "}";

                    cookie += ";Path=/;";
//                String message = model.toJson("Success", String.class);
                    String message = "Success";
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.getResponseHeaders().set("Set-Cookie", cookie);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                } else {

                    String message = "Failed to login - bad username or password.";
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                }
            } catch (Exception e) {
                String error = "ERROR in logging in";
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

            //un-package the data
            Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
            User user = model.fromJson(reader, User.class);

            if (user.getUsername() == null || user.getPassword() == null) {
                String error = "Username and password can't be null";
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            //call the appropriate fascade (real or mock)
            boolean result = false;
            try {
                result = ServerFascade.getSingleton().register(user);
                //result = MockServerFascade.getSingleton().register(user);
            } catch (ServerException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

            //re-package and return the data
            if (result) {
                String message = "Success";
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, message.length());
                exchange.getResponseBody().write(message.getBytes());
                exchange.getResponseBody().close();
            } else {
                String message = "Failed to register - bad username or password.";
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
                String response = "[";
                gamesList = ServerFascade.getSingleton().listGames();
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

                //check that the booleans are actually booleans for the swagger
                //<editor-fold desc="Check Booleans">
                BufferedReader br = new BufferedReader(reader);
                Scanner sc = new Scanner(br);
                boolean validRandomValues = true;
                while (sc.hasNextLine()) {
                    String line = sc.next();
                    if (line.contains(",")) {
                        if (!line.contains("true") && !line.contains("false")) {
                            //invalid request
                            validRandomValues = false;
                            break;
                        }
                    }
                    messageBody += line;
                }
                //</editor-fold>
                GameInfo gameInfo = null;
                if (validRandomValues) {
                    CreateGameRequest createGame = model.fromJson(messageBody, CreateGameRequest.class);

                    if (createGame.isRandomNumbers() == null || createGame.isRandomPorts() == null || createGame.isRandomTiles() == null) {
                        String error = "Create game booleans can't be null";
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                        exchange.getResponseBody().write(error.getBytes());
                        exchange.getResponseBody().close();
                        return;
                    }

                    if (createGame.getName() == null) {
                        String error = "Name can't be null";
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                        exchange.getResponseBody().write(error.getBytes());
                        exchange.getResponseBody().close();
                        return;
                    }

                    try {
                        gameInfo = ServerFascade.getSingleton().createGame(createGame);
                    } catch (Exception e) {
                        String error = "Create game error with fascade trying to create game";
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

                    String message = validRandomValues ? "Failed to createGame - need a name." : "Failed to create game - all random values (i.e. randomTiles) must be \"true\" or \"false\"";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in create game";
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
            //un-package the data
            try {
                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                JoinGameRequest joinGameRequest = model.fromJson(reader, JoinGameRequest.class);
                exchange.getResponseHeaders().set("Content-Type", "text/html");

                String userCookie = exchange.getRequestHeaders().getFirst("Cookie");

                if (userCookie == null || userCookie.equals("")) {
                    String message = "Need to login first.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (joinGameRequest.getId() == null) {
                    String message = "Need a Game Id.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (joinGameRequest.getId() < 0 || AllOfOurInformation.getSingleton().getGames().size() <= joinGameRequest.getId()) {
                    String message = "Invalid Game Id.";
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
                    if (AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame().getPlayers().get(i) != null) {
                        takenColors += AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame().getPlayers().get(i).getColor();
                    }
                    if (AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame().getPlayers().get(i) == null) {
                        //dont allow the player to join the game with the same color as another player
                        if (takenColors.contains(joinGameRequest.getColor())) {
                            colorTaken = true;
                        }
                        break;
                    }

                    if (AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame().getPlayers().get(i).getId() == playerIdThisOne) {
                        found = true;
                        AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame().getPlayers().get(i).setColor(joinGameRequest.getColor().toLowerCase());
                        break;
                    }
                    if (i == 3) {
                        finished = true;
                    }
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
                            if (AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame().getPlayers().get(i) == null) {
                                AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame().getPlayers().set(i, player);
                                break;
                            }
                        }

                        int spot = AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame().getPlayers().indexOf(player);

                        if (spot == -1 || 3 < spot) {
                            AllOfOurInformation.getSingleton().getGames().get(joinGameRequest.getId()).getGame().getPlayers().remove(player);
                            String message = "Already full.";
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
                            //Error, someone joining the same game twice simeltaneously. (Twice at the same time)
                        }
                    }
                }

            } catch (Exception e) {
                String error = "ERROR in join game";
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
                    return;
                }

                Integer version = exchange.getRequestURI().toString().indexOf("version=");

                if (version == -1) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to provide a version parameter.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
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
                    return;
                }

                //call the appropriate fascade (real or mock)
                String result;

                if (version < AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame().getVersion()) {
                    result = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame().toString();
                } else {
                    result = "\"true\"";
                }

                //re-package and return the data
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                exchange.getResponseBody().write(result.getBytes());
                exchange.getResponseBody().close();
            } catch (Exception e) {
                String error = "ERROR in grabbing model";
                exchange.getResponseHeaders().set("Content-Type", "text/html");
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
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, message.length());
            exchange.getResponseBody().write(message.getBytes());
            exchange.getResponseBody().close();
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

                //make sure the player index is valid (a number that gson can parse)
                SendChat sendChatRequest = null;
                try {
                    sendChatRequest = model.fromJson(reader, SendChat.class);
                } catch (com.google.gson.JsonSyntaxException jse) {
                    invalidIndex = true;
                }

                if (sendChatRequest.getPlayerIndex() == null) {
                    String message = "playerIndex can't be null.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                //get the cookie from the header
                String userCookie = exchange.getRequestHeaders().getFirst("Cookie");

                //make sure the user is logged in
                if (userCookie == null || userCookie.equals("")) {
                    notLoggedIn = true;
                }

                //pass in the game id 
                GameIdPlayerIdAndPlayerIndex gameAndPlayerId = verifyPlayer(exchange);
                if ((gameAndPlayerId != null) && (sendChatRequest != null)) {

                    sendChatRequest.setContent(sendChatRequest.getContent() + ";" + gameAndPlayerId.getGameId());
                } else {
                    notLoggedIn = true;
                }

                //make sure there is a cookie
                if (invalidIndex || notLoggedIn) {
                    String message = invalidIndex ? "Invalid command.  ['playerIndex' field has an invalid value.]" : "Need to login and join a game first.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                } else {
                    // call the appropriate facade
                    GameInfo result = null;
                    try {
                        result = ServerFascade.getSingleton().sendChat(sendChatRequest);
                    } catch (NullPointerException npe) {
                        String message = "For the specified game there is no player with the index: " + sendChatRequest.getPlayerIndex();
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
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                RollNumber rollNumber = model.fromJson(reader, RollNumber.class);
                if (rollNumber.getType()==null||!rollNumber.getType().equals("rollNumber")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (rollNumber.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (rollNumber.getPlayerIndex() != gameAndPlayer.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (rollNumber.getNumber() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need a number.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameInfo gi = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame();

                if (gameAndPlayer.getPlayerIndex() != gi.getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (!gi.getTurnTracker().getStatus().equals("Rolling")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not Rolling status in turn tracker.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (rollNumber.getNumber() < 2 || 12 < rollNumber.getNumber()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Invalid number to roll.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                rollNumber.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    game = ServerFascade.getSingleton().rollNumber(rollNumber);
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
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in rolling number";
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
            try{
                GameIdPlayerIdAndPlayerIndex gameAndPlayer = verifyPlayer(exchange);

                if (gameAndPlayer == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to login and join a valid game.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                RobPlayer robPlayer = model.fromJson(reader, RobPlayer.class);

                if (robPlayer.getType()==null||!robPlayer.getType().equals("robPlayer")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (robPlayer.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (robPlayer.getPlayerIndex() != robPlayer.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (robPlayer.getVictimIndex()==null||robPlayer.getVictimIndex()<-1||3<robPlayer.getVictimIndex()||robPlayer.getVictimIndex()==robPlayer.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Victim index isn't valid.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                
                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                
                if(!gm.getGame().getTurnTracker().getStatus().equals("Robbing")){
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Turn tracker status must be \"Robbing\".";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                
                if(!gm.canPlaceRobber(robPlayer.getLocation())){
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Cannot place robber there";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
//                HexLocation hexLoc, int playerId
                if(!gm.canRobPlayer(robPlayer.getLocation(),robPlayer.getVictimIndex())){
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Cannot rob that player";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                
                robPlayer.setGameId(gameAndPlayer.getGameId());
                
                GameInfo game;
                try {
                    game = ServerFascade.getSingleton().robPlayer(robPlayer);
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
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }
                
            }catch(Exception e){
                String error = "ERROR in rob player";
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
                    return;
                }
                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                FinishMove finishMove = model.fromJson(reader, FinishMove.class);

                if (finishMove.getType()==null||!finishMove.getType().equals("finishTurn")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (finishMove.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != finishMove.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameInfo gi = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame();
                if (gameAndPlayer.getPlayerIndex() != gi.getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                String status = gi.getTurnTracker().getStatus();
                if (status.equals("Rolling")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Must Roll first.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                if (status.equals("Robbing")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Must rob first.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                if (status.equals("Discarding")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Must discard first.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
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
                        return;
                    }
                }

                finishMove.setGameId(gameAndPlayer.getGameId());
                GameInfo game;
                try {
                    game = ServerFascade.getSingleton().finishMove(finishMove);
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
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in finishing turn";
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };
    /**
     * Handler to buy a development card
     */
    private HttpHandler buyDevCardHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: buy a development card

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to use a year of plenty card
     */
    private HttpHandler year_of_plentyHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: use year of plenty card

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to use a road building card
     */
    private HttpHandler road_buildingHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: use the road building card

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to use a soldier card
     */
    private HttpHandler soldierHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: use the soldier card

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to user a monument card
     */
    private HttpHandler monumentHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: use monument card

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to offer a trade
     */
    private HttpHandler offerTradeHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: offer a trade to another player

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to accept a trade
     */
    private HttpHandler acceptTradeHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: accept a trade from another player

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
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                BuildSettlement buildSettlement = model.fromJson(reader, BuildSettlement.class);

                if (buildSettlement.getType()==null||!buildSettlement.getType().equals("buildSettlement")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (buildSettlement.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != buildSettlement.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (buildSettlement.isFree() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need free boolean.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (buildSettlement.getVertexLocation() == null || buildSettlement.getVertexLocation().getX() == null || buildSettlement.getVertexLocation().getY() == null || buildSettlement.getVertexLocation().getDirection() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need a proper settlementLocation.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                String status = gm.getGame().getTurnTracker().getStatus();

                if (!(status.equals("Playing") || status.equals("FirstRound") || status.equals("SecondRound"))) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Cannot place settlement in this turn tracker status.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (status.equals("FirstRound") || status.equals("SecondRound")) {
                    
                    if (!buildSettlement.isFree()) {
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        String message = "Building a settlement must be free in the first two rounds.";
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                        exchange.getResponseBody().write(message.getBytes());
                        exchange.getResponseBody().close();
                        return;
                    }
                    
                    if(status.equals("FirstRound")){
                        for(int i=0;i<gm.getGame().getPlayers().size();i++){
                            if(gm.getGame().getPlayers().get(i)==null){
                                exchange.getResponseHeaders().set("Content-Type", "text/html");
                                String message = "Need 4 players first.";
                                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                                exchange.getResponseBody().write(message.getBytes());
                                exchange.getResponseBody().close();
                                return;
                            }
                        }
                        
                        if(gm.getGame().getPlayers().get(gameAndPlayer.getPlayerIndex()).getSettlements()!=5){
                            exchange.getResponseHeaders().set("Content-Type", "text/html");
                            String message = "Incorrect number of settlements.";
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                            return;
                        }
                        
                    }
                    
                    if(status.equals("SecondRound")){
                        if(gm.getGame().getPlayers().get(gameAndPlayer.getPlayerIndex()).getSettlements()!=4){
                            exchange.getResponseHeaders().set("Content-Type", "text/html");
                            String message = "Incorrect number of settlements.";
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                            return;
                        }
                    
                    }
                    
                }

                if (!buildSettlement.isFree()) {
                    if (gm.canBuildSettlement()) {
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        String message = "Not enough resouces.";
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                        exchange.getResponseBody().write(message.getBytes());
                        exchange.getResponseBody().close();
                        return;
                    }
                }

                HexLocation hexSpot = new HexLocation(buildSettlement.getVertexLocation().getX(), buildSettlement.getVertexLocation().getY());
                VertexDirection vertexDirection = buildSettlement.getVertexLocation().getDirection();
                VertexLocation vertexLocation = new VertexLocation(hexSpot, vertexDirection);

                if (!gm.canPlaceSettlement(vertexLocation)) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Cannot place settlement there.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                buildSettlement.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    game = ServerFascade.getSingleton().buildSettlement(buildSettlement);
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
                }

            } catch (Exception e) {
                String error = "ERROR in build settlement";
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
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
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                BuildCity buildCity = model.fromJson(reader, BuildCity.class);

                if (buildCity.getType()==null||!buildCity.getType().equals("buildCity")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (buildCity.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != buildCity.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (buildCity.getVertexLocation() == null || buildCity.getVertexLocation().getX() == null || buildCity.getVertexLocation().getY() == null || buildCity.getVertexLocation().getDirection() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need a proper cityLocation.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                String status = gm.getGame().getTurnTracker().getStatus();

                if (!(status.equals("Playing"))) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Cannot place city in this turn tracker status.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gm.canBuildCity()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not enough resouces.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                HexLocation hexSpot = new HexLocation(buildCity.getVertexLocation().getX(), buildCity.getVertexLocation().getY());
                VertexDirection vertexDirection = buildCity.getVertexLocation().getDirection();
                VertexLocation vertexLocation = new VertexLocation(hexSpot, vertexDirection);

                if (!gm.canPlaceCity(vertexLocation)) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Cannot place city there.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                buildCity.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    game = ServerFascade.getSingleton().buildCity(buildCity);
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
                    result = "Couldn't grab game in build city";
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }

            } catch (Exception e) {
                String error = "ERROR in build city";
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
                    return;
                }

                Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
                BuildRoad buildRoad = model.fromJson(reader, BuildRoad.class);

                if (buildRoad.getType()==null||!buildRoad.getType().equals("buildRoad")) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (buildRoad.getPlayerIndex() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "playerIndex can't be null.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (gameAndPlayer.getPlayerIndex() != buildRoad.getPlayerIndex()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (buildRoad.isFree() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need free boolean.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (buildRoad.getRoadLocation() == null || buildRoad.getRoadLocation().getX() == null || buildRoad.getRoadLocation().getY() == null || buildRoad.getRoadLocation().getDirection() == null) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need a proper roadLocation.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                GameManager gm = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId());
                if (gameAndPlayer.getPlayerIndex() != gm.getGame().getTurnTracker().getCurrentTurn()) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                String status = gm.getGame().getTurnTracker().getStatus();

                if (!(status.equals("Playing") || status.equals("FirstRound") || status.equals("SecondRound"))) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Cannot place road in this turn tracker status.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if (status.equals("FirstRound") || status.equals("SecondRound")) {
                    if (!buildRoad.isFree()) {
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        String message = "Building a road must be free in the first two rounds.";
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                        exchange.getResponseBody().write(message.getBytes());
                        exchange.getResponseBody().close();
                        return;
                    }
                    
                    if(status.equals("FirstRound")){
                        if(gm.getGame().getPlayers().get(gameAndPlayer.getPlayerIndex()).getSettlements()!=4){
                            exchange.getResponseHeaders().set("Content-Type", "text/html");
                            String message = "Place exactly 1 settlement first.";
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                            return;
                        }
                        
                        if(gm.getGame().getPlayers().get(gameAndPlayer.getPlayerIndex()).getRoads()!=15){
                            exchange.getResponseHeaders().set("Content-Type", "text/html");
                            String message = "Incorrect number of roads";
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                            return;
                        }

                    }
                    
                    if(status.equals("SecondRound")){
                        if(gm.getGame().getPlayers().get(gameAndPlayer.getPlayerIndex()).getSettlements()!=3){
                            exchange.getResponseHeaders().set("Content-Type", "text/html");
                            String message = "Place exactly 1 settlement first in the second round (you should have 3).";
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                            return;
                        }
                        
                        if(gm.getGame().getPlayers().get(gameAndPlayer.getPlayerIndex()).getRoads()!=14){
                            exchange.getResponseHeaders().set("Content-Type", "text/html");
                            String message = "Incorrect number of roads";
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                            exchange.getResponseBody().write(message.getBytes());
                            exchange.getResponseBody().close();
                            return;
                        }
                    }
                }

                if (!buildRoad.isFree()) {
                    if (gm.canBuildRoad()) {
                        exchange.getResponseHeaders().set("Content-Type", "text/html");
                        String message = "Not enough resouces.";
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                        exchange.getResponseBody().write(message.getBytes());
                        exchange.getResponseBody().close();
                        return;
                    }
                }

                HexLocation hexSpot = new HexLocation(buildRoad.getRoadLocation().getX(), buildRoad.getRoadLocation().getY());
                EdgeDirection edgeDirection = buildRoad.getRoadLocation().getDirection();
                EdgeLocation edgeLocation = new EdgeLocation(hexSpot, edgeDirection);

                if (!gm.canPlaceRoad(edgeLocation)) {
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Cannot place road there.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                buildRoad.setGameId(gameAndPlayer.getGameId());

                GameInfo game;
                try {
                    game = ServerFascade.getSingleton().buildRoad(buildRoad);
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
                    result = "Couldn't grab game in build road";
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, result.length());
                    exchange.getResponseBody().write(result.getBytes());
                    exchange.getResponseBody().close();
                }
            } catch (Exception e) {
                String error = "ERROR in build road";
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, error.length());
                exchange.getResponseBody().write(error.getBytes());
                exchange.getResponseBody().close();
            }
        }
    };
    /**
     * Handler to do a maritime trade
     */
    private HttpHandler maritimeTradeHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: perform a maritime trade

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to discard cards
     */
    private HttpHandler discardCardsHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: discard cards

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };

    private GameIdPlayerIdAndPlayerIndex verifyPlayer(HttpExchange exchange) {
        try {
            String userCookie = exchange.getRequestHeaders().getFirst("Cookie");
            GameIdPlayerIdAndPlayerIndex gameAndPlayer = null;
            if (userCookie == null || userCookie.equals("")) {
                return gameAndPlayer;
            }

            String decodedCookie = URLDecoder.decode(userCookie.split("=")[1], "UTF-8");
            Integer playerIdThisOne = model.fromJson(decodedCookie.split(";")[0], CookieModel.class).getPlayerID();
            Integer gameId = Integer.parseInt(URLDecoder.decode(userCookie.split("=")[2], "UTF-8"));
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
                gameAndPlayer = new GameIdPlayerIdAndPlayerIndex(playerIdThisOne, gameId, playerIndex);
            }
            return gameAndPlayer;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Maps each parameter to it's value
     *
     * @param query the query from the url
     * @return a map containing the parameter name as it's key and the parameter
     * value as it's value
     *
     * NOTE: THIS REPLACES ALL THE COMMUNICATION INPUT CLASSES!!!
     */
    public Map<String, String> saveGameToMap() {
        /* for (String param : query.split("&")) {
         String pair[] = param.split("=");
         if (pair.length > 1) {
         result.put(pair[0], pair[1]);
         } else {
         result.put(pair[0], "");
         }
         }*/
        return games;
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

        new Server().run();
    }

}
