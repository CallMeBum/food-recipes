package of.bum.food_recipes.adapter.viewholder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import of.bum.food_recipes.R;

/*
 * Should be GenericViewHolder with Exhausted  TODO
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

  public CircleImageView imageCate;
  public TextView nameCate;
  private OnCategoryClick mOnCategoryClick;

  public CategoryViewHolder(@NonNull View itemView, OnCategoryClick onCategoryClick) {
    super(itemView);
    imageCate = itemView.findViewById(R.id.category_image);
    nameCate = itemView.findViewById(R.id.category_name);
    mOnCategoryClick = onCategoryClick;
    itemView.setOnClickListener(this);
  }

  @Override
  public void onClick(View view) {
    mOnCategoryClick.categoryClick(nameCate.getText().toString());
  }
}
