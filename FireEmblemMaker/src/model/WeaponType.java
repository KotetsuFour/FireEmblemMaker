package model;

public class WeaponType {

	private String name;
	
	private WeaponType advantage;
	
	public WeaponType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setAdvantage(WeaponType adv) {
		this.advantage = adv;
	}
	public WeaponType getAdvantage() {
		return advantage;
	}
	
	public boolean isAdvantageousAgainst(Weapon w) {
		return advantage == w.getType();
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
