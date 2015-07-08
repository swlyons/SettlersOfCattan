package client.data;

import shared.definitions.ResourceType;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ddennis
 */
public class ResourceList {

	private int brick;
	private int ore;
	private int sheep;
	private int wheat;
	private int wood;

	public ResourceList() {
		brick = 0;
		ore = 0;
		sheep = 0;
		wheat = 0;
		wood = 0;
	}

	public ResourceList(boolean isMainBankInitializer) {
		if (isMainBankInitializer) {
			brick = 19;
			ore = 19;
			sheep = 19;
			wheat = 19;
			wood = 19;	
		} else {
			brick = 0;
			ore = 0;
			sheep = 0;
			wheat = 0;
			wood = 0;
		}
	}

	public ResourceList(ResourceType type) {
		brick = 0;
		ore = 0;
		sheep = 0;
		wheat = 0;
		wood = 0;

		switch (type) {
		case brick:
			brick++;
			break;
		case ore:
			ore++;
			break;
		case sheep:
			sheep++;
			break;
		case wheat:
			wheat++;
			break;
		case wood:
			wood++;
			break;
		default:
			// Should never occur
		}
	}

	public ResourceList(int brick, int ore, int sheep, int wheat, int wood) {
		this.brick = brick;
		this.ore = ore;
		this.sheep = sheep;
		this.wheat = wheat;
		this.wood = wood;
	}

	public int getBrick() {
		return brick;
	}

	public void setBrick(int brick) {
		this.brick = brick;
	}

	public void addBrick(int amount) {
		this.brick += amount;
	}

	public void removeBrick(int amount) {
		this.brick -= amount;
	}

	public int getOre() {
		return ore;
	}

	public void setOre(int ore) {
		this.ore = ore;
	}

	public void addOre(int amount) {
		this.ore += amount;
	}

	public void removeOre(int amount) {
		this.ore -= amount;
	}

	public int getSheep() {
		return sheep;
	}

	public void setSheep(int sheep) {
		this.sheep = sheep;
	}

	public void addSheep(int amount) {
		this.sheep += amount;
	}

	public void removeSheep(int amount) {
		this.sheep -= amount;
	}

	public int getWheat() {
		return wheat;
	}

	public void setWheat(int wheat) {
		this.wheat = wheat;
	}

	public void addWheat(int amount) {
		this.wheat += amount;
	}

	public void removeWheat(int amount) {
		this.wheat -= amount;
	}

	public int getWood() {
		return wood;
	}

	public void setWood(int wood) {
		this.wood = wood;
	}

	public void addWood(int amount) {
		this.wood += amount;
	}

	public void removeWood(int amount) {
		this.wood -= amount;
	}

	public void add(ResourceType type, int amount) {
		switch(type) {
		case brick:
			this.brick += amount;
			break;
		case ore:
			this.ore += amount;
			break;
		case sheep:
			this.sheep += amount;
			break;
		case wheat:
			this.wheat += amount;
			break;
		case wood:
			this.wood += amount;
			break;
		default:
			return;
		}
	}
	
	public boolean hasCardsAvailable(ResourceList request) {
		return request.getBrick() <= this.getBrick() && request.getOre() <= this.getOre()
				&& request.getSheep() <= this.getSheep() && request.getWheat() <= this.getWheat()
				&& request.getWheat() <= this.getWheat();
	}

	/**
	 * 
	 * @pre none
	 * 
	 * @post returns a sum of all the resources in the current resource list
	 * 
	 */
	public int getTotalResources() {
		return (brick + ore + sheep + wood + wheat);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + brick;
		result = prime * result + ore;
		result = prime * result + sheep;
		result = prime * result + wheat;
		result = prime * result + wood;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResourceList other = (ResourceList) obj;
		if (brick != other.getBrick())
			return false;
		if (ore != other.getOre())
			return false;
		if (sheep != other.getSheep())
			return false;
		if (wheat != other.getWheat())
			return false;
		if (wood != other.getWood())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "{" + "brick : " + brick + ", ore : " + ore + ", sheep : " + sheep + ", wheat : " + wheat + ", wood : "
				+ wood + '}';
	}

}
