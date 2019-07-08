package of.bum.food_recipes.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import of.bum.food_recipes.model.Recipe;

public class RecipeResponse {

  @SerializedName("recipe")
  @Expose()
  private Recipe recipe;

  public Recipe getRecipe(){
    return recipe;
  }

  @Override
  public String toString() {
    return "RecipeResponse{" +
        "recipe=" + recipe +
        '}';
  }

}
