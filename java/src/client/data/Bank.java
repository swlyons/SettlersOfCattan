package client.data;

import java.util.ArrayList;

public class Bank {

    private int ownerID;
    private int settlements;
    private int cities;
    private int roads;
    private boolean hasLongestRoad;
    private boolean hasLargestArmy;
    private ArrayList<Card> resourcesCards;
    private ArrayList<Card> developmentCards;
    private ArrayList<Card> soldiers;
    private ArrayList<Card> monuments;

    public Bank(int ownerID, boolean hasLongestRoad, boolean hasLargestArmy, ArrayList<Card> resourcesCards, ArrayList<Card> developmentCards, ArrayList<Card> soldiers, ArrayList<Card> monuments) {
        this.ownerID = ownerID;
        this.hasLongestRoad = hasLongestRoad;
        this.hasLargestArmy = hasLargestArmy;
        this.resourcesCards = resourcesCards;
        this.developmentCards = developmentCards;
        this.soldiers = soldiers;
        this.monuments = monuments;
        settlements = 5;
        cities = 4;
        roads = 15;
    }

    public int getSettlements() {
        return settlements;
    }

    public void setSettlements(int settlements) {
        this.settlements = settlements;
    }

    public int getCities() {
        return cities;
    }

    public void setCities(int cities) {
        this.cities = cities;
    }

    public int getRoads() {
        return roads;
    }

    public void setRoads(int roads) {
        this.roads = roads;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public boolean isHasLongestRoad() {
        return hasLongestRoad;
    }

    public void setHasLongestRoad(boolean hasLongestRoad) {
        this.hasLongestRoad = hasLongestRoad;
    }

    public boolean isHasLargestArmy() {
        return hasLargestArmy;
    }

    public void setHasLargestArmy(boolean hasLargestArmy) {
        this.hasLargestArmy = hasLargestArmy;
    }

    public ArrayList<Card> getResourcesCards() {
        return resourcesCards;
    }

    public void setResourcesCards(ArrayList<Card> resourcesCards) {
        this.resourcesCards = resourcesCards;
    }

    public ArrayList<Card> getDevelopmentCards() {
        return developmentCards;
    }

    public void setDevelopmentCards(ArrayList<Card> developmentCards) {
        this.developmentCards = developmentCards;
    }

    public ArrayList<Card> getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(ArrayList<Card> soldiers) {
        this.soldiers = soldiers;
    }

    public ArrayList<Card> getMonuments() {
        return monuments;
    }

    public void setMonuments(ArrayList<Card> monuments) {
        this.monuments = monuments;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.ownerID;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bank other = (Bank) obj;
        if (this.ownerID != other.ownerID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Bank{" + "ownerID=" + ownerID + ", hasLongestRoad=" + hasLongestRoad + ", hasLargestArmy=" + hasLargestArmy + ", resourcesCards=" + resourcesCards + ", developmentCards=" + developmentCards + ", soldiers=" + soldiers + ", monuments=" + monuments + '}';
    }

}
