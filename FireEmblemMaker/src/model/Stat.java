package model;

public class Stat {

	private String name;
	private String abbr;
	private int absoluteMin;
	private int absoluteMax;
	
	public Stat(String name, String abbr, int min, int max) {
		this.name = name;
		this.abbr = abbr;
		this.absoluteMin = min;
		this.absoluteMax = max;
	}
	public String getName() {
		return name;
	}
	public String getAbbreviation() {
		return abbr;
	}
	public int getAbsoluteMin() {
		return absoluteMin;
	}
	public int getAbsoluteMax() {
		return absoluteMax;
	}
}
