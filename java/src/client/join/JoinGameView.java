package client.join;

import shared.data.PlayerInfo;
import shared.data.GameInfo;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import client.base.*;

/**
 * Implementation for the join game view, which lets the user select a game to
 * join
 */
@SuppressWarnings("serial")
public class JoinGameView extends OverlayView implements IJoinGameView {

    private final int LABEL_TEXT_SIZE = 40;
    private final int PANEL_TEXT_SIZE = 14;
    private final int BUTTON_TEXT_SIZE = 28;
    private final int BORDER_WIDTH = 10;

    private JLabel label;
    private JLabel subLabel;

    private JLabel hash;
    private JLabel name;
    private JLabel currentPlayer;
    private JLabel join;

    private JLabel playerNames;

    private JButton createButton;
    private JButton tempJoinButton;

    private JPanel labelPanel;
    private JPanel gamePanel;
    private JPanel buttonPanel;

    private Font labelFont;

    private GameInfo[] games;
    private PlayerInfo localPlayer;

    public JoinGameView() {
        this.initialize();
    }

    private void initialize() {
        this.initializeView();
    }

    private void initializeView() {
        this.setOpaque(true);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black, BORDER_WIDTH));

        label = new JLabel("Welcome to the game hub");
        labelFont = label.getFont();
        labelFont = labelFont.deriveFont(labelFont.getStyle(), LABEL_TEXT_SIZE);
        label.setFont(labelFont);
        subLabel = new JLabel("Join an existing game, or create your own");
        labelFont = subLabel.getFont();
        labelFont = labelFont.deriveFont(labelFont.getStyle(), LABEL_TEXT_SIZE * 2 / 3);
        subLabel.setFont(labelFont);

        labelPanel = new JPanel();
        labelPanel.setLayout(new FlowLayout());
        labelPanel.add(label);
        labelPanel.add(subLabel);
        this.add(labelPanel, BorderLayout.NORTH);
        
        gamePanel = new JPanel();
        gamePanel.setBorder(BorderFactory.createLineBorder(Color.white, BORDER_WIDTH));
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
        hash = new JLabel("#");
        labelFont = new Font(labelFont.getFontName(), Font.BOLD, PANEL_TEXT_SIZE);
        hash.setFont(labelFont);
        name = new JLabel("Name");
        name.setFont(labelFont);
        currentPlayer = new JLabel("Current Players");
        currentPlayer.setFont(labelFont);
        join = new JLabel("Join");
        join.setFont(labelFont);

        // add the header information
        JPanel gameInfo = new JPanel(new GridLayout(games == null ? 4 : (games.length + 1),4));
        JPanel hashPanel = new JPanel();
        hashPanel.add(hash);
        gameInfo.add(hashPanel);
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(name);
        gameInfo.add(namePanel);
        JPanel currentPlayerPanel = new JPanel();
        currentPlayerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        currentPlayerPanel.add(currentPlayer);
        gameInfo.add(currentPlayerPanel);
        JPanel joinLabelPanel = new JPanel();
        joinLabelPanel.add(join);
        gameInfo.add(joinLabelPanel);
        
        // This is the looped layout
        if (games != null && games.length > 0) {
            labelFont = labelFont.deriveFont(labelFont.getStyle(), PANEL_TEXT_SIZE);
            for (GameInfo game : games) {
                JLabel tmp1 = new JLabel(String.valueOf(game.getId()));
                tmp1.setFont(labelFont);
                JPanel gameIdPanel = new JPanel();
                gameIdPanel.add(tmp1);
                gameInfo.add(gameIdPanel);
                JLabel tmp2 = new JLabel(game.getTitle());
                tmp2.setFont(labelFont);
                JPanel gameTitlePanel = new JPanel();
                gameTitlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                gameTitlePanel.add(tmp2);
                gameInfo.add(gameTitlePanel);
                String players = String.valueOf(game.getPlayers().size()) + "/4 : ";
                for (int j = 0; j < game.getPlayers().size(); j++) {
                    if (j < game.getPlayers().size() - 1) {
                        players = players + game.getPlayers().get(j).getName() + ", ";
                    } else {
                        players = players + game.getPlayers().get(j).getName();
                    }
                }
                playerNames = new JLabel(players);
                playerNames.setFont(labelFont);
                playerNames.setLayout(new GridLayout());
                JPanel playerPanel = new JPanel();
                playerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                playerPanel.add(playerNames);
                gameInfo.add(playerPanel);
                JButton joinButton;
                JPanel joinPanel = new JPanel();
                if (game.getPlayers().contains(localPlayer)) {
                    joinButton = new JButton("Re-Join");
                } else if (game.getPlayers().size() >= 4) {
                    joinButton = new JButton("Full");
                    joinButton.setEnabled(false);
                } else {
                    joinButton = new JButton("Join");
                }
                joinButton.setActionCommand("" + game.getId());
                joinButton.addActionListener(actionListener);
                joinButton.setPreferredSize(new Dimension(150, 30));
                joinPanel.add(joinButton);
                gameInfo.add(joinPanel);
                gamePanel.add(gameInfo);
            }

        }

        //add the gamePanel to a scroll pane and then that pane to the center
        JScrollPane pane = new JScrollPane(gamePanel);
        if (getParent() != null) {
            if (games != null) {
                pane.setPreferredSize(new Dimension (getParent().getSize().width/5,getParent().getSize().height /5));
            }

        }
        //Add all the above
        pane.getVerticalScrollBar().setUnitIncrement(16);
        this.add(pane, BorderLayout.CENTER);

        createButton = new JButton("Create Game");
        createButton.addActionListener(actionListener);
        createButton.setPreferredSize(new Dimension(250, 30));
        createButton.setFont(labelFont);

        buttonPanel = new JPanel();
        buttonPanel.add(new JLabel("Number of Games: " + (games == null ? 0 : games.length)));
        buttonPanel.add(createButton);

        this.add(buttonPanel, BorderLayout.SOUTH);

    }

    @Override
    public IJoinGameController getController() {
        return (IJoinGameController) super.getController();
    }

    @Override
    public void setGames(GameInfo[] games, PlayerInfo localPlayer) {
        this.games = games;
        this.localPlayer = localPlayer;
        this.removeAll();
        this.initializeView();
    }

    public GameInfo[] getGames() {
        return games;
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == createButton) {
                getController().startCreateNewGame();
            } else {
                try {
                    //System.out.println(e.getActionCommand());
                    int gameId = Integer.parseInt(e.getActionCommand());
                    GameInfo game = null;
                    for (GameInfo g : games) {
                        if (g.getId() == gameId) {
                            game = g;
                            break;
                        }
                    }
                    getController().startJoinGame(game);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };
}
