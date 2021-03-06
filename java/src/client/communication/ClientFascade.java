/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import client.main.ClientException;
import shared.data.User;
import shared.data.GameInfo;
import java.util.ArrayList;
import java.util.Map;
import shared.model.*;

/**
 *
 * @author ddennis
 */
public class ClientFascade implements Fascade {

    public static ClientFascade fascade = null;

    public static ClientFascade getSingleton() {
        if (fascade == null) {
            fascade = new ClientFascade();
        }
        return fascade;
    }

    /**
     * Client Communicator facade constructor
     */
    public ClientFascade() {

    }

    @Override
    public boolean login(User credentials) throws ClientException {
        return (ClientCommunicator
                .getSingleton()
                .doPost(LOGIN_USER, credentials, -1).getResponseBody()).equals("Success");

    }

    public boolean register(User credentials) throws ClientException {
        return (ClientCommunicator.getSingleton().doPost(REGISTER_USER, credentials, -1).getResponseBody()).equals("Success");
    }
    // Local Image Methods and Constants
    private static final String LOGIN_USER = "user/login";
    private static final String REGISTER_USER = "user/register";

    public ArrayList<GameInfo> listGames() throws ClientException {
        return (ArrayList<GameInfo>) ClientCommunicator.getSingleton().doGet(LIST_GAMES, "", -1).getResponseBody();
    }

