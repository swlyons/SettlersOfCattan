/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.main;

import java.util.ArrayList;
import shared.data.GameInfo;
import shared.data.User;
import shared.model.*;

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
        return new ArrayList<GameInfo>();
    }

    @Override
    public GameInfo createGame(CreateGameRequest createGameRequest) throws ServerException {
        return new GameInfo("Default Game");
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
        return new GameInfo("Default Game");
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
    public GameInfo executeGameCommands(ArrayList<Command> commands) throws ServerException {
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
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo rollNumber(RollNumber rollNumber) throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo robPlayer(RobPlayer robPlayer) throws ServerException {
        return new GameInfo("Default Game");
    }

    public GameInfo finishMove(FinishMove finishMove) throws ServerException {
        return new GameInfo("Default Game");
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
        return new GameInfo("Default Game");
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
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo buildCity(BuildCity buildCity) throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo buildRoad(BuildRoad buildRoad) throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo maritimeTrade(MaritimeTrade maritimeTrade) throws ServerException {
        return new GameInfo("Default Game");
    }

    @Override
    public GameInfo discardCards(DiscardCards discardCards) throws ServerException {
        return new GameInfo("Default Game");
    }

}
