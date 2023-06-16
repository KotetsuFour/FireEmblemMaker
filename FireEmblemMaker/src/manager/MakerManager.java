package manager;

import java.util.ArrayList;
import java.util.Stack;

import model.Stat;
import model.TileType;
import model.Unit;
import model.UnitClass;
import model.UnitType;
import model.Weapon;
import model.WeaponType;

public class MakerManager {

	public static final String ACCURACY_ABBR = "ACC";
	public static final String AVOIDANCE_ABBR = "AVO";
	public static final String CRITICAL_ABBR = "CRT";
	public static final String CRITICAL_AVOID_ABBR = "CRA";
	public static final String STAFF_ACCURACY_ABBR = "MAC";
	public static final String STAFF_AVOIDANCE_ABBR = "MAV";
	public static final String ATTACK_SPEED_ABBR = "ATS";

	public static String accuracyCalculation;
	public static String avoidanceCalculation;
	public static String criticalCalculation;
	public static String critAvoidCalculation;
	public static String staffAccuracyCalculation;
	public static String staffAvoidCalculation;
	public static String attackSpeedCalculation;
	public static String physicalMightCalculation;
	public static String physicalDefenseCalculation;
	public static String magicMightCalculation;
	public static String magicDefenseCalculation;
	
	public static ArrayList<Stat> stats = new ArrayList<>();
	public static ArrayList<Stat> wepStats = new ArrayList<>();
	public static ArrayList<UnitType> unitTypes = new ArrayList<>();
	public static ArrayList<WeaponType> weaponTypes = new ArrayList<>();
	public static ArrayList<Weapon> weapons = new ArrayList<>();
	public static ArrayList<UnitClass> unitClasses = new ArrayList<>();
	public static ArrayList<Unit> units = new ArrayList<>();
	public static ArrayList<TileType> tileTypes = new ArrayList<>();
	
	public static int inventorySize = 5;
	
	public static boolean weaponDurability = true;
	
	public static void setDefaults() {
		stats.add(new Stat("Strength", "STR", 0, 20));
		stats.add(new Stat("Magic", "MAG", 0, 20));
		stats.add(new Stat("Skill", "SKL", 0, 20));
		stats.add(new Stat("Speed", "SPD", 0, 20));
		stats.add(new Stat("Luck", "LCK", 0, 20));
		stats.add(new Stat("Defense", "DEF", 0, 20));
		stats.add(new Stat("Resistance", "RES", 0, 20));
		
		wepStats.add(new Stat("Might", "MT", 0, 20));
		wepStats.add(new Stat("Accuracy", "HIT", 0, 100));
		wepStats.add(new Stat("Critical", "CRT", 0, 100));
		wepStats.add(new Stat("Weight", "WT", 0, 20));
		
		accuracyCalculation = convertToRPN("#2 * 2 + #4 + W1"); //Skill times 2 plus luck plus weapon-accuracy
		avoidanceCalculation = convertToRPN("#3 * 2 + #4"); //Speed times 2 plus luck
		criticalCalculation = convertToRPN("#2 + W2"); //Skill plus weapon-critical
		critAvoidCalculation = convertToRPN("#4"); //Luck
		staffAccuracyCalculation = convertToRPN("#1 * 2 + #4"); //Magic times 2 plus luck
		staffAvoidCalculation = convertToRPN("#6 * 2 + #4"); //Resistance times 2 plus luck
		attackSpeedCalculation = convertToRPN("#3 - P ( W3 - #0 )"); //Speed minus max between 0 and (weapon-weight minus strength)
		physicalMightCalculation = convertToRPN("#0 + W0"); //Strength plus weapon-might
		physicalDefenseCalculation = convertToRPN("#5"); //Defense
		magicMightCalculation = convertToRPN("#1 + W0"); //Magic plus weapon-might
		magicDefenseCalculation = convertToRPN("#6"); //Resistance
	}
	
