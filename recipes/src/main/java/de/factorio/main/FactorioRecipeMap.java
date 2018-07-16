package de.factorio.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FactorioRecipeMap {
	private HashMap<String, Recipe> recipeNameMap = new HashMap<>();
	private HashMap<String, Set<Recipe>> resultHashMap = new HashMap<>();;

	public FactorioRecipeMap(URL... urls) {
		this(Arrays.asList(urls));
	}

	public FactorioRecipeMap(URI... uris) {
		this(Stream.of(uris).map(t -> {
			try {
				return t.toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList()));
	}

	public FactorioRecipeMap(List<URL> urls) {
		for (URL file : urls) {
			try {
				HashMap<String, Recipe> extractRecipesToHashMapFromFile = RecipeMatcher
						.extractRecipesToHashMapFromFile(file);
				recipeNameMap.putAll(extractRecipesToHashMapFromFile);
			} catch (IOException e) {
				System.out.println(file + " could not be parsed");
				e.printStackTrace();
			}
		}
		for (Recipe recipe : recipeNameMap.values()) {
			resultHashMap.computeIfAbsent(recipe.getResult(), k -> new HashSet<Recipe>()).add(recipe);
		}

	}

	public Set<Recipe> getRecipesWithResult(String result) {
		return resultHashMap.getOrDefault(result, new HashSet<>());
	}

	public RecipeTree getRecipePathFor(String result) {
		Set<Recipe> recipes = getRecipesWithResult(result);
		ArrayList<RecipeTree> ingredientPaths = new ArrayList<RecipeTree>();
		for (Recipe recipe : recipes) {
			List<String> ingredients = recipe.getIngredients().stream()//
					.map(Ingredient::getType).collect(Collectors.toList());
			for (String ingredient : ingredients) {
				RecipeTree recipePathForIngredient = getRecipePathFor(ingredient);
				ingredientPaths.add(recipePathForIngredient);

			}

		}

		return new RecipeTree(result, ingredientPaths);
	}

}
