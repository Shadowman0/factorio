package de.factorio.main;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizJdkEngine;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

public class LuaParserTest {
	Pattern ingredientPattern = Pattern.compile("\\{\"((\\w|-)+)\",\\s*(\\d+)\\s*\\}");
	Pattern ingredientFluidPattern = Pattern.compile("name=\"((\\w|-)+)\",\\s*amount=(\\d+)");
	Pattern resultPattern = Pattern.compile("result = \"((\\w|-)+)\"");

	@Test
	public void testName() throws Exception {
		String file = "C:\\Spiele\\Factorio\\data\\base\\prototypes\\recipe\\recipe.lua";
		ArrayList<Recipe> recipes = extractRecipes(file);
		Input input = new Input(recipes);
		SourceFileWritingService sourceFileWritingService = new SourceFileWritingService();
		sourceFileWritingService.createFilesSafely(input);
		assertThat(recipes).isEqualTo("");
	}

	@Test
	public void testName2() throws Exception {
		String file = "C:\\Spiele\\Factorio\\data\\base\\prototypes\\recipe\\recipe.lua";
		ArrayList<Recipe> recipes = extractRecipes(file);

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

		System.out.println(g.toString());
		// Graphviz.fromGraph(g).render(Format.PNG).toFile(new File("example/ex1.png"));
		assertThat(recipes).isEqualTo("");
	}

	private ArrayList<Recipe> extractRecipes(String file) throws IOException {
		Path path = new File(file).toPath();
		List<String> lines = Files.readAllLines(path);
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).contains("ingredients")) {
				ArrayList<Ingredient> ingredients = new ArrayList<>();
				while (!lines.get(i).contains("result")) {
					Matcher normalIngredientMatcher = ingredientPattern.matcher(lines.get(i));
					while (normalIngredientMatcher.find()) {
						Ingredient e = new Ingredient(normalIngredientMatcher.group(1),
								normalIngredientMatcher.group(3));
						ingredients.add(e);
					}
					Matcher m = ingredientFluidPattern.matcher(lines.get(i));
					while (m.find()) {
						Ingredient e = new Ingredient(m.group(1), m.group(3));
						ingredients.add(e);
					}
					i++;
				}
				if (lines.get(i).contains("result")) {
					Matcher m = resultPattern.matcher(lines.get(i));
					if (m.find()) {
						recipes.add(new Recipe(m.group(1), ingredients));
					}
				}
			}
		}
		return recipes;
	}
}
