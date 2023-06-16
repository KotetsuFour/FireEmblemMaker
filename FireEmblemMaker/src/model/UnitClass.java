package model;

import java.util.ArrayList;

public class UnitClass {

	private String name;
	
	private ArrayList<UnitType> types;

	private ArrayList<WeaponType> equippables;
	
	private UnitClass promotion;
	
	private int[] minStats;
	
	private int[] minGrowths;
	
	private int movement;
	
	public UnitClass(String name, int movement, int[] minStats, int[] minGrowths) {
		this.name = name;
		this.types = new ArrayList<>();
		this.equippables = new ArrayList<>();
		this.movement = movement;
		this.minStats = minStats;
		this.minGrowths = minGrowths;
	}
	
	public String getName() {
		return name;
	}
	public ArrayList<UnitType> getTypes() {
		return types;
	}
	public ArrayList<WeaponType> getEquippableTypes() {
		return equippables;
	}
	public void setPromotion(UnitClass uc) {
		this.promotion = uc;
	}
	public UnitClass getPromotion() {
		return promotion;
	}
	public int[] getMinStats() {
		return minStats;
	}
	public int[] getMinGrowths() {
		return minGrowths;
	}
	public int getMovement() {
		return movement;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