    public GameInfo createGame(CreateGameRequest createGameRequest) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(CREATE_GAME, createGameRequest, -1).getResponseBody();
    }

    public boolean joinGame(JoinGameRequest joinGameRequest) throws ClientException {
        return ClientCommunicator.getSingleton().doPost(JOIN_GAME, joinGameRequest, 0).getResponseBody().equals("Success");
    }

    public boolean saveGame(SaveGameRequest saveGameRequest) throws ClientException {
        return ClientCommunicator.getSingleton().doPost(SAVE_GAME, saveGameRequest, -1).getResponseBody().equals("Success");
    }

    public boolean loadGame(LoadGameRequest loadGameRequest) throws ClientException {
        return ClientCommunicator.getSingleton().doPost(LOAD_GAME, loadGameRequest, -1).getResponseBody().equals("Success");
    }

    public GameInfo getGameModel(String version) throws ClientException {
        GameInfo result = (GameInfo) ClientCommunicator.getSingleton().doGet(MODEL_GAME, version, 0).getResponseBody();
        return result;
    }

    public GameInfo resetGame() throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(RESET_GAME, "", 0).getResponseBody();
    }

    public ArrayList getGameCommands() throws ClientException {
        return (ArrayList) ClientCommunicator.getSingleton().doGet(COMMANDS_GAME, "", 0).getResponseBody();
    }

    public GameInfo executeGameCommands(ArrayList<Command> commands) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(COMMANDS_GAME, commands, 0).getResponseBody();
    }

    public boolean addAIToGame(AddAIRequest addAIRequest) throws ClientException {
        return ClientCommunicator.getSingleton().doPost(ADD_AI_GAME, addAIRequest, 0).getResponseBody().equals("Success");
    }

    public ArrayList<String> listAITypesInGame() throws ClientException {
        return (ArrayList<String>) ClientCommunicator.getSingleton().doGet(LIST_AI_GAME, "", 0).getResponseBody();
    }
    // Local User Methods and Constants
    private static final String LIST_GAMES = "games/list";
    private static final String CREATE_GAME = "games/create";
    private static final String JOIN_GAME = "games/join";
    private static final String SAVE_GAME = "games/save";
    private static final String LOAD_GAME = "games/load";
    private static final String MODEL_GAME = "game/model";
    private static final String RESET_GAME = "game/reset";
    private static final String COMMANDS_GAME = "game/commands";
    private static final String ADD_AI_GAME = "game/addAI";
    private static final String LIST_AI_GAME = "game/listAI";

    @Override
    public GameInfo sendChat(SendChat sendChat) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(SEND_CHAT_MOVES, sendChat, 0).getResponseBody();
    }

    @Override
    public GameInfo rollNumber(RollNumber rollNumber) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(ROLL_MOVES, rollNumber, 0).getResponseBody();
    }

    @Override
    public GameInfo robPlayer(RobPlayer robPlayer) throws ClientException {
//    	System.out.println(ClientCommunicator.getSingleton().doPost(ROB_PLAYER_MOVES, robPlayer, 0).getResponseBody());
//    	return new GameInfo("Default");
        return (GameInfo) ClientCommunicator.getSingleton().doPost(ROB_PLAYER_MOVES, robPlayer, 0).getResponseBody();
    }

    public GameInfo finishMove(FinishMove finishMove) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(FINISH_TURN_MOVES, finishMove, 0).getResponseBody();
    }

    @Override
    public GameInfo buyDevCard(BuyDevCard buyDevCard) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(BUY_DEV_CARD_MOVES, buyDevCard, 0).getResponseBody();
    }

    @Override
    public GameInfo year_Of_Plenty(Year_Of_Plenty year_of_plenty) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(YEAR_OF_PLENTY_MOVES, year_of_plenty, 0).getResponseBody();
    }

    @Override
    public GameInfo roadBuilding(Road_Building roadBuilding) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(ROAD_BUILDING_MOVES, roadBuilding, 0).getResponseBody();
    }

    @Override
    public GameInfo soldier(Soldier soldier) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(SOLDIER_MOVES, soldier, 0).getResponseBody();
    }

    @Override
    public GameInfo monopoly(Monopoly monopoly) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(MONOPOLY_MOVES, monopoly, 0).getResponseBody();
    }

    @Override
    public GameInfo monument(Monument monument) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(MONUMENT_MOVES, monument, 0).getResponseBody();
    }

    @Override
    public GameInfo offerTrade(OfferTrade offerTrade) throws ClientException {
        GameInfo result = (GameInfo) ClientCommunicator.getSingleton().doPost(OFFER_TRADE_MOVES, offerTrade, 0).getResponseBody();
        return result;
    }

    @Override
    public GameInfo acceptTrade(AcceptTrade acceptTrade) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(ACCEPT_TRADE_MOVES, acceptTrade, 0).getResponseBody();
    }

    @Override
    public GameInfo buildSettlement(BuildSettlement buildSettlement) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(BUILD_SETTLEMENT_MOVES, buildSettlement, 0).getResponseBody();
    }

    @Override
    public GameInfo buildCity(BuildCity buildCity) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(BUILD_CITY_MOVES, buildCity, 0).getResponseBody();
    }

    @Override
    public GameInfo buildRoad(BuildRoad buildRoad) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(BUILD_ROAD_MOVES, buildRoad, 0).getResponseBody();
    }

    @Override
    public GameInfo maritimeTrade(MaritimeTrade maritimeTrade) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(MARITIME_TRADE_MOVES, maritimeTrade, 0).getResponseBody();
    }

    @Override
    public GameInfo discardCards(DiscardCards discardCards) throws ClientException {
        return (GameInfo) ClientCommunicator.getSingleton().doPost(DISCARD_CARDS_MOVES, discardCards, 0).getResponseBody();
    }
    private static final String SEND_CHAT_MOVES = "moves/sendChat";
    private static final String ROLL_MOVES = "moves/rollNumber";
    private static final String ROB_PLAYER_MOVES = "moves/robPlayer";
    private static final String FINISH_TURN_MOVES = "moves/finishTurn";
    private static final String BUY_DEV_CARD_MOVES = "moves/buyDevCard";
    private static final String YEAR_OF_PLENTY_MOVES = "moves/Year_of_Plenty";
    private static final String ROAD_BUILDING_MOVES = "moves/Road_Building";
    private static final String SOLDIER_MOVES = "moves/Soldier";
    private static final String MONOPOLY_MOVES = "moves/Monopoly";
    private static final String MONUMENT_MOVES = "moves/Monument";
    private static final String OFFER_TRADE_MOVES = "moves/offerTrade";
    private static final String ACCEPT_TRADE_MOVES = "moves/acceptTrade";
    private static final String BUILD_SETTLEMENT_MOVES = "moves/buildSettlement";
    private static final String BUILD_CITY_MOVES = "moves/buildCity";
    private static final String BUILD_ROAD_MOVES = "moves/buildRoad";
    private static final String MARITIME_TRADE_MOVES = "moves/maritimeTrade";
    private static final String DISCARD_CARDS_MOVES = "moves/discardCards";

    //not implemented for this phase
    //private static final String LOG_LEVEL_UTIL = "util/changeLogLevel";
    public Map<Integer, String> getCookies() {
        return ClientCommunicator.getSingleton().getCookies();
    }

}
