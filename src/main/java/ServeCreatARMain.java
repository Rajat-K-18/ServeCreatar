//import com.sun.org.apache.xml.internal.serializer.utils.Utils;
import helper.Logger;
import helper.database.DBHelper;
import model.MagicData;
import okio.ByteString;
import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
//import org.jcp.xml.dsig.internal.dom.Utils;
import org.opencv.core.*;


import java.io.*;
import java.util.Scanner;


public class ServeCreatARMain {
    private static final int MAX_MESSAGE_SIZE = 3000000;
    private static final int PORT_NO = 9999;
    private static final String TAG = ServeCreatARMain.class.getSimpleName();
    private static java.awt.image.BufferedImage bufferedImage;
    private static DBHelper mDBHelper;

    // Compulsory statement to run opencv
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) throws Exception {

        doTheIntitialLoading();

        System.out.print("Welcome to ServeCreatAR \n" +
                "1.Start Server\n" +
                "2.Load Database\n" +
                "3.Do Configuratons \n" +
                "4.Exit \n" +
                "Please select one of the above options : ");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.close();
        switch(choice){

            case 1 : startServer();break;
            case 2 : loadDatabase();break;
            case 3 : goToSettings();break;
            case 4 : System.exit(0);break;
            default:System.out.print("\ninvalid option , please try again\n");

        }
        
        closeOpenResources();

      //  startServer(); //do need to see what happens when multiple devices access at the same time
        //FileRead r = new FileRead();
        //r.FileRead();

        //method2();
        //method1();

        }

        /*
        This method will be used to close out any open resources , such as database connection
        and so on
         */
    private static void closeOpenResources() {
        //TODO:implement it

    }

        /* This function will be responsible for tasks such as loading the helper.database.DBHelper
           by first loading various configuratons from a configuration file
           These configurations will have stuff such ip address of database , its user name
           and password , and other project related settings
         */

    private static void doTheIntitialLoading() {

        mDBHelper = new DBHelper();
        //find the serveCreatARconfig.xml file and load it
        //if can't find , create a new one
        //TODO : implement using jaxb


        mDBHelper.loadMarkersFromDatabase();



    }

    private static void goToSettings() {

    }

    private static void loadDatabase() throws Exception {

        System.out.print("\nEnter the absolute path of MagicContent folder :- \n" +
                "(you may do so by looking at the properties of the folder) \n");


        Scanner scanner =  new Scanner(System.in);
        String magicDataFolderPath = scanner.next();
        scanner.close();

        File magicDataFolderFile = new File(magicDataFolderPath);
        loadDataBaseFromFolder(magicDataFolderFile);
    }

    private static void loadDataBaseFromFolder(File magicDataFolderFile) {

        for (File file : magicDataFolderFile.listFiles()){
            insertMarker(file);
        }

    }

    private static void insertMarker(File markerFolderFile) {

        byte[] markerEncodedData = null;
        MagicData.Information information = null;
        byte[] imageRawFileData = null;
        MagicData.Marker marker = null;

        try {
            for(File file:markerFolderFile.listFiles()){
                if(file.getName().contentEquals("markerNFTData")){
                    marker = getNFTMarkerFromFile(markerFolderFile);
                     markerEncodedData = MagicData.Marker.ADAPTER.encode(marker);
                }
                else if(file.getName().contentEquals("information")){
                     information = getMarkerInformationFromFile(file);
                }
                else if(file.getName().contains(".png") || file.getName().contains(".jpg")){
                    imageRawFileData = FileUtils.readFileToByteArray(file);
                }
                else{
                    //TODO : to be decided
                }
            mDBHelper.insertData(marker.markerName,
                    markerEncodedData ,
                    imageRawFileData,
                    information.obj.toByteArray(), // to be filled
                    information.mtl.toByteArray()); // to be filled
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*
    Information folder will contain the following files : -
    1) .obj file
    2) .mtl file
    3) .png images
     */
    private static MagicData.Information getMarkerInformationFromFile(File informationFolderFile) throws IOException {
        byte[] objData = null , mtlData = null ;

        for(File file : informationFolderFile.listFiles()){
            if(file.getName().contains(".obj")){
                objData = FileUtils.readFileToByteArray(file);
            }else if(file.getName().contains(".mtl")){
                mtlData = FileUtils.readFileToByteArray(file);
            }else if(file.getName().contains(".png")){
                //TODO : to be implemented later on
            }else{
                //this will be decided later on
                //if the need arises
            }
            MagicData.Information information = new MagicData.Information.Builder()
                                                .image(null)
                                                .mtl(ByteString.of(mtlData))
                                                .obj(ByteString.of(objData))
                                                .build();
            return information;
        }

        //TODO: implement it
        return null;
    }

    private static MagicData.Marker getNFTMarkerFromFile(File markerNFTDataFolderFile) throws Exception {
        String markerName = null;
        byte[] isetData=null,fsetData=null,fset3Data =null;
        for (File file : markerNFTDataFolderFile.listFiles()){
            if(file.getName().contains(".iset")){
                markerName = file.getName().substring(0 ,//start index
                            file.getName().indexOf('.') //end index  -- TODO: check it should be reduced by 1
                        );
                isetData = FileUtils.readFileToByteArray(file);
            }else if(file.getName().contains(".fset")){
                 fsetData = FileUtils.readFileToByteArray(file);
            }else if(file.getName().contains(".fset3")){
                 fset3Data = FileUtils.readFileToByteArray(file);
            }
        }

        if(markerName!=null && isetData !=null && fset3Data!=null && fsetData!=null){
            //this means marker data has been picked up
            MagicData.Marker marker = new MagicData.Marker.Builder()
                    .markerName(markerName)
                    .fset(ByteString.of(fsetData))
                    .fset3(ByteString.of(fset3Data))
                    .iset(ByteString.of(isetData))
                    .build();
            Logger.log(TAG,"marker created successfully for folder "
                    +markerNFTDataFolderFile.getAbsolutePath());
            return marker;
        }else{
            Logger.log(TAG,"failed to load the given marker data in folder "
            + markerNFTDataFolderFile.getAbsolutePath());
            throw new Exception("failed to load the given marker data in folder "
                    + markerNFTDataFolderFile.getAbsolutePath());
        }

    }


    private static void startServer() throws Exception {
        final Server server = new Server(PORT_NO);
        WebSocketHandler wsHandler = new WebSocketHandler() {
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.getPolicy().setMaxBinaryMessageSize(MAX_MESSAGE_SIZE);
                factory.register(MyWebSocketHandler.class);


            }
        };
        server.setHandler(wsHandler);
        server.start();
        server.join();

    }
}