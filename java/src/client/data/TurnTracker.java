package client.data;

public class TurnTracker {

	private int currentRound;
	private int currentPlayerIndex;
	private String status;
	private int longestRoadHolder;
	private int largestArmyHolder;

	public TurnTracker() {
		currentRound = 1;
		currentPlayerIndex = 0;
		longestRoadHolder = 4;
		largestArmyHolder = 4;
	}

	public int getcurrentRound() {
		return currentRound;
	}

	public void setcurrentRound(int currentRound) {
		this.currentRound = currentRound;
	}

	public void nextTurn() {
		currentPlayerIndex++;
		if (currentPlayerIndex == 4) {
			nextRound();
		}
	}

	private void nextRound() {
		currentPlayerIndex = 0;
		currentRound++;
	}

	public int getCurrentPlayerIndex() {
		if (currentRound != 2) {
			return currentPlayerIndex;
		} else {
			switch (currentPlayerIndex) {
			case 0:
				return 3;
			case 1:
				return 2;
			case 2:
				return 1;
			case 3:
				return 0;
			default:	//shouldn't hit this
				return -1;
			}
		}
	}

	public void setCurrentPlayerIndex(int currentPlayerIndex) {
		this.currentPlayerIndex = currentPlayerIndex;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getLongestRoadHolder() {
		return longestRoadHolder;
	}

	public void setLongestRoadHolder(int longestRoadHolder) {
		this.longestRoadHolder = longestRoadHolder;
	}

	public int getLargestArmyHolder() {
		return largestArmyHolder;
	}

	public void setLargestArmyHolder(int largestArmyHolder) {
		this.largestArmyHolder = largestArmyHolder;
	}

	@Override
	public String toString() {
		return "{" + "currentRound : " + currentRound + ", status : " + status + ", currentPlayer: " + currentPlayerIndex 
				+ ", longestRoadHolder : " + longestRoadHolder + ", largestArmyHolder : " + largestArmyHolder + '}';
	}

}
