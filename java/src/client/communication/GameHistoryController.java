package client.communication;


import client.base.*;

/**
 * Game history controller implementation
 */
public class GameHistoryController extends Controller implements IGameHistoryController {

    public GameHistoryController(IGameHistoryView view) {
        super(view);
    }

    @Override
    public IGameHistoryView getView() {

        return (IGameHistoryView) super.getView();
    }

}
