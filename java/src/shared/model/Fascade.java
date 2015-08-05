/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import java.util.ArrayList;
import shared.data.*;

/**
 *
 * @author ddennis
 */
public interface Fascade {

    /**
     * Logs the player in
     *
     * @param request the player's credentials
     * @return a cookie
     * @throws java.lang.Exception
     * @pre the requests username and password are valid for login
     * @post returns a valid cookie for the user whose credentials were passed
     * in.
     */
    public boolean login(User request) throws Exception;

    /**
     * Registers a user for the app. Auto logs in.
     *
     * @param request the credentials for the user.
     * @return a session cookie.
     * @pre The username and password are valid AND the username is not already
     * in use.
     * @post returns a valid cookie for the user that just registered.
     */
    public boolean register(User request) throws Exception;

    /**
     * Gets a list of games
     *
     * @return a list of current games
     * @pre none
     * @post returns a list of GameData for all currently unfinished games.
     */
    public ArrayList<GameInfo> listGames() throws Exception;

    /**
     * Creates a new game
     *
     * @param request The data for the new game
     * @return the new game's data
     * @pre valid request is passed in.
     * @post the game is created and will show on the game list.
     */
    public GameInfo createGame(CreateGameRequest request) throws Exception;

    /**
     * Allows a player to join a specified game.
     *
     * @param request the request data needed
     *
     * @pre the request contains valid data
     * @post the player is joined to the game specified with the color
     * indicated.
     */
    public boolean joinGame(JoinGameRequest request) throws Exception;

    /**
     * Saves a game on the server.
     *
     * @param request contains the needed information for saving.
     * @pre the request has valid data
     * @post the game indicated is saved under the specified name.
     */
    public boolean saveGame(SaveGameRequest request) throws Exception;

    /**
     * Loads a game from a file
     *
     * @param request the request with load info
     * @pre the load request is valid AND the file to load exists on the server
     * @post the specified game is loaded and restored
     */
    public boolean loadGame(LoadGameRequest request) throws Exception;

    /**
     * Gets a specified version of the game model.
     *
     * @param version the version number you want to get
     * @return the game model
     * @throws java.lang.Exception
     * @pre version is >= 0 AND version <= the highest version number @post
     * result is the game model version that matches the version input.
     */
    public GameInfo getGameModel(String version) throws Exception;

    /**
     * Resets the game
     *
     * @return the game model after being reset.
     * @throws java.lang.Exception
     * @pre none
     * @post the result game model's newest version is the same as its version 0
     */
    public GameInfo resetGame() throws Exception;

    /**
     * Tells the server to do the specified commands.
     *
     * @param commands
     * @return
     * @throws java.lang.Exception
     * @pre the passed commands are all valid.
     * @post the server has done all the commands passed in and no others.
     */
    public GameInfo executeGameCommands(ArrayList<Command> commands) throws Exception;

    /**
     * Gets a list of all commands that can be issued the server.
     *
     * @return a list of commands
     * @throws java.lang.Exception
     * @pre none
     * @post returns a list of any and all commands
     */
    public ArrayList getGameCommands() throws Exception;

    /**
     * Adds AI player to game
     *
     * @param request
     * @return
     * @throws java.lang.Exception
     */
    public boolean addAIToGame(AddAIRequest request) throws Exception;

    /**
     * Lists the types of AI in the game
     *
     * @return
     * @throws java.lang.Exception
     */
    public ArrayList<String> listAITypesInGame() throws Exception;

    /**
     * Sends a message to the chat
     *
     * @param request the information for the message
     * @return the new game state
     * @throws java.lang.Exception
     * @pre the request is valid
     * @post The game model is updated with the new message AND the returned
     * model is the new game state.
     */
    public GameInfo sendChat(SendChat request) throws Exception;

    /**
     * Forces a player to roll a specified number
     *
     * @param request specifies which player rolls what number.
     * @return The game model
     * @throws java.lang.Exception
     * @pre the data in the request must be valid
     * @post the specified player rolls the indicated number AND it is their
     * turn AND the result is the game state after that roll is made.
     */
    public GameInfo rollNumber(RollNumber request) throws Exception;

    /**
     * Causes a player to rob another player
     *
     * @param request Specifies the details of the rob
     * @return the game state ofter the rob takes place
     * @throws java.lang.Exception
     * @pre The request must be valid AND the robbing player index != victim
     * index AND victim has a building on the hex
     * @post returns the game state of the same game, just after the rob takes
     * place.
     */
    public GameInfo robPlayer(RobPlayer request) throws Exception;

    /**
     * makes a player end their turn
     *
     * @param request contains the player who is ending their turn
     * @return the game state
     * @throws java.lang.Exception
     * @pre the request is valid AND the specified player is the current player
     * @post the specified player's turn is over AND returns the game state
     * immediately after it happens
     */
    public GameInfo finishMove(FinishMove request) throws Exception;

    /**
     * makes a player buy a dev card
     *
     * @param request the information about buying the card
     * @return the game state
     * @throws java.lang.Exception
     * @pre the request is valid AND the indicated player has the requisite
     * resource cards
     * @post the indicated player buys a dev card AND returns the game state
     * immediately afterwards
     */
    public GameInfo buyDevCard(BuyDevCard request) throws Exception;

    /**
     * makes a player play a YoP dev card
     *
     * @param request contains the information about who and what choices are
     * involved.
     * @return the game state
     * @throws java.lang.Exception
     * @pre The specified player has the dev card AND they didn't draw it this
     * turn AND they haven't played any other dev cards this turn AND it is
     * their turn AND the two chosen resources are different
     * @post The specifed player plays a YoP card and gets the resource cards of
     * the two chosen types AND returns the game state immediately afterwards.
     */
    public GameInfo year_Of_Plenty(Year_Of_Plenty request) throws Exception;

