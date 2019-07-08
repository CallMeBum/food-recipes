package of.bum.food_recipes.util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class DragItemRecyclerView extends ItemTouchHelper.SimpleCallback {
  public DragItemRecyclerView(int dragDirs, int swipeDirs) {
    super(dragDirs, swipeDirs);
  }

  @Override
  public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull ViewHolder viewHolder,
      @NonNull ViewHolder target) {
    return false;
  }

  @Override
  public void onSwiped(@NonNull ViewHolder viewHolder, int direction) {

  }
}
