package client.data;

public class TurnTracker {

	private int currentTurn;
	private String status;
	private int longestRoad;
	private int largestArmy;

	public TurnTracker() {
		currentTurn = 0;
		longestRoad = 4;
		largestArmy = 4;
	}

	public int getCurrentTurn() {
            return currentTurn;
        }

	public void setCurrentTurn(int currentPlayerIndex) {
		this.currentTurn = currentPlayerIndex;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getLongestRoad() {
		return longestRoad;
	}

	public void setLongestRoad(int longestRoad) {
		this.longestRoad = longestRoad;
	}

	public int getLargestArmy() {
		return largestArmy;
	}

	public void setLargestArmy(int largestArmy) {
		this.largestArmy = largestArmy;
	}

	@Override
	public String toString() {
<<<<<<< HEAD
		return "{, status : " + status + ", longestRoad : "
				+ longestRoad + ", largestArmy : " + largestArmy + '}';
=======
		return "{" + "currentRound : " + currentRound + ", status : " + status + ", currentPlayer: " + currentPlayerIndex 
				+ ", longestRoadHolder : " + longestRoadHolder + ", largestArmyHolder : " + largestArmyHolder + '}';
>>>>>>> c6f6f7ddf7f5128c584373477466f29c17629913
	}

}
