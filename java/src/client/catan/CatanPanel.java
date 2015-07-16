package client.catan;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import shared.definitions.ResourceType;
import client.discard.DiscardController;
import client.discard.DiscardView;
import client.map.MapPoller;
import client.misc.WaitView;
import client.roll.RollController;
import client.roll.RollResultView;
import client.roll.RollView;
import java.util.Timer;

@SuppressWarnings("serial")
public class CatanPanel extends JPanel
{
	private TitlePanel titlePanel;
	private LeftPanel leftPanel;
	private MidPanel midPanel;
        private Timer timer;

    public TitlePanel getTitlePanel() {
        return titlePanel;
    }

    public void setTitlePanel(TitlePanel titlePanel) {
        this.titlePanel = titlePanel;
    }

    public LeftPanel getLeftPanel() {
        return leftPanel;
    }

    public void setLeftPanel(LeftPanel leftPanel) {
        this.leftPanel = leftPanel;
    }

    public MidPanel getMidPanel() {
        return midPanel;
    }

    public void setMidPanel(MidPanel midPanel) {
        this.midPanel = midPanel;
    }

    public RightPanel getRightPanel() {
        return rightPanel;
    }

    public void setRightPanel(RightPanel rightPanel) {
        this.rightPanel = rightPanel;
    }
	private RightPanel rightPanel;
	
	private DiscardView discardView;
	private WaitView discardWaitView;
	private DiscardController discardController;
	
	private RollView rollView;
	private RollResultView rollResultView;
	private RollController rollController;
	
	public CatanPanel()
	{
		this.setLayout(new BorderLayout());
		
		titlePanel = new TitlePanel();
		midPanel = new MidPanel();
		leftPanel = new LeftPanel(titlePanel, midPanel.getGameStatePanel());
		rightPanel = new RightPanel(midPanel.getMapController());
		
		this.add(titlePanel, BorderLayout.NORTH);
		this.add(leftPanel, BorderLayout.WEST);
		this.add(midPanel, BorderLayout.CENTER);
		this.add(rightPanel, BorderLayout.EAST);
		
		discardView = new DiscardView();
		discardWaitView = new WaitView();
		discardWaitView.setMessage("Waiting for other Players to Discard");
		discardController = new DiscardController(discardView, discardWaitView);
		discardView.setController(discardController);
		discardWaitView.setController(discardController);
		
		rollView = new RollView();
		rollResultView = new RollResultView();
		rollController = new RollController(rollView, rollResultView);
		rollView.setController(rollController);
		rollResultView.setController(rollController);
		
		JButton testButton = new JButton("Test");
		testButton.addActionListener(new ActionListener() {
			
//			 @Override
//			 public void actionPerformed(ActionEvent e) {
//			
//			 new client.points.GameFinishedView().showModal();
//			 }
//			
//			 @Override
//			 public void actionPerformed(ActionEvent e) {
//			
//			 rollView.showModal();
//			 }
//			
//			 @Override
//			 public void actionPerformed(java.awt.event.ActionEvent
//			 e) {
//			
//			 midPanel.getMapController().startMove(PieceType.ROBBER,
//			 false, false);
//			 }
			
			int state = 0;
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
//				rollView.showModal();
				
				discardView.setResourceMaxAmount(ResourceType.wood, 1);
				discardView.setResourceMaxAmount(ResourceType.brick, 0);
				discardView.setResourceMaxAmount(ResourceType.sheep, 11);
				discardView.setResourceMaxAmount(ResourceType.wheat, 1);
				discardView.setResourceMaxAmount(ResourceType.ore, 0);
				
				discardView.setResourceAmountChangeEnabled(ResourceType.wood, true, false);
				discardView.setResourceAmountChangeEnabled(ResourceType.sheep, true, false);
				discardView.setResourceAmountChangeEnabled(ResourceType.wheat, true, false);
				
				discardView.setStateMessage("0/6");
				
				discardView.setDiscardButtonEnabled(true);
				
				if(state == 0)
				{
					discardView.showModal();
					state = 1;
				}
				else if(state == 1)
				{
					discardWaitView.showModal();
					state = 2;
				}
   
			}
		});
		this.add(testButton, BorderLayout.SOUTH);
                timer = new Timer();
                timer.schedule(new MapPoller(this), 0, 3000);
	}
	
}

