package of.bum.food_recipes.util;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.State;

public class RecipeItemDecorator extends RecyclerView.ItemDecoration {

  private final int verticalSpaceHeight;

  public RecipeItemDecorator(int verticalSpaceHeight) {
    this.verticalSpaceHeight = verticalSpaceHeight;
  }

  @Override
  public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
      @NonNull RecyclerView parent, @NonNull State state) {
    outRect.top = verticalSpaceHeight;
  }
}