	public static void setDefaultUnitsAndWeapons() {
		UnitType prota = new UnitType("Protagonist");
		prota.setSeize(true);
		unitTypes.add(prota);
		UnitType astra = new UnitType("Meteor Fighter");
		astra.setAstra(true);
		unitTypes.add(astra);
		UnitType lunar = new UnitType("Lunar Fighter");
		lunar.setLuna(true);
		unitTypes.add(lunar);
		UnitType solar = new UnitType("Solar Fighter");
		solar.setSol(true);
		unitTypes.add(solar);
		UnitType thief = new UnitType("Thief");
		thief.setSteal(true);
		unitTypes.add(thief);
		UnitType dance = new UnitType("Invigorator");
		dance.setInvigorate(true);
		unitTypes.add(dance);
		UnitType adjDn = new UnitType("AOE Invigorator");
		adjDn.setAoeInvigorate(true);
		unitTypes.add(adjDn);
		UnitType montn = new UnitType("Mountain Walker");
		montn.setMountainWalk(true);
		unitTypes.add(montn);
		UnitType water = new UnitType("Water Walker");
		water.setWaterWalk(true);
		unitTypes.add(water);
		UnitType desrt = new UnitType("Desert Walker");
		desrt.setDesertWalk(true);
		unitTypes.add(desrt);
		UnitType horse = new UnitType("Horse Rider");
		horse.setMount(true);
		unitTypes.add(horse);
		UnitType mount = new UnitType("Mounted");
		mount.setMount(true);
		unitTypes.add(mount);
		UnitType flyer = new UnitType("Flying");
		flyer.setFlying(true);
		unitTypes.add(flyer);
		UnitType draco = new UnitType("Draconian");
		unitTypes.add(draco);
		UnitType healr = new UnitType("Healer");
		healr.setAoeHeal(true);
		unitTypes.add(healr);
		UnitType block = new UnitType("Shielded");
		block.setShield(true);
		unitTypes.add(block);
		UnitType killr = new UnitType("Assassin");
		killr.setKill(true);
		unitTypes.add(killr);
		
		WeaponType sword = new WeaponType("Sword");
		weaponTypes.add(sword);
		WeaponType lance = new WeaponType("Lance");
		weaponTypes.add(lance);
		WeaponType axe = new WeaponType("Axe");
		weaponTypes.add(axe);
		WeaponType bow = new WeaponType("Bow");
		weaponTypes.add(bow);
		WeaponType anima = new WeaponType("Anima");
		weaponTypes.add(anima);
		WeaponType dark = new WeaponType("Dark");
		weaponTypes.add(dark);
		WeaponType light = new WeaponType("Light");
		weaponTypes.add(light);
		
		//TODO classes
		
		//TODO weapons
	}
	
	public static void setDefaultTiles() {
		tileTypes.add(new TileType("Grass", 1, 1, 0));
		tileTypes.add(new TileType("Sand", 2, 1, 5));
		tileTypes.add(new TileType("Tree", 2, 1, 20));
		tileTypes.add(new TileType("Thicket", 6, 1, 40));
		tileTypes.add(new TileType("Mountain", 4, 1, 30));
		tileTypes.add(new TileType("Peak", 4, 1, 40));
		tileTypes.add(new TileType("House", 1, 1, 10));
		tileTypes.add(new TileType("Door", 1, 1, 10));
		tileTypes.add(new TileType("Rubble", 2, 1, 0));
		tileTypes.add(new TileType("Pillar", 2, 6, 20));
		tileTypes.add(new TileType("Gate", 1, 1, 20));
		tileTypes.add(new TileType("Wall", Integer.MAX_VALUE, 1, 0));
		tileTypes.add(new TileType("Shallow Water", 3, 1, 10));
		tileTypes.add(new TileType("Deep Water", Integer.MAX_VALUE, 1, 10));
		tileTypes.add(new TileType("Cave", 1, 6, 10));
		tileTypes.add(new TileType("Molten Rock", 3, 3, 10));
		tileTypes.add(new TileType("Floor", 1, 5, 0));
		tileTypes.add(new TileType("Throne", 1, 5, 30));
		tileTypes.add(new TileType("Chest", 1, 5, 0));
		tileTypes.add(new TileType("Wetland", 2, 1, 5));
		tileTypes.add(new TileType("Wasteland", 1, 1, 0));
		tileTypes.add(new TileType("Road", 1, 1, 0));
		tileTypes.add(new TileType("Warp Tile", 2, 3, 0));
		tileTypes.add(new TileType("Deck", 1, 1, 0));
		tileTypes.add(new TileType("Dock", 1, 1, 5));
		tileTypes.add(new TileType("Snow", 2, 1, 5));
	}
	
	public static void main(String[] args) {
		setDefaults();
		System.out.println(accuracyCalculation);
		System.out.println(avoidanceCalculation);
		System.out.println(attackSpeedCalculation);
		
		UnitType type = new UnitType("Flying");
		type.setFlying(true);
		UnitType type2 = new UnitType("Regal");
		type2.setSeize(true);
		
		WeaponType lance = new WeaponType("Lance");
		WeaponType sword = new WeaponType("Sword");
		lance.setAdvantage(sword);
		
		Weapon peggiLance = new Weapon("Pegasus Lance", lance, 45, 50, 1, 2,
				new int[] {10, 90, 10, 3}, wepStats.size());
		
		UnitClass peggy = new UnitClass("Noble Pegasus Knight", 8,
				new int[] {0, 0, 10, 10, 10, 0, 5, 14},
				new int[] {0, 10, 30, 50, 40, 5, 10, 10});
		
		Unit erin = new Unit("Erin", peggy, 30, 50,
				new int[] {15, 10, 15, 15, 15, 15, 15},
				new int[] {50, 50, 50, 50, 50, 50, 50}, inventorySize, 10);
		erin.getInventory()[0] = peggiLance;
		
		System.out.println(getAccuracy(erin));
		System.out.println(getAvoidance(erin));
		System.out.println(getAttackSpeed(erin));
	}
	
