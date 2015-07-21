package shared.model;

import java.util.List;

import shared.data.Game;
import shared.data.User;
import client.managers.GameManager;

public class MockServer implements IServer {

    @Override
    public String login(User request) {
        String goodUsername = "Jamie";
        String goodPassword = "password";

        if (request.getUsername() == goodUsername && request.getPassword() == goodPassword) {
            return "success";
        }

        return "Failed to login - bad username or password.";
    }

    @Override
    public String Register(User request) {
        String usedUsername = "Jamie";

        if (!request.getUsername().equals(usedUsername) && request.getUsername().length() > 0) {
            return "success";
        }

        return "Failed to login - bad username or password.";
    }

    @Override
    public String ListGames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String CreateGame(CreateGameRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String JoinGame(JoinGameRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String SaveGame(SaveGameRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String LoadGame(LoadGameRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String GetModel(int version) {
        return this.UpdateMap();
    }

    @Override
    public String ResetGame() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String DoGameCommands(List<Command> commands) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String GetGameCommands() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String SendChat(shared.model.SendChat request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String RollNumber(shared.model.RollNumber request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String RobPlayer(shared.model.RobPlayer request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String FinishTurn(FinishMove request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String BuyDevCard(shared.model.BuyDevCard request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String PlayYearOfPlenty(Year_Of_Plenty request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String PlayRoadBuilding(Road_Building request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String PlaySoldier(Soldier request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String PlayMonopoly(Monopoly request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String PlayMonument(Monument request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String BuildRoad(shared.model.BuildRoad request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String BuildSettlement(shared.model.BuildSettlement request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String BuildCity(shared.model.BuildCity request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String OfferTrade(shared.model.OfferTrade request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String AcceptTrade(shared.model.AcceptTrade request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String MaritimeTrade(shared.model.MaritimeTrade request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String DiscardCards(shared.model.DiscardCards request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String ChangeLogLevel(ChangeLogLevelRequest request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String UpdateMap() {
        return "{\"deck\": {\"yearOfPlenty\": 2,    \"monopoly\": 2,    \"soldier\": 14,    \"roadBuilding\": 2,    \"monument\": 5  },"
                + "  \"map\": {    \"hexes\": [      {        \"location\": {          \"x\": 0,          \"y\": -2        }      },"
                + "      {        \"resource\": \"brick\",        \"location\": {          \"x\": 1,          \"y\": -2        },        \"number\": 4"
                + "      },      {        \"resource\": \"wood\",        \"location\": {          \"x\": 2,          \"y\": -2        },"
                + "        \"number\": 11      },      {        \"resource\": \"brick\",        \"location\": {          \"x\": -1,          \"y\": -1"
                + "        },        \"number\": 8      },      {        \"resource\": \"wood\",        \"location\": {          \"x\": 0,          \"y\": -1        },        \"number\": 3      },      {        \"resource\": \"ore\",        \"location\": {          \"x\": 1,          \"y\": -1        },        \"number\": 9      },      {        \"resource\": \"sheep\",        \"location\": {          \"x\": 2,          \"y\": -1        },        \"number\": 12      },      {        \"resource\": \"ore\",        \"location\": {          \"x\": -2,          \"y\": 0        },        \"number\": 5      },      {"
                + "        \"resource\": \"sheep\",        \"location\": {          \"x\": -1,          \"y\": 0        },        \"number\": 10      },      {        \"resource\": \"wheat\",        \"location\": {          \"x\": 0,          \"y\": 0        },        \"number\": 11      },      {        \"resource\": \"brick\",        \"location\": {          \"x\": 1,          \"y\": 0        },        \"number\": 5      },      {        \"resource\": \"wheat\",        \"location\": {          \"x\": 2,"
                + "          \"y\": 0        },        \"number\": 6      },      {        \"resource\": \"wheat\",        \"location\": {          \"x\": -2,"
                + "          \"y\": 1        },        \"number\": 2      },      {        \"resource\": \"sheep\",        \"location\": {          \"x\": -1,"
                + "          \"y\": 1        },        \"number\": 9      },      {        \"resource\": \"wood\",        \"location\": {          \"x\": 0,"
                + "          \"y\": 1        },        \"number\": 4      },      {        \"resource\": \"sheep\",        \"location\": {          \"x\": 1,"
                + "          \"y\": 1        },        \"number\": 10      },      {        \"resource\": \"wood\",        \"location\": {          \"x\": -2,"
                + "          \"y\": 2        },        \"number\": 6      },      {        \"resource\": \"ore\",        \"location\": {          \"x\": -1,"
                + "          \"y\": 2        },        \"number\": 3      },      {        \"resource\": \"wheat\",        \"location\": {          \"x\": 0,"
                + "          \"y\": 2        },        \"number\": 8      }    ],    \"roads\": [],    \"cities\": [],    \"settlements\": [],    \"radius\": 3,"
                + "    \"ports\": [      {        \"ratio\": 2,        \"resource\": \"ore\",        \"direction\": \"S\",        \"location\": {          \"x\": 1,"
                + "          \"y\": -3        }      },      {        \"ratio\": 2,        \"resource\": \"brick\",        \"direction\": \"NE\",        \"location\": {"
                + "          \"x\": -2,          \"y\": 3        }      },      {        \"ratio\": 3,        \"direction\": \"N\",        \"location\": {"
                + "          \"x\": 0,          \"y\": 3        }      },      {        \"ratio\": 2,        \"resource\": \"wheat\",        \"direction\": \"S\","
                + "        \"location\": {          \"x\": -1,          \"y\": -2        }      },      {        \"ratio\": 3,        \"direction\": \"SW\","
                + "        \"location\": {          \"x\": 3,          \"y\": -3        }      },      {        \"ratio\": 3,        \"direction\": \"NW\","
                + "        \"location\": {          \"x\": 2,          \"y\": 1        }      },      {        \"ratio\": 2,        \"resource\": \"wood\",        \"direction\": \"NE\","
                + "        \"location\": {          \"x\": -3,          \"y\": 2        }      },      {        \"ratio\": 3,        \"direction\": \"SE\","
                + "        \"location\": {          \"x\": -3,          \"y\": 0        }      },      {        \"ratio\": 2,        \"resource\": \"sheep\","
                + "        \"direction\": \"NW\",        \"location\": {          \"x\": 3,          \"y\": -1        }      }    ],    \"robber\": {"
                + "      \"x\": 0,      \"y\": -2    }  },  \"players\": [    {      \"resources\": {        \"brick\": 0,        \"wood\": 0,        \"sheep\": 0,"
                + "        \"wheat\": 0,        \"ore\": 0      },      \"oldDevCards\": {        \"yearOfPlenty\": 0,        \"monopoly\": 0,        \"soldier\": 0,"
                + "        \"roadBuilding\": 0,        \"monument\": 0      },      \"newDevCards\": {        \"yearOfPlenty\": 0,        \"monopoly\": 0,        \"soldier\": 0,        \"roadBuilding\": 0,"
                + "        \"monument\": 0      },      \"roads\": 15,      \"cities\": 4,      \"settlements\": 5,      \"soldiers\": 0,      \"victoryPoints\": 0,"
                + "      \"monuments\": 0,      \"playedDevCard\": false,      \"discarded\": false,      \"playerID\": 12,      \"playerIndex\": 0,"
                + "      \"name\": \"string\",      \"color\": \"red\"    },    null,    null,    null  ],  \"log\": {    \"lines\": []  },  \"chat\": {    \"lines\": []"
                + "  },  \"bank\": {    \"brick\": 24,    \"wood\": 24,    \"sheep\": 24,    \"wheat\": 24,    \"ore\": 24  },  \"turnTracker\": {    \"status\": \"FirstRound\","
                + "    \"currentTurn\": 0,    \"longestRoad\": -1,    \"largestArmy\": -1  },  \"winner\": -1,  \"version\": 0}";
    }

}
