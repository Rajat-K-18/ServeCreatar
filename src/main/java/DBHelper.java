import okio.ByteString;
import java.io.*;
import java.sql.*;



/**
 * Created by rajat on 20/4/17.
 */
public class DBHelper {

    static final String JDBC_driver = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";
    static final String USERNAME = "root";
    static final String PASSWORD = "root";
    private static Connection mDatabaseConnection;


    //byte[] bytesArray = new byte[10000];

    DBHelper() {

        if (mDatabaseConnection == null) {
            try {
                createNewDatabaseConnection();
            } catch (SQLException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
        }
    }


    private void createNewDatabaseConnection() throws SQLException, ClassNotFoundException {
        Statement stmt = null;

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

            //STEP 4: Execute a query
            System.out.println("Creating database...");
            stmt = mDatabaseConnection.createStatement();

            String create_database_sql = "CREATE DATABASE IF NOT EXISTS creatar";
            stmt.executeUpdate(create_database_sql);
            System.out.println("Database created successfully...");


            stmt = mDatabaseConnection.createStatement();
            String use_database_sql = "USE creatar";
            stmt.executeUpdate(use_database_sql);



            stmt = mDatabaseConnection.createStatement();
            String create_table_sql = "CREATE TABLE IF NOT EXISTS markerandinformation" +
                    "(markername varchar(255)," +
                    "markernft LONGBLOB NOT NULL," +
                    "markerpng LONGBLOB NOT NULL," +
                    "objfile LONGBLOB NOT NULL," +
                    "mtlfile LONGBLOB NOT NULL," +
                    "PRIMARY KEY(markername))";
            stmt.executeUpdate(create_table_sql);
            System.out.println("Tables are created");



            //byte[] t = file_read("pinball.fset");
            //t = file_read("pinball.iset");
            MagicData.Marker marker = new MagicData.Marker.Builder()
                    .fset(ByteString.of(file_read("pinball.fset")))
                    .fset3(ByteString.of(file_read("pinball.fset3")))
                    .iset(ByteString.of(file_read("pinball.iset")))
                    .markerName("pinball")
                    .build();

            byte[] r = MagicData.Marker.ADAPTER.encode(marker);


            //String insert_table_sql = "INSERT INTO markerandinformation values(?,?,?,?,?)";
            //String insert_table_sql = "INSERT INTO abc (name, number1) values (?,?)";

            PreparedStatement statement = mDatabaseConnection.prepareStatement
                    ("INSERT INTO markerandinformation values(?,?,?,?,?)");

            statement.setString(1, "pinball");
            statement.setBytes(2, r);
            statement.setBytes(3, r);
            statement.setBytes(4, r);
            statement.setBytes(5, r);


            statement.executeUpdate();


        } catch (
                SQLException se)

        {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (
                Exception e)

        {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally

        {
            //finally block used to close resources
            try {

                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (mDatabaseConnection != null)
                    mDatabaseConnection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");


    }



    public static byte[] file_read(String fileName) throws IOException {
        File f = new File(fileName);
        byte[] bytesArray = new byte[(int) f.length()];
        FileInputStream f1 = new FileInputStream(f);
        f1.read(bytesArray);
        f1.close();
        return bytesArray;
    }
}
