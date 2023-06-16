package model;

public class UnitType {

	private String name;
	
	private boolean mount;
	private boolean mountainWalk;
	private boolean desertWalk;
	private boolean waterWalk;
	private boolean flying;
	private boolean seize;
	private boolean steal;
	private boolean invigorate;
	private boolean aoeInvigorate;
	private boolean aoeHeal;
	private boolean astra;
	private boolean luna;
	private boolean sol;
	private boolean shield;
	private boolean kill;
	
	public UnitType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public boolean isMount() {
		return mount;
	}

	public void setMount(boolean mount) {
		this.mount = mount;
	}

	public boolean isMountainWalk() {
		return mountainWalk;
	}

	public void setMountainWalk(boolean mountainWalk) {
		this.mountainWalk = mountainWalk;
	}

	public boolean isDesertWalk() {
		return desertWalk;
	}

	public void setDesertWalk(boolean desertWalk) {
		this.desertWalk = desertWalk;
	}

	public boolean isFlying() {
		return flying;
	}

	public void setFlying(boolean flying) {
		this.flying = flying;
	}

	public boolean isSeize() {
		return seize;
	}

	public void setSeize(boolean seize) {
		this.seize = seize;
	}

	public boolean isSteal() {
		return steal;
	}

	public void setSteal(boolean steal) {
		this.steal = steal;
	}

	public boolean isInvigorate() {
		return invigorate;
	}

	public void setInvigorate(boolean invigorate) {
		this.invigorate = invigorate;
	}

	public boolean isAoeInvigorate() {
		return aoeInvigorate;
	}

	public void setAoeInvigorate(boolean aoeInvigorate) {
		this.aoeInvigorate = aoeInvigorate;
	}

	public boolean isAoeHeal() {
		return aoeHeal;
	}

	public void setAoeHeal(boolean aoeHeal) {
		this.aoeHeal = aoeHeal;
	}

	public boolean isAstra() {
		return astra;
	}

	public void setAstra(boolean astra) {
		this.astra = astra;
	}

	public boolean isLuna() {
		return luna;
	}

	public void setLuna(boolean luna) {
		this.luna = luna;
	}

	public boolean isSol() {
		return sol;
	}

	public void setSol(boolean sol) {
		this.sol = sol;
	}

	public boolean isShield() {
		return shield;
	}

	public void setShield(boolean shield) {
		this.shield = shield;
	}

	public boolean isKill() {
		return kill;
	}

	public void setKill(boolean kill) {
		this.kill = kill;
	}
	
	public void setWaterWalk(boolean waterWalk) {
		this.waterWalk = waterWalk;
	}
	
	public boolean isWaterWalk() {
		return waterWalk;
	}
}
