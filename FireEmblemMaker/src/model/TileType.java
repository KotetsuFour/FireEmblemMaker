package model;

public class TileType {

	private String name;
	private int groundCost;
	private int flyingCost;
	private int avoidanceBonus;

	public TileType(String name, int ground, int flying, int avo) {
		this.name = name;
		this.groundCost = ground;
		this.flyingCost = flying;
		this.avoidanceBonus = avo;
	}

	public String getName() {
		return name;
	}

	public int getAvoidanceBonus() {
		return avoidanceBonus;
	}

	public int getGroundCost() {
		return groundCost;
	}

	public int getFlyingCost() {
		return flyingCost;
	}

	
}
