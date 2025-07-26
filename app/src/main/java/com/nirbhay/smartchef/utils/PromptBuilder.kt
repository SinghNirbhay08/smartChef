package com.nirbhay.smartchef.utils

object PromptBuilder {
    fun buildRecipePrompt(ingredients: String): String {
        return """
        Generate 5 detailed recipes using these ingredients: $ingredients
        
        Format each recipe exactly like this:
        ---
        RECIPE_NAME: [Recipe Name]
        INGREDIENTS: [List of ingredients with quantities]
        COOKING_TIME: [Time in minutes]
        INSTRUCTIONS: [Step by step cooking instructions]
        ---
        
        Make sure each recipe is separated by --- and follows the exact format above.
        """.trimIndent()
    }
}