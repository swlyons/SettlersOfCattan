/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import server.command.Command;
import server.receiver.AllOfOurInformation;
import shared.model.BuyDevCard;

/**
 *
 * @author Samuel
 */
public class BuyDevCardCommand implements Command {

    private BuyDevCard buyDevCard;

    public BuyDevCardCommand(BuyDevCard buyDevCard) {
        this.buyDevCard = buyDevCard;
    }

    @Override
    public boolean execute() {
        try {
            AllOfOurInformation.getSingleton().getGames().get(buyDevCard.getGameId()).buyDevCard();
            AllOfOurInformation.getSingleton().getGames().get(buyDevCard.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(buyDevCard.getGameId()).getGame().getVersion()+1);
            AllOfOurInformation.getSingleton().getGames().get(buyDevCard.getGameId()).log("Player bought something from some merchants.");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
