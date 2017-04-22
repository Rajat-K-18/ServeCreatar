package helper;

/**
 * Created by kartik on 23/4/17.
 */
public class Logger {

    /*
      This class has been created to just use logging system similar to android.
      Also , we may need to write logs to files intead of console , so its better
      to do so through a layer of abstraction , so that calls don't change , only
      the implementation changes
     */

    public static void log(String TAG,String message){
        System.out.println(TAG+":"+message);
    }

}
