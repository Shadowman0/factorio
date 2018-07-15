package de.factorio.main;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RecipeMatcherTest {
	private static final String TWO_RECIPES = "{\r\n" + "  {\r\n" + "    type = \"recipe\",\r\n"
			+ "    name = \"player-port\",\r\n" + "    enabled = false,\r\n" + "    ingredients =\r\n" + "    {\r\n"
			+ "      {\"electronic-circuit\", 10},\r\n" + "      {\"iron-gear-wheel\", 5},\r\n"
			+ "      {\"iron-plate\", 1 }\r\n" + "    },\r\n" + "    result = \"player-port\"\r\n" + "  },\r\n"
			+ "  {\r\n" + "    type = \"recipe\",\r\n" + "    name = \"fast-transport-belt\",\r\n"
			+ "    enabled = false,\r\n" + "    ingredients =\r\n" + "    {\r\n" + "      {\"iron-gear-wheel\", 5},\r\n"
			+ "      {\"transport-belt\", 1}\r\n" + "    },\r\n" + "    result = \"fast-transport-belt\"\r\n" + "  }}";
	private static final String REFINED_CONCRETE = "{\r\n" + "    type = \"recipe\",\r\n"
			+ "    name = \"refined-concrete\",\r\n" + "    energy_required = 15,\r\n" + "    enabled = false,\r\n"
			+ "    category = \"crafting-with-fluid\",\r\n" + "    ingredients =\r\n" + "    {\r\n"
			+ "      {\"concrete\", 20},\r\n" + "      {\"iron-stick\", 8},\r\n" + "      {\"steel-plate\", 1},\r\n"
			+ "      {type=\"fluid\", name=\"water\", amount=100}\r\n" + "    },\r\n"
			+ "    result= \"refined-concrete\",\r\n" + "    result_count = 10\r\n" + "  },";
	private static final String KOVAREX_RECIPE = "{\r\n" + "    type = \"recipe\",\r\n"
			+ "    name = \"kovarex-enrichment-process\",\r\n" + "    energy_required = 50,\r\n"
			+ "    enabled = false,\r\n" + "    category = \"centrifuging\",\r\n"
			+ "    ingredients = {{\"uranium-235\", 40}, {\"uranium-238\", 5}},\r\n"
			+ "    icon = \"__base__/graphics/icons/kovarex-enrichment-process.png\",\r\n" + "    icon_size = 32,\r\n"
			+ "    subgroup = \"intermediate-product\",\r\n"
			+ "    order = \"r[uranium-processing]-c[kovarex-enrichment-process]\",\r\n"
			+ "    main_product = \"\",\r\n" + "    results =\r\n" + "    {\r\n" + "      {\r\n"
			+ "        name = \"uranium-235\",\r\n" + "        amount = 41\r\n" + "      },\r\n" + "      {\r\n"
			+ "        name = \"uranium-238\",\r\n" + "        amount = 2\r\n" + "      }\r\n" + "    },\r\n"
			+ "    allow_decomposition = false\r\n" + "  },";
	private static final String FILE = "C:\\Spiele\\Factorio\\data\\base\\prototypes\\recipe\\recipe.lua";
	private static final String FILE_DEMO_RECIPES = "demo-recipe.lua";

	@Test
	public void findResults() throws Exception {
		String findResults = RecipeMatcher.findResults(KOVAREX_RECIPE);
		assertThat(findResults).isEqualTo("{\r\n" + "      {\r\n" + "        name = \"uranium-235\",\r\n"
				+ "        amount = 41\r\n" + "      },\r\n" + "      {\r\n" + "        name = \"uranium-238\",\r\n"
				+ "        amount = 2\r\n" + "      }");
	}

	@Test
	public void findIngredients() throws Exception {
		String findResults = RecipeMatcher.findIngredients(KOVAREX_RECIPE);
		assertThat(findResults).isEqualTo("{{\"uranium-235\", 40}, {\"uranium-238\", 5}}");
	}

	@Test
	public void splitIngredients() throws Exception {
		ArrayList<String> findResults = RecipeMatcher
				.splitIngredients("ingredients = {{\"uranium-235\", 40}, {\"uranium-238\", 5}");
		assertThat(findResults.toString()).isEqualTo("[{\"uranium-235\", 40}, {\"uranium-238\", 5}]");
	}

	@Test
	public void splitIngredients_AfterFind() throws Exception {
		String ingredients = RecipeMatcher.findIngredients(REFINED_CONCRETE);
		ArrayList<String> findResults = RecipeMatcher.splitIngredients(ingredients);
		assertThat(findResults.toString()).isEqualTo(
				"[{\"concrete\", 20}, {\"iron-stick\", 8}, {\"steel-plate\", 1}, {type=\"fluid\", name=\"water\", amount=100}]");
	}

	@Test
	public void findRecipes() throws Exception {
		ArrayList<String> findResults = RecipeMatcher.findRecipesBlocks(TWO_RECIPES);
		assertThat(findResults.toString()).isEqualTo("[{\r\n" + "    type = \"recipe\",\r\n"
				+ "    name = \"player-port\",\r\n" + "    enabled = false,\r\n" + "    ingredients =\r\n" + "    {\r\n"
				+ "      {\"electronic-circuit\", 10},\r\n" + "      {\"iron-gear-wheel\", 5},\r\n"
				+ "      {\"iron-plate\", 1 }\r\n" + "    },\r\n" + "    result = \"player-port\"\r\n" + "  }, {\r\n"
				+ "    type = \"recipe\",\r\n" + "    name = \"fast-transport-belt\",\r\n" + "    enabled = false,\r\n"
				+ "    ingredients =\r\n" + "    {\r\n" + "      {\"iron-gear-wheel\", 5},\r\n"
				+ "      {\"transport-belt\", 1}\r\n" + "    },\r\n" + "    result = \"fast-transport-belt\"\r\n"
				+ "  }]");
	}

	@Test
	public void findRecipesAsHashMap() throws Exception {
		ArrayList<Map<String, String>> findRecipesAsHashMap = RecipeMatcher.findRecipesAsHashMap(TWO_RECIPES);
		String name = findRecipesAsHashMap.get(0).get("name");
		String ingredients = findRecipesAsHashMap.get(0).get("ingredients");
		String energy_required = findRecipesAsHashMap.get(0).get("energy_required");
		String result = findRecipesAsHashMap.get(0).get("result");
		assertThat(name).isEqualTo("player-port");
		assertThat(ingredients).isEqualTo("{\r\n" + "      {\"electronic-circuit\", 10},\r\n"
				+ "      {\"iron-gear-wheel\", 5},\r\n" + "      {\"iron-plate\", 1 }\r\n" + "    }");
		assertThat(energy_required).isEqualTo("0.5");
		assertThat(result).isEqualTo("player-port");

	}

	@Test
	public void extractRecipesToHashMap_ConvertToRecipes() throws Exception {
		HashMap<String, Recipe> hashMap = RecipeMatcher.extractRecipesToHashMap(TWO_RECIPES);
		assertThat(hashMap).isEqualTo("");

	}

	@Test
	public void extractRecipesToHashMapFromFile() throws Exception {
		HashMap<String, Recipe> hashMap = RecipeMatcher.extractRecipesToHashMapFromFile(FILE);
		assertThat(hashMap).isEqualTo("");
	}

	@Test
	public void extractRecipesToHashMapFromFile_DemoRecipes() throws Exception {
		HashMap<String, Recipe> hashMap = RecipeMatcher.extractRecipesToHashMapFromFile(getClass().getResource(FILE_DEMO_RECIPES).toURI().getPath());
		assertThat(hashMap).isEqualTo("");
	}

	@Test
	public void findResult() throws Exception {
		String findResults = RecipeMatcher.findResult(REFINED_CONCRETE);
		assertThat(findResults).isEqualTo("refined-concrete");
	}

	@Test
	public void findResultCount() throws Exception {
		String findResults = RecipeMatcher.findResultCount(REFINED_CONCRETE);
		assertThat(findResults).isEqualTo("10");
	}

	@Test
	public void findEnergyRequired() throws Exception {
		String findResults = RecipeMatcher.findEnergyRequired(REFINED_CONCRETE);
		assertThat(findResults).isEqualTo("15");
	}

	@Test
	public void findName() throws Exception {
		String findResults = RecipeMatcher.findName(KOVAREX_RECIPE);
		assertThat(findResults).isEqualTo("kovarex-enrichment-process");
	}
}
