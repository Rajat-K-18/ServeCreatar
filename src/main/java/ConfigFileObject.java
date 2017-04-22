/**
 * Created by kartik on 22/4/17.
 */
public  class  ConfigFileObject {

    /* This class will hold static variables for all user_names and passwords
       and other configurations which may not be changed during runtime but over
       time , and systems , it may need to change

       The reason for using static and public is obviously to give global access
       to these variables throughout the program . We know that some people are against
       the use of statics , but thats what we are going for now atleast.

     */

    public static String DataBaseName ;
    public static String DataBaseUserName ;
    public static String DataBaseUserPassword ;

    public  ConfigFileObject(){

    }

    //TODO: to be completed

}
