package client.managers;

import java.util.ArrayList;
import javax.smartcardio.Card;

public class GameManager {
	
	public GameManager () { 
            LocationManager locationManager = new LocationManager();
            
        
        }
	
	
	/**
	 * @author Curt
	 * @param startInfo = string of information necessary to start the game
	 * @pre gameID matches an existing game. Players are in the game
	 * @post GameManager will walk players through starting 2 turns and structure placement for gameInit
	 */
	public void initializeGame(int gameID, String startInfo) {

	}
	
	/**
	 * @author Curt
	 * @param gameID = unique ID of a game in the server's games list
	 * @pre gameID matches an existing game
	 * @post Game will be terminated. Players will be evicted from game if still inside.
	 */
	public void endGame(int gameID) {
		
	}
	
	/**
	 * @author Curt
	 * @pre The server is running. Method triggered by a request from a successfully logged in user.
	 * @post Game is added to the list of games on server
	 */
	public void createGame() {
		
	}

	
	/**
	 * @author Curt
	 * @pre Server is running
	 * @post If games exists, all existing games will be listed. Otherwise a message will display "No games found"
	 */
	public ArrayList<Game> listGames() {
		return new ArrayList<Game>();
	}
	
	/**
	 * @author Curt
	 * @param gameID = unique ID of a game in the server's games list
	 * @pre gameID matches an existing Game
	 * @post Player will join a game
	 */
	public void joinGame(int gameID) {
		
	}
	
	/**
	 * @author Curt
	 * @param gameID = unique ID of a game in the server's games list
	 * @pre gameID matches an existing Game
	 * @post returns boolean value for whether game has 4 players or not
	 */
	public boolean gameHasFourPlayers(int gameID) {
		
		return false;
	}
	
	/**
	 * @author Curt
	 * @param gameID = unique ID of a game in the server's games list
	 * @param edge = valid edge location where road should be placed
	 * @pre gameID matches existing game. Edge Locations is a valid location in this game's map
	 * @post adds a road to the requested edge in the game
	 */
	public void placeRoad(int gameID, EdgeLocation edge) {
		
	}
	
	/**
	 * @author Curt
	 * @param gameID = unique ID of a game in the server's games list
	 * @param vertex = vertex where the settlement should be placed
	 * @pre gameID matches existing game. Hex and VertexLocations are valid location in this game's map.
	 * @post 
	 */
	public void placeSettlement(int gameID, VertexLocation v) {
		
	}
	
	/**
	 * @author Curt
	 * @param gameID = unique ID of a game in the server's games list
	 * @pre gameId matches an existing game. It's the start of a player's turn
	 * @post diceRollEvents from the MapManager are triggered.
	 */
	public void rollDice(int gameID) {
		
	}
	
	/**
	 * @author Curt
	 * @param gameID = unique ID of a game in the server's games list
	 * @param card = requested card type
	 * @pre gameId matches an existing game. Card is a valid cardType
	 * @post Success = Cards will be transferred between current player bank and game bank.
	 */
	public boolean buy(int gameID, String card) {
		
		return false;
	}
	
	/**
	 * @author Curt
	 * @param gameID = unique ID of a game in the server's games list
	 * @param object = structure type
	 * @pre gameID matches an existing game. object matches a valid structure
	 * @post Success = Structure built, player victory point incremented, structure count decremented.
	 */
	public boolean build(int gameID, String object) {
	
		return false;
	}
	
	/**
	 * @author Curt
	 * @param gameID = unique ID of a game in the server's games list
	 * @param senderID = unique ID of the current player
	 * @param receiverID = unique ID of the trade recipient (game's bankID if maritime trade)
	 * @param offer = List of cards being offered by current player
	 * @param request = List of cards requested from the trade recipient
	 * @pre gameID is valid. Sender and receiver are valid. Sender has all cards in the offer available
	 * @post Failure = Receiver doesn't have the requested cards; Success = cards are transferred between the two player's banks.
	 */
	public boolean trade(int gameID, int senderID, int receiverID, ArrayList<Card> offer, ArrayList<Card> request) {
		
		return false;
	}
	
	/**
	 * @author Curt
	 * @param gameID = unique ID of a game in the server's games list
	 * @param card = card being used.
	 * @pre Card exists in the player's hand
	 * @post Card will be returned to game's bank (unless it's a development card. Then it will be moved to another part of the player's bank) Card effect will be carried out.
	 */
	public boolean useCard(int gameID, Card c){
		
		return false;
	}
	
	/**
	 * @author Curt
	 * @param gameID = unique ID of a game in the server's games list
	 * @pre gameID exists. Method only called after other turn-operations are all completed (No trades ongoing, cards being played, etc)
	 * @post The current player pointer will move to the next player. All cards in all player's hands will be marked as usable.
	 */
	public void endTurn(int gameID) {
		
	}
		
}
