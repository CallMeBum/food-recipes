package of.bum.food_recipes;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import of.bum.food_recipes.adapter.RecipeListAdapter;
import of.bum.food_recipes.adapter.viewholder.OnCategoryClick;
import of.bum.food_recipes.base.BaseActivity;
import of.bum.food_recipes.util.DragItemRecyclerView;
import of.bum.food_recipes.util.RecipeItemDecorator;
import of.bum.food_recipes.viewmodel.RecipeListViewModel;

public class RecipeListActivity extends BaseActivity implements OnCategoryClick {

  private static final String TAG = "RecipeListActivity";
  // ui component
  private SearchView mSearchView;
  // vars
  private RecipeListViewModel mRecipeListViewModel;
  private RecyclerView mRecyclerView;
  private RecipeListAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);
    // init view model
    mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
    setSupportActionBar(findViewById(R.id.tool_bar));
    initRecyclerView();
    subscribeRecipes();
    initSearchView();

    if (!mRecipeListViewModel.isViewingRecipes()){
      // display category
      displaySearchCategories();
    }
  }

  private void displaySearchCategories() {
    mRecipeListViewModel.setViewingRecipes(false);
    mAdapter.displaySearchCategories();
  }

  private void initSearchView() {
    mSearchView = findViewById(R.id.search_view);
    mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String s) {
        mAdapter.displayLoadingView();
        mRecipeListViewModel.searchRecipeApi(s, 1);
        mSearchView.clearFocus();
        return false;
      }

      @Override
      public boolean onQueryTextChange(String s) {
        return false;
      }
    });
  }

  private void subscribeRecipes() {
    mRecipeListViewModel.getRecipes().observe(this, recipes -> {
      if (recipes != null && mRecipeListViewModel.isViewingRecipes()) {
        Log.d(TAG, "subscribeRecipes: still running");
        mRecipeListViewModel.setPerformingRequest(false);
        mRecipeListViewModel.setRetrieveRecipes(true);
        mAdapter.setRecipes(recipes);
      }
    });

    mRecipeListViewModel.isRecipeRequestTimeOut().observe(this, isTimeOut -> {
      if(isTimeOut && !mRecipeListViewModel.isRetrieveRecipes()) {
        Log.d(TAG, "subscribeRecipes: Time out");
      }
    });
    mRecipeListViewModel.getIsExhausted().observe(this, isExhausted -> {
      if (isExhausted) {
        Log.d(TAG, "subscribeRecipes is exhausted");
        mAdapter.setQueryExhausted();
      }
    });
  }

  private void initRecyclerView() {
    mRecyclerView = findViewById(R.id.recipeRecyclerView);
    mAdapter = new RecipeListAdapter(this);
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.addItemDecoration(new RecipeItemDecorator(10));
    new ItemTouchHelper(new DragItemRecyclerView(ItemTouchHelper.UP, ItemTouchHelper.RIGHT)).attachToRecyclerView(mRecyclerView);
    // pagination
    mRecyclerView.addOnScrollListener(new OnScrollListener() {
      @Override
      public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        if (!mRecyclerView.canScrollVertically(1)) {
          // search the next page
          mRecipeListViewModel.searchNextPage();
        }
        super.onScrollStateChanged(recyclerView, newState);
      }
    });
  }

  @Override
  public void categoryClick(String category) {
    mAdapter.displayLoadingView();
    mRecipeListViewModel.searchRecipeApi(category, 1);
    mSearchView.clearFocus();
  }

  @Override
  public void onBackPressed() {
    if(mRecipeListViewModel.onBackPressed()) {
      super.onBackPressed();
    } else {
      displaySearchCategories();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.categories) {
      displaySearchCategories();
    }
    return super.onOptionsItemSelected(item);
  }
}


