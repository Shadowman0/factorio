package de.factorio.main;

import java.util.List;

public class Recipe {

	private String result;
	private List<Ingredient> ingredients;
	private double energy;

	public Recipe(String result, double energy, List<Ingredient> ingredients) {
		this.setEnergy(energy);
		this.setResult(result);
		this.setIngredients(ingredients);
	}

	@Override
	public String toString() {
		return "Recipe [result=" + result + ", ingredients=" + ingredients + ", energy=" + energy + "]";
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ingredients == null) ? 0 : ingredients.hashCode());
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recipe other = (Recipe) obj;
		if (ingredients == null) {
			if (other.ingredients != null)
				return false;
		} else if (!ingredients.containsAll(other.ingredients) || !other.ingredients.containsAll(ingredients))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		return true;
	}

}
