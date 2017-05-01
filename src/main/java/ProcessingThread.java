import helper.Logger;
import helper.database.DBHelper;
import model.MagicData;
import okio.ByteString;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.Highgui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rajat on 30/3/17.
 */
public class ProcessingThread extends Thread implements Serializable{
    private static final String TAG = ProcessingThread.class.getSimpleName();
    public byte[] mData ;
    public MyWebSocketHandler mWebSocketHandler;
    public String mUniqueId;
    private byte[] data;
    private byte[] marker;

    @Override
    public void run() {
        super.run();


        DBHelper mDBHelper = new DBHelper();
        ResultSet markerpngresultset = null;
        markerpngresultset = mDBHelper.getMarkersResultSet();


        Statement stmt = null;


        markerpngresultset = mDBHelper.getMarkersResultSet();

        Logger.log(TAG, "entering run");
        try {
            if(markerpngresultset!=null) {
                while (markerpngresultset.next()) {
                    //System.out.println(markerpngresultset.getString(1));
                    String temp = markerpngresultset.getString(1);
                    //System.out.println("size of temp is:"+markerpngresultset.getByte(1));
                    //MyMarkerDescriptor myKeyPoints = (MyMarkerDescriptor) markerpngresultset.getByte(1);
                    recogniseImage(mData,temp);

                }
                markerpngresultset.beforeFirst();
               // //markerpngresultset.first();
                //markerpngresultset.;
                Logger.log(TAG, "Not able to iterate thought markerpngresult list");
            }else{
                Logger.log(TAG,"markerresultset is null1");
                }


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;

    }

    ProcessingThread(byte[] data, MyWebSocketHandler mWebSocketHandler, String mUniqueId){
        mData=data;
        this.mWebSocketHandler = mWebSocketHandler;

    }

    private  void recogniseImage(byte[] data , String markerPNGPath) throws IOException{

        // http://stackoverflow.com/questions/2699963/storing-result-set-into-an-array

        String bookObject = markerPNGPath;
        //String bookScene = "booknewcrop"+System.currentTimeMillis()+".jpg";
        //String bookScene = "booknewcrop.jpg";

        Mat objectImage = Highgui.imread(bookObject, Highgui.CV_LOAD_IMAGE_COLOR);
        //Mat sceneImage = Highgui.imread(bookScene, Highgui.CV_LOAD_IMAGE_COLOR);

        BufferedImage temp = null,temp2=null;
        try {
            //temp2 = ImageIO.read(new ByteArrayInputStream(markerPNGBytes));
            temp = ImageIO.read(new ByteArrayInputStream(data));
            if ((temp == null) ){
                System.out.println("Temp is empty");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Mat sceneImage = matify(temp);
        //Mat objectImage = matify(temp2);

        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.ORB);


        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        System.out.println("Detecting key points...");
        featureDetector.detect(objectImage, objectKeyPoints);

        KeyPoint[] keypoints = objectKeyPoints.toArray();

        //KeyPoint[] keypoints = getKeyPoints(data, markerPNGBytes);

        //System.out.println(keypoints);

        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
        System.out.println("Computing descriptors...");
        descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);

        // Create the matrix for output image.
        Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar newKeypointColor = new Scalar(255, 0, 0);

        System.out.println("Drawing key points on object image...");
        Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);

        // Match object image with the scene image
        MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
        System.out.println("Detecting key points in background image...");
        featureDetector.detect(sceneImage, sceneKeyPoints);
        System.out.println("Computing descriptors in background image...");
        descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);

        Mat matchoutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar matchestColor = new Scalar(0, 255, 0);

