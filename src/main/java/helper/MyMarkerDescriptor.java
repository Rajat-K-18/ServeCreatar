package helper;

import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.KeyPoint;

import java.io.Serializable;

/**
 * Created by rajat on 1/5/17.
 */
public class MyMarkerDescriptor implements Serializable {
    //KeyPoint[] myKeyPoints = null;
    MatOfKeyPoint mMatOfKeyPoint=null;
    public MyMarkerDescriptor(MatOfKeyPoint matOfKeyPoint){


        mMatOfKeyPoint = matOfKeyPoint;
    }

    public MatOfKeyPoint getMarkerDescriptor() {

        return mMatOfKeyPoint;
    }
}
