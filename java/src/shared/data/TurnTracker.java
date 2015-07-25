package shared.data;

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
        return "{\"status\" : \"" + status + "\" , \"longestRoad\" : "
                + longestRoad + ", \"largestArmy\" : " + largestArmy + 
                ",\"currentTurn\":"+currentTurn+"}";
    }

}
