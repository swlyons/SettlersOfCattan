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
        server.createContext("/user/login", loginHandler);
        server.createContext("/user/register", registerHandler);

        // Games contexts
        server.createContext("/games/list", listHandler);
        server.createContext("/games/create", createHandler);
        server.createContext("/games/join", joinHandler);

        // Game contexts
        server.createContext("/game/model", modelHandler);

        // Moves contexts
        server.createContext("/moves/sendChat", sendChatHandler);
        server.createContext("/moves/rollNumber", rollNumberHandler);
        server.createContext("/moves/robPlayer", robPlayerHandler);
        server.createContext("/moves/finishTurn", finishTurnHandler);
        server.createContext("/moves/buyDevCard", buyDevCardHandler);
        server.createContext("/moves/Year_Of_Plenty", year_of_plentyHandler);
        server.createContext("/moves/Road_Building", road_buildingHandler);
        server.createContext("/moves/Soldier", soldierHandler);
        server.createContext("/moves/Monument", monumentHandler);
        server.createContext("/moves/offerTrade", offerTradeHandler);
        server.createContext("/moves/acceptTrade", acceptTradeHandler);
        server.createContext("/moves/buildSettlement", buildSettlementHandler);
        server.createContext("/moves/buildCity", buildCityHandler);
        server.createContext("/moves/buildRoad", buildRoadHandler);
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

            //un-package the data
            Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
            User user = model.fromJson(reader, User.class);

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
            
            ArrayList<GameInfo> gamesList = new ArrayList<GameInfo>();
            String response = "[";
            try {
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
                String message = "Failed to list Games.";
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                exchange.getResponseBody().write(message.getBytes());
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
                try {
                    gameInfo = ServerFascade.getSingleton().createGame(createGame);
                } catch (Exception e) {
                    
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
                e.printStackTrace();
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
                
                if(version==-1){
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Need to provide a version parameter.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                
                try{
                    version = Integer.parseInt(exchange.getRequestURI().toString().substring(version+8));
                }catch(Exception e){
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Had trouble parsing version parameter.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                //call the appropriate fascade (real or mock)
                String result;
                
                if(version < AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame().getVersion()){
                    result = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame().toString();
                }else{
                    result = "\"true\"";
                }

                //re-package and return the data
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
                exchange.getResponseBody().write(result.getBytes());
                exchange.getResponseBody().close();
            } catch (Exception e) {
                exchange.getResponseHeaders().set("Content-Type", "text/html");
                String message = "Unable to grab model.";
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

                //make sure the player index is valid (a number that gson can parse)
                SendChat sendChatRequest = null;
                try {
                    sendChatRequest = model.fromJson(reader, SendChat.class);
                } catch (com.google.gson.JsonSyntaxException jse) {
                    invalidIndex = true;
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
                    String message = invalidIndex ? "Invalid command.  ['playerIndex' field has an invalid value.]" : "Need to login first.";
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
                e.printStackTrace();
            }
            
        }
    };
    /**
     * Handler to get roll a number
     */
    private HttpHandler rollNumberHandler = new HttpHandler() {
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
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
                if(!rollNumber.getType().equals("rollNumber")){
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect type.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                if(rollNumber.getPlayerIndex()<0||rollNumber.getPlayerIndex()<4||rollNumber.getPlayerIndex()!=gameAndPlayer.getPlayerIndex()){
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Incorrect playerIndex.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                
                if(gameAndPlayer.getPlayerIndex()!=AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame().getTurnTracker().getCurrentTurn()){
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not your turn.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                
                if(!AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame().getTurnTracker().getStatus().equals("Rolling")){
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Not Rolling status in turn tracker.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;                
                }
            
                if(rollNumber.getNumber()<2||12<rollNumber.getNumber()){
                    exchange.getResponseHeaders().set("Content-Type", "text/html");
                    String message = "Invalid number to roll.";
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, message.length());
                    exchange.getResponseBody().write(message.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }
                
                AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).rollDice(rollNumber.getNumber());
                AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame().getVersion()+1);
                
                String result = AllOfOurInformation.getSingleton().getGames().get(gameAndPlayer.getGameId()).getGame().toString();
                
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: roll a number
            
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, result.length());
            exchange.getResponseBody().write(result.getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to rob a player
     */
    private HttpHandler robPlayerHandler = new HttpHandler() {
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: rob a player

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to finish a turn
     */
    private HttpHandler finishTurnHandler = new HttpHandler() {
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: finish the current player's turn

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
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
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: build a settlement

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to build a city
     */
    private HttpHandler buildCityHandler = new HttpHandler() {
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: build a city

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to build a road
     */
    private HttpHandler buildRoadHandler = new HttpHandler() {
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            //TODO: build a road

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
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
                            playerIndex=i;
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
