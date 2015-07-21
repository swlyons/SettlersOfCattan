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
import server.dao.Database;

public class Server {

	/**
	 * index on a phone key pad (default port used if none specified
	 */
	private static int SERVER_PORT_NUMBER = 8081;
	private static final int MAX_WAITING_CONNECTIONS = 10;

	private HttpServer server;
	private Gson model = new GsonBuilder().create();

	/**
	 * Server constructor
	 */
	private Server() {
		return;
	}

	/** Initializes the Database */
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

		// User contexts
		server.createContext("/user/login", loginHandler);
		server.createContext("/user/register", registerHandler);

		// Game contexts
		server.createContext("/game/list", listHandler);
		server.createContext("/game/create", createHandler);
                server.createContext("/game/join", joinHandler);
		server.createContext("/game/save", saveHandler);
                server.createContext("/game/load", loadHandler);
		server.createContext("/game/model", modelHandler);
                server.createContext("/game/reset", resetHandler);
		server.createContext("/game/commands", commandsHandler);
                server.createContext("/game/addAI", addAIHandler);
		server.createContext("/game/listAI", listAIHandler);

		// Move contexts
		server.createContext("/games/sendChat", sendChatHandler);
		server.createContext("/games/rollNumber", rollNumberHandler);
		server.createContext("/games/robPlayer", robPlayerHandler);
                server.createContext("/games/finishTurn", finishTurnHandler);
		server.createContext("/games/buyDevCard", buyDevCardHandler);
		server.createContext("/games/Year_Of_Plenty", year_of_plentyHandler);
                server.createContext("/games/Road_Building", road_buildingHandler);
                server.createContext("/games/Soldier", soldierHandler);
		server.createContext("/games/Monopoly", monopolyHandler);
		server.createContext("/games/Monument", monumentHandler);
                server.createContext("/games/offerTrade", offerTradeHandler);
		server.createContext("/games/acceptTrade", acceptTradeHandler);
		server.createContext("/games/buildSettlement", buildSettlementHandler);
                server.createContext("/games/buildCity", buildCityHandler);
		server.createContext("/games/buildRoad", buildRoadHandler);
		server.createContext("/games/maritimeTrade", maritimeTradeHandler);
		server.createContext("/games/discardCards", discardCardsHandler);
		server.createContext("/games/changeLogLevel", changeLogLevelHandler);

		// Empty (good for testing if service is working)
		server.createContext("/", downloadHandler);
                
                //swagger
                server.createContext("/docs/api/data", new Handlers.JSONAppender(""));
                server.createContext("/docs/api/view", new Handlers.BasicFile(""));
                
                //allow cors for swagger
		server.start();
	}

	private HttpHandler downloadHandler = new HttpHandler() {
		/**
		 * Handler to download a file from the server
		 */
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			String[] pathParts = exchange.getRequestURI().getPath().split("/");
			String path = new File(".").getCanonicalPath() + File.separator
					+ "importdata";
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

	/** Handler to get all projects */
	private HttpHandler loginHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			System.out.println("Here");
			//TODO: Check that the user can login
                    //HttpURLConnection.
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().add("Content-Type", "application/json");
                        exchange.getResponseBody().write(model.toJson("Login Response").getBytes());
			exchange.getResponseBody().close();
		}
	};

	/** Handler to validate a User */
	private HttpHandler registerHandler = new HttpHandler() {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: check that the user can register
                    
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Register Response".getBytes());
			exchange.getResponseBody().close();
		}
	};

	/** Handler to get all Fields */
	private HttpHandler listHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			
                        // TODO: return the list of games
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("List Games Response".getBytes());
			exchange.getResponseBody().close();
		}
	};

	/** Handler to delete a specific field in the Fields Table */
	private HttpHandler createHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Create a game
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Creat Game Response".getBytes());
			exchange.getResponseBody().close();
		}
	};

	/** Handler to get a sample Image for a project */
	private HttpHandler joinHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Join a game
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Join Game Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};

	/** Handler to download an Image for a project */
	private HttpHandler saveHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Save a game
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Save Game Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};

	/** Handler to download an Image for a project */
	private HttpHandler loadHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Load a game
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Load Game Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        
        /** Handler to download an Image for a project */
	private HttpHandler modelHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Model a game
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Get Model Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler resetHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Reset a game
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Reset Game Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler commandsHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Either return a list of Cammands  or Execute a Command for a game
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("List or Send Command Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler addAIHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Add AI to a game
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Add AI Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler listAIHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: List available AI a game
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("List AI Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler sendChatHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Send Chat move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Send Chat Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler rollNumberHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Roll Number move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Roll Number Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler robPlayerHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Rob Player move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Rob Player Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler finishTurnHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Finish Turn move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Finish Turn Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler buyDevCardHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Buy Development Card move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Buy development Card Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler year_of_plentyHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Year of Plenty move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Year Of Plenty Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler road_buildingHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Road Building move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Road Building Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler soldierHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Soldier move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Soldier Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler monopolyHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Monopoly move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Monopoly Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler monumentHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Monument move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Monument Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler offerTradeHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Offer Trade move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Offer Trade Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler acceptTradeHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Accept Trade move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Accept Trade Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler buildSettlementHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Build Settlement move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Build Settlement Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler buildCityHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Build City move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Build City Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler buildRoadHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Build Road move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Build Road Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler maritimeTradeHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Maritime Trade move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Maritime Trade Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler discardCardsHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Discard Cards move
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Discard Cards Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
        private HttpHandler changeLogLevelHandler = new HttpHandler() {

		@Override
		public void handle(HttpExchange exchange) throws IOException {
			//TODO: Change Log Level Utility
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseBody().write("Change Log Level Response".getBytes());
			
			exchange.getResponseBody().close();
		}
	};
	/**
	 * Maps each parameter to it's value
	 *
	 * @param query
	 *            the query from the url
	 * @return a map containing the parameter name as it's key and the parameter
	 *         value as it's value
	 *
	 *         NOTE: THIS REPLACES ALL THE COMMUNICATION INPUT CLASSES!!!
	 */
	public Map<String, String> queryToMap(String query) {
		Map<String, String> result = new HashMap<String, String>();
		for (String param : query.split("&")) {
			String pair[] = param.split("=");
			if (pair.length > 1) {
				result.put(pair[0], pair[1]);
			} else {
				result.put(pair[0], "");
			}
		}
		return result;
	}

	/**
	 * 
	 * @param args
	 *            the port number to run the server on
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
