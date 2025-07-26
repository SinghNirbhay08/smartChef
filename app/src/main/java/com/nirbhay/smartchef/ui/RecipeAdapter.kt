package com.nirbhay.smartchef.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nirbhay.smartchef.R
import com.nirbhay.smartchef.models.RecipeModel

class RecipeAdapter(
    private var recipes: List<RecipeModel>,
    private val onItemClick: (RecipeModel) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipeName: TextView = itemView.findViewById(R.id.recipeTitleList)
        val recipeTime: TextView = itemView.findViewById(R.id.recipeTimeList)
        val recipeImage: ImageView = itemView.findViewById(R.id.recipeImageList)
        val viewRecipeBtn: Button = itemView.findViewById(R.id.viewRecipeBtn) // ✅ Add this
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]

        holder.recipeName.text = recipe.name
        holder.recipeTime.text = recipe.cookingTime

        val imageUrl = recipe.imageUrl.trim()
        if (imageUrl.isNotEmpty() && imageUrl.startsWith("http")) {
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.recipeImage)
        } else {
            holder.recipeImage.setImageResource(R.drawable.placeholder_image)
        }

        // ✅ Click listener for View Recipe button only
        holder.viewRecipeBtn.setOnClickListener {
            onItemClick(recipe)
        }
    }

    override fun getItemCount(): Int = recipes.size

    fun updateRecipes(newRecipes: List<RecipeModel>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }
}
