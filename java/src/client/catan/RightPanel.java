package client.catan;

import javax.swing.*;

import shared.definitions.PieceType;
import client.communication.ClientCommunicator;
import client.points.*;
import client.resources.*;
import client.base.*;
import client.communication.ClientCommunicatorFascadeSettlersOfCatan;
import client.map.*;
import client.devcards.*;

@SuppressWarnings("serial")
public class RightPanel extends JPanel {

    private PlayDevCardView playCardView;
    private BuyDevCardView buyCardView;
    private DevCardController devCardController;
    private PointsView pointsView;
    private GameFinishedView finishedView;
    private PointsController pointsController;
    private ResourceBarView resourceView;
    private ResourceBarController resourceController;

    public RightPanel(final IMapController mapController) {

        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		// Initialize development card views and controller
        //
        playCardView = new PlayDevCardView();
        buyCardView = new BuyDevCardView();
        IAction soldierAction = new IAction() {
            @Override
            public void execute() {
                mapController.playSoldierCard();
            }
        };
        IAction roadAction = new IAction() {
            @Override
            public void execute() {
                mapController.playRoadBuildingCard();
            }
        };
        devCardController = new DevCardController(playCardView, buyCardView,
                soldierAction, roadAction);
        playCardView.setController(devCardController);
        buyCardView.setController(devCardController);

		// Initialize victory point view and controller
        //
        pointsView = new PointsView();
        finishedView = new GameFinishedView();
        pointsController = new PointsController(pointsView, finishedView);
        pointsView.setController(pointsController);

		// Initialize resource bar view and controller
        //
        resourceView = new ResourceBarView();
        resourceController = new ResourceBarController(resourceView);
        resourceController.setElementAction(ResourceBarElement.ROAD,
                createStartMoveAction(mapController,
                        PieceType.ROAD));
        resourceController.setElementAction(ResourceBarElement.SETTLEMENT,
                createStartMoveAction(mapController,
                        PieceType.SETTLEMENT));
        resourceController.setElementAction(ResourceBarElement.CITY,
                createStartMoveAction(mapController,
                        PieceType.CITY));
        resourceController.setElementAction(ResourceBarElement.BUY_CARD,
                new IAction() {
                    @Override
                    public void execute() {
                        try{
                            ClientCommunicator.getSingleton().getGameManager().initializeGame(ClientCommunicatorFascadeSettlersOfCatan.getSingleton().getGameModel(0 + ""));
                            if(resourceController.canBuyCard()){
                                devCardController.startBuyCard();
                            }
                        }catch(Exception e){}
                    }
                });
        resourceController.setElementAction(ResourceBarElement.PLAY_CARD,
                new IAction() {
                    @Override
                    public void execute() {
                        try{
                            ClientCommunicator.getSingleton().getGameManager().initializeGame(ClientCommunicatorFascadeSettlersOfCatan.getSingleton().getGameModel(0 + ""));
                            if(resourceController.canPlayCard()){
                                devCardController.startPlayCard();
                            }
                        }catch(Exception e){}
                    }
                });
        resourceView.setController(resourceController);

        this.add(pointsView);
        this.add(resourceView);
    }

    private IAction createStartMoveAction(final IMapController mapController,
            final PieceType pieceType) {

        return new IAction() {

            @Override
            public void execute() {
                boolean isFree = false;
                boolean allowDisconnected = false;
                boolean isAllowed = false;
                try{
                    ClientCommunicator.getSingleton().getGameManager().initializeGame(ClientCommunicatorFascadeSettlersOfCatan.getSingleton().getGameModel(0 + ""));
                    switch (pieceType) {
                        case ROAD:
                            isAllowed = resourceController.canBuildRoad();
                            break;
                        case SETTLEMENT:
                            isAllowed = resourceController.canBuildSettlement();
                            break;
                        case CITY:
                            isAllowed = resourceController.canBuildCity();
                            break;
                        default:
                            break;
                    }

                    if(isAllowed){
                        mapController.startMove(pieceType, isFree, allowDisconnected);
                    }
                }catch(Exception e){}
            }
        };
    }

    public PointsController getPointsController() {
        return pointsController;
    }

    public void setPointsController(PointsController pointsController) {
        this.pointsController = pointsController;
    }

    public ResourceBarController getResourceController() {
        return resourceController;
    }

    public void setResourceController(ResourceBarController resourceController) {
        this.resourceController = resourceController;
    }

}
