package de.factorio.main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecipeExtractor {
	private static final Pattern INGREDIENTPATTERN = Pattern.compile("\\{\"((\\w|-)+)\",\\s*(\\d+)\\s*\\}");
	private static final Pattern INGREDIENTFLUIDPATTERN = Pattern.compile("name=\"((\\w|-)+)\",\\s*amount=(\\d+)");
	private static final Pattern RESULTPATTERN = Pattern.compile("result = \"((\\w|-)+)\"");
	private static final Pattern ENERGYPATTERN = Pattern.compile("energy_required = (\\d+)");

	public static ArrayList<Recipe> extractRecipesWithEnergy(String file) throws IOException {
		Path path = new File(file).toPath();
		List<String> lines = Files.readAllLines(path);
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.contains("energy_required")) {
				Matcher m1 = ENERGYPATTERN.matcher(lines.get(i));
				double energy = 0;
				if (m1.find()) {
					energy = Double.parseDouble(m1.group(1));
				}
				i++;
				if (lines.get(i).contains("ingredients")) {
					ArrayList<Ingredient> ingredients = new ArrayList<>();
					while (!lines.get(i).contains("result")) {
						Matcher normalIngredientMatcher = INGREDIENTPATTERN.matcher(lines.get(i));
						while (normalIngredientMatcher.find()) {
							Ingredient e = new Ingredient(normalIngredientMatcher.group(1),
									normalIngredientMatcher.group(3));
							ingredients.add(e);
						}
						Matcher m = INGREDIENTFLUIDPATTERN.matcher(lines.get(i));
						while (m.find()) {
							Ingredient e = new Ingredient(m.group(1), m.group(3));
							ingredients.add(e);
						}
						i++;
					}
					if (lines.get(i).contains("result")) {
						Matcher m = RESULTPATTERN.matcher(lines.get(i));
						if (m.find()) {
							recipes.add(new Recipe(m.group(1), energy, ingredients));
						}
					}
				}
			}
		}
		return recipes;
	}

	public static ArrayList<Recipe> extractRecipesWithoutEnergy(String file) throws IOException {
		Path path = new File(file).toPath();
		List<String> lines = Files.readAllLines(path);
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (lines.get(i).contains("ingredients")) {
				double energy = 0.5;
				ArrayList<Ingredient> ingredients = new ArrayList<>();
				while (!lines.get(i).contains("result")) {
					Matcher normalIngredientMatcher = INGREDIENTPATTERN.matcher(lines.get(i));
					while (normalIngredientMatcher.find()) {
						Ingredient e = new Ingredient(normalIngredientMatcher.group(1),
								normalIngredientMatcher.group(3));
						ingredients.add(e);
					}
					Matcher m = INGREDIENTFLUIDPATTERN.matcher(lines.get(i));
					while (m.find()) {
						Ingredient e = new Ingredient(m.group(1), m.group(3));
						ingredients.add(e);
					}
					i++;
				}
				if (lines.get(i).contains("result")) {
					Matcher m = RESULTPATTERN.matcher(lines.get(i));
					if (m.find()) {
						recipes.add(new Recipe(m.group(1), energy, ingredients));
					}
				}
			}
		}
		return recipes;
	}

}
