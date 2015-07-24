package client.catan;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import client.base.IAction;
import client.communication.ClientCommunicator;
import client.communication.ClientFascade;
import client.main.ClientException;
import client.main.FirstRoundState;
import client.managers.GameManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import shared.model.FinishMove;

@SuppressWarnings("serial")
public class GameStatePanel extends JPanel {

    private JButton button;
    private JPanel centered;
    private int playerIndex;

    public GameStatePanel() {
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);
        this.setOpaque(true);

        centered = new JPanel(new BorderLayout());

        button = new JButton();

        Font font = button.getFont();
        Font newFont = font.deriveFont(font.getStyle(), 20);
        button.setFont(newFont);

        centered.setPreferredSize(new Dimension(400, 50));
        button.setContentAreaFilled(false);
        centered.add(button, BorderLayout.CENTER);
        this.add(centered);

        updateGameState("Waiting for other players", false);
    }

    public JButton getButton() {
        return button;
    }

    public JPanel getCentered() {
        return centered;
    }

    public void updateGameState(String stateMessage, boolean enable) {
        button.setText(stateMessage);
        button.setEnabled(enable);
    }

    public void setButtonAction(final IAction action) {
        ActionListener[] listeners = button.getActionListeners();
        for (ActionListener listener : listeners) {
            button.removeActionListener(listener);
        }

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.execute();
                GameManager gameManager = ClientCommunicator.getSingleton().getGameManager();
                FinishMove fm = new FinishMove(gameManager.getGame().getTurnTracker().getCurrentTurn());
                fm.setType("finishTurn");
                fm.setPlayerIndex(playerIndex);
                try {
                    gameManager.initializeGame(ClientFascade.getSingleton().finishMove(fm));
                } catch (ClientException ex) {
                    ex.printStackTrace();
                    Logger.getLogger(FirstRoundState.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        button.addActionListener(actionListener);
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

}