	public static String convertToRPN(String calc) {
		Stack<String> stack = new Stack<>();
		StringBuilder sb = new StringBuilder();
		
		for (int q = 0; q < calc.length(); q++) {
			if (calc.charAt(q) == ' ') {
				continue;
			}
			if (calc.charAt(q) == '#' || calc.charAt(q) == 'W') {
				sb.append(calc.substring(q, q + 2));
				q++;
			} else if (Character.isDigit(calc.charAt(q))) {
				sb.append(calc.charAt(q));
			} else if (calc.charAt(q) == 'P') {
				String func = "" + calc.charAt(q);
				stack.push(func);
			} else if (calc.charAt(q) == '+' || calc.charAt(q) == '-' || calc.charAt(q) == '*'
					|| calc.charAt(q) == '/') {
				String s = "" + calc.charAt(q);
				String top = null;
				if (!stack.isEmpty()) {
					top = stack.peek();
				}
				while (top != null && !top.equals("(")
						&& (top.equals("*") || top.equals("/")
								|| top.equals("+") || top.equals("-"))) {
					sb.append(stack.pop());
					if (!stack.isEmpty()) {
						top = stack.peek();
					} else {
						top = null;
					}
				}
				stack.push(s);
			} else if (calc.charAt(q) == '(') {
				stack.push("" + calc.charAt(q));
			} else if (calc.charAt(q) == ')') {
				String s = stack.peek();
				while (!s.equals("(")) {
					if (stack.isEmpty()) {
						throw new IllegalArgumentException("Mismatched parentheses");
					}
					sb.append(stack.pop());
					s = stack.peek();
				}
				stack.pop();
				if (stack.peek().equals("P")) {
					sb.append(stack.pop());
				}
			} else {
				throw new IllegalArgumentException("Used an illegal character " + calc.charAt(q));
			}
		}
		while (!stack.isEmpty()) {
			sb.append(stack.pop());
		}
		return sb.toString().trim();
	}
	
	public static int performCalculation(Unit u, String calc) {
		Stack<Integer> stack = new Stack<>();
		for (int q = 0; q < calc.length(); q++) {
			if (calc.charAt(q) == ' ') {
				continue;
			}
			char c = calc.charAt(q);
			if (c == '*') {
				int second = stack.pop();
				int first = stack.pop();
				stack.push(first * second);
			} else if (c == '/') {
				int second = stack.pop();
				int first = stack.pop();
				stack.push(first / Math.max(1, second));
			} else if (c == '+') {
				int second = stack.pop();
				int first = stack.pop();
				stack.push(first + second);
			} else if (c == '-') {
				int second = stack.pop();
				int first = stack.pop();
				stack.push(first - second);
			} else if (c == 'P') {
				int param = stack.pop();
				stack.push(Math.max(param, 0));
			} else if (c == '#') {
				int num = Integer.parseInt(calc.charAt(q + 1) + "");
				q++;
				stack.push(u.getStats()[num]);
			} else if (c == 'W') {
				int num = Integer.parseInt(calc.charAt(q + 1) + "");
				q++;
				if (u.getEquippedItem() instanceof Weapon) {
					Weapon w = (Weapon)u.getEquippedItem();
					stack.push(w.getStats()[num]);
				} else {
					stack.push(0);
				}
			} else if (Character.isDigit(c)) {
				int num = Integer.parseInt(calc.charAt(q) + "");
				stack.push(num);
			}
		}
		return stack.pop();
	}
	public static int getAccuracy(Unit u) {
		return performCalculation(u, accuracyCalculation);
	}
	public static int getAvoidance(Unit u) {
		return performCalculation(u, avoidanceCalculation);
	}
	public static int getCritical(Unit u) {
		return performCalculation(u, criticalCalculation);
	}
	public static int getCriticalAvoid(Unit u) {
		return performCalculation(u, critAvoidCalculation);
	}
	public static int getStaffAccuracy(Unit u) {
		return performCalculation(u, staffAccuracyCalculation);
	}
	public static int getStaffAvoidance(Unit u) {
		return performCalculation(u, staffAvoidCalculation);
	}
	public static int getAttackSpeed(Unit u) {
		return performCalculation(u, attackSpeedCalculation);
	}
	public static int getPhysicalMight(Unit u) {
		return performCalculation(u, physicalMightCalculation);
	}
	public static int getPhysicalDefense(Unit u) {
		return performCalculation(u, physicalDefenseCalculation);
	}
	public static int getMagicMight(Unit u) {
		return performCalculation(u, magicMightCalculation);
	}
	public static int getMagicDefense(Unit u) {
		return performCalculation(u, magicDefenseCalculation);
	}

	
}
