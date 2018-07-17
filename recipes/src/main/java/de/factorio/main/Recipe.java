package de.factorio.main;

import java.util.List;

public class Recipe {

	private List<Product> results;
	private double energy;
	private List<Product> ingredients;

	public Recipe(List<Product> results, double energy, List<Product> ingredients) {
		this.results = results;
		this.energy = energy;
		this.ingredients = ingredients;

	}

	@Override
	public String toString() {
		return String.format("Recipe [results=%s, energy=%s, ingredients=%s]", results, energy, ingredients);
	}

	public List<Product> getResults() {
		return results;
	}

	public void setResults(List<Product> results) {
		this.results = results;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public List<Product> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Product> ingredients) {
		this.ingredients = ingredients;
	}

}
