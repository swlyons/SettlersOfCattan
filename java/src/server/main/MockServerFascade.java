/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import shared.data.GameInfo;
import shared.data.PlayerInfo;
import shared.data.User;
import shared.model.*;

/**
 *
 * @author ddennis
 */
public class MockServerFascade implements Fascade {

    //<editor-fold desc="JSON canned game string to pass back">

    String jsonGameString = "{\n"
            + "  \"deck\": {\n"
            + "    \"yearOfPlenty\": 2,\n"
            + "    \"monopoly\": 2,\n"
            + "    \"soldier\": 14,\n"
            + "    \"roadBuilding\": 2,\n"
            + "    \"monument\": 5\n"
            + "  },\n"
            + "  \"map\": {\n"
            + "    \"hexes\": [\n"
            + "      {\n"
            + "        \"location\": {\n"
            + "          \"x\": 0,\n"
            + "          \"y\": -2\n"
            + "        }\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"brick\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 1,\n"
            + "          \"y\": -2\n"
            + "        },\n"
            + "        \"number\": 4\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"wood\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 2,\n"
            + "          \"y\": -2\n"
            + "        },\n"
            + "        \"number\": 11\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"brick\",\n"
            + "        \"location\": {\n"
            + "          \"x\": -1,\n"
            + "          \"y\": -1\n"
            + "        },\n"
            + "        \"number\": 8\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"wood\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 0,\n"
            + "          \"y\": -1\n"
            + "        },\n"
            + "        \"number\": 3\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"ore\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 1,\n"
            + "          \"y\": -1\n"
            + "        },\n"
            + "        \"number\": 9\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"sheep\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 2,\n"
            + "          \"y\": -1\n"
            + "        },\n"
            + "        \"number\": 12\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"ore\",\n"
            + "        \"location\": {\n"
            + "          \"x\": -2,\n"
            + "          \"y\": 0\n"
            + "        },\n"
            + "        \"number\": 5\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"sheep\",\n"
            + "        \"location\": {\n"
            + "          \"x\": -1,\n"
            + "          \"y\": 0\n"
            + "        },\n"
            + "        \"number\": 10\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"wheat\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 0,\n"
            + "          \"y\": 0\n"
            + "        },\n"
            + "        \"number\": 11\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"brick\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 1,\n"
            + "          \"y\": 0\n"
            + "        },\n"
            + "        \"number\": 5\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"wheat\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 2,\n"
            + "          \"y\": 0\n"
            + "        },\n"
            + "        \"number\": 6\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"wheat\",\n"
            + "        \"location\": {\n"
            + "          \"x\": -2,\n"
            + "          \"y\": 1\n"
            + "        },\n"
            + "        \"number\": 2\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"sheep\",\n"
            + "        \"location\": {\n"
            + "          \"x\": -1,\n"
            + "          \"y\": 1\n"
            + "        },\n"
            + "        \"number\": 9\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"wood\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 0,\n"
            + "          \"y\": 1\n"
            + "        },\n"
            + "        \"number\": 4\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"sheep\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 1,\n"
            + "          \"y\": 1\n"
            + "        },\n"
            + "        \"number\": 10\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"wood\",\n"
            + "        \"location\": {\n"
            + "          \"x\": -2,\n"
            + "          \"y\": 2\n"
            + "        },\n"
            + "        \"number\": 6\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"ore\",\n"
            + "        \"location\": {\n"
            + "          \"x\": -1,\n"
            + "          \"y\": 2\n"
            + "        },\n"
            + "        \"number\": 3\n"
            + "      },\n"
            + "      {\n"
            + "        \"resource\": \"wheat\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 0,\n"
            + "          \"y\": 2\n"
            + "        },\n"
            + "        \"number\": 8\n"
            + "      }\n"
            + "    ],\n"
            + "    \"roads\": [],\n"
            + "    \"cities\": [],\n"
            + "    \"settlements\": [],\n"
            + "    \"radius\": 3,\n"
            + "    \"ports\": [\n"
            + "      {\n"
            + "        \"ratio\": 3,\n"
            + "        \"direction\": \"SE\",\n"
            + "        \"location\": {\n"
            + "          \"x\": -3,\n"
            + "          \"y\": 0\n"
            + "        }\n"
            + "      },\n"
            + "      {\n"
            + "        \"ratio\": 2,\n"
            + "        \"resource\": \"brick\",\n"
            + "        \"direction\": \"NE\",\n"
            + "        \"location\": {\n"
            + "          \"x\": -2,\n"
            + "          \"y\": 3\n"
            + "        }\n"
            + "      },\n"
            + "      {\n"
            + "        \"ratio\": 2,\n"
            + "        \"resource\": \"wood\",\n"
            + "        \"direction\": \"NE\",\n"
            + "        \"location\": {\n"
            + "          \"x\": -3,\n"
            + "          \"y\": 2\n"
            + "        }\n"
            + "      },\n"
            + "      {\n"
            + "        \"ratio\": 2,\n"
            + "        \"resource\": \"wheat\",\n"
            + "        \"direction\": \"S\",\n"
            + "        \"location\": {\n"
            + "          \"x\": -1,\n"
            + "          \"y\": -2\n"
            + "        }\n"
            + "      },\n"
            + "      {\n"
            + "        \"ratio\": 3,\n"
            + "        \"direction\": \"N\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 0,\n"
            + "          \"y\": 3\n"
            + "        }\n"
            + "      },\n"
            + "      {\n"
            + "        \"ratio\": 2,\n"
            + "        \"resource\": \"sheep\",\n"
            + "        \"direction\": \"NW\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 3,\n"
            + "          \"y\": -1\n"
            + "        }\n"
            + "      },\n"
            + "      {\n"
            + "        \"ratio\": 3,\n"
            + "        \"direction\": \"SW\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 3,\n"
            + "          \"y\": -3\n"
            + "        }\n"
            + "      },\n"
            + "      {\n"
            + "        \"ratio\": 2,\n"
            + "        \"resource\": \"ore\",\n"
            + "        \"direction\": \"S\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 1,\n"
            + "          \"y\": -3\n"
            + "        }\n"
            + "      },\n"
            + "      {\n"
            + "        \"ratio\": 3,\n"
            + "        \"direction\": \"NW\",\n"
            + "        \"location\": {\n"
            + "          \"x\": 2,\n"
            + "          \"y\": 1\n"
            + "        }\n"
            + "      }\n"
            + "    ],\n"
            + "    \"robber\": {\n"
            + "      \"x\": 0,\n"
            + "      \"y\": -2\n"
            + "    }\n"
            + "  },\n"
            + "  \"players\": [\n"
            + "    {\n"
            + "      \"resources\": {\n"
            + "        \"brick\": 0,\n"
            + "        \"wood\": 0,\n"
            + "        \"sheep\": 0,\n"
            + "        \"wheat\": 0,\n"
            + "        \"ore\": 0\n"
            + "      },\n"
            + "      \"oldDevCards\": {\n"
            + "        \"yearOfPlenty\": 0,\n"
            + "        \"monopoly\": 0,\n"
            + "        \"soldier\": 0,\n"
            + "        \"roadBuilding\": 0,\n"
            + "        \"monument\": 0\n"
            + "      },\n"
            + "      \"newDevCards\": {\n"
            + "        \"yearOfPlenty\": 0,\n"
            + "        \"monopoly\": 0,\n"
            + "        \"soldier\": 0,\n"
            + "        \"roadBuilding\": 0,\n"
            + "        \"monument\": 0\n"
            + "      },\n"
            + "      \"roads\": 15,\n"
            + "      \"cities\": 4,\n"
            + "      \"settlements\": 5,\n"
            + "      \"soldiers\": 0,\n"
            + "      \"victoryPoints\": 0,\n"
            + "      \"monuments\": 0,\n"
            + "      \"playedDevCard\": false,\n"
            + "      \"discarded\": false,\n"
            + "      \"playerID\": 12,\n"
            + "      \"playerIndex\": 0,\n"
            + "      \"name\": \"first\",\n"
            + "      \"color\": \"red\"\n"
            + "    },\n"
            + "    null,\n"
            + "    null,\n"
            + "    null\n"
            + "  ],\n"
            + "  \"log\": {\n"
            + "    \"lines\": []\n"
            + "  },\n"
            + "  \"chat\": {\n"
            + "    \"lines\": []\n"
            + "  },\n"
            + "  \"bank\": {\n"
            + "    \"brick\": 24,\n"
            + "    \"wood\": 24,\n"
            + "    \"sheep\": 24,\n"
            + "    \"wheat\": 24,\n"
            + "    \"ore\": 24\n"
            + "  },\n"
            + "  \"turnTracker\": {\n"
            + "    \"status\": \"FirstRound\",\n"
            + "    \"currentTurn\": 0,\n"
            + "    \"longestRoad\": -1,\n"
            + "    \"largestArmy\": -1\n"
            + "  },\n"
            + "  \"winner\": -1,\n"
            + "  \"version\": 0\n"
            + "}";
    //</editor-fold>

