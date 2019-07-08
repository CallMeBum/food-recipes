package of.bum.food_recipes.adapter.viewholder;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import of.bum.food_recipes.R;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

  public TextView title, publisher, socialRank;
  public AppCompatImageView image;

  public RecipeViewHolder(@NonNull View itemView) {
    super(itemView);
    title = itemView.findViewById(R.id.recipe_title);
    publisher = itemView.findViewById(R.id.recipe_publisher);
    socialRank = itemView.findViewById(R.id.recipe_social_score);
    image = itemView.findViewById(R.id.recipe_image);
  }
}
