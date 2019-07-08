package of.bum.food_recipes.network.retrofit;

import of.bum.food_recipes.util.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
  private static Retrofit retrofit = new  Retrofit.Builder()
      .baseUrl(Constants.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build();

  private static RecipeService recipeService = retrofit.create(RecipeService.class);
  public static RecipeService getRecipeService() {
    return recipeService;
  }
}
