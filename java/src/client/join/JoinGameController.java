package client.join;

import shared.definitions.CatanColor;
import client.base.*;
import client.data.*;
import client.misc.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController {

	private INewGameView newGameView;
	private ISelectColorView selectColorView;
	private IMessageView messageView;
	private IAction joinAction;
	private ArrayList<GameInfo> games = new ArrayList<>();
        int currentGameId = 0;
	/**
	 * JoinGameController constructor
	 * 
	 * @param view Join game view
	 * @param newGameView New game view
	 * @param selectColorView Select color view
	 * @param messageView Message view (used to display error messages that occur while the user is joining a game)
	 */
	public JoinGameController(IJoinGameView view, INewGameView newGameView, 
								ISelectColorView selectColorView, IMessageView messageView) {

		super(view);

		setNewGameView(newGameView);
		setSelectColorView(selectColorView);
		setMessageView(messageView);
	}
	
	public IJoinGameView getJoinGameView() {
		
		return (IJoinGameView)super.getView();
	}
	
	/**
	 * Returns the action to be executed when the user joins a game
	 * 
	 * @return The action to be executed when the user joins a game
	 */
	public IAction getJoinAction() {
		
		return joinAction;
	}

	/**
	 * Sets the action to be executed when the user joins a game
	 * 
	 * @param value The action to be executed when the user joins a game
	 */
	public void setJoinAction(IAction value) {	
		
		joinAction = value;
	}
	
	public INewGameView getNewGameView() {
		
		return newGameView;
	}

	public void setNewGameView(INewGameView newGameView) {
		
		this.newGameView = newGameView;
	}
	
	public ISelectColorView getSelectColorView() {
		
		return selectColorView;
	}
	public void setSelectColorView(ISelectColorView selectColorView) {
		
		this.selectColorView = selectColorView;
	}
	
	public IMessageView getMessageView() {
		
		return messageView;
	}
	public void setMessageView(IMessageView messageView) {
		
		this.messageView = messageView;
	}

	@Override
	public void start() {
		
		getJoinGameView().showModal();
	}

	@Override
	public void startCreateNewGame() {
		
		getNewGameView().showModal();
	}

	@Override
	public void cancelCreateNewGame() {
		
		getNewGameView().closeModal();
	}

	@Override
	public void createNewGame() {
		//add player to list of existing game
                PlayerInfo player = new PlayerInfo();
                player.setName("");
                player.setPlayerIndex(0);
                Random rand = new Random();
                //need a random number for the player index (unique)
                player.setId(rand.nextInt());
                
                //set GameInfo for new Game
                GameInfo game = new GameInfo();
                game.addPlayer(player);
                game.setTitle(getNewGameView().getTitle());
                game.setId(currentGameId);
                
                //clear all the New Game View options
                getNewGameView().setTitle("");
                getNewGameView().setRandomlyPlaceHexes(false);
                getNewGameView().setRandomlyPlaceNumbers(false);
                getNewGameView().setUseRandomPorts(false);
                
                //add game to list of games
                games.add(currentGameId, game);
                
                
                //update the list of games in the JoinGame View 
                GameInfo[] newGames = new GameInfo[games.size()];
                newGames = games.toArray(newGames);
                getJoinGameView().setGames(newGames, player);
                
		getNewGameView().closeModal();
                
                //increment the game id
                currentGameId++;
	}

	@Override
	public void startJoinGame(GameInfo game) {

		getSelectColorView().showModal();
	}

	@Override
	public void cancelJoinGame() {
	
		getJoinGameView().closeModal();
	}

	@Override
	public void joinGame(CatanColor color) {
		
		// If join succeeded
		getSelectColorView().closeModal();
		getJoinGameView().closeModal();
		joinAction.execute();
	}

}

