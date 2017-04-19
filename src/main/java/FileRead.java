import java.io.*;


/**
 * Created by rajat on 7/4/17.
 */
public class FileRead implements Serializable {

    // size of .obj file byte array =   835
    // size of .mtl file byte array =   237

    public byte[] FileRead() throws IOException {
        File file = new File("cube.obj");
        byte[] bytesArray = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytesArray);
        fis.close();


        System.out.println(bytesArray.length);
        return bytesArray;
}

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //FileRead r = new FileRead();
        byte[] bytearr = new byte[1000];
        //bytearr = r.FileRead();



        // Serializable
        /*
        FileOutputStream fout = new FileOutputStream("f.txt");
        ObjectOutputStream out = new ObjectOutputStream(fout);

        out.writeObject(r.FileRead());
        out.flush();
        */


        //Deserializable
        /*
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("f.txt"));


        */
        System.out.println("success");

    }




}