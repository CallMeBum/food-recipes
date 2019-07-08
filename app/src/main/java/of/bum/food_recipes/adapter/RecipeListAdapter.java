package of.bum.food_recipes.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import java.util.ArrayList;
import java.util.List;
import of.bum.food_recipes.R;
import of.bum.food_recipes.adapter.viewholder.CategoryViewHolder;
import of.bum.food_recipes.adapter.viewholder.ExhaustedViewHolder;
import of.bum.food_recipes.adapter.viewholder.LoadingViewHolder;
import of.bum.food_recipes.adapter.viewholder.OnCategoryClick;
import of.bum.food_recipes.adapter.viewholder.RecipeViewHolder;
import of.bum.food_recipes.model.Recipe;
import of.bum.food_recipes.util.Constants;

public class RecipeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int RECIPE_TYPE = 1;
  private static final int LOADING_TYPE = 2;
  private static final int CATEGORY_TYPE = 3;
  private static final int EXHAUSTED_TYPE = 4;

  private List<Recipe> mRecipes;
  private OnCategoryClick mOnCategoryClick;

  public RecipeListAdapter(OnCategoryClick onCategoryClick) {
    mOnCategoryClick = onCategoryClick;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view;
    switch (viewType) {
      case LOADING_TYPE:
        view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.layout_loading_list_item, parent, false);
        return new LoadingViewHolder(view);
      case EXHAUSTED_TYPE:
        view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.layout_exhausted_list_item, parent, false);
        return new ExhaustedViewHolder(view);
      case CATEGORY_TYPE:
        view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.layout_category_list_item, parent, false);
        return new CategoryViewHolder(view, mOnCategoryClick);
      case RECIPE_TYPE:
      default:
        view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.layout_recipe_list_item, parent, false);
        return new RecipeViewHolder(view);
    }
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    int viewType = getItemViewType(position);
    if (mRecipes != null) {
      RequestOptions requestOptions = new RequestOptions()
          .placeholder(R.drawable.ic_launcher_background);
      if (viewType == RECIPE_TYPE) {
        RecipeViewHolder recipeHolder = (RecipeViewHolder) holder;
        Recipe recipe = mRecipes.get(position);
        recipeHolder.title.setText(recipe.getTitle());
        recipeHolder.publisher.setText(recipe.getPublisher());
        recipeHolder.socialRank.setText(String.valueOf(Math.round(recipe.getSocial_rank())));
        Glide.with(holder.itemView)
            .setDefaultRequestOptions(requestOptions)
            .load(recipe.getImage_url())
            .into(recipeHolder.image);
      } else if (viewType == CATEGORY_TYPE) {
        CategoryViewHolder categoryHolder = (CategoryViewHolder) holder;
        Uri path = Uri.parse(
            "android.resource://of.bum.food_recipes/drawable/" + mRecipes.get(position)
                .getImage_url());
        Glide.with(holder.itemView)
            .setDefaultRequestOptions(requestOptions)
            .load(path)
            .into(categoryHolder.imageCate);
        categoryHolder.nameCate.setText(mRecipes.get(position).getTitle());
      }
    }
  }

  @Override
  public int getItemCount() {
    return mRecipes == null ? 0 : mRecipes.size();
  }

  public void setRecipes(List<Recipe> recipes) {
    mRecipes = recipes;
    notifyDataSetChanged();
  }

  @Override
  public int getItemViewType(int position) {
    if (mRecipes.get(position).getSocial_rank() == -1) {
      return CATEGORY_TYPE;
    } else if (mRecipes.get(position).getTitle().equals("Loading...")) {
      return LOADING_TYPE;}
    else if (mRecipes.get(position).getTitle().equals("Exhausted...")) {
      return EXHAUSTED_TYPE;
    } else if (position == mRecipes.size() - 1
        && position != 0
        && !mRecipes.get(position).getTitle().equals("Exhausted...")) {
      return LOADING_TYPE;
    } else {
      return RECIPE_TYPE;
    }
  }

  public void displayLoadingView() {
    if (!isLoadingView()) {
      Recipe recipe = new Recipe();
      recipe.setTitle("Loading...");
      List<Recipe> list = new ArrayList<>();
      list.add(recipe);
      mRecipes = list;
      notifyDataSetChanged();
    }
  }

  public boolean isLoadingView() {
    if (mRecipes != null && mRecipes.size() > 0) {
      if (mRecipes.get(mRecipes.size() - 1).getTitle().equals("Loading...")) {
        return true;
      }
    }
    return false;
  }

  public void displaySearchCategories() {
    List<Recipe> categories = new ArrayList<>();
    for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORIES.length; i++) {
      Recipe recipe = new Recipe();
      recipe.setTitle(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
      recipe.setImage_url(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
      recipe.setSocial_rank(-1);
      categories.add(recipe);
    }
    mRecipes = categories;
    notifyDataSetChanged();
  }

  public void setQueryExhausted(){
    hideLoading();
    Recipe exhaustedRecipe = new Recipe();
    exhaustedRecipe.setTitle("Exhausted...");
    mRecipes.add(exhaustedRecipe);
    notifyDataSetChanged();
  }

  private void hideLoading(){
    if(isLoadingView()){
      for(Recipe recipe: mRecipes){
        if(recipe.getTitle().equals("Loading...")){
          mRecipes.remove(recipe);
        }
      }
      notifyDataSetChanged();
    }
  }
}
