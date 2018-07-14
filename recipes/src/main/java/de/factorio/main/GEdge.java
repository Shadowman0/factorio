package de.factorio.main;

public class GEdge {

	private String source;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private String target;
	private int value;

	public GEdge(String source, String target, int value) {
		this.source = source;
		this.target = target;
		this.value = value;
	}

}
