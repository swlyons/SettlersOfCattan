package shared.data;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import shared.definitions.DevCardType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ddennis
 */
public class DevCardList implements Serializable {

    private Integer monopoly;
    private Integer monument;
    private Integer roadBuilding;
    private Integer soldier;
    private Integer yearOfPlenty;

    public DevCardList() {
        this.monopoly = 0;
        this.monument = 0;
        this.roadBuilding = 0;
        this.soldier = 0;
        this.yearOfPlenty = 0;
    }

    public DevCardList(boolean isMainBankInitializer) {
        if (isMainBankInitializer) {
            monopoly = 2;
            monument = 5;
            roadBuilding = 2;
            soldier = 14;
            yearOfPlenty = 2;
        } else {
            monopoly = 0;
            monument = 0;
            roadBuilding = 0;
            soldier = 0;
            yearOfPlenty = 0;
        }
    }

    public DevCardList(List<PlayerInfo> players) {
        this.monopoly = 2;
        this.monument = 5;
        this.roadBuilding = 2;
        this.soldier = 14;
        this.yearOfPlenty = 2;

        for (PlayerInfo p : players) {
            this.monopoly -= p.getOldDevCards().getMonopoly();
            this.monument -= p.getOldDevCards().getMonument();
            this.monument -= p.getMonuments();
            this.roadBuilding -= p.getOldDevCards().getRoadBuilding();
            this.yearOfPlenty -= p.getOldDevCards().getYearOfPlenty();
            this.soldier -= p.getOldDevCards().getSoldier();
            this.soldier -= p.getSoldiers();

        }
    }

    public DevCardList(Integer monopoly, Integer monument, Integer roadBuilding, Integer soldier, Integer yearOfPlenty) {
        this.monopoly = monopoly;
        this.monument = monument;
        this.roadBuilding = roadBuilding;
        this.soldier = soldier;
        this.yearOfPlenty = yearOfPlenty;
    }

    public Integer getMonopoly() {
        return monopoly;
    }

    public void setMonopoly(Integer monopoly) {
        this.monopoly = monopoly;
    }

    public void addMonopoly() {
        this.monopoly++;
    }

    public void removeMonopoly() {
        this.monopoly--;
    }

    public Integer getMonument() {
        return monument;
    }

    public void setMonument(Integer monument) {
        this.monument = monument;
    }

    public void addMonument() {
        this.monument++;
    }

    public void removeMonument() {
        this.monument--;
    }

    public Integer getRoadBuilding() {
        return roadBuilding;
    }

    public void setRoadBuilding(Integer roadBuilding) {
        this.roadBuilding = roadBuilding;
    }

    public void addRoadBuilding() {
        this.roadBuilding++;
    }

    public void removeRoadBuilding() {
        this.roadBuilding--;
    }

    public Integer getSoldier() {
        return soldier;
    }

    public void setSoldier(Integer soldier) {
        this.soldier = soldier;
    }

    public void addSoldier() {
        this.soldier++;
    }

    public void removeSoldier() {
        this.soldier--;
    }

    public Integer getYearOfPlenty() {
        return yearOfPlenty;
    }

    public void setYearOfPlenty(Integer yearOfPlenty) {
        this.yearOfPlenty = yearOfPlenty;
    }

    public void addYearOfPlenty() {
        this.yearOfPlenty++;
    }

    public void removeYearOfPlenty() {
        this.yearOfPlenty--;
    }

    public DevCardType selectRandomDevCard() {
        Random r = new Random();
        Integer cardType = r.nextInt(totalCardsRemaining()) + 1;
        if (cardType <= this.monopoly) {
            return DevCardType.MONOPOLY;
        } else if (cardType <= this.monopoly + this.monument) {
            return DevCardType.MONUMENT;
        } else if (cardType <= this.monopoly + this.monument + this.roadBuilding) {
            return DevCardType.ROAD_BUILD;
        } else if (cardType <= this.monopoly + this.monument + this.roadBuilding + this.yearOfPlenty) {
            return DevCardType.YEAR_OF_PLENTY;
        } else {
            return DevCardType.SOLDIER;
        }
    }

    public Integer totalCardsRemaining() {
        return this.monopoly + this.monument + this.roadBuilding + this.yearOfPlenty + this.soldier;
    }

    @Override
    public String toString() {
        return "{" + "\"monopoly\" : " + monopoly + ", \"monument\" : " + monument + ", \"roadBuilding\" : " + roadBuilding
                + ", \"soldier\" : " + soldier + ", \"yearOfPlenty\" : " + yearOfPlenty + "}";
    }

}
