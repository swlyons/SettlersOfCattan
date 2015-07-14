package client.join;

import client.ClientException;
import shared.definitions.CatanColor;
import client.base.*;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.communication.CookieModel;
import client.data.*;
import client.proxy.CreateGameRequest;
import client.proxy.JoinGameRequest;
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
    private IAction joinAction;

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

    @Override
    public void start() {
        try {
            ArrayList<GameInfo> gamesOnServer = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().listGames();
            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.setId(ClientCommunicator.getSingleton().getPlayerId());
            playerInfo.setName(ClientCommunicator.getSingleton().getName());
            for (GameInfo gameInfo : gamesOnServer) {
                for (int i = 0; i < gameInfo.getPlayers().size(); i++) {
                    if (gameInfo.getPlayers().get(i).getId() == -1) {
                        gameInfo.getPlayers().remove(i);
                        i--;
                    }
                }
            }
            GameInfo[] allGames = new GameInfo[gamesOnServer.size()];
            gamesOnServer.toArray(allGames);
            getJoinGameView().setGames(allGames, playerInfo);
        } catch (Exception e) {
        } finally {
            getJoinGameView().showModal();
        }
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
        CreateGameRequest gameRequest = new CreateGameRequest(getNewGameView().getRandomlyPlaceHexes(), getNewGameView().getRandomlyPlaceNumbers(), getNewGameView().getUseRandomPorts(), getNewGameView().getTitle());

        try {
            GameInfo gameInfo = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().createGame(gameRequest);
            JoinGameRequest joinRequest = new JoinGameRequest(gameInfo.getId(), CatanColor.WHITE.toString().toLowerCase());
            ClientCommunicatorFascadeSettlersOfCatan.getSingleton().joinGame(joinRequest);
            ArrayList<GameInfo> gamesOnServer = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().listGames();
            for (GameInfo gameInfo2 : gamesOnServer) {
                for (int i = 0; i < gameInfo2.getPlayers().size(); i++) {
                    if (gameInfo2.getPlayers().get(i).getId() == -1) {
                        gameInfo2.getPlayers().remove(i);
                        i--;
                    }
                }
            }
            PlayerInfo playerInfo = new PlayerInfo();
            playerInfo.setId(ClientCommunicator.getSingleton().getPlayerId());
            playerInfo.setName(ClientCommunicator.getSingleton().getName());
            GameInfo[] allGames = new GameInfo[gamesOnServer.size()];
            gamesOnServer.toArray(allGames);
            getJoinGameView().setGames(allGames, playerInfo);
        } catch (Exception e) {
        } finally {
            getNewGameView().closeModal();
        }
    }

    private Integer gameId = -1;

    @Override
    public void startJoinGame(GameInfo game) {
        gameId = game.getId();
        getSelectColorView().showModal();
    }

    @Override
    public void cancelJoinGame() {
        getSelectColorView().closeModal();
        getJoinGameView().showModal();
    }

    @Override
    public void joinGame(CatanColor color) {
        JoinGameRequest request = new JoinGameRequest(gameId, color.toString().toLowerCase());
        try {
            if (ClientCommunicatorFascadeSettlersOfCatan.getSingleton().joinGame(request)) {

                // If join succeeded
                getSelectColorView().closeModal();
                getJoinGameView().closeModal();
                joinAction.execute();
            } else {
                ArrayList<GameInfo> gamesOnServer = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().listGames();
                PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.setId(ClientCommunicator.getSingleton().getPlayerId());
                playerInfo.setName(ClientCommunicator.getSingleton().getName());
                GameInfo[] allGames = new GameInfo[gamesOnServer.size()];
                gamesOnServer.toArray(allGames);
                getJoinGameView().setGames(allGames, playerInfo);
            }
        } catch (Exception e) {
            getSelectColorView().closeModal();
            try {
                ArrayList<GameInfo> gamesOnServer = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().listGames();
                PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.setId(ClientCommunicator.getSingleton().getPlayerId());
                playerInfo.setName(ClientCommunicator.getSingleton().getName());
                GameInfo[] allGames = new GameInfo[gamesOnServer.size()];
                gamesOnServer.toArray(allGames);
                getJoinGameView().setGames(allGames, playerInfo);
            } catch (Exception e2) {
            }
        }
    }

}
