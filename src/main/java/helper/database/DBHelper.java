package helper.database;
import model.MagicData;

import helper.Logger;
import okio.ByteString;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;

import java.sql.*;
import java.util.LinkedList;


/**
 * Created by rajat on 20/4/17.
 */
public class DBHelper {

    static final String JDBC_driver = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";
    static final String USERNAME = "root";
    static final String PASSWORD = "root";

    private static final String TAG = DBHelper.class.getSimpleName();
    private static Connection mDatabaseConnection;
    private static ResultSet mMarkerResultSet = null;
    private static PreparedStatement mPreparedCreateStatement = null;
    private static Statement mCreateDatabaseStatement = null;
    private static Statement mCreateTableStatement = null;

    //byte[] bytesArray = new byte[10000];

    public DBHelper() {

        if (mDatabaseConnection == null) {
            try {
                createNewDatabaseConnection();
                setupDatabase();
                createTable();

            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }


    private void createNewDatabaseConnection() throws SQLException, ClassNotFoundException {

        try {
            Class.forName("com.mysql.jdbc.Driver");//TODO:whats the use of this statement
            System.out.println("Connecting to database...");
            mDatabaseConnection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    /* static counterpart of the above function
    String argument is just there for making use of function overloading
    Will be removed later on
     */
    private static void createNewDatabaseConnection(String unused) throws SQLException, ClassNotFoundException {

        try {
            Class.forName("com.mysql.jdbc.Driver");//TODO:whats the use of this statement
            System.out.println("Connecting to database...");
            mDatabaseConnection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        Statement stmt=null;


        try {
            createNewDatabaseConnection("its a useless string");

          MagicData.Marker marker = new MagicData.Marker.Builder()
                    .fset(ByteString.of(file_read("pinball.fset")))
                    .fset3(ByteString.of(file_read("pinball.fset3")))
                    .iset(ByteString.of(file_read("pinball.iset")))
                    .markerName("pinball")
                    .build();

            byte[] r = MagicData.Marker.ADAPTER.encode(marker);
            File read_png = new File("pinball.jpg");
            byte[] l  = FileUtils.readFileToByteArray(read_png);


        } catch (
                SQLException se)

        {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (
                Exception e)

        {
            //Handle errors for Class.forName ---this statement has been moved to another method
            //check out what to do about it
            e.printStackTrace();
        } finally

        {
            //finally block used to close resources
            try {

                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
                }// nothing we can do



        }


    }

    public ResultSet getMarkersResultSet(){
        return mMarkerResultSet;
    }

    public void loadMarkersFromDatabase(){
        Statement stmt = null;

        try {
            stmt = mDatabaseConnection.createStatement();
            mMarkerResultSet = stmt.executeQuery("SELECT markerpngpath from markerandinformation");
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.log(TAG, "Failed to get resultset of markerpngs");
        }


    }
    public  static void setupDatabase(){

        try {
            mCreateDatabaseStatement = mDatabaseConnection.createStatement();
            String create_database_sql = "CREATE DATABASE IF NOT EXISTS creatar";
            mCreateDatabaseStatement.executeUpdate(create_database_sql);
            Logger.log(TAG,"created database if it wasn't there already");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            mCreateDatabaseStatement = mDatabaseConnection.createStatement();
            String use_database_sql = "USE creatar";
            mCreateDatabaseStatement.executeUpdate(use_database_sql);
            Logger.log(TAG,"started using creatar database");
        } catch (SQLException e) {

            e.printStackTrace();
        }



    }

    public  static void createTable(){
        try {
            mCreateTableStatement = mDatabaseConnection.createStatement();

            String create_table_sql1 = "CREATE TABLE IF NOT EXISTS "+"markerandinformation" +
                    "(markername varchar(255)," +
                    "markernft LONGBLOB NOT NULL," +
                    "markerkeypoints LONGBLOB NOT NULL," +
                    "markerpngpath varchar(2000) NOT NULL," +
                    "objfile LONGBLOB NOT NULL," +
                    "mtlfile LONGBLOB NOT NULL," +
                    "PRIMARY KEY(markername))";
            mCreateTableStatement.executeUpdate(create_table_sql1);

            String create_table_sql2 = "CREATE TABLE IF NOT EXISTS "+"markerinfoimages" +
                    "(markername varchar(255) NOT NULL," +
                    "imagepath varchar(2000) NOT NULL," +
                    "imagename varchar(255) NOT NULL," +
                    "FOREIGN KEY(markername) REFERENCES markerandinformation(markername)" +
                    ")";
            mCreateTableStatement.executeUpdate(create_table_sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static byte[] file_read(String fileName) throws IOException {
        File f = new File(fileName);
        byte[] bytesArray = new byte[(int) f.length()];
        FileInputStream f1 = new FileInputStream(f);
        f1.read(bytesArray);
        f1.close();
        return bytesArray;
    }

    public MagicData getMagicData(String markerPngPath) throws IOException {
        Statement stmt1 = null;
        Statement stmt2 = null;
        ResultSet markerAndInformationResultSet;
        ResultSet markerInfoImagesResultSet;
        MagicData magicData = null;

        try {
            stmt1 = mDatabaseConnection.createStatement();
            markerAndInformationResultSet = stmt1.executeQuery
                    ("SELECT *" +
                            "FROM markerandinformation where" +
                            " markerpngpath=" + "\'"+markerPngPath + "\'");

            markerAndInformationResultSet.next();

            MagicData.Marker marker = MagicData.Marker.ADAPTER.decode(
                    markerAndInformationResultSet.getBytes(MarkerAndInformationTable.COLUMN_MARKER_NFT_INDEX));

            String markerName = marker.markerName;

            stmt2 = mDatabaseConnection.createStatement();
            markerInfoImagesResultSet = stmt2.executeQuery
                    ("SELECT *" +
                            "FROM markerinfoimages" +
                            " WHERE markername=" + "\'"+markerName+"\'" + "");


            LinkedList<MagicData.Images> images = new LinkedList<MagicData.Images>();
            while (markerInfoImagesResultSet.next()) {
                MagicData.Images image = new MagicData.Images.Builder().
                        imageNameWithExtension(
                                markerInfoImagesResultSet.getString(
                                        MarkerInfoImagesTable.COLUMN_IMAGE_NAME_WITH_EXTENSION_INDEX
                                )
                        ).
                        imagebytes(
                                ByteString.of(getFileBytesFromPath(
                                        markerInfoImagesResultSet.getString(
                                                MarkerInfoImagesTable.COLUMN_IMAGE_PATH_INDEX
                                        )
                                ))
                        )
                        .build();

                /////////////////////////
                images.add(image);
            }





            MagicData.Information information = new MagicData.Information.Builder().
                    mtl(
                            ByteString.of(
                                    markerAndInformationResultSet.getBytes(MarkerAndInformationTable.COLUMN_MARKER_MTL_INDEX)
                            )
                    ).
                    obj(
                            ByteString.of(
                                    markerAndInformationResultSet.getBytes(MarkerAndInformationTable.COLUMN_MARKER_OBJ_FILE_INDEX)
                            )
                    ).
                    image(images).
                    build();

            magicData = new MagicData.Builder()
                    .marker(marker)
                    .information(information)
                    .build();


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  magicData;
    }

    private byte[] getFileBytesFromPath(String path) {
        //TODO to be implemented
        File readfile = new File(path);
        byte[] byteArray = new byte[(int) readfile.length()];

        try {
            FileInputStream f1 = new FileInputStream(readfile);
            f1.read(byteArray);
            f1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArray;
    }

    public void closeResources(){
        closeStatements();
        closeConnection();
    }

    public void insertData(String markerName, byte[] markerEncodedData,byte[] markerKeyPoints ,String markerPngPath,byte[] informationObj, byte[] informationMtl){


        //TODO:see if i need to create a new mPreparedCreateStatement every time
        try {
            mPreparedCreateStatement = mDatabaseConnection.prepareStatement("INSERT INTO markerandinformation values(?,?,?,?,?,?)");
            mPreparedCreateStatement.setString(1, markerName);
            mPreparedCreateStatement.setBytes(2, markerEncodedData);
            mPreparedCreateStatement.setBytes(3, markerKeyPoints);
            mPreparedCreateStatement.setString(4, markerPngPath);
            mPreparedCreateStatement.setBytes(5, informationObj);
            mPreparedCreateStatement.setBytes(6, informationMtl);
            mPreparedCreateStatement.executeUpdate();

            //TODO:check do we have to close the prepared statement and other statements
            //
        } catch (SQLException e) {
            Logger.log(TAG,"failed to insert data");
            e.printStackTrace();

        }

    }
    public void insertDataImageTable(String markerName, String imagePath, String imageName){

        try {
            mPreparedCreateStatement = mDatabaseConnection.prepareStatement("INSERT INTO markerinfoimages values(?,?,?)");
            mPreparedCreateStatement.setString(1,markerName);
            mPreparedCreateStatement.setString(2,imagePath);
            mPreparedCreateStatement.setString(3,imageName);
            mPreparedCreateStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.log(TAG,"failed to insert data in table2");
            e.printStackTrace();
        }
    }


    private void closeStatements() {
        try {
            mCreateDatabaseStatement.close();
            mPreparedCreateStatement.close();
            mCreateTableStatement.close();
            Logger.log(TAG,"successfully closed database statements");
        } catch (SQLException e) {
            Logger.log(TAG,"error in closing statements");
            e.printStackTrace();
        }
    }

    public void closeConnection(){

        //TODO:se if we need to close/clear the statemments as well

        try {
            if(mDatabaseConnection!=null){
                mDatabaseConnection.close();
            }
            Logger.log(TAG,"closed the database connection");
        } catch (SQLException e) {
            Logger.log(TAG,"Error in closing the database connection");
            e.printStackTrace();
        }
    }
}
