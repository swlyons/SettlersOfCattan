package client.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import client.data.Bank;
import client.data.Card;
import client.data.Player;

public class ResourceManager {

    private ArrayList<Bank> gameBanks;
    private Map<Award, Player> awardMap = new HashMap<Award, Player>();

    public ResourceManager() {
        gameBanks = new ArrayList<Bank>();
        awardMap = new HashMap<Award, Player>();
    }

    public ResourceManager(ArrayList<Bank> gameBanks, Map<Award, Player> awardMap) {
        this.gameBanks = gameBanks;
        this.awardMap = awardMap;
    }

    public ArrayList<Bank> getGameBanks() {
        return gameBanks;
    }

    public void setGameBanks(ArrayList<Bank> gameBanks) {
        this.gameBanks = gameBanks;
    }

    public Map<Award, Player> getAwardMap() {
        return awardMap;
    }

    public void setAwardMap(Map<Award, Player> awardMap) {
        this.awardMap = awardMap;
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
    public boolean transferCard(int startIndex, int destinationIndex, Card cardType) {
        // if the card from the player is not available. return false.

        return false;
    }

    /**
     *
     * @param bankIndex
     * @param cardType
     * @pre bankIndex must be an index of either an existing player or the bank
     * and cardType must be an actual cardType
     *
     * @post returns if the player (or bank) has the specified cardType
     */
    public boolean hasCardAvailable(int bankIndex, int cardType) {
        boolean isAvailable = false;

        return isAvailable;
    }

    /**
     *
     * @pre largest army award must be in the game
     * @post returns if the largest army award has been given
     */
    public boolean awardLargestArmy() {
        boolean awarded = false;

        return awarded;
    }

    /**
     *
     * @pre longest road award must be in the game
     * @post returns if the longest road award has been given
     */
    public boolean awardLongestRoad() {
        boolean awarded = false;

        return awarded;
    }

    /**
     * @pre there must be development cards in the game
     *
     * @post will have shuffled to development cards in the deck
     */
    public void shuffleDevelopmentCards() {

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
    public void useSoldier(int bankIndex) {

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
    public void useMonument(int bankIndex) {

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
    public void useMonopoly(int bankIndex) {

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
    public void useYearOfPlenty(int bankIndex) {

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
    public void useRoadBuilding(int bankIndex) {

    }

    /**
     *
     * @param bankIndex
     *
     * @pre bankIndex must be the index of either and existing player or game
     * bank
     *
     * @post the player/bank specified by the bankIndex will have bought another
     * development card
     */
    public void buyDevelopmentCard(int bankIndex) {

    }

    /**
     * @pre assumes that the player exists and has cards to be made usable
     *
     * @post will have made the cards usable for current player
     */
    public void makeCardsUsable() {

    }

    //player has seven or more card. The robber was moved. The player must now discard half of their cards
    public void playerDiscardsHalfCards(int playerId) {

    }
    
    //called after checking to see if the place is valid for a settlement or city.
    // Subtract from the person's bank a settlement and return true.
    // if none are left return false
    public boolean placedSettlement(int playerId){
        return false;
    }

     //called after checking to see if the place is valid for a settlement or city.
     //Subtract from the person's bank a city and add to the person's bank a settlement return true
    // if none are left return false (or if somehow this method was called and the person has 5 settlements already)
    public boolean placedCity(int playerId){
        return false;
    }
    
    // called after checking to see if the place is valid for a road.
    // Subtract from the person's bank a road and return true.
    // if none are left return false
    public boolean placedRoad(int playerId){
        return false;
    }

    @Override
    public String toString() {
        return "ResourceManager{" + "gameBanks=" + gameBanks + ", awardMap=" + awardMap + '}';
    }

}
