package de.factorio.main;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizJdkEngine;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

public class LuaParserTest {
	private static final String FILE = "C:\\Spiele\\Factorio\\data\\base\\prototypes\\recipe\\recipe.lua";
	Pattern ingredientPattern = Pattern.compile("\\{\"((\\w|-)+)\",\\s*(\\d+)\\s*\\}");
	Pattern ingredientFluidPattern = Pattern.compile("name=\"((\\w|-)+)\",\\s*amount=(\\d+)");
	Pattern resultPattern = Pattern.compile("result = \"((\\w|-)+)\"");
	Pattern energyPattern = Pattern.compile("energy_required = \"((\\w|-)+)\"");

	@Test
	@Ignore
	public void extractRecipes_AndWriteJsonWithTemplate() throws Exception {
		String file = FILE;
		SourceFileWritingService sourceFileWritingService = new SourceFileWritingService();
		sourceFileWritingService.createFilesSafely(new Input(RecipeMatcher.extractRecipesToHashMapFromFile(file)));
	}

	@Test
	public void extractRecipes_AndWriteJsonWithJackson() throws Exception {
		String file = FILE;
		ObjectMapper objectMapper = new ObjectMapper();
		GraphInput graphInput = new GraphInput(new Input(RecipeMatcher.extractRecipesToHashMapFromFile(file)));
		objectMapper.writeValue(new File("example//d3test//recipes.json"), graphInput);
	}

	@Test
	@Ignore
	public void extractRecipes_AndDrawGraph() throws Exception {
		String file = FILE;
		Collection<Recipe> recipes = RecipeMatcher.extractRecipesToHashMapFromFile(file).values();

		GraphvizJdkEngine engine = new GraphvizJdkEngine();
		Graphviz.useEngine(engine, engine);
		Graph g = graph("example1").cluster();
		for (Recipe recipe : recipes) {
			Node classNode = node(recipe.getResult());
			System.out.println();
			List<Ingredient> ingredients = recipe.getIngredients();
			for (Ingredient ingredient : ingredients) {
				classNode = classNode.link(ingredient.getType());

			}
			g = g.with(classNode);
		}
		Graphviz.fromGraph(g).render(Format.PNG).toFile(new File("example/ex1.png"));
	}

	@Test
	public void asd() throws Exception {
		String[] extractRecipes = extractRecipesBla(FILE);
		List<String> asList = Arrays.asList(extractRecipes);
		Map<Boolean, List<String>> collect = asList.stream()
				.collect(Collectors.partitioningBy(s -> s.contains("results")));
		List<String> recipesWithMultipleResults = collect.get(true);
		for (String recipe : recipesWithMultipleResults) {
			String name = RecipeMatcher.findName(recipe);
			System.out.println(name);
		}
		assertThat(recipesWithMultipleResults).isEqualTo("");
	}

	public static String[] extractRecipesBla(String fileName) throws IOException {
		Path path = new File(fileName).toPath();
		List<String> lines = Files.readAllLines(path);

		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		StringBuilder stringBuilder = new StringBuilder("");
		for (String string : lines) {
			stringBuilder.append(string);
		}
		String file = stringBuilder.toString();
		String[] split = file.split("type = \"recipe\"");
		return split;
		// for (int i = 0; i < lines.size(); i++) {
		// String line = lines.get(i);
		// if (line.contains("energy_required")) {
		// Matcher m1 = ENERGYPATTERN.matcher(lines.get(i));
		// double energy = 0;
		// if (m1.find()) {
		// energy = Double.parseDouble(m1.group(1));
		// }
		// i++;
		// if (lines.get(i).contains("ingredients")) {
		// ArrayList<Ingredient> ingredients = new ArrayList<>();
		// while (!lines.get(i).contains("result")) {
		// Matcher normalIngredientMatcher = INGREDIENTPATTERN.matcher(lines.get(i));
		// while (normalIngredientMatcher.find()) {
		// Ingredient e = new Ingredient(normalIngredientMatcher.group(1),
		// normalIngredientMatcher.group(3));
		// ingredients.add(e);
		// }
		// Matcher m = INGREDIENTFLUIDPATTERN.matcher(lines.get(i));
		// while (m.find()) {
		// Ingredient e = new Ingredient(m.group(1), m.group(3));
		// ingredients.add(e);
		// }
		// i++;
		// }
		// if (lines.get(i).contains("result")) {
		// Matcher m = RESULTPATTERN.matcher(lines.get(i));
		// if (m.find()) {
		// recipes.add(new Recipe(m.group(1), energy, ingredients));
		// }
		// }
		// }
		// }
		// }
	}
}
