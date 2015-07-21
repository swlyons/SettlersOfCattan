package client.managers;

import java.util.List;
import java.util.ArrayList;
import client.data.Bank;
import client.data.DevCardList;
import client.data.ResourceList;
import shared.definitions.DevCardType;

public class ResourceManager {

    private List<Bank> gameBanks;
    private final int mainBankIndex = 4;

    public ResourceManager() {
        gameBanks = new ArrayList<Bank>();
        for (int i = 0; i <= 4; i++) {
            if (i != mainBankIndex) {
                gameBanks.add(new Bank(false));
                gameBanks.get(i).setOwnerID(i);
            } else {
                gameBanks.add(new Bank(true));
            }
        }
    }

    public ResourceManager(ArrayList<Bank> gameBanks) {
        this.gameBanks = gameBanks;
    }

    public List<Bank> getGameBanks() {
        return gameBanks;
    }

    public void setGameBanks(List<Bank> gameBanks) {
        this.gameBanks = gameBanks;
    }

    /**
     *
     * @param startIndex
     * @param destinationIndex
     * @param cardType
     *
     * @pre startIndex and destinationIndex are actual indexes of players and
     * cardType is an actual type of card
     *
     * @post a card specified by the cardType will have been transfered from the
     * player with the startIndex to the player with the destinationIndex
     * returns success of transaction
     */
    public boolean transferResourceCard(int startIndex, int destinationIndex, ResourceList request) {
        // if the card from the player is not available. return false.
        ResourceList start = gameBanks.get(startIndex).getResourcesCards();
        ResourceList destination = gameBanks.get(destinationIndex).getResourcesCards();

        if (start.hasCardsAvailable(request)) {

            start.removeBrick(request.getBrick());
            start.removeOre(request.getOre());
            start.removeWheat(request.getWheat());
            start.removeSheep(request.getSheep());
            start.removeWood(request.getWood());

            destination.addBrick(request.getBrick());
            destination.addOre(request.getOre());
            destination.addWheat(request.getWheat());
            destination.addSheep(request.getSheep());
            destination.addWood(request.getWood());
            return true;

        } else {
            return false;
        }

    }

    /**
     *
     * @pre largest army award must be in the game
     * @post returns if the largest army award has been given
     */
    public int moveLargestArmy() {
        int max = -1;
        int previousOwnerIndex = -1;
        for (int i = 0; i < gameBanks.size(); i++) {
            Bank b = gameBanks.get(i);
            if (b.HasLargestArmy()) {
                max = b.getSoldiers();
                previousOwnerIndex = i;
            }
        }

        int newOwnerIndex = previousOwnerIndex;
        for (int i = 0; i < gameBanks.size(); i++) {
            Bank b = gameBanks.get(i);
            if (b.getSoldiers() > max && i != previousOwnerIndex) {
                // Remove Largest army from previous owner:
                Bank prev = gameBanks.get(previousOwnerIndex);
                prev.setHasLargestArmy(false);

                // Give it to the new owner
                b.setHasLargestArmy(true);
                newOwnerIndex = i;
            }
        }

        return newOwnerIndex;
    }

    /**
     *
     * @pre longest road award must be in the game
     * @post returns if the longest road award has been given
     */
    public boolean moveLongestRoad() {
        boolean changed = false;

        int max = -1;
        int previousOwnerIndex = -1;
        for (int i = 0; i < gameBanks.size(); i++) {
            Bank b = gameBanks.get(i);
            if (b.HasLongestRoad()) {
                max = 15 - b.getRoads();
                previousOwnerIndex = i;
            }
        }

        for (int i = 0; i < gameBanks.size(); i++) {
            Bank b = gameBanks.get(i);
            if ((15 - b.getSoldiers()) > max && i != previousOwnerIndex) {
                // Remove Largest army from previous owner:
                Bank prev = gameBanks.get(previousOwnerIndex);
                prev.setHasLongestRoad(false);

                // Give it to the new owner
                b.setHasLongestRoad(true);
                changed = true;
            }
        }

        return changed;
    }

    /**
     *
     * @param bankIndex
     *
     * @pre bankIndex must be the index of either and existing player or game
     * bank
     *
     * @post the soldier card for the the specified bankIndex(player or bank)
     * will be used
     */
    public void soldierUsed(int bankIndex) {
        Bank b = gameBanks.get(bankIndex);
        b.getDevelopmentCards().removeSoldier();
        b.addSoldier();
    }

    /**
     *
     * @param bankIndex
     *
     * @pre bankIndex must be the index of either and existing player or game
     * bank
     *
     * @post the monument card for the the specified bankIndex(player or bank)
     * will be used
     */
    public void monumentUsed(int bankIndex) {
        Bank b = gameBanks.get(bankIndex);
        b.getDevelopmentCards().removeMonument();
        b.addMonument();
    }

