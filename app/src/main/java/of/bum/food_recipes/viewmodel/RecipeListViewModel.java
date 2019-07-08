package of.bum.food_recipes.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import of.bum.food_recipes.model.Recipe;
import of.bum.food_recipes.repository.RecipeRepository;

public class RecipeListViewModel extends AndroidViewModel {

  private RecipeRepository mRecipeRepository;
  private boolean mIsViewingRecipes;
  private boolean mIsPerformingRequest;
  private boolean isRetrieveRecipes;

  public RecipeListViewModel(@NonNull Application application) {
    super(application);
    mRecipeRepository = RecipeRepository.getInstance();
    mIsPerformingRequest = false;
  }

  public boolean isRetrieveRecipes() {
    return isRetrieveRecipes;
  }

  public void setRetrieveRecipes(boolean retrieveRecipes) {
    isRetrieveRecipes = retrieveRecipes;
  }

  public boolean isViewingRecipes() {
    return mIsViewingRecipes;
  }

  public void setViewingRecipes(boolean viewingRecipes) {
    mIsViewingRecipes = viewingRecipes;
  }

  public LiveData<List<Recipe>> getRecipes() {
    return mRecipeRepository.getRecipes();
  }

  public void searchRecipeApi(String query, int pageNumber) {
    mIsPerformingRequest = true;
    mIsViewingRecipes = true;
    isRetrieveRecipes = false;
    mRecipeRepository.searchRecipeApi(query, pageNumber);
  }

  public boolean onBackPressed() {
    if (mIsPerformingRequest) {
      mRecipeRepository.cancelRequest();
    }
    if (mIsViewingRecipes) {
      mIsViewingRecipes = false;
      return false;
    }
    return true;
  }

  public LiveData<Boolean> getIsExhausted() {
    return mRecipeRepository.getIsExhausted();
  }


  public boolean isPerformingRequest() {
    return mIsPerformingRequest;
  }

  public void searchNextPage() {
    if (!mIsPerformingRequest
        && mIsViewingRecipes
        && !getIsExhausted().getValue()) {
      mRecipeRepository.searchNextPage();
    }
  }

  public void setPerformingRequest(boolean performingRequest) {
    mIsPerformingRequest = performingRequest;
  }

  public LiveData<Boolean> isRecipeRequestTimeOut() {
    return mRecipeRepository.isRecipeRequestTimeOut();
  }
}
