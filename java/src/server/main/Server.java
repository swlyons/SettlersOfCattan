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
import java.util.logging.Level;
import java.util.logging.Logger;
import server.receiver.*;
import shared.data.*;
import shared.model.*;
import sun.font.CreatedFontTracker;

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

        // Game contexts
        server.createContext("/games/list", listHandler);
        server.createContext("/games/create", createHandler);
        server.createContext("/games/join", joinHandler);

        // Move contexts
        server.createContext("/game/model", modelHandler);
        server.createContext("/game/sendChat", sendChatHandler);
        server.createContext("/game/rollNumber", rollNumberHandler);
        server.createContext("/game/robPlayer", robPlayerHandler);
        server.createContext("/game/finishTurn", finishTurnHandler);
        server.createContext("/game/buyDevCard", buyDevCardHandler);
        server.createContext("/game/Year_Of_Plenty", year_of_plentyHandler);
        server.createContext("/game/Road_Building", road_buildingHandler);
        server.createContext("/game/Soldier", soldierHandler);
        server.createContext("/game/Monument", monumentHandler);
        server.createContext("/game/offerTrade", offerTradeHandler);
        server.createContext("/game/acceptTrade", acceptTradeHandler);
        server.createContext("/game/buildSettlement", buildSettlementHandler);
        server.createContext("/game/buildCity", buildCityHandler);
        server.createContext("/game/buildRoad", buildRoadHandler);
        server.createContext("/game/maritimeTrade", maritimeTradeHandler);
        server.createContext("/game/discardCards", discardCardsHandler);

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
            if (id!=-1) {
                String cookie = "catan.user=";
//                cookie+="{\"name\":\""+AllOfOurInformation.getSingleton().getUsers().get(id).getUsername()+"\",";
//                cookie+="\"password\":\""+AllOfOurInformation.getSingleton().getUsers().get(id).getPassword()+"\",";
//                cookie+="\"playerID\":" +id+"}";
                cookie+="{name:"+AllOfOurInformation.getSingleton().getUsers().get(id).getUsername()+",";
                cookie+="password:"+AllOfOurInformation.getSingleton().getUsers().get(id).getPassword()+",";
                cookie+="playerID:" +id+"}";
                
                cookie += ";Path=/;";
//                String message = model.toJson("Success", String.class);
                String message = "Success";
                exchange.getResponseHeaders().set("Content-Type", "application/json");                
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
            exchange.getResponseHeaders().add("Content-Type", "text/xml");

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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            //TODO: return the list of games running on the server

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, "[{\"title\":\"Default Game\", \"id\":\"0\"}]".length());
            exchange.getResponseBody().write("[{\"title\":\"Default Game\", \"id\":\"0\"}]".getBytes());
            exchange.getResponseBody().close();
        }
    };

    /**
     * Handler to create a game
     */
    private HttpHandler createHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            
            Reader reader = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
            CreateGameRequest createGame = model.fromJson(reader, CreateGameRequest.class);
            GameInfo gameInfo = null;
            try{
                gameInfo = ServerFascade.getSingleton().createGame(createGame);
            }catch(Exception e){
            
            }
            if (gameInfo!=null) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, gameInfo.toString().length());
                exchange.getResponseBody().write(gameInfo.toString().getBytes());
                exchange.getResponseBody().close();
            } else {

                String message = "Failed to createGame - need a name.";
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
            exchange.getResponseHeaders().add("Content-Type", "text/html");
            //TODO: join a game

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };

    /**
     * Handler to get a the game model
     */
    private HttpHandler modelHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            
            //call the appropriate fascade (real or mock)
            GameInfo result = null;
            try {
                result = ServerFascade.getSingleton().getGameModel("");
                //result = MockServerFascade.getSingleton().getModel("");
            } catch (ServerException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

            //re-package and return the data
            //TODO: All toString methods need to have quotes around the json fields and values
            String message = result.toString();
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            //TODO: send a chat move

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to get roll a number
     */
    private HttpHandler rollNumberHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            //TODO: roll a number

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };
    /**
     * Handler to rob a player
     */
    private HttpHandler robPlayerHandler = new HttpHandler() {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
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
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            //TODO: discard cards

            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            exchange.getResponseBody().write("Success".getBytes());
            exchange.getResponseBody().close();
        }
    };

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
