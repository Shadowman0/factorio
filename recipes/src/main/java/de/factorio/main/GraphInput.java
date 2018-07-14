package de.factorio.main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphInput {

	private Set<GNode> nodes;
	private Set<GEdge> links = new HashSet<>();

	public GraphInput(Input input) {
		nodes = input.getRecipes().stream()//
				.flatMap(r -> r.getIngredients().stream().map(Ingredient::getType))//
				.map(type -> new GNode(type, 1))//
				.collect(Collectors.toSet());//
		nodes.addAll(input.getRecipes().stream() //
				.map(Recipe::getResult)//
				.map(type -> new GNode(type, 1))//
				.collect(Collectors.toSet()) //
		);

		for (Recipe recipe : input.getRecipes()) {
			List<Ingredient> ingredients = recipe.getIngredients();
			for (Ingredient ingredient : ingredients) {
				links.add(new GEdge(recipe.getResult(), ingredient.getType(), 1));
			}
		}
	}

	public Set<GNode> getNodes() {
		return nodes;
	}

	public void setNodes(Set<GNode> nodes) {
		this.nodes = nodes;
	}

	public Set<GEdge> getLinks() {
		return links;
	}

	public void setLinks(Set<GEdge> links) {
		this.links = links;
	}

}
