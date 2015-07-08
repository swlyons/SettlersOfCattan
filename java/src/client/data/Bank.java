package client.data;

public class Bank {

    private int ownerID;
    private int settlements;
    private int cities;
    private int roads;
    private boolean hasLongestRoad;
    private boolean hasLargestArmy;
    private ResourceList resourcesCards;
    private DevCardList developmentCards;
    private DevCardList unusableDevCards;
    private int soldiers;
    private int monuments;
    private final int mainBankIndex = 4;

    public Bank(Player p, boolean hasLargestArmy, boolean hasLongestRoad) {
	    this.ownerID = p.getPlayerID();
	    this.hasLongestRoad = hasLongestRoad;
	    this.hasLargestArmy = hasLargestArmy;
	    this.resourcesCards = p.getResources();
	    this.developmentCards = p.getOldDevCards();
	    this.unusableDevCards = new DevCardList();
	    this.soldiers = p.getSoldiers();
	    this.monuments = p.getMonuments();
	    this.settlements = p.getSettlements();
	    this.cities = p.getCities();
	    this.roads = p.getRoads();
	}
    
    
    public Bank(int ownerID, boolean hasLongestRoad, boolean hasLargestArmy, ResourceList resourcesCards, DevCardList developmentCards, DevCardList unusableDevCards,
    			int soldiers, int monuments, int settlements, int cities, int roads) {
        this.ownerID = ownerID;
        this.hasLongestRoad = hasLongestRoad;
        this.hasLargestArmy = hasLargestArmy;
        this.resourcesCards = resourcesCards;
        this.developmentCards = developmentCards;
        this.unusableDevCards = unusableDevCards;
        this.soldiers = soldiers;
        this.monuments = monuments;
        this.settlements = settlements;
        this.cities = cities;
        this.roads = roads;
    }
    

    public Bank(ResourceList resourceCards, DevCardList developmentCards, boolean hasLargestArmy, boolean hasLongestRoad) {
        this.ownerID = mainBankIndex;
        this.hasLongestRoad = hasLongestRoad;
        this.hasLargestArmy = hasLargestArmy;
        this.resourcesCards = resourceCards;
        this.developmentCards = developmentCards;
        this.unusableDevCards = new DevCardList();
        this.soldiers = 0;
        this.monuments = 0;
        this.settlements = 0;
        this.cities = 0;
        this.roads = 0;	
    }


	public int getSettlements() {
        return settlements;
    }

    public void setSettlements(int settlements) {
        this.settlements = settlements;
    }

    public void removeSettlement(){
    	settlements--;
    }
    
    public void addSettlement() {
    	settlements++;
    }
    
    public int getCities() {
        return cities;
    }

    public void setCities(int cities) {
        this.cities = cities;
    }

    public void removeCity(){
    	cities--;
    }
    
    public int getRoads() {
        return roads;
    }

    public void setRoads(int roads) {
        this.roads = roads;
    }

    public void removeRoad(){
    	roads--;
    }
    
    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public boolean HasLongestRoad() {
        return hasLongestRoad;
    }

    public void setHasLongestRoad(boolean hasLongestRoad) {
        this.hasLongestRoad = hasLongestRoad;
    }

    public boolean HasLargestArmy() {
        return hasLargestArmy;
    }

    public void setHasLargestArmy(boolean hasLargestArmy) {
        this.hasLargestArmy = hasLargestArmy;
    }

    public ResourceList getResourcesCards() {
        return resourcesCards;
    }

    public void setResourcesCards(ResourceList resourcesCards) {
        this.resourcesCards = resourcesCards;
    }

    public DevCardList getDevelopmentCards() {
        return developmentCards;
    }

    public void setDevelopmentCards(DevCardList developmentCards) {
        this.developmentCards = developmentCards;
    }

    public int getSoldiers() {
        return soldiers;
    }

    public void setSoldiers(int soldiers) {
        this.soldiers = soldiers;
    }
    
    public void addSoldier() {
    	this.soldiers++;
    }

    public int getMonuments() {
        return monuments;
    }

    public void setMonuments(int monuments) {
        this.monuments = monuments;
    }
    
    public void addMonument(){
    	this.monuments++;
    }

	public DevCardList getUnusableDevCards() {
		return unusableDevCards;
	}

	public void setUnusableDevCards(DevCardList unusableDevCards) {
		this.unusableDevCards = unusableDevCards;
	}

	public void makeCardsUsable() {
		int monuments = unusableDevCards.getMonument();
		for(int i = 0; i < monuments; i++){
			this.unusableDevCards.removeMonument();
			this.developmentCards.addMonument();
		}
		int monopolies = unusableDevCards.getMonopoly();
		for(int i = 0; i < monopolies; i++){
			this.unusableDevCards.removeMonopoly();
			this.developmentCards.addMonopoly();
		}
		int yearsOfPlenty = unusableDevCards.getYearOfPlenty();
		for(int i = 0; i < yearsOfPlenty; i++){
			this.unusableDevCards.removeYearOfPlenty();
			this.developmentCards.addYearOfPlenty();
		}
		int roadBuildings = unusableDevCards.getRoadBuilding();
		for(int i = 0; i < roadBuildings; i++){
			this.unusableDevCards.removeRoadBuilding();
			this.developmentCards.addRoadBuilding();
		}
		int soldiers = unusableDevCards.getSoldier();
		for(int i = 0; i < soldiers; i++){
			this.unusableDevCards.removeSoldier();
			this.developmentCards.addSoldier();
		}		
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
