package com.nirbhay.smartchef

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nirbhay.smartchef.models.RecipeModel

class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var ivRecipeImage: ImageView
    private lateinit var tvRecipeName: TextView

    private lateinit var tvIngredients: TextView
    private lateinit var tvInstructions: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_detail)

        initViews()
        displayRecipe()
    }

    private fun initViews() {
        ivRecipeImage = findViewById(R.id.recipeImage)
        tvRecipeName = findViewById(R.id.title)

        tvIngredients = findViewById(R.id.ingredientsTv)
        tvInstructions = findViewById(R.id.instructiontv)
    }

    private fun displayRecipe() {
        val recipe = intent.getParcelableExtra<RecipeModel>("recipe") ?: return

        tvRecipeName.text = recipe.name

        tvIngredients.text =convertToBulletedList( recipe.ingredients)
        tvInstructions.text = formatInstructionsExistingSteps(recipe.instructions)

        // Load image
        if (recipe.imageUrl.isNotEmpty()) {
            Glide.with(this)
                .load(recipe.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(ivRecipeImage)
        } else {
            ivRecipeImage.setImageResource(R.drawable.placeholder_image)
        }
    }

    private fun formatInstructionsExistingSteps(text: String): String {
        return text
            .split(Regex("""\d+\."""))  // split on "1.", "2.", etc.
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .mapIndexed { index, step -> "${index + 1}. $step" }
            .joinToString("\n")
    }



    private fun convertToBulletedList(text: String): String {
        return text
            .split("\n", ".", ",", ";", "•") // Split on common separators
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .joinToString("\n") { "• $it" }
    }
}