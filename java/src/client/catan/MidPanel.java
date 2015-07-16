package client.catan;

import java.awt.*;
import javax.swing.*;
import java.util.Timer;

import client.map.*;
import client.map.MapPoller;

@SuppressWarnings("serial")
public class MidPanel extends JPanel
{
	
	private TradePanel tradePanel;
	private MapView mapView;
	private RobView robView;
	private MapController mapController;
	private GameStatePanel gameStatePanel;
	private Timer timer;
	public MidPanel()
	{
		
		this.setLayout(new BorderLayout());
		
		tradePanel = new TradePanel();
		
		mapView = new MapView();
		robView = new RobView();
		mapController = new MapController(mapView, robView);
		mapView.setController(mapController);
		robView.setController(mapController);
		
		gameStatePanel = new GameStatePanel();
		
		this.add(tradePanel, BorderLayout.NORTH);
		this.add(mapView, BorderLayout.CENTER);
		this.add(gameStatePanel, BorderLayout.SOUTH);
		                
		this.setPreferredSize(new Dimension(800, 700));                
                timer = new Timer();
<<<<<<< HEAD
                timer.schedule(new MapPoller(mapController), 0, 3000);
                
               
=======
                timer.schedule(new MapPoller(mapController), 0, 10000);
>>>>>>> 0e5c9ef5384f6c28be124ee1282cb8ade00260ed
	}
	
	public GameStatePanel getGameStatePanel()
	{
		return gameStatePanel;
	}
	
	public IMapController getMapController()
	{
		return mapController;
	}
	
}

