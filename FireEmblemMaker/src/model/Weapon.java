package model;

import java.util.ArrayList;

public class Weapon extends Item {

	private WeaponType type;
	private ArrayList<UnitType> effective;
	
	private StaffEffect effect;
	
	private int[] stats;
	
	private int[] unitStatsEffects;
	
	private boolean brave;
	
	private int maxDurability;
	private int durability;
	
	private int requiredProficiency;
	
	private int minRange;
	private int maxRange;
	
	public Weapon(String name, WeaponType type, int maxDurability, int requiredProficiency,
			int minRange, int maxRange, int[] wepStats, int numUnitStats) {
		super(name);
		this.type = type;
		this.effective = new ArrayList<>();
		this.maxDurability = maxDurability;
		this.durability = maxDurability;
		this.requiredProficiency = requiredProficiency;
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.stats = wepStats;
		this.unitStatsEffects = new int[numUnitStats];
	}
	
	public WeaponType getType() {
		return type;
	}

	public StaffEffect getEffect() {
		return effect;
	}

	public void setEffect(StaffEffect effect) {
		this.effect = effect;
	}

	public boolean isBrave() {
		return brave;
	}

	public void setBrave(boolean brave) {
		this.brave = brave;
	}

	public ArrayList<UnitType> getEffective() {
		return effective;
	}

	public int[] getStats() {
		return stats;
	}

	public int[] getUnitStatsEffects() {
		return unitStatsEffects;
	}

	public int getMaxDurability() {
		return maxDurability;
	}

	public int getDurability() {
		return durability;
	}

	public int getRequiredProficiency() {
		return requiredProficiency;
	}

	public int getMinRange() {
		return minRange;
	}

	public int getMaxRange() {
		return maxRange;
	}
	

}
