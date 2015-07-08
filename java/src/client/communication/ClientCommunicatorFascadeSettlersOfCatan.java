/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import client.proxy.JoinGameRequest;
import client.proxy.LoadGameRequest;
import client.proxy.CreateGameRequest;
import client.proxy.SaveGameRequest;
import client.ClientException;
import client.data.Game;
import client.data.User;
import java.util.ArrayList;

/**
 *
 * @author ddennis
 */
public class ClientCommunicatorFascadeSettlersOfCatan {

    public static ClientCommunicatorFascadeSettlersOfCatan fascade = null;

    public static ClientCommunicatorFascadeSettlersOfCatan getSingleton() {
        if (fascade == null) {
            fascade = new ClientCommunicatorFascadeSettlersOfCatan();
        }
        return fascade;
    }

    /**
     * Client Communicator facade constructor
     */
    public ClientCommunicatorFascadeSettlersOfCatan() {

    }

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

    public ArrayList listGames() throws ClientException {
        return (ArrayList) ClientCommunicator.getSingleton().doGet(LIST_GAMES, "", -1).getResponseBody();
    }

    public Game createGame(CreateGameRequest createGameRequest) throws ClientException {
        return (Game) ClientCommunicator.getSingleton().doPost(CREATE_GAME, createGameRequest, -1).getResponseBody();
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
    
    public Game getGameModel(String version) throws ClientException {
        return (Game) ClientCommunicator.getSingleton().doGet(MODEL_GAME, version, 0).getResponseBody();
    }
    public Game resetGame() throws ClientException {
        return (Game) ClientCommunicator.getSingleton().doPost(RESET_GAME, "", 0).getResponseBody();
    }
    public Game getGameCommands() throws ClientException {
        return (Game) ClientCommunicator.getSingleton().doGet(COMMANDS_GAME, "", 0).getResponseBody();
    }
    public Game executeGameCommand() throws ClientException {
        return (Game) ClientCommunicator.getSingleton().doPost(COMMANDS_GAME, "", 0).getResponseBody();
    }
    public Game addAIToGame() throws ClientException {
        return (Game) ClientCommunicator.getSingleton().doPost(ADD_AI_GAME, "", 0).getResponseBody();
    }
    public Game listAITypesInGame() throws ClientException {
        return (Game) ClientCommunicator.getSingleton().doGet(LIST_AI_GAME, "", 0).getResponseBody();
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

    private static final String SEND_CHAT_MOVES = "moves/sendChat";
    private static final String ROLL_MOVES = "moves/rollNumber";
    private static final String ROB_PLAYER_MOVES = "moves/robPlayer";
    private static final String FINISH_TURN_MOVES = "moves/rollNumber";
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

    private static final String LOG_LEVEL_UTIL = "util/changeLogLevel";

}