    /**
     * makes a player play a Road Building dev card
     *
     * @param request contains the information about who and where roads are put
     * @return the game state
     * @throws java.lang.Exception
     * @pre the specified player has the dev card AND they didn't draw it this
     * turn AND they haven't played any other dev cards this turn AND it is
     * their turn AND the edge locations are valid for building.
     * @post the specified player plays the card with the indicated choices AND
     * returns the game state immediately after.
     */
    public GameInfo roadBuilding(Road_Building request) throws Exception;

    /**
     * makes a player play a soldier card
     *
     * @param request contains the information about who and what choices are
     * made
     * @return the game state
     * @throws java.lang.Exception
     * @pre the specified player has the dev card AND they didn't draw it this
     * turn AND they haven't played any other dev cards this turn AND it is
     * their turn AND victim index != player index AND location didn't have the
     * robber previously.
     * @post the specified player plays the card with the indicated choices AND
     * returns the game state immediately after.
     */
    public GameInfo soldier(Soldier request) throws Exception;

    /**
     * makes a player play a monopoly card
     *
     * @param request contains the information about who and what choice was
     * made
     * @return the game state
     * @throws java.lang.Exception
     * @pre the specified player has the dev card AND they didn't draw it this
     * turn AND they haven't played any other dev cards this turn AND it is
     * their turn
     * @post the specified player plays the card with the indicated choice AND
     * returns the game state immediately after.
     */
    public GameInfo monopoly(Monopoly request) throws Exception;

    /**
     * makes a player play a monument card
     *
     * @param request contains the information about who played the card
     * @return the game state
     * @throws java.lang.Exception
     * @pre the specified player has the dev card AND they didn't draw it this
     * turn AND they haven't played any other dev cards this turn AND it is
     * their turn
     * @post the specified player plays the card AND returns the game state
     * immediately after.
     */
    public GameInfo monument(Monument request) throws Exception;

    /**
     * Causes a player to build a road
     *
     * @param request contains information about building the road
     * @return the game state
     * @throws java.lang.Exception
     * @pre the request is valid AND the indicated player has the requisite
     * resources IF not free AND they have a road piece to place AND the chosen
     * edge is valid for building AND it is their turn
     * @post The indicated play builds a road on the chosen spot AND lose the
     * requisite resources IF not free AND returns the game state immediately
     * after.
     */
    public GameInfo buildRoad(BuildRoad request) throws Exception;

    /**
     * Causes a player to build a settlement
     *
     * @param request contains information about building the settlement
     * @return the game state
     * @throws java.lang.Exception
     * @pre the request is valid AND the indicated player has the requisite
     * resources IF not free AND they have a settlement piece to place AND the
     * chosen location is valid for building AND it is their turn
     * @post The indicated play builds a settlement on the chosen location AND
     * lose the requisite resources IF not free AND returns the game state
     * immediately after.
     */
    public GameInfo buildSettlement(BuildSettlement request) throws Exception;

    /**
     * Causes a player to build a city
     *
     * @param request contains information about building the city
     * @return the game state
     * @throws java.lang.Exception
     * @pre the request is valid AND the indicated player has the requisite
     * resources AND they have a city piece to place AND they have a settlement
     * on the selected vertex AND it is their turn
     * @post The indicated play builds a city on the chosen vertex AND lose the
     * requisite resources AND returns the settlement that was there to their
     * supply AND returns the game state immediately after.
     */
    public GameInfo buildCity(BuildCity request) throws Exception;

    /**
     * causes a player to offer a trade
     *
     * @param request contains information about the trade offered
     * @return the game state
     * @throws java.lang.Exception
     * @pre the indicated player has the cards they offer AND it is their turn
     * AND the offer contains 1 or more cards AND the receiver isn't the sending
     * player AND it is the sender's turn.
     * @post the trade offer is extended AND the game state immediately after is
     * returned.
     */
    public GameInfo offerTrade(OfferTrade request) throws Exception;

    /**
     * causes a player to accept a trade
     *
     * @param request contains information about the acceptance
     * @return the game state
     * @throws java.lang.Exception
     * @pre the indicated player was offered a trade AND it isn't their turn
     * @post the indicated player either accepts or rejects depending on
     * willAccept AND the game state immediately after this is returned.
     */
    public GameInfo acceptTrade(AcceptTrade request) throws Exception;

    /**
     * causes a player to make a maritime trade
     *
     * @param request contains information about who and what choices are made.
     * @return the game state
     * @throws java.lang.Exception
     * @pre the indicated player has a port for the appropriate trading ratio
     * AND the have a sufficient number of input resource cards AND it is their
     * turn.
     * @post the trade is performed AND returns the game state immediately
     * afterward.
     */
    public GameInfo maritimeTrade(MaritimeTrade request) throws Exception;

    /**
     * causes a player to discard cards
     *
     * @param request contains information about who and which cards were
     * discarded
     * @return the game state
     * @throws java.lang.Exception
     * @pre the indicated player has >= 7 cards in their hand AND the cards to
     * be discarded total in number half the players hand size rounded down AND
     * all the discarded cards were cards the player had in their hand.
     * @post the player discards the cards AND the bank gains the discarded
     * cards AND returns the game state immediately after this action.
     */
    public GameInfo discardCards(DiscardCards request) throws Exception;

    /**
     * changes the server's log level for debugging purposes
     *
     * @param request says what to change the log level to
     * @return
     * @pre the request is valid
     * @post the server's log level is changes to what was specified.
     */
    //public GameInfo changeLogLevel(ChangeLogLevelRequest request);
}
