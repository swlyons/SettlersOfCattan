/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.communication;

import client.ClientException;
import client.data.User;

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
                .doPost(LOGIN_USER,credentials).getResponseBody()).equals("Success");
        
    }
    public boolean register(User credentials) throws ClientException {
        return (ClientCommunicator
                .getSingleton()
                .doPost(REGISTER_USER,credentials).getResponseBody()).equals("Success");
    }
    // Local Image Methods and Constants
    private static final String LOGIN_USER = "user/login";
    private static final String REGISTER_USER = "/user/register";


    // Local User Methods and Constants
    private static final String LIST_GAMES = "games/list";
    private static final String CREATE_GAME = "games/create";
    private static final String JOIN_GAME = "games/join";
    private static final String SAVE_GAME = "games/save";
    private static final String LOAD_GAME = "games/load";
    private static final String MODEL_GAME = "games/model";
    private static final String RESET_GAME = "games/reset";
    private static final String COMMANDS_GAME = "games/commands";
    private static final String ADD_AI_GAME = "games/addAI";
    private static final String LIST_AI_GAME = "games/listAI";
    
    
    
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
