package com.nirbhay.smartchef

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nirbhay.smartchef.adapter.RecipeAdapter
import com.nirbhay.smartchef.models.RecipeModel
import com.nirbhay.smartchef.network.ApiClient
import com.nirbhay.smartchef.network.GeminiRequest
import com.nirbhay.smartchef.utils.PromptBuilder
import kotlinx.coroutines.*

class RecipeListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvNoRecipes: TextView
    private lateinit var adapter: RecipeAdapter

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_list)

        initViews()
        setupRecyclerView()

        val ingredients = intent.getStringExtra("ingredients") ?: ""
        if (ingredients.isNotEmpty()) {
            fetchRecipes(ingredients)
        } else {
            showNoRecipes()
        }
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recipeRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        tvNoRecipes = findViewById(R.id.noResultsText)

        progressBar.visibility = View.GONE
        tvNoRecipes.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        adapter = RecipeAdapter(emptyList()) { recipe ->
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("recipe", recipe)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun fetchRecipes(ingredients: String) {
        showLoading(true)

        scope.launch {
            try {
                val recipes = getRecipesFromGemini(ingredients)

                if (recipes.isEmpty()) {
                    showNoRecipes()
                    return@launch
                }

                val recipesWithImages = getImagesForRecipes(recipes)
                for (recipe in recipesWithImages) {
                    Log.d("IMAGE_URL_CHECK", "Recipe: ${recipe.name}, URL: ${recipe.imageUrl}")
                }
                runOnUiThread {
                    adapter.updateRecipes(recipesWithImages)
                    showLoading(false)
                }

            } catch (e: Exception) {
                showError("Failed to fetch recipes: ${e.message}")
            }
        }
    }

    private suspend fun getRecipesFromGemini(ingredients: String): List<RecipeModel> {
        return withContext(Dispatchers.IO) {
            val prompt = PromptBuilder.buildRecipePrompt(ingredients)
            val request = GeminiRequest.createSimpleRequest(prompt)

            val response = ApiClient.geminiApi.generateRecipe(
                apiKey = ApiClient.getGeminiApiKey(),
                request = request
            )

            if (response.isSuccessful) {
                val text = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
                parseRecipesFromText(text)
            } else {
                throw Exception("API call failed: ${response.message()}")
            }
        }
    }

    private suspend fun getImagesForRecipes(recipes: List<RecipeModel>): List<RecipeModel> {
        return withContext(Dispatchers.IO) {
            recipes.map { recipe ->
                try {
                    val response = ApiClient.pexelsApi.searchPhotos(
                        query = "${recipe.name} food",
                        perPage = 1,
                        authorization = ApiClient.getPexelsApiKey()
                    )

                    if (response.isSuccessful) {
                        val imageUrl = response.body()?.photos?.firstOrNull()?.src?.medium ?: ""
                        Log.d("IMAGE_URL", "Image for ${recipe.name}: $imageUrl")
                        recipe.copy(imageUrl = imageUrl)

                    }
                    else {
                        Log.e("PEXELS", "Image fetch failed: ${response.message()}")
                        recipe
                    }
                } catch (e: Exception) {
                    Log.e("PEXELS", "Exception during image fetch: ${e.message}")
                    recipe
                }
            }
        }
    }

    private fun parseRecipesFromText(text: String): List<RecipeModel> {
        val recipes = mutableListOf<RecipeModel>()
        val recipeBlocks = text.split("---").filter { it.trim().isNotEmpty() }

        for (block in recipeBlocks) {
            try {
                val lines = block.trim().lines().filter { it.trim().isNotEmpty() }
                var name = ""
                var ingredients = ""
                var cookingTime = ""
                var instructions = ""

                for (line in lines) {
                    when {
                        line.startsWith("RECIPE_NAME:", ignoreCase = true) -> {
                            name = line.substringAfter(":").trim()
                        }
                        line.startsWith("INGREDIENTS:", ignoreCase = true) -> {
                            ingredients = line.substringAfter(":").trim()
                        }
                        line.startsWith("COOKING_TIME:", ignoreCase = true) -> {
                            cookingTime = line.substringAfter(":").trim()
                        }
                        line.startsWith("INSTRUCTIONS:", ignoreCase = true) -> {
                            instructions = line.substringAfter(":").trim()
                        }
                    }
                }

                if (name.isNotEmpty() && instructions.isNotEmpty()) {
                    recipes.add(
                        RecipeModel(
                            name = name,
                            ingredients = ingredients,
                            instructions = instructions,
                            cookingTime = cookingTime.ifEmpty { "30 minutes" }
                        )
                    )
                }
            } catch (_: Exception) {
                // skip malformed block
            }
        }

        return recipes
    }

    private fun showLoading(show: Boolean) {
        runOnUiThread {
            if (::progressBar.isInitialized && ::recyclerView.isInitialized) {
                progressBar.visibility = if (show) View.VISIBLE else View.GONE
                recyclerView.visibility = if (show) View.GONE else View.VISIBLE
            }
        }
    }

    private fun showNoRecipes() {
        runOnUiThread {
            showLoading(false)
            if (::tvNoRecipes.isInitialized && ::recyclerView.isInitialized) {
                tvNoRecipes.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
        }
    }

    private fun showError(message: String) {
        runOnUiThread {
            showLoading(false)
            Toast.makeText(this@RecipeListActivity, message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}
