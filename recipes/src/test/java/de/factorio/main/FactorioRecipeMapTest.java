package de.factorio.main;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

public class FactorioRecipeMapTest {
	private static final String FILE_DEMO_RECIPES = "demo-recipe.lua";

	@Test
	public void getRecipesWithResult_AllRecipes() throws Exception {
		FactorioRecipeMap factorioRecipeMap = createMapWithAllRecipes();
		assertThat(factorioRecipeMap.getRecipesWithResult("iron-gear-wheel")).isEqualTo("");
	}

	private FactorioRecipeMap createMapWithAllRecipes() throws IOException {
		List<Path> allFilesInDirectory = getAllFilesInDirectory("src\\test\\resources\\de\\factorio\\main\\");
		List<URL> list = allFilesInDirectory.stream().map(Path::toUri).map(t -> {
			try {
				return t.toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
		FactorioRecipeMap factorioRecipeMap2 = new FactorioRecipeMap(list);
		return factorioRecipeMap2;
	}

	@Test
	public void getRecipesWithResult_SingeFile() throws Exception {
		FactorioRecipeMap factorioRecipeMap = new FactorioRecipeMap(getClass().getResource(FILE_DEMO_RECIPES));
		Set<Recipe> recipesWithResult = factorioRecipeMap.getRecipesWithResult("iron-gear-wheel");
		assertThat(recipesWithResult).isEqualTo("");
	}

	@Test
	public void testName() throws Exception {
		FactorioRecipeMap factorioRecipeMap = createMapWithAllRecipes();
		RecipeTree recipeTree = factorioRecipeMap.getRecipePathFor("rocket");
		assertThat(recipeTree.toString()).isEqualTo("");
	}

	@Test
	public void printRecipes() throws Exception {
		FactorioRecipeMap factorioRecipeMap = createMapWithAllRecipes();
		Set<String> allRecipes = factorioRecipeMap.allRecipes();
		assertThat(allRecipes).isEqualTo("");
	}

	public List<Path> getAllFilesInDirectory(String directory) throws IOException {
		return Files.walk(Paths.get(directory)).filter(Files::isRegularFile).collect(Collectors.toList());
	}
}