        List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        System.out.println("Matching object and scene images...");
        descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);

        System.out.println("Calculating good match list...");
        LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

        float nndrRatio = 0.5f;

        for (int i = 0; i < matches.size(); i++) {
            MatOfDMatch matofDMatch = matches.get(i);
            DMatch[] dmatcharray = matofDMatch.toArray();
            DMatch m1 = dmatcharray[0];
            DMatch m2 = dmatcharray[1];

            if (m1.distance <= m2.distance * nndrRatio) {
                goodMatchesList.addLast(m1);

            }
        }

        if (goodMatchesList.size() >= 7) {
            System.out.println("Object Found!!!");

            List<KeyPoint> objKeypointlist = objectKeyPoints.toList();
            List<KeyPoint> scnKeypointlist = sceneKeyPoints.toList();

            LinkedList<Point> objectPoints = new LinkedList<Point>();
            LinkedList<Point> scenePoints = new LinkedList<Point>();

            for (int i = 0; i < goodMatchesList.size(); i++) {
                objectPoints.addLast(objKeypointlist.get(goodMatchesList.get(i).queryIdx).pt);
                scenePoints.addLast(scnKeypointlist.get(goodMatchesList.get(i).trainIdx).pt);
            }

            MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
            objMatOfPoint2f.fromList(objectPoints);
            MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
            scnMatOfPoint2f.fromList(scenePoints);

            Mat homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3);

            Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
            Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);

            obj_corners.put(0, 0, new double[]{0, 0});
            obj_corners.put(1, 0, new double[]{objectImage.cols(), 0});
            obj_corners.put(2, 0, new double[]{objectImage.cols(), objectImage.rows()});
            obj_corners.put(3, 0, new double[]{0, objectImage.rows()});

            System.out.println("Transforming object corners to scene corners...");
            Core.perspectiveTransform(obj_corners, scene_corners, homography);

            Mat img = Highgui.imread(markerPNGPath, Highgui.CV_LOAD_IMAGE_COLOR);

            Core.line(img, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
            Core.line(img, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
            Core.line(img, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
            Core.line(img, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)), new Scalar(0, 255, 0), 4);

            System.out.println("Drawing matches image...");
            MatOfDMatch goodMatches = new MatOfDMatch();
            goodMatches.fromList(goodMatchesList);

            Features2d.drawMatches(objectImage, objectKeyPoints, sceneImage, sceneKeyPoints, goodMatches, matchoutput, matchestColor, newKeypointColor, new MatOfByte(), 2);

            Highgui.imwrite("output//outputImage"+System.currentTimeMillis()+".jpg", outputImage);
            Highgui.imwrite("output//matchoutput"+System.currentTimeMillis()+".jpg", matchoutput);
            Highgui.imwrite("output//img"+System.currentTimeMillis()+".jpg", img);

            mWebSocketHandler.sendClient("OBJECT  FOUND!!!!!!!!!"+System.currentTimeMillis());

            //mWebSocketHandler.onMessage(System.currentTimeMillis()+":"+"ObjectFound");



        } else {
            //mWebSocketHandler.sendClient("OBJECT NOT FOUND!!!!!!!!!"+System.currentTimeMillis());



            //mWebSocketHandler.onMessage(System.currentTimeMillis()+":"+"ObjectNOTTTTTTTTTTTTTTTFound");
            //System.out.println(TAG+":"+mWebSocketHandler.getSession().getRemoteAddress());
            System.out.println("Object Not Found");

            /* Read the fset file */
            File read_fset = new File("pinball.fset");
            byte[] bytesArray1 = new byte[(int) read_fset.length()];
            FileInputStream f1 = new FileInputStream(read_fset);
            f1.read(bytesArray1);
            f1.close();

        /* Read the fset3 file */
            File read_fset3 = new File("pinball.fset3");
            byte[] bytesArray2 = new byte[(int) read_fset3.length()];
            FileInputStream f2 = new FileInputStream(read_fset3);
            f2.read(bytesArray2);
            f2.close();

        /* Read the iset file */
            File read_iset = new File("pinball.iset");
            byte[] bytesArray3 = new byte[(int) read_iset.length()];
            FileInputStream f3 = new FileInputStream(read_iset);
            f3.read(bytesArray3);
            f3.close();


            MagicData.Marker m = new MagicData.Marker.Builder()
                    .markerName("pinball")
                    .fset(ByteString.of(bytesArray1))
                    .fset3(ByteString.of(bytesArray2))
                    .iset(ByteString.of(bytesArray3))
                    .build();

            MagicData.Information i = new MagicData.Information.Builder()
                    .mtl(ByteString.of(new byte[10]))
                    .obj(ByteString.of(new byte[10]))
                    .image(new LinkedList<MagicData.Images>())
                    .build();

            MagicData magicData = new MagicData.Builder()
                    .marker(m)
                    .information(i)
                    .build();

            //byte[] markerByteArray =  model.MagicData.Marker.ADAPTER.encode(m);

            byte[] magicDataByteArray = MagicData.ADAPTER.encode(magicData);



            //model.MagicData.Marker s = model.MagicData.Marker.ADAPTER.decode(markerByteArray);
            //System.out.println("It is fest file:..."+s.fset.toString());

            mWebSocketHandler.getSession().getRemote().sendBytes(ByteBuffer.wrap(magicDataByteArray));

        }

        System.out.println("Ended....");
    }

    private static KeyPoint[] getKeyPoints(byte[] markerPNGBytes){

        if(markerPNGBytes == null){
            System.out.println("markerPNGBytes is empty");
        }
        BufferedImage temp = null,temp2=null;
        try {
            temp2 = ImageIO.read(new ByteArrayInputStream(markerPNGBytes));
            if ((temp2 == null) ){
                System.out.println("Temp is empty");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Mat objectImage = matify(temp2);

        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.ORB);
        //System.out.println("Detecting key points...");
        featureDetector.detect(objectImage, objectKeyPoints);
        KeyPoint[] keypoints = objectKeyPoints.toArray();
        return keypoints;

    }

    private static KeyPoint[] getKeyPointsFromBytes(byte[] markerKeyPointsBytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(markerKeyPointsBytes);
        ObjectInput in = null;
        KeyPoint[] keyPoints = null;
        try {
            in = new ObjectInputStream(bis);
            keyPoints = (KeyPoint[]) in.readObject();

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return keyPoints;
    }



    public static Mat matify(BufferedImage im) {
        if(im==null){
            Logger.log(TAG,"matify input is null");
        }
        // Convert INT to BYTE
        //im = new BufferedImage(im.getWidth(), im.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        // Convert bufferedimage to byte array
        byte[] pixels = ((DataBufferByte) im.getRaster().getDataBuffer())
                .getData();

        // Create a Matrix the same size of image
        Mat image = new Mat(im.getHeight(), im.getWidth(), CvType.CV_8UC3);
        // Fill Matrix with image values
        image.put(0, 0, pixels);

        return image;

    }


}
