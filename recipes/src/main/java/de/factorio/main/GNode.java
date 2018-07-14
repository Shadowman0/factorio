package de.factorio.main;

public class GNode {

	private String id;
	private int group;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public GNode(String id, int group) {
		this.id = id;
		this.group = group;
	}

}
