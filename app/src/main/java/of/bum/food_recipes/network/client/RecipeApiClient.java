package of.bum.food_recipes.network.client;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import of.bum.food_recipes.executors.AppExecutors;
import of.bum.food_recipes.model.Recipe;
import of.bum.food_recipes.network.response.RecipeSearchResponse;
import of.bum.food_recipes.network.retrofit.ServiceGenerator;
import of.bum.food_recipes.util.Constants;
import retrofit2.Call;
import retrofit2.Response;


public class RecipeApiClient {

  private static final String TAG = "RecipeApiClient";
  private static RecipeApiClient instance;
  private MutableLiveData<List<Recipe>> mRecipes;
  private MutableLiveData<Boolean> mRecipeRequestTimeOut;

  // runnable fetch search data
  private RetrieveRecipesRunnable mRetrieveRecipesRunnable;

  private RecipeApiClient() {
    mRecipes = new MutableLiveData<>();
    mRecipeRequestTimeOut = new MutableLiveData<>();
  }

  public static RecipeApiClient getInstance() {
    if (instance == null) {
      instance = new RecipeApiClient();
    }
    return instance;
  }

  public LiveData<Boolean> isRecipeRequestTimeOut() {
    return mRecipeRequestTimeOut;
  }

  public LiveData<List<Recipe>> getRecipes() {
    return mRecipes;
  }

  /*
   *  Here we use thread pool to fetch data
   *  We use submit to Runnable & use schedule to run a schedule to cancel if time out
   *  ===> fetch data ===> time out
   */
  public void searchRecipeApi(String query, int pageNumber) {
    // check null and set null ?? is that needed
    mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query, pageNumber);
    // fetch data
    final Future handler = AppExecutors.getInstance().networkIO().submit(mRetrieveRecipesRunnable);
    AppExecutors.getInstance().networkIO().schedule(()->{
      // let user know its time out
      handler.cancel(true);
      mRecipeRequestTimeOut.postValue(true);
      //mRetrieveRecipesRunnable.cancelRequest();
    }, Constants.NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
  }

  private class RetrieveRecipesRunnable implements Runnable {
    private String query;
    private int pageNumber;
    private boolean cancelRequest;

    public RetrieveRecipesRunnable(String query, int pageNumber) {
      this.query = query;
      this.pageNumber = pageNumber;
    }

    @Override
    public void run() {
      try {
        Response response = getRecipes().execute();
        if (cancelRequest) return;
        if (response.code() == 200) {
          List<Recipe> recipes  = new ArrayList<>(((RecipeSearchResponse) response.body()).getRecipes());
          // concat list
          if (pageNumber == 1) {
            mRecipes.postValue(recipes);
          } else {
            List<Recipe> currentList = mRecipes.getValue();
            currentList.addAll(recipes);
            mRecipes.postValue(currentList);
          }
        } else {
          String error = response.errorBody().toString();
          Log.e(TAG, "run: " + error);
          mRecipes.postValue(null);
        }
      } catch (IOException e) {
        e.printStackTrace();
        mRecipes.postValue(null);
      }
    }

    private Call<RecipeSearchResponse> getRecipes() {
      return ServiceGenerator.
          getRecipeService().
          searchRecipes(
              Constants.API_KEY,
              query,
              String.valueOf(pageNumber));
    }

    private void cancelRequest() {
      Log.d(TAG, "cancelRequest: canceling search request");
      cancelRequest = true;
    }
  }

  public void cancelRequest() {
    if (mRetrieveRecipesRunnable != null) {
      mRetrieveRecipesRunnable.cancelRequest();
    }
  }
}