    /**
     *
     * @param bankIndex
     *
     * @pre bankIndex must be the index of either and existing player or game
     * bank
     *
     * @post the monopoly card for the the specified bankIndex(player or bank)
     * will be used
     */
    public void monopolyUsed(int bankIndex) {
        Bank b = gameBanks.get(bankIndex);
        b.getDevelopmentCards().removeMonopoly();
    }

    /**
     *
     * @param bankIndex
     *
     * @pre bankIndex must be the index of either and existing player or game
     * bank
     *
     * @post the year of plenty card for the the specified bankIndex(player or
     * bank) will be used
     */
    public void yearOfPlentyUsed(int bankIndex) {
        Bank b = gameBanks.get(bankIndex);
        b.getDevelopmentCards().removeYearOfPlenty();
    }

    /**
     *
     * @param bankIndex
     *
     * @pre bankIndex must be the index of either and existing player or game
     * bank
     *
     * @post the roadBuilding card for the the specified bankIndex(player or
     * bank) will be used
     */
    public void roadBuildingUsed(int bankIndex) {
        Bank b = gameBanks.get(bankIndex);
        b.getDevelopmentCards().removeRoadBuilding();
    }

    /**
     *
     * @param bankIndex
     * @throws Exception
     *
     * @pre bankIndex must be the index of either and existing player or game
     * bank
     *
     * @post the player/bank specified by the bankIndex will have bought another
     * development card
     */
    public DevCardType devCardBought(int destinationIndex) {
        Bank mainBank = gameBanks.get(mainBankIndex);

        DevCardType type = mainBank.getDevelopmentCards().selectRandomDevCard();
        transferDevelopmentCard(destinationIndex, type);
        return type;
    }

    /**
     *
     * @param destinationIndex
     * @param requestType
     *
     * @pre destinationIndex are actual indexes of players and requestType is an
     * actual type of card
     *
     * @post a card specified by the requestType will have been transfered from
     * the main bank to the player with the destinationIndex returns success of
     * transaction
     */
    public boolean transferDevelopmentCard(int destinationIndex, DevCardType requestType) {
        // this will only be called by the buyDevelopment card, which will never
        // request a card the main bank doesn't have.
        DevCardList start = gameBanks.get(mainBankIndex).getDevelopmentCards();
        DevCardList destination = gameBanks.get(destinationIndex).getUnusableDevCards();

        if (requestType == DevCardType.MONOPOLY) {
            start.removeMonopoly();
            destination.addMonopoly();
        } else if (requestType == DevCardType.MONUMENT) {
            start.removeMonument();
            destination.addMonument();
        } else if (requestType == DevCardType.ROAD_BUILD) {
            start.removeRoadBuilding();
            destination.addRoadBuilding();
        } else if (requestType == DevCardType.YEAR_OF_PLENTY) {
            start.removeYearOfPlenty();
            destination.addYearOfPlenty();
        } else {
            start.removeSoldier();
            destination.addSoldier();
        }

        return true;
    }

    /**
     * @pre assumes that the player exists and has cards to be made usable
     *
     * @post will have made the cards usable for current player
     */
    public void makeCardsUsable() {
        for (Bank b : gameBanks) {
            b.makeCardsUsable();
        }
    }

    // player has seven or more card. The robber was moved. The player must now
    // discard half of their cards
    public void playerDiscardsHalfCards(int playerIndex, ResourceList toDiscard) {
        transferResourceCard(playerIndex, mainBankIndex, toDiscard);
    }

    // called after checking to see if the place is valid for a settlement or
    // city.
    // Subtract from the person's bank a settlement and return true.
    // if none are left return false
    public boolean placedSettlement(int playerIndex) {
        Bank b = gameBanks.get(playerIndex);
        if (b.getSettlements() > 0) {
            b.removeSettlement();
            return true;
        } else {
            return false;
        }

    }

    // called after checking to see if the place is valid for a settlement or
    // city.
    // Subtract from the person's bank a city and add to the person's bank a
    // settlement return true
    // if none are left return false (or if somehow this method was called and
    // the person has 5 settlements already)
    public boolean placedCity(int playerIndex) {
        Bank b = gameBanks.get(playerIndex);
        if (b.getCities() > 0) {
            b.addSettlement();
            b.removeCity();
            return true;
        } else {
            return false;
        }
    }

    // called after checking to see if the place is valid for a road.
    // Subtract from the person's bank a road and return true.
    // if none are left return false
    public boolean placedRoad(int playerIndex) {
        Bank b = gameBanks.get(playerIndex);
        if (b.getRoads() > 0) {
            b.removeRoad();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "ResourceManager{" + "gameBanks=" + gameBanks.toString() + "}";
    }

}
