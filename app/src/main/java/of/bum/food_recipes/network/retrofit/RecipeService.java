package of.bum.food_recipes.network.retrofit;

import of.bum.food_recipes.network.response.RecipeResponse;
import of.bum.food_recipes.network.response.RecipeSearchResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeService {

  @GET("api/search")
  Call<RecipeSearchResponse> searchRecipes(
      @Query("key") String key,
      @Query("q") String query,
      @Query("page") String page);

  @GET("api/get")
  Call<RecipeResponse> getRecipe(
      @Query("key") String key,
      @Query("rId") String recipeId);
}
