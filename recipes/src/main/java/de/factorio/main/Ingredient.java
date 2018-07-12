package de.factorio.main;

public class Ingredient {

	private String type;
	private String count;

	public Ingredient(String type, String count) {
		this.type = type;
		this.count = count;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Ingredient [type=" + type + ", count=" + count + "]";
	}

}
