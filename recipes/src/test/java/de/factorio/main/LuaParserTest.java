package de.factorio.main;

import static de.factorio.main.RecipeExtractor.extractRecipesWithEnergy;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.node;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizJdkEngine;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

public class LuaParserTest {
	Pattern ingredientPattern = Pattern.compile("\\{\"((\\w|-)+)\",\\s*(\\d+)\\s*\\}");
	Pattern ingredientFluidPattern = Pattern.compile("name=\"((\\w|-)+)\",\\s*amount=(\\d+)");
	Pattern resultPattern = Pattern.compile("result = \"((\\w|-)+)\"");
	Pattern energyPattern = Pattern.compile("energy_required = \"((\\w|-)+)\"");

	@Test
	@Ignore
	public void extractRecipes_AndWriteJsonWithTemplate() throws Exception {
		String file = "C:\\Spiele\\Factorio\\data\\base\\prototypes\\recipe\\recipe.lua";
		SourceFileWritingService sourceFileWritingService = new SourceFileWritingService();
		sourceFileWritingService.createFilesSafely(new Input(extractRecipesWithEnergy(file)));
	}

	@Test
	public void extractRecipes_AndWriteJsonWithJackson() throws Exception {
		String file = "C:\\Spiele\\Factorio\\data\\base\\prototypes\\recipe\\recipe.lua";
		ObjectMapper objectMapper = new ObjectMapper();
		GraphInput graphInput = new GraphInput(new Input(extractRecipesWithEnergy(file)));
		objectMapper.writeValue(new File("example//d3test//recipes.json"), graphInput);
	}

	@Test
	public void extractRecipes_WithEnergy() throws Exception {
		String file = "C:\\Spiele\\Factorio\\data\\base\\prototypes\\recipe\\recipe.lua";
		Input input = new Input(RecipeExtractor.extractRecipesWithoutEnergy(file));
		assertThat(input).isEqualTo("");
	}

	@Test
	public void extractRecipes_WithoutEnergy() throws Exception {
		String file = "C:\\Spiele\\Factorio\\data\\base\\prototypes\\recipe\\recipe.lua";
		Input input = new Input(RecipeExtractor.extractRecipesWithEnergy(file));
		assertThat(input).isEqualTo("");
	}

	@Test
	public void extractAllRecipes() throws Exception {
		String file = "C:\\Spiele\\Factorio\\data\\base\\prototypes\\recipe\\recipe.lua";
		Input withEnergy = new Input(RecipeExtractor.extractRecipesWithEnergy(file));
		Input withoutEnergy = new Input(RecipeExtractor.extractRecipesWithoutEnergy(file));
		assertThat(withEnergy).isEqualTo("");
	}

	@Test
	@Ignore
	public void extractRecipes_AndDrawGraph() throws Exception {
		String file = "C:\\Spiele\\Factorio\\data\\base\\prototypes\\recipe\\recipe.lua";
		ArrayList<Recipe> recipes = extractRecipesWithEnergy(file);

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

}
