package de.factorio.main;

import java.util.ArrayList;

public class RecipeTree {

	private String result;
	// hashMap mit ingredientName als key
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

	@Override
	public String toString() {
		return result + "<-(\n "
				+ ingredientPaths.stream().map(RecipeTree::toString).reduce((r1, r2) -> r1.concat("\n\t" + r2)) + ")";
	}

	public void setResult(String result) {
		this.result = result;
	}

}
