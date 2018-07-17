package de.factorio.main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RecipeMatcher {

	private static final Pattern NAME_MATCHER = Pattern.compile("name\\s*=\\s*\"((\\w|-)+)\"");
	private static final Pattern TYPE_MATCHER = Pattern.compile("type\\s*=\\s*\"((\\w|-)+)\"");
	private static final Pattern RESULT_MATCHER = Pattern.compile("result\\s*=\\s*\"((\\w|-)+)\"");
	private static final Pattern RESULT_COUNT_MATCHER = Pattern.compile("result_count\\s*=\\s*(\\d+)");
	private static final Pattern ENERGY_REQUIRED_MATCHER = Pattern.compile("energy_required\\s*=\\s*((\\d|\\.)+)");
	private static final Pattern AMOUNT_MATCHER = Pattern.compile("amount\\s*=\\s*((\\d|\\.)+)");
	private static final Pattern INGREDIENT_AMOUNT_MATCHER = Pattern.compile("\"\\s*,\\s*((\\d|\\.)+)\\}");

	public static String findName(String recipe) {
		return findField(recipe, NAME_MATCHER);
	}

	private static String findField(String recipe, Pattern fieldMatcher) {
		Matcher matcher = fieldMatcher.matcher(recipe);
		String name = "";
		if (matcher.find()) {
			name = matcher.group(1);
		}
		return name;
	}

	public static String findResult(String recipe) {
		return findField(recipe, RESULT_MATCHER);
	}

	public static String findResultCount(String recipe) {
		return findField(recipe, RESULT_COUNT_MATCHER);
	}

	public static String findEnergyRequired(String recipe) {
		return findField(recipe, ENERGY_REQUIRED_MATCHER);
	}

	public static String findResults(String recipe) {
		int startIndex = recipe.indexOf("results");
		if (startIndex == -1) {
			return null;
		}
		int blockEnd = findBlockEndIndex(recipe, startIndex);
		return recipe.substring(findNextBlockBeginIndex(recipe, startIndex), blockEnd).trim();
	}

	public static String findIngredients(String recipe) {
		int startIndex = recipe.indexOf("ingredients");
		int blockEnd = findBlockEndIndex(recipe, startIndex);
		return recipe.substring(findNextBlockBeginIndex(recipe, startIndex), blockEnd + 1).trim();
	}

	public static ArrayList<String> splitIngredients(String string) {
		ArrayList<String> ingredients = new ArrayList<>();
		for (int i = string.indexOf("{") + 1; i < string.length(); i++) {
			if (string.charAt(i) == '{') {
				int blockEndIndex = findBlockEndIndex(string, i);
				ingredients.add(string.substring(i, blockEndIndex + 1));
				i = blockEndIndex + 1;
			}
		}
		return ingredients;
	}

	public static ArrayList<String> findRecipesBlocks(String string) {
		ArrayList<String> recipes = new ArrayList<>();
		for (int i = string.indexOf("{") + 1; i < string.length(); i++) {
			if (string.charAt(i) == '{') {
				int blockEndIndex = findBlockEndIndex(string, i);
				recipes.add(string.substring(i, blockEndIndex + 1));
				i = blockEndIndex + 1;
			}
		}
		return recipes;
	}

	public static ArrayList<Map<String, String>> findRecipesAsHashMap(String string) {
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		ArrayList<String> findRecipes = findRecipesBlocks(string);
		for (String recipe : findRecipes) {
			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("energy_required", "0.5");
			hashMap.put("result_count", "1");
			hashMap.put("name", findName(recipe));
			hashMap.put("results", findResults(recipe));
			hashMap.put("ingredients", findIngredients(recipe));
			hashMap.putIfAbsent("result", findResult(recipe));
			String findEnergyRequired = findEnergyRequired(recipe);
			if (findEnergyRequired != "") {
				hashMap.put("energy_required", findEnergyRequired);
			}
			String findResultCount = findField(recipe, RESULT_COUNT_MATCHER);
			if (findResultCount != "") {
				hashMap.put("result_count", findResultCount);
			}
			result.add(hashMap);
		}
		return result;
	}

	public static ArrayList<HashMap<String, String>> splitIngredientsToMap(String ingredientsAsString) {
		ArrayList<String> splitIngredients = splitIngredients(ingredientsAsString);
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
		for (String ingredientAsString : splitIngredients) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			if (ingredientAsString.contains("=")) {
				hashMap.put("type", findField(ingredientAsString, TYPE_MATCHER));
				hashMap.put("name", findField(ingredientAsString, NAME_MATCHER));
				hashMap.put("amount", findField(ingredientAsString, AMOUNT_MATCHER));
			} else {
				hashMap.put("type", "solid");
				hashMap.put("name", ingredientAsString.substring(ingredientAsString.indexOf('"') + 1,
						ingredientAsString.lastIndexOf('"')));
				hashMap.put("amount", findField(ingredientAsString, INGREDIENT_AMOUNT_MATCHER));
			}
			result.add(hashMap);
		}
		return result;
	}

	public static HashMap<String, Recipe> extractRecipesToHashMapFromFile(URL file) throws IOException {
		Path path = new File(file.getPath()).toPath();
		return extractRecipesToRecipeNameHashMap(new String(Files.readAllBytes(path)));
	}

	public static HashMap<String, Recipe> extractRecipesToHashMapFromFile(String file) throws IOException {
		Path path = new File(file).toPath();
		return extractRecipesToRecipeNameHashMap(new String(Files.readAllBytes(path)));
	}

	public static HashMap<String, Recipe> extractRecipesToRecipeNameHashMap(String recipesString) {
		ArrayList<Map<String, String>> findRecipesAsHashMap = RecipeMatcher.findRecipesAsHashMap(recipesString);
		HashMap<String, Recipe> hashMap = new HashMap<String, Recipe>();
		for (Map<String, String> recipeMap : findRecipesAsHashMap) {
			String ingredientsAsString = recipeMap.get("ingredients");
			ArrayList<HashMap<String, String>> splitIngredients = RecipeMatcher
					.splitIngredientsToMap(ingredientsAsString);
			List<Ingredient> ingredientsList = splitIngredients.stream()
					.map(ingredientAsMap -> new Ingredient(ingredientAsMap.get("name"), ingredientAsMap.get("amount")))
					.collect(Collectors.toList());
			Recipe recipe = new Recipe(recipeMap.get("result"), Double.parseDouble(recipeMap.get("result_count")),
					Double.parseDouble(recipeMap.get("energy_required")), ingredientsList);
			hashMap.put(recipeMap.get("name"), recipe);
		}
		return hashMap;
	}

	private static int findNextBlockBeginIndex(String recipe, int startIndex) {
		String substring = recipe.substring(startIndex);
		int indexOf = substring.indexOf('{');
		return indexOf + startIndex;
	}

	private static int findBlockEndIndex(String recipe, int startIndex) {
		int braOpen = 0;
		for (int i = startIndex; i < recipe.length(); i++) {
			if (recipe.charAt(i) == '{') {
				braOpen++;
			}
			if (recipe.charAt(i) == '}') {
				braOpen--;
				if (braOpen == 0) {
					return i;
				}
			}
		}
		return -1;
	}

}
