package of.bum.food_recipes.base;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import of.bum.food_recipes.R;

public abstract class BaseActivity extends AppCompatActivity {

  private ProgressBar mProgressBar;

  @Override
  public void setContentView(int layoutResID) {
    /*
     * This activity don't have any reference to R.layout.activity_base
     * So we have to inflate view
     */
    ConstraintLayout parent = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
    // inflate --> root ? null ... : (attachToRoot)
    FrameLayout frameLayout = parent.findViewById(R.id.activity_content);
    getLayoutInflater().inflate(layoutResID, frameLayout, true);
    mProgressBar = parent.findViewById(R.id.progress_bar);
    super.setContentView(parent);
  }

  public void showProgressBar() {
    mProgressBar.setVisibility(View.VISIBLE);
  }

  public void hideProgressBar() {
    mProgressBar.setVisibility(View.GONE);
  }
}
