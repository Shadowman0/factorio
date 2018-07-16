package de.factorio.main;

import java.util.ArrayList;

public class RecipeTree {

	private String result;
	private ArrayList<RecipeTree> ingredientPaths;

	public RecipeTree(String result, ArrayList<RecipeTree> ingredientPaths) {
		this.setResult(result);
		this.setIngredientPaths(ingredientPaths);
	}

	public ArrayList<RecipeTree> getIngredientPaths() {
		return ingredientPaths;
	}

	public void setIngredientPaths(ArrayList<RecipeTree> ingredientPaths) {
		this.ingredientPaths = ingredientPaths;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
