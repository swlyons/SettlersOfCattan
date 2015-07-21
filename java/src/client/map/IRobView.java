package client.map;

import shared.data.RobPlayerInfo;
import client.base.*;

/**
 * Interface for the rob view, which lets the user select a player to rob
 */
public interface IRobView extends IOverlayView {

    void setPlayers(RobPlayerInfo[] candidateVictims);
}
