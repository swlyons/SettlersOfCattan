/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import server.receiver.AllOfOurInformation;
import shared.data.GameInfo;
import shared.data.MessageLine;

/**
 *
 * @author ddennis
 */
public class SendChatCommand implements Command {

    private int playerIndex;
    private String content;
    private int gameId;

    public SendChatCommand(int playerIndex, String content, int gameId) {
        this.playerIndex = playerIndex;
        this.content = content;
        this.gameId = gameId;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    @Override
    public boolean execute() {
        boolean successful = false;
        try {
            //return true if the chat entry was successfully added
            GameInfo game = AllOfOurInformation.getSingleton().getGames().get(gameId).getGame();

            //get the player name
            String playerName = game.getPlayers().get(playerIndex).getName();

            //create the message line for the chat
            MessageLine messageLine = new MessageLine();
            messageLine.setSource(playerName);
            messageLine.setSource(content);

            game.getChat().getLines().add(messageLine);
            
            //return success if we made it this far
            successful = true;
        } catch (ArrayIndexOutOfBoundsException aio) {
            aio.printStackTrace();
        }
        return successful;
    }

}
