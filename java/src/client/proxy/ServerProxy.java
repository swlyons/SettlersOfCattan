package client.proxy;

import java.util.List;

import client.data.Game;
import client.data.User;

public class ServerProxy implements IServer {

	private static IServer singleton = null;
	public static IServer getSingleton() {
		if (singleton == null) {
			singleton = new ServerProxy();
		}
		return singleton;
	}
	
	private ServerProxy() {
		
	}
	
	/**
	 * Logs the player in
	 * @param request the player's credentials
	 * @return a cookie
	 * @pre the requests username and password are valid for login
	 * @post returns a valid cookie for the user whose credentials were passed in.
	 */
	public String login(User request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Registers a user for the app. Auto logs in.
	 * @param request the credentials for the user.
	 * @return a session cookie.
	 * @pre The username and password are valid AND the username is not already in use.
	 * @post returns a valid cookie for the user that just registered.
	 */
	public String Register(User request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Gets a list of games
	 * @return a list of current games
	 * @pre none
	 * @post returns a list of GameData for all currently unfinished games.
	 */
	public List<GameData> ListGames() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Creates a new game
	 * @param request The data for the new game
	 * @return the new game's data
	 * @pre valid request is passed in.
	 * @post the game is created and will show on the game list.
	 */
	public GameData CreateGame(CreateGameRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Allows a player to join a specified game.
	 * @param request the request data needed
	 * @pre the request contains valid data
	 * @post the player is joined to the game specified with the color indicated.
	 */
	public void JoinGame(JoinGameRequest request) {
		// TODO Auto-generated method stub

	}

	/**
	 * Saves a game on the server.
	 * @param request contains the needed information for saving.
	 * @pre the request has valid data
	 * @post the game indicated is saved under the specified name.
	 */
	public void SaveGame(SaveGameRequest request) {
		// TODO Auto-generated method stub

	}

	/**
	 * Loads a game from a file
	 * @param request the request with load info
	 * @pre the load request is valid AND the file to load exists on the server
	 * @post the specified game is loaded and restored
	 */
	public void LoadGame(LoadGameRequest request) {
		// TODO Auto-generated method stub

	}

	/**
	 * Gets a specified version of the game model.
	 * @param version the version number you want to get
	 * @return the game model
	 * @pre version is >= 0 AND version <= the highest version number
	 * @post result is the game model version that matches the version input.
	 */
	public Game GetModel(int version) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Resets the game
	 * @return the game model after being reset.
	 * @pre none
	 * @post the result game model's newest version is the same as its version 0
	 */
	public Game ResetGame() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Tells the server to do the specified commands.
	 * @pre the passed commands are all valid.
	 * @post the server has done all the commands passed in and no others.
	 */
	public void DoGameCommands(List<Command> commands) {
		// TODO Auto-generated method stub

	}

	/**
	 * Gets a list of all commands that can be issued the server.
	 * @return a list of commands
	 * @pre none
	 * @post returns a list of any and all commands
	 */
	public List<Command> GetGameCommands() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Sends a message to the chat
	 * @param request the information for the message
	 * @return the new game state
	 * @pre the request is valid
	 * @post The game model is updated with the new message AND the returned model is the new game state.
	 */
	public Game SendChat(client.proxy.SendChat request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Forces a player to roll a specified number
	 * @param request specifies which player rolls what number.
	 * @return The game model
	 * @pre the data in the request must be valid
	 * @post the specified player rolls the indicated number AND it is their turn AND the result is the game state after that roll is made.
	 */
	public Game RollNumber(client.proxy.RollNumber request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * makes a player end their turn
	 * @param request contains the player who is ending their turn
	 * @return the game state
	 * @pre the request is valid AND the specified player is the current player
	 * @post the specified player's turn is over AND returns the game state immediately after it happens
	 */
	public Game RobPlayer(client.proxy.RobPlayer request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * makes a player end their turn
	 * @param request contains the player who is ending their turn
	 * @return the game state
	 * @pre the request is valid AND the specified player is the current player
	 * @post the specified player's turn is over AND returns the game state immediately after it happens
	 */
	public Game FinishTurn(FinishMove request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * makes a player buy a dev card
	 * @param request the information about buying the card
	 * @return the game state
	 * @pre the request is valid AND the indicated player has the requisite resource cards
	 * @post the indicated player buys a dev card AND returns the game state immediately afterwards
	 */
	public Game BuyDevCard(client.proxy.BuyDevCard request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * makes a player play a YoP dev card
	 * @param request contains the information about who and what choices are involved.
	 * @return the game state
	 * @pre The specified player has the dev card AND they didn't draw it this turn AND they haven't played any other dev cards this turn AND it is their turn AND the two chosen resources are different
	 * @post The specifed player plays a YoP card and gets the resource cards of the two chosen types AND returns the game state immediately afterwards.
	 */
	public Game PlayYearOfPlenty(Year_Of_Plenty request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * makes a player play a Road Building dev card
	 * @param request contains the information about who and where roads are put
	 * @return the game state
	 * @pre the specified player has the dev card AND they didn't draw it this turn AND they haven't played any other dev cards this turn AND it is their turn AND the edge locations are valid for building.
	 * @post the specified player plays the card with the indicated choices AND returns the game state immediately after.
	 */
	public Game PlayRoadBuilding(Road_Building request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * makes a player play a soldier card
	 * @param request contains the information about who and what choices are made
	 * @return the game state
	 * @pre the specified player has the dev card AND they didn't draw it this turn AND they haven't played any other dev cards this turn AND it is their turn AND victim index != player index AND location didn't have the robber previously.
	 * @post the specified player plays the card with the indicated choices AND returns the game state immediately after.
	 */
	public Game PlaySoldier(Soldier request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * makes a player play a monopoly card
	 * @param request contains the information about who and what choice was made
	 * @return the game state
	 * @pre the specified player has the dev card AND they didn't draw it this turn AND they haven't played any other dev cards this turn AND it is their turn
	 * @post the specified player plays the card with the indicated choice AND returns the game state immediately after.
	 */
	public Game PlayMonopoly(Monopoly request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * makes a player play a monument card
	 * @param request contains the information about who played the card
	 * @return the game state
	 * @pre the specified player has the dev card AND they didn't draw it this turn AND they haven't played any other dev cards this turn AND it is their turn
	 * @post the specified player plays the card AND returns the game state immediately after.
	 */
	public Game PlayMonument(Monument request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Causes a player to build a road
	 * @param request contains information about building the road
	 * @return the game state
	 * @pre the request is valid AND the indicated player has the requisite resources IF not free AND they have a road piece to place AND the chosen edge is valid for building AND it is their turn
	 * @post The indicated play builds a road on the chosen spot AND lose the requisite resources IF not free AND returns the game state immediately after.
	 */
	public Game BuildRoad(client.proxy.BuildRoad request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Causes a player to build a settlement
	 * @param request contains information about building the settlement
	 * @return the game state
	 * @pre the request is valid AND the indicated player has the requisite resources IF not free AND they have a settlement piece to place AND the chosen location is valid for building AND it is their turn
	 * @post The indicated play builds a settlement on the chosen location AND lose the requisite resources IF not free AND returns the game state immediately after.
	 */
	public Game BuildSettlement(client.proxy.BuildSettlement request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Causes a player to build a city
	 * @param request contains information about building the city
	 * @return the game state
	 * @pre the request is valid AND the indicated player has the requisite resources AND they have a city piece to place AND they have a settlement on the selected vertex AND it is their turn
	 * @post The indicated play builds a city on the chosen vertex AND lose the requisite resources AND returns the settlement that was there to their supply AND returns the game state immediately after.
	 */
	public Game BuildCity(client.proxy.BuildCity request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * causes a player to offer a trade
	 * @param request contains information about the trade offered
	 * @return the game state
	 * @pre the indicated player has the cards they offer AND it is their turn AND the offer contains 1 or more cards AND the receiver isn't the sending player AND it is the sender's turn.
	 * @post the trade offer is extended AND the game state immediately after is returned.
	 */
	public Game OfferTrade(client.proxy.OfferTrade request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * causes a player to accept a trade
	 * @param request contains information about the acceptance
	 * @return the game state
	 * @pre the indicated player was offered a trade AND it isn't their turn
	 * @post the indicated player either accepts or rejects depending on willAccept AND the game state immediately after this is returned.
	 */
	public Game AcceptTrade(client.proxy.AcceptTrade request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * causes a player to make a maritime trade
	 * @param request contains information about who and what choices are made.
	 * @return the game state
	 * @pre the indicated player has a port for the appropriate trading ratio AND the have a sufficient number of input resource cards AND it is their turn.
	 * @post the trade is performed AND returns the game state immediately afterward.
	 */
	public Game MaritimeTrade(client.proxy.MaritimeTrade request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * causes a player to discard cards
	 * @param request contains information about who and which cards were discarded
	 * @return the game state
	 * @pre the indicated player has >= 7 cards in their hand AND the cards to be discarded total in number half the players hand size rounded down AND all the discarded cards were cards the player had in their hand.
	 * @post the player discards the cards AND the bank gains the discarded cards AND returns the game state immediately after this action. 
	 */
	public Game DiscardCards(client.proxy.DiscardCards request) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * changes the server's log level for debugging purposes
	 * @param request says what to change the log level to
	 * @pre the request is valid
	 * @post the server's log level is changes to what was specified.
	 */
	public void ChangeLogLevel(ChangeLogLevelRequest request) {
		// TODO Auto-generated method stub

	}

	/**
	 * Called by the poller
	 * @return the current game state
	 * @pre none
	 * @post returns the current state of the current game
	 */
	public Game UpdateMap() {
		// TODO Auto-generated method stub
		return null;
	}

}
