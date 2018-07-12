package de.factorio.main;

import java.util.ArrayList;

public class Input {

	private ArrayList<Recipe> recipes;

	public Input(ArrayList<Recipe> recipes) {
		this.recipes = recipes;
	}

	public ArrayList<Recipe> getRecipes() {
		return recipes;
	}

	public void setRecipes(ArrayList<Recipe> recipes) {
		this.recipes = recipes;
	}

	public String getTemplate() {
		return "templates/nodes.ftl";
	}

}
