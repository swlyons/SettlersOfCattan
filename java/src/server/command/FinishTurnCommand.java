/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.command;

import shared.model.FinishMove;
import server.receiver.AllOfOurInformation;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.EdgeDirection;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

/**
 *
 * @author Samuel
 */
public class FinishTurnCommand implements Command {

    FinishMove finishMove;

    public FinishTurnCommand(FinishMove finishMove) {
        this.finishMove = finishMove;
    }

    @Override
    public boolean execute() {
        try {
            int currentTurn = AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().getCurrentTurn();
            String status = AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().getStatus();
            currentTurn++;
            if (status.equals("Playing")) {
                if (4 <= currentTurn) {
                    currentTurn = 0;
                }
                AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).endTurn();
                AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().setStatus("Rolling");
            } else {
                if (status.equals("FirstRound")) {
                    if (4 <= currentTurn) {
                        currentTurn = 3;
                        AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().setStatus("SecondRound");
                    }
                } else {
                    if (status.equals("SecondRound")) {
                        currentTurn -= 2;
                        if (currentTurn < 0) {
                            currentTurn++;
                            AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().setStatus("Rolling");
                            status = "Rolling";
                        }
                    } else {
                        return false;
                    }
                }
            }

            AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).endTurn();
            AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).log("Player is resting now.");

            while (AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getPlayers().get((currentTurn)).getId() < -1) {
            AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().setCurrentTurn(currentTurn);
                if (!(status.equals("SecondRound") || status.equals("FirstRound"))) {
                    currentTurn++;
                    if (currentTurn == 4) {
                        currentTurn = 0;
                    }
                    continue;
                }

                if (status.equals("SecondRound") && currentTurn == 0) {
                    System.out.println("Errors with AI");
                    break;
                }

                if (status.equals("FirstRound")) {

                    boolean builtSettlement = false;
                    boolean builtRoad = false;
                    for (int i = -3; i < 4; i++) {
                        for (int j = -3; j < 4; j++) {
                            VertexLocation vl = new VertexLocation(new HexLocation(i, j), VertexDirection.NE);
                            if (AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).canPlaceSettlement(vl)) {
                                AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).placeFirstSettlement(vl);
                                builtSettlement = true;
                                AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getVersion() + 1);
                                break;
                            }
                        }
                        if (builtSettlement) {
                            break;
                        }
                    }

                    if (!builtSettlement) {
                        for (int i = -3; i < 4; i++) {
                            for (int j = -3; j < 4; j++) {
                                VertexLocation vl = new VertexLocation(new HexLocation(i, j), VertexDirection.NW);
                                if (AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).canPlaceSettlement(vl)) {
                                    AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).placeFirstSettlement(vl);
                                    builtSettlement = true;
                                    AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getVersion() + 1);
                                    break;
                                }
                            }
                            if (builtSettlement) {
                                break;
                            }
                        }

                    }

                    for (int i = -3; i < 4; i++) {
                        for (int j = -3; j < 4; j++) {
                            EdgeLocation el = new EdgeLocation(new HexLocation(i, j), EdgeDirection.N);

                            if (AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).canPlaceRoad(el)) {
                                AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).placeFreeRoad(el);
                                builtRoad = true;
                                AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getVersion() + 1);
                                break;
                            }
                        }
                        if (builtRoad) {
                            break;
                        }
                    }

                    if (!builtRoad) {
                        for (int i = -3; i < 4; i++) {
                            for (int j = -3; j < 4; j++) {
                                EdgeLocation el = new EdgeLocation(new HexLocation(i, j), EdgeDirection.NE);
                                if (AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).canPlaceRoad(el)) {
                                    AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).placeFreeRoad(el);
                                    builtRoad = true;
                                    AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getVersion() + 1);
                                    break;
                                }
                            }
                            if (builtRoad) {
                                break;
                            }
                        }
                    }

                    if (!builtRoad) {

                        for (int i = -3; i < 4; i++) {
                            for (int j = -3; j < 4; j++) {
                                EdgeLocation el = new EdgeLocation(new HexLocation(i, j), EdgeDirection.NW);
                                if (AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).canPlaceRoad(el)) {
                                    AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).placeFreeRoad(el);
                                    builtRoad = true;
                                    AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getVersion() + 1);
                                    break;
                                }
                            }
                            if (builtRoad) {
                                break;
                            }
                        }
                    }

                    currentTurn++;
                    if (currentTurn == 4) {
                        currentTurn--;
                        status = "SecondRound";
                        AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().setStatus("SecondRound");
                    }
                } else {

                    if (status.equals("SecondRound")) {
                        boolean builtSettlement = false;
                        boolean builtRoad = false;
                        for (int i = -3; i < 4; i++) {
                            for (int j = -3; j < 4; j++) {
                                VertexLocation vl = new VertexLocation(new HexLocation(i, j), VertexDirection.NE);
                                if (AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).canPlaceSettlement(vl)) {
                                    AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).placeSecondSettlement(vl);
                                    builtSettlement = true;
                                    AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getVersion() + 1);
                                    break;
                                }
                            }
                            if (builtSettlement) {
                                break;
                            }
                        }

                        if (!builtSettlement) {
                            for (int i = -3; i < 4; i++) {
                                for (int j = -3; j < 4; j++) {
                                    VertexLocation vl = new VertexLocation(new HexLocation(i, j), VertexDirection.NW);
                                    if (AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).canPlaceSettlement(vl)) {
                                        AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).placeSecondSettlement(vl);
                                        builtSettlement = true;
                                        AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getVersion() + 1);
                                        break;
                                    }
                                }
                                if (builtSettlement) {
                                    break;
                                }
                            }

                        }

                        for (int i = -3; i < 4; i++) {
                            for (int j = -3; j < 4; j++) {
                                EdgeLocation el = new EdgeLocation(new HexLocation(i, j), EdgeDirection.N);

                                if (AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).canPlaceRoad(el)) {
                                    AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).placeFreeRoad(el);
                                    AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getVersion() + 1);
                                    builtRoad = true;
                                    break;
                                }
                            }
                            if (builtRoad) {
                                break;
                            }
                        }

                        if (!builtRoad) {
                            for (int i = -3; i < 4; i++) {
                                for (int j = -3; j < 4; j++) {
                                    EdgeLocation el = new EdgeLocation(new HexLocation(i, j), EdgeDirection.NE);
                                    if (AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).canPlaceRoad(el)) {
                                        AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).placeFreeRoad(el);
                                        AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getVersion() + 1);
                                        builtRoad = true;
                                        break;
                                    }
                                }
                                if (builtRoad) {
                                    break;
                                }
                            }
                        }

                        if (!builtRoad) {

                            for (int i = -3; i < 4; i++) {
                                for (int j = -3; j < 4; j++) {
                                    EdgeLocation el = new EdgeLocation(new HexLocation(i, j), EdgeDirection.NW);
                                    if (AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).canPlaceRoad(el)) {
                                        AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).placeFreeRoad(el);
                                        AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getVersion() + 1);
                                        builtRoad = true;
                                        break;
                                    }
                                }
                                if (builtRoad) {
                                    break;
                                }
                            }
                        }

                        currentTurn--;
                    }

                }
            }
            AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getTurnTracker().setCurrentTurn(currentTurn);
            AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getPlayers().get(currentTurn).setPlayedDevCard(false);
            AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().setVersion(AllOfOurInformation.getSingleton().getGames().get(finishMove.getGameId()).getGame().getVersion() + 1);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
