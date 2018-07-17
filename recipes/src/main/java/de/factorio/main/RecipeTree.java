package de.factorio.main;

import java.util.HashMap;
import java.util.List;

public class RecipeTree {

	private String result;
	// hashMap mit ingredientName als key
	private HashMap<String, List<RecipeTree>> ingredientPaths;

	public RecipeTree(String result, HashMap<String, List<RecipeTree>> ingredientPaths) {
		this.setResult(result);
		this.ingredientPaths = ingredientPaths;
	}

	public String getResult() {
		return result;
	}

	@Override
	public String toString() {
		return result + "<-(\n " + ingredientPaths.values().stream().map(list -> list.get(0).toString())
				.reduce((r1, r2) -> r1.concat("\n\t" + r2)) + ")";
	}

	public void setResult(String result) {
		this.result = result;
	}

}
