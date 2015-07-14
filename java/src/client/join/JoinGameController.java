package client.join;

import client.ClientException;
import shared.definitions.CatanColor;
import client.base.*;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.communication.CookieModel;
import client.data.*;
import client.misc.*;
import client.proxy.CreateGameRequest;
import client.proxy.JoinGameRequest;
import com.google.gson.GsonBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation for the join game controller
 */
public class JoinGameController extends Controller implements IJoinGameController {

    private INewGameView newGameView;
    private ISelectColorView selectColorView;
    private IMessageView messageView;
    private IPlayerWaitingView playerWaitingView;
    private IAction joinAction;
    private ArrayList<GameInfo> games = new ArrayList<>();

    int currentGameId = 0;

    /**
     * JoinGameController constructor
     *
     * @param view Join game view
     * @param newGameView New game view
     * @param selectColorView Select color view
     * @param messageView Message view (used to display error messages that
     * occur while the user is joining a game)
     */
    public JoinGameController(IJoinGameView view, INewGameView newGameView,
            ISelectColorView selectColorView, IMessageView messageView) {

        super(view);

        setNewGameView(newGameView);
        setSelectColorView(selectColorView);
        setMessageView(messageView);
    }

    public IJoinGameView getJoinGameView() {

        return (IJoinGameView) super.getView();
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

    public IPlayerWaitingView getPlayerWaitingView() {
        return playerWaitingView;
    }

    public void setPlayerWaitingView(IPlayerWaitingView playerWaitingView) {
        this.playerWaitingView = playerWaitingView;
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
        
        try {
            //add player to list of existing game
            PlayerInfo player = new PlayerInfo();
            ClientCommunicatorFascadeSettlersOfCatan client = new ClientCommunicatorFascadeSettlersOfCatan();
            
            String cookie = "";
            for (java.util.Map.Entry<Integer, String> playerCookie : client.getCookies().entrySet()) {
                cookie = playerCookie.getValue();
            }
            player.setName(new GsonBuilder().create().fromJson(URLDecoder.decode(cookie.split("=")[1], "UTF-8"), CookieModel.class).getName());
            player.setPlayerIndex(0);
            Random rand = new Random();

            //need a random number for the player index (unique..period)
            player.setId(rand.nextInt());
            
            client.createGame(new CreateGameRequest(getNewGameView().getRandomlyPlaceHexes(), getNewGameView().getRandomlyPlaceNumbers(),getNewGameView().getUseRandomPorts(),new GsonBuilder().create().fromJson(URLDecoder.decode(cookie.split("=")[1], "UTF-8"), CookieModel.class).getName()));
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
        } catch (ClientException ex) {
            Logger.getLogger(JoinGameController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(JoinGameController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        ClientCommunicatorFascadeSettlersOfCatan client = new ClientCommunicatorFascadeSettlersOfCatan();
        
        try {
            if (client.joinGame(new JoinGameRequest(0, color.name().toLowerCase()))) {
                String cookie = "";
                for (java.util.Map.Entry<Integer, String> playerCookie : client.getCookies().entrySet()) {
                    cookie = playerCookie.getValue();
                }
                System.out.println(cookie);
            }
            else{
                System.out.println("Failed");
            }
            //get the current GameID
        } catch (ClientException ex) {
            Logger.getLogger(JoinGameController.class.getName()).log(Level.SEVERE, null, ex);
        }

        getSelectColorView().closeModal();
        getJoinGameView().closeModal();
        joinAction.execute();
    }

}
