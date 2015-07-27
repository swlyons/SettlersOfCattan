/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.main;

import client.communication.CookieModel;
import java.net.URLDecoder;
import java.util.ArrayList;
import server.command.*;
import shared.data.*;
import shared.model.*;
import server.receiver.AllOfOurInformation;

/**
 * For Implementation See:
 * https://students.cs.byu.edu/~cs340ta/summer2015/group_project/Web_API_Documentation.pdf
 *
 * @author ddennis
 *
 * Each method will need to use the Command Design Pattern. It should create an
 * appropriate Command object for the request, and call execute on it. The
 * command object will actually execute the logic for the operation.
 *
 */
public class ServerFascade implements Fascade {
    private final Agent agent = new Agent();
    
    public static ServerFascade fascade = null;

    public static ServerFascade getSingleton() {
        if (fascade == null) {
            fascade = new ServerFascade();
        }
        return fascade;
    }
    
    @Override
    public boolean login(User credentials) throws ServerException {
        return false;
    }
    
    public int getLoginId(User credentials){
        int id=-1;
        for (int i=0;i<AllOfOurInformation.getSingleton().getUsers().size();i++) {
            if (credentials.getUsername().equals(AllOfOurInformation.getSingleton().getUsers().get(i).getUsername()) && credentials.getPassword().equals(AllOfOurInformation.getSingleton().getUsers().get(i).getPassword())) {
                id = i;
            }
        }
        return id;
    }
    
    @Override
    public boolean register(User credentials) throws ServerException {
        //Command Pattern
        RegisterCommand registerCommand = new RegisterCommand(credentials);
        return agent.sendCommand(registerCommand);
    }

    @Override
    public ArrayList<GameInfo> listGames() throws ServerException {
        ArrayList<GameInfo> games = new ArrayList<GameInfo>();
        for(int i=0;i<AllOfOurInformation.getSingleton().getGames().size();i++){
            games.add(AllOfOurInformation.getSingleton().getGames().get(i).getGame());
        }        
        return games;
    }

    @Override
    public GameInfo createGame(CreateGameRequest createGameRequest) throws ServerException {
        CreateGameCommand createGame = new CreateGameCommand(createGameRequest.isRandomTiles(),createGameRequest.isRandomNumbers(),createGameRequest.isRandomPorts(),createGameRequest.getName());
        GameInfo gi = null;
        if(agent.sendCommand(createGame)){
            gi = AllOfOurInformation.getSingleton().getGames().get(createGame.getGameId()).getGame();
        }
        return gi;
    }

    @Override
    public boolean joinGame(JoinGameRequest joinGameRequest) throws ServerException {
        return false;
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
        return null;
    }

    @Override
    public GameInfo resetGame() throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public ArrayList getGameCommands() throws ServerException {
        return new ArrayList();
    }

    @Override
    public GameInfo executeGameCommands(ArrayList<shared.model.Command> commands) throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public boolean addAIToGame(AddAIRequest addAIRequest) throws ServerException {
        return true;
    }

    @Override
    public ArrayList<String> listAITypesInGame() throws ServerException {
        return new ArrayList<String>();
    }

    @Override
    public GameInfo sendChat(SendChat sendChat) throws ServerException {
        int playerIndex = sendChat.getPlayerIndex();
        String content = sendChat.getContent().split(";")[0];
        int gameId = Integer.parseInt(sendChat.getContent().split(";")[1]);
        
        SendChatCommand sendChatCommand = new SendChatCommand(playerIndex, content, gameId);
        
        GameInfo gameInformation = null;
        if(agent.sendCommand(sendChatCommand)){
            gameInformation = AllOfOurInformation.getSingleton().getGames().get(sendChatCommand.getGameId()).getGame();
        }
        return gameInformation;
    }

    @Override
    public GameInfo rollNumber(RollNumber rollNumber) throws ServerException {
        RollNumberCommand rollNumberCommand = new RollNumberCommand(rollNumber);
        GameInfo gi = null;
        if(agent.sendCommand(rollNumberCommand)){
            gi = AllOfOurInformation.getSingleton().getGames().get(rollNumber.getGameId()).getGame();
        }
        return gi;
    }

    @Override
    public GameInfo robPlayer(RobPlayer robPlayer) throws ServerException {
        RobPlayerCommand robPlayerCommand = new RobPlayerCommand(robPlayer);
        GameInfo gi = null;
        if(agent.sendCommand(robPlayerCommand)){
            gi = AllOfOurInformation.getSingleton().getGames().get(robPlayer.getGameId()).getGame();
        }
        return gi;
    }

    public GameInfo finishMove(FinishMove finishMove) throws ServerException {
        FinishTurnCommand finishMoveCommand = new FinishTurnCommand(finishMove);
        GameInfo gi = null;
        if(agent.sendCommand(finishMoveCommand)){
            gi = AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame();
        }
        return gi;
    }

    @Override
    public GameInfo buyDevCard(BuyDevCard buyDevCard) throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo year_Of_Plenty(Year_Of_Plenty year_of_plenty) throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo roadBuilding(Road_Building roadBuilding) throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo soldier(Soldier soldier) throws ServerException {
        SoldierCommand soldierCommand = new SoldierCommand(soldier);
        GameInfo gi = null;
        if(agent.sendCommand(soldierCommand)){
            gi = AllOfOurInformation.getSingleton().getGames().get(soldier.getGameId()).getGame();
        }
        return gi;

    }

    @Override
    public GameInfo monopoly(Monopoly monopoly) throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo monument(Monument monument) throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo offerTrade(OfferTrade offerTrade) throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo acceptTrade(AcceptTrade acceptTrade) throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo buildSettlement(BuildSettlement buildSettlement) throws ServerException {
        BuildSettlementCommand buildSettlementCommand = new BuildSettlementCommand(buildSettlement);
        GameInfo gi = null;
        if(agent.sendCommand(buildSettlementCommand)){
            gi = AllOfOurInformation.getSingleton().getGames().get(buildSettlement.getGameId()).getGame();
        }
        return gi;
    }

    @Override
    public GameInfo buildCity(BuildCity buildCity) throws ServerException {
        BuildCityCommand buildCityCommand = new BuildCityCommand(buildCity);
        GameInfo gi = null;
        if(agent.sendCommand(buildCityCommand)){
            gi = AllOfOurInformation.getSingleton().getGames().get(buildCity.getGameId()).getGame();
        }
        return gi;
    }

    @Override
    public GameInfo buildRoad(BuildRoad buildRoad) throws ServerException {
        BuildRoadCommand buildRoadCommand = new BuildRoadCommand(buildRoad);
        GameInfo gi = null;
        if(agent.sendCommand(buildRoadCommand)){
            gi = AllOfOurInformation.getSingleton().getGames().get(buildRoad.getGameId()).getGame();
        }
        return gi;
    }

    @Override
    public GameInfo maritimeTrade(MaritimeTrade maritimeTrade) throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo discardCards(DiscardCards discardCards) throws ServerException {
        DiscardCardsCommand discardCardsCommand = new DiscardCardsCommand(discardCards);
        GameInfo gi = null;
        if(agent.sendCommand(discardCardsCommand)){
            gi = AllOfOurInformation.getSingleton().getGames().get(discardCards.getGameId()).getGame();
        }
        return gi;

    }

}
