package client.join;

import client.ClientException;
import shared.definitions.CatanColor;
import client.base.*;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.data.*;
import client.map.MapController;
import client.map.MapView;
import client.map.RobView;
import client.misc.*;
import client.proxy.CreateGameRequest;
import client.proxy.JoinGameRequest;
import java.util.ArrayList;
import java.util.Timer;
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
    private IPlayerWaitingView playerWaitingView;
    private PlayerWaitingViewPoller playerWaitingViewPoller;
    private JoinGameViewPoller joinGameViewPoller;
    private java.util.Timer playerWaitingTimer = new java.util.Timer();
    private java.util.Timer joinGameTimer = new java.util.Timer();

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
            ISelectColorView selectColorView, IMessageView messageView, IPlayerWaitingView playerWaitingView, PlayerWaitingViewPoller playerWaitingViewPoller, JoinGameViewPoller joinGameViewPoller) {

        super(view);

        setNewGameView(newGameView);
        setSelectColorView(selectColorView);
        setMessageView(messageView);
        setPlayerWaitingView(playerWaitingView);
        setPlayerWaitingViewPoller(playerWaitingViewPoller);
        setJoinGameViewPoller(joinGameViewPoller);
    }

    public IJoinGameView getJoinGameView() {

        return (IJoinGameView) super.getView();
    }

    public Timer getPlayerWaitingTimer() {
        return playerWaitingTimer;
    }

    public void setPlayerWaitingTimer(Timer playerWaitingTimer) {
        this.playerWaitingTimer = playerWaitingTimer;
    }

    /**
     * Returns the action to be executed when the user joins a game
     *
     * @return The action to be executed when the user joins a game
     */
    public IAction getJoinAction() {

        return joinAction;
    }

    public PlayerWaitingViewPoller getPlayerWaitingViewPoller() {
        return playerWaitingViewPoller;
    }

    public void setPlayerWaitingViewPoller(PlayerWaitingViewPoller playerWaitingViewPoller) {
        this.playerWaitingViewPoller = playerWaitingViewPoller;
    }

    public JoinGameViewPoller getJoinGameViewPoller() {
        return joinGameViewPoller;
    }

    public void setJoinGameViewPoller(JoinGameViewPoller joinGameViewPoller) {
        this.joinGameViewPoller = joinGameViewPoller;
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

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    @Override
    public void start() {
        joinGameTimer.schedule(new JoinGameViewPoller(this), 0, 1000);
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
            getJoinGameView().setGames(allGames, playerInfo, true);

            //if successful clear out old options
            getNewGameView().setRandomlyPlaceHexes(false);
            getNewGameView().setRandomlyPlaceNumbers(false);
            getNewGameView().setUseRandomPorts(false);
            getNewGameView().setTitle("");
        } catch (Exception e) {
        } finally {
            getNewGameView().closeModal();
        }
    }

    private Integer gameId = -1;

    @Override
    public void startJoinGame(GameInfo game) {
        gameId = game.getId();
        int numberOfPlayers = 4;
        ArrayList<GameInfo> activeGames = new ArrayList<>();
        try {
            activeGames = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().listGames();
        } catch (ClientException ex) {
            Logger.getLogger(JoinGameController.class.getName()).log(Level.SEVERE, null, ex);
        }
        GameInfo[] games = new GameInfo[activeGames.size()];
        activeGames.toArray(games);
        int activePlayer = ClientCommunicator.getSingleton().getPlayerId();
        ArrayList<PlayerInfo> activePlayers = new ArrayList<>();
        String color = "";
        //diable the necessary player colors
        for (GameInfo activeGame : activeGames) {
            if (activeGame.getId() == gameId) {
                //current player should be able to choose another color

                for (PlayerInfo player : activeGame.getPlayers()) {
                    if (player.getId() != -1) {
                        numberOfPlayers--;
                        activePlayers.add(player);
                        if (activePlayer != player.getId()) {
                            getSelectColorView().setColorEnabled(CatanColor.valueOf(player.getColor().toUpperCase()), false);
                        } else {
                            color = player.getColor().toLowerCase();
                        }
                    }
                }
            }
        }
        if (numberOfPlayers != 0) {
            getSelectColorView().showModal();
        } else {
            getJoinGameView().closeModal();
            //automatically join the person to the game
            JoinGameRequest request = new JoinGameRequest(gameId, color);
            try {
                if (ClientCommunicatorFascadeSettlersOfCatan.getSingleton().joinGame(request)) {
                    //if successful in re-joining game
                    PlayerInfo[] players = new PlayerInfo[activePlayers.size()];
                    activePlayers.toArray(players);
                    getPlayerWaitingView().setPlayers(players);
                }
            } catch (ClientException ex) {
                Logger.getLogger(JoinGameController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @Override
    public void cancelJoinGame() {
        getSelectColorView().closeModal();
        getJoinGameView().showModal();
    }

    @Override
    public void joinGame(CatanColor color) {
        JoinGameRequest request = new JoinGameRequest(gameId, color.name().toLowerCase());
        try {
            if (ClientCommunicatorFascadeSettlersOfCatan.getSingleton().joinGame(request)) {
                //start the View Poller (runs every 1 seconds)
                playerWaitingTimer.schedule(new PlayerWaitingViewPoller(this, playerWaitingTimer, joinGameTimer), 0, 1000);

            } else {
                ArrayList<GameInfo> gamesOnServer = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().listGames();
                PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.setId(ClientCommunicator.getSingleton().getPlayerId());
                playerInfo.setName(ClientCommunicator.getSingleton().getName());
                GameInfo[] allGames = new GameInfo[gamesOnServer.size()];
                gamesOnServer.toArray(allGames);
                getJoinGameView().setGames(allGames, playerInfo, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            getSelectColorView().closeModal();
            try {
                ArrayList<GameInfo> gamesOnServer = ClientCommunicatorFascadeSettlersOfCatan.getSingleton().listGames();
                PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.setId(ClientCommunicator.getSingleton().getPlayerId());
                playerInfo.setName(ClientCommunicator.getSingleton().getName());
                GameInfo[] allGames = new GameInfo[gamesOnServer.size()];
                gamesOnServer.toArray(allGames);
                getJoinGameView().setGames(allGames, playerInfo, true);
            } catch (Exception e2) {
            }
        }
    }

}
