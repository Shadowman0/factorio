package de.factorio.main;

import java.util.List;

public class Recipe {

	private String result;
	private List<Ingredient> ingredients;

	public Recipe(String result, List<Ingredient> ingredients) {
		this.setResult(result);
		this.setIngredients(ingredients);
	}

	@Override
	public String toString() {
		return "Recipe [result=" + getResult() + ", ingredients=" + getIngredients() + "]";
	}

	public List<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
