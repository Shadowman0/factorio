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

import org.springframework.core.style.ToStringCreator;

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
			List<Product> results = recipe.getResults();
			for (Product product : results) {
				resultHashMap.computeIfAbsent(product.getType(), k -> new HashSet<Recipe>()).add(recipe);
			}
		}

	}

	public Set<Recipe> getRecipesWithResult(String result) {
		return resultHashMap.getOrDefault(result, new HashSet<>());
	}

	public RecipeTree getRecipePathFor(String result) {
		Set<Recipe> recipes = getRecipesWithResult(result);
		HashMap<String, List<RecipeTree>> ingredientPaths = new HashMap();
		for (Recipe recipe : recipes) {
			ToStringCreator toStringCreator = new ToStringCreator(recipe);
			System.out.println(toStringCreator.toString());
			List<String> ingredients = recipe.getIngredients().stream()//
					.map(Product::getType).collect(Collectors.toList());
			for (String ingredient : ingredients) {
				RecipeTree recipePathForIngredient = getRecipePathFor(ingredient);
				ingredientPaths.computeIfAbsent(ingredient, k -> new ArrayList<>()).add(recipePathForIngredient);
			}

		}

		return new RecipeTree(result, ingredientPaths);
	}

	public Set<String> allRecipes() {
		return recipeNameMap.keySet();
	}

}