    private final Gson model = new GsonBuilder().create();
    public static MockServerFascade fascade = null;

    public static MockServerFascade getSingleton() {
        if (fascade == null) {
            fascade = new MockServerFascade();
        }
        return fascade;
    }

    @Override
    public boolean login(User credentials) throws ServerException {
        return true;

    }

    @Override
    public boolean register(User credentials) throws ServerException {
        return true;
    }

    @Override
    public ArrayList<GameInfo> listGames() throws ServerException {
        ArrayList<GameInfo> games = new ArrayList<>();

        GameInfo game = new GameInfo("Default Game");

        //Canned Response
        game.setId(0);
        PlayerInfo player = new PlayerInfo();
        player.setColor("orange");
        player.setName("Sam");
        player.setId(0);
        game.getPlayers().add(player);
        player.setColor("blue");
        player.setName("Brooke");
        player.setId(1);
        game.getPlayers().add(player);
        player.setColor("red");
        player.setName("Pete");
        player.setId(10);
        game.getPlayers().add(player);
        player.setColor("green");
        player.setName("Mark");
        player.setId(11);
        game.getPlayers().add(player);

        games.add(game);

        return games;
    }

    @Override
    public GameInfo createGame(CreateGameRequest createGameRequest) throws ServerException {
        GameInfo game = new GameInfo("Ha");

        //Canned Response
        game.setId(3);

        return game;
    }

