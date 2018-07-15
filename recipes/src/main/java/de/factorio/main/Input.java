package de.factorio.main;

import java.util.HashMap;

public class Input {

	private HashMap<String, Recipe> recipes;
	private String template = "templates/nodes.ftl";

	public Input(HashMap<String, Recipe> hashMap) {
		this.recipes = hashMap;
	}

	public HashMap<String, Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(HashMap<String, Recipe> recipes) {
		this.recipes = recipes;
	}

	public String getTemplate() {
		return template;
	}
}
