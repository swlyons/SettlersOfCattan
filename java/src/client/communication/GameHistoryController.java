package client.communication;

import java.util.*;
import java.util.List;

import client.base.*;
import client.data.Game;
import client.data.MessageLine;
import client.data.Player;
import shared.definitions.*;


/**
 * Game history controller implementation
 */
public class GameHistoryController extends Controller implements IGameHistoryController {

	public GameHistoryController(IGameHistoryView view) {
		
		super(view);
		
		initFromModel();
	}
	
	@Override
	public IGameHistoryView getView() {
		
		return (IGameHistoryView)super.getView();
	}
	
	private void initFromModel() {
		Game game = ClientCommunicator.getSingleton().getGameManager().getGame();
		List<LogEntry> entries = new ArrayList<LogEntry>();
		
		//for each line...
		/*for(int lineIndex = 0; lineIndex < game.getLog().getLines().size(); lineIndex++) {
			MessageLine line = game.getLog().getLines().get(lineIndex);
			String message = line.getMessage();
			String source = line.getSource();

			//find the matching player and get their color
			CatanColor color = CatanColor.WHITE;
			for(Player player : game.getPlayers()) {
				if (player.getName().equals(source)) {
					color = CatanColor.valueOf(player.getColor());
					break;
				}
			}
			
			//create the line
			entries.add(new LogEntry(color, message));
		}*/
		
		getView().setEntries(entries);
	}
	
}

