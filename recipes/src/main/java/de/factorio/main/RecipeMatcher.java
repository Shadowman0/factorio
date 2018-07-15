package de.factorio.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public static ArrayList<String> findRecipes(String string) {
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
		ArrayList<String> findRecipes = findRecipes(string);
		for (String recipe : findRecipes) {
			HashMap<String, String> hashMap = new HashMap<>();
			hashMap.put("energy_required", "0.5");
			hashMap.put("name", findName(recipe));
			hashMap.put("results", findResults(recipe));
			hashMap.put("ingredients", findIngredients(recipe));
			hashMap.putIfAbsent("result", findResult(recipe));
			String findEnergyRequired = findEnergyRequired(recipe);
			if (findEnergyRequired != "") {
				hashMap.put("energy_required", findEnergyRequired);
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
				hashMap.put("name", ingredientAsString.substring(ingredientAsString.indexOf('"'),
						ingredientAsString.lastIndexOf('"')));
				hashMap.put("amount", findField(ingredientAsString, INGREDIENT_AMOUNT_MATCHER));
			}
			result.add(hashMap);
		}
		return result;
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
