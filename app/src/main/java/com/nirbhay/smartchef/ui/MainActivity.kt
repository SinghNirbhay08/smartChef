package com.nirbhay.smartchef

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etIngredients: EditText
    private lateinit var btnGetRecipes: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupClickListeners()
    }

    private fun initViews() {
        etIngredients = findViewById(R.id.ingredients_input)
        btnGetRecipes = findViewById(R.id.btn_find_recipes)
    }

    private fun setupClickListeners() {
        btnGetRecipes.setOnClickListener {
            val ingredients = etIngredients.text.toString().trim()

            if (ingredients.isEmpty()) {
                showFunnyEmptyMessage()
                return@setOnClickListener
            }

            val intent = Intent(this, RecipeListActivity::class.java)
            intent.putExtra("ingredients", ingredients)
            startActivity(intent)
        }
    }

    private fun showFunnyEmptyMessage() {
        Toast.makeText(this, "Bhai kuch kharid le, itne mein kuch nahi banega! 😭", Toast.LENGTH_LONG).show()
    }
}
