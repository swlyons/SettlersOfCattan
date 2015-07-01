package client.data;

import shared.definitions.CatanColor;

public class Player {

	private String name;
	private CatanColor color;
	private int age;
	private int cities;
	private int settlements;
	private int roads;
	private int victoryPoints;
	private Bank hand;
	private int index;
	private int id;
	
	public Player() {
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CatanColor getColor() {
		return color;
	}
	public void setColor(CatanColor color) {
		this.color = color;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getCities() {
		return cities;
	}
	public void setCities(int cities) {
		this.cities = cities;
	}
	public int getSettlements() {
		return settlements;
	}
	public void setSettlements(int settlements) {
		this.settlements = settlements;
	}
	public int getRoads() {
		return roads;
	}
	public void setRoads(int roads) {
		this.roads = roads;
	}
	public int getVictoryPoints() {
		return victoryPoints;
	}
	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}
	public Bank getHand() {
		return hand;
	}
	public void setHand(Bank hand) {
		this.hand = hand;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
/*
+ name : String
+ color : CatanColor color
+ age : int
+ cities : int
+ settlements : int
+ roads :int
+ victoryPoints : int 
+ bank : ResourceList bank
+ index : int
+ id : GUID id
+ discarded : boolean
+ playedDevCard : boolean
+ monuments : int 
 */
	
}
