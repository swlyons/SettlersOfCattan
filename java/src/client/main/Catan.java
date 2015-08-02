package client.main;

import javax.swing.*;

import client.catan.*;
import client.login.*;
import client.join.*;
import client.misc.*;
import client.base.*;
import client.communication.ClientCommunicator;
import client.map.MapController;

/**
 * Main entry point for the Catan program
 */
@SuppressWarnings("serial")
public class Catan extends JFrame {

    private static CatanPanel catanPanel;

    public Catan() {

        client.base.OverlayView.setWindow(this);

        this.setTitle("Settlers of Catan (Team Sure Style)");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        catanPanel = new CatanPanel();
        this.setContentPane(catanPanel);

        display();
    }

    private void display() {
        pack();
        setVisible(true);
    }

    //
    // Main
    //
    public static void main(final String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();;
        }

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                // set the host and port for the client to run on
                ClientCommunicator.getSingleton().setSERVER_HOST(args[0]);
                ClientCommunicator.getSingleton().setSERVER_PORT(
                        Integer.parseInt(args[1]));
                //start catan
                new Catan();
                
                CatanStateMachine catanStateMachine = new CatanStateMachine();

                PlayerWaitingView playerWaitingView = new PlayerWaitingView();
                final PlayerWaitingController playerWaitingController = new PlayerWaitingController(
                        playerWaitingView);
                playerWaitingView.setController(playerWaitingController);
                playerWaitingController.setPlayerAction(new IAction() {
                    @Override
                    public void execute() {
                        
                        ((MapController) catanPanel.getMidPanel().getMapController()).getMapPoller().setCatanPanel(catanPanel);
                        catanStateMachine.setCatanPanel(catanPanel);
                        
                        //end PlayerWaiting
                        catanStateMachine.move((MapController) catanPanel.getMidPanel().getMapController());
                        //setup
                        catanStateMachine.move((MapController) catanPanel.getMidPanel().getMapController());
                        //first round
                        
                        catanStateMachine.move((MapController) catanPanel.getMidPanel().getMapController());

                        //second round
                        catanStateMachine.move((MapController) catanPanel.getMidPanel().getMapController());

                        //gameplay
                        catanStateMachine.stay((MapController) catanPanel.getMidPanel().getMapController());
                    }
                });
                JoinGameView joinView = new JoinGameView();
                NewGameView newGameView = new NewGameView();
                SelectColorView selectColorView = new SelectColorView();
                MessageView joinMessageView = new MessageView();

                final JoinGameController joinController = new JoinGameController(
                        joinView,
                        newGameView,
                        selectColorView,
                        joinMessageView, playerWaitingView);

                joinController.setJoinAction(new IAction() {
                    @Override
                    public void execute() {
                        playerWaitingController.start();
                        catanStateMachine.stay(joinController);
                    }
                });
                joinView.setController(joinController);
                newGameView.setController(joinController);
                selectColorView.setController(joinController);
                joinMessageView.setController(joinController);

                LoginView loginView = new LoginView();
                MessageView loginMessageView = new MessageView();
                LoginController loginController = new LoginController(
                        loginView,
                        loginMessageView);
                loginController.setLoginAction(new IAction() {
                    @Override
                    public void execute() {
                        joinController.start();
                        catanStateMachine.move(joinController);
                    }
                });
                loginView.setController(loginController);
                loginView.setController(loginController);

                loginController.start();
                catanStateMachine.move(loginController);

            }

        });
    }

}
