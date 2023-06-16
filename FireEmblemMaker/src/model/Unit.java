package model;

import java.util.ArrayList;
import java.util.Random;

public class Unit {

	private int maxHP;
	private int currentHP;
	private int[] stats;
	private int[] growths;
	private String name;
	private UnitClass unitClass;
	private int hpGrowth;
	private Item[] inventory;
	private int[] inventoryDurabilities;
	private int level;
	private int experience;
	private ArrayList<Integer> proficiencies;
	
	public static final int MAX_EXP = 100;
	
	public Unit(String name, UnitClass unitClass, int maxHP, int hpGrowth, int[] stats,
			int[] growths, int invSize, int level) {
		this.name = name;
		this.unitClass = unitClass;
		this.maxHP = Math.max(maxHP, stats[stats.length - 1]);
		this.currentHP = this.maxHP;
		this.hpGrowth = Math.max(hpGrowth, growths[growths.length - 1]);
		this.stats = stats;
		this.growths = growths;
		for (int q = 0; q < unitClass.getMinStats().length; q++) {
			stats[q] = Math.max(stats[q], unitClass.getMinStats()[q]);
			growths[q] = Math.max(growths[q], unitClass.getMinGrowths()[q]);
		}
		this.inventory = new Item[invSize];
		this.proficiencies = new ArrayList<>(unitClass.getEquippableTypes().size());
		for (int q = 0; q < unitClass.getEquippableTypes().size(); q++) {
			proficiencies.add(0);
		}
	}

	public int getMaxHP() {
		return maxHP;
	}

	public int getCurrentHP() {
		return currentHP;
	}

	public int[] getStats() {
		return stats;
	}

	public int[] getGrowths() {
		return growths;
	}

	public String getName() {
		return name;
	}

	public UnitClass getUnitClass() {
		return unitClass;
	}

	public int getHpGrowth() {
		return hpGrowth;
	}
	
	public Item[] getInventory() {
		return inventory;
	}
	public Item getEquippedItem() {
		return inventory[0];
	}
	public int[] getInventoryDurabilities() {
		return inventoryDurabilities;
	}
	public int getLevel() {
		return level;
	}
	public ArrayList<Integer> getProficiencies() {
		return proficiencies;
	}
	public int[] addExperience(int exp) {
		experience += exp;
		if (experience >= MAX_EXP) {
			experience -= MAX_EXP;
			int[] ret = new int[stats.length + 1]; //Plus 1 for HP
			Random rng = new Random();
			for (int q = 0; q < stats.length; q++) {
				if (growths[q] > rng.nextInt(100)) {
					stats[q]++;
					ret[q] = 1;
				}
			}
			if (hpGrowth > rng.nextInt(100)) {
				maxHP++;
				ret[ret.length - 1] = 1;
			}
			return ret;
		}
		return null;
	}
}
