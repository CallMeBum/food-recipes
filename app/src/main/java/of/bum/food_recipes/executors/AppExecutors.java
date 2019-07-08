package of.bum.food_recipes.executors;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import of.bum.food_recipes.model.Recipe;

/**
 *  Response for run network fetch data in background
 *  Thread, Looper, Handler
 *  HandlerThread
 *  AsyncTask
 *  Thread pool (Executors util)
 *
 *  ===> more control, load balance > enqueue
 */
public class AppExecutors {

  private static AppExecutors instance;
  public static AppExecutors getInstance() {
    if (instance == null) {
      instance = new AppExecutors();
    }
    return instance;
  }

  private AppExecutors() {
  }

  // new stuff here
  private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);

  public ScheduledExecutorService networkIO() {
    return mNetworkIO;
  }
}