    @Override
    public boolean joinGame(JoinGameRequest joinGameRequest) throws ServerException {
        return true;
    }

    @Override
    public boolean saveGame(SaveGameRequest saveGameRequest) throws ServerException {
        return true;
    }

    @Override
    public boolean loadGame(LoadGameRequest loadGameRequest) throws ServerException {
        return true;
    }

    @Override
    public GameInfo getGameModel(String version) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo resetGame() throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public ArrayList getGameCommands() throws ServerException {
        return new ArrayList();
    }

    @Override
    public GameInfo executeGameCommands(ArrayList<Command> commands) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public boolean addAIToGame(AddAIRequest addAIRequest) throws ServerException {
        return true;
    }

    @Override
    public ArrayList<String> listAITypesInGame() throws ServerException {
        ArrayList<String> AITypes = new ArrayList<>();
        AITypes.add("LONGEST ROAD");
        return AITypes;
    }

    @Override
    public GameInfo sendChat(SendChat sendChat) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo rollNumber(RollNumber rollNumber) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo robPlayer(RobPlayer robPlayer) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo finishMove(FinishMove finishMove) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo buyDevCard(BuyDevCard buyDevCard) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo year_Of_Plenty(Year_Of_Plenty year_of_plenty) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo roadBuilding(Road_Building roadBuilding) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo soldier(Soldier soldier) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo monopoly(Monopoly monopoly) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo monument(Monument monument) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo offerTrade(OfferTrade offerTrade) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo acceptTrade(AcceptTrade acceptTrade) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo buildSettlement(BuildSettlement buildSettlement) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo buildCity(BuildCity buildCity) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo buildRoad(BuildRoad buildRoad) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo maritimeTrade(MaritimeTrade maritimeTrade) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }

    @Override
    public GameInfo discardCards(DiscardCards discardCards) throws ServerException {
        return ((GameInfo) model.fromJson(jsonGameString, GameInfo.class));
    }
}
