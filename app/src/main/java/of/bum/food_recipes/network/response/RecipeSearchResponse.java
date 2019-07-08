package of.bum.food_recipes.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import of.bum.food_recipes.model.Recipe;

public class RecipeSearchResponse {

  @SerializedName("count")
  @Expose
  private int count;
  @SerializedName("recipes")
  @Expose
  private List<Recipe> recipes;

  public RecipeSearchResponse(int count, List<Recipe> recipes) {
    this.count = count;
    this.recipes = recipes;
  }

  public RecipeSearchResponse() {
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public List<Recipe> getRecipes() {
    return recipes;
  }

  public void setRecipes(List<Recipe> recipes) {
    this.recipes = recipes;
  }

  @Override
  public String toString() {
    return "RecipeSearchResponse{" +
        "count=" + count +
        ", recipes=" + recipes +
        '}';
  }
}
