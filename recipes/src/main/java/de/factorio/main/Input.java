package de.factorio.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Input {

	private ArrayList<Recipe> recipes;

	public Input(ArrayList<Recipe> recipes) {
		this.recipes = recipes;
		HashMap<String, List<Recipe>> orderedMap = new HashMap<>();
		for (Recipe recipe : recipes) {
			orderedMap.putIfAbsent(recipe.getResult(), Arrays.asList(recipe));
			orderedMap.computeIfPresent(recipe.getResult(), (key, oldList) -> {
				oldList.add(recipe);
				return oldList;
			});
		}
		for (String recipe : orderedMap.keySet()) {
			orderedMap.compute(recipe, (key, oldList) -> {
				for (Recipe r : recipes) {
					for (int i = 0; i < oldList.size(); i++) {
						if (r.equals(oldList.get(i))) {

						}

					}
				}
				return oldList;
			});
		}
	}

	@Override
	public String toString() {
		return "Input [recipes=" + recipes + "]";
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
