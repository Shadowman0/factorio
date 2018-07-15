package de.factorio.main;

import java.util.List;

public class Recipe {

	private String result;

	private List<Ingredient> ingredients;
	private double energy;
	private double resultCount;

	public Recipe(String result, double resultCount, double energy, List<Ingredient> ingredients) {
		this.setResultCount(resultCount);
		this.setEnergy(energy);
		this.setResult(result);
		this.setIngredients(ingredients);
	}

	@Override
	public String toString() {
		return "Recipe [result=" + result + ", ingredients=" + ingredients + ", energy=" + energy + ", resultCount="
				+ resultCount + "]";
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

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public double getResultCount() {
		return resultCount;
	}

	public void setResultCount(double resultCount) {
		this.resultCount = resultCount;
	}

}
