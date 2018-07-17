package de.factorio.main;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GraphInput {

	private Set<GNode> nodes;
	private Set<GEdge> links = new HashSet<>();

	public GraphInput(Input input) {
		nodes = input.getRecipes().values().stream()//
				.flatMap(r -> r.getIngredients().stream().map(Product::getType))//
				.map(type -> new GNode(type, 1))//
				.collect(Collectors.toSet());//
		nodes.addAll(input.getRecipes().values().stream() //
				.flatMap(r -> r.getResults().stream())//
				.map(product -> new GNode(product.getType(), 1))//
				.collect(Collectors.toSet()) //
		);

		for (Recipe recipe : input.getRecipes().values()) {
			List<Product> ingredients = recipe.getIngredients();
			for (Product ingredient : ingredients) {
				List<Product> results = recipe.getResults();
				for (Product product : results) {
					links.add(new GEdge(product.getType(), ingredient.getType(), 1));
				}
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
