package of.bum.food_recipes.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import of.bum.food_recipes.model.Recipe;
import of.bum.food_recipes.network.client.RecipeApiClient;

public class RecipeRepository {

  private static RecipeRepository instance;
  private String mQuery;
  private int mPageNumber;
  private MutableLiveData<Boolean> isExhausted;
  private MediatorLiveData<List<Recipe>> mRecipes;


  // network
  private RecipeApiClient mRecipeApiClient;

  private RecipeRepository() {
    mRecipeApiClient = RecipeApiClient.getInstance();
    isExhausted = new MutableLiveData<>();
    mRecipes = new MediatorLiveData<>();
    initMediator();
  }

  /*
   * This is MAIN THREAD ==> setValue
   */
  private void initMediator() {
    LiveData<List<Recipe>> recipeListApiSource = mRecipeApiClient.getRecipes();
    mRecipes.addSource(recipeListApiSource, recipes -> {
      if (recipes != null){
        mRecipes.setValue(recipes);
        doneQuery(recipes);
      } else {
        // search database cache
        doneQuery(null);
      }
    });

  }

  public LiveData<Boolean> getIsExhausted() {
    return isExhausted;
  }

  private void doneQuery(List<Recipe> recipes) {
    if (recipes != null){
      if (recipes.size() % 30 != 0) isExhausted.setValue(true);
    } else {
      isExhausted.setValue(true);
    }
  }

  public static RecipeRepository getInstance() {
    if (instance == null) {
      instance = new RecipeRepository();
    }
    return instance;
  }

  public LiveData<List<Recipe>> getRecipes() {
    return mRecipes;
  }

  public void searchRecipeApi(String query, int pageNumber) {
    if (pageNumber == 0) pageNumber = 1;
    mQuery = query;
    mPageNumber = pageNumber;
    isExhausted.setValue(false);
    mRecipeApiClient.searchRecipeApi(query, pageNumber);
  }

  public LiveData<Boolean> isRecipeRequestTimeOut() {
    return mRecipeApiClient.isRecipeRequestTimeOut();
  }

  public void searchNextPage() {
    searchRecipeApi(mQuery, mPageNumber + 1);
  }

  public void cancelRequest() {
    mRecipeApiClient.cancelRequest();
  }
}
