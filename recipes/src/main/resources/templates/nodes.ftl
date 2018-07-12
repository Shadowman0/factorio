{
  "nodes": [
  	<#list recipes as recipe>
    {"id": "${recipe.result}", "group": 1}<#sep>,
	</#list >
	],
	  "links": [
	<#list recipes as recipe>
		 <#list recipe.ingredients as ingredient>
		 {"source": "${recipe.result}", "target": "${ingredient.type}", "value": 1}<#sep>,
		</#list ><#sep>,
	</#list >
	 ]
}
	