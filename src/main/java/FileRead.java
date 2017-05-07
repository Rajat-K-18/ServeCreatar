import model.MagicData;
import okio.ByteString;

import java.io.*;
import java.util.LinkedList;


/**
 * Created by rajat on 7/4/17.
 */
public  class FileRead {
    /*This class is there solely for purpose of some testing work*/

    // size of .obj file byte array =   835
    // size of .mtl file byte array =   237

    public static File objFile;
    public static File mtlFile;
    private static byte[] fsetByteArray;
    private static byte[] fset3ByteArray;
    private static byte[] isetByteArray;
    private static byte[] objByteArray;
    private static byte[] mtlByteArray;
    private static byte[] image1ByteArray;
    private static byte[] image2ByteArray;
    private static byte[] image3ByteArray;
    private static byte[] magicDataByteArray;
    private static byte[] mMagicDataByteArray;

    public static byte[] giveMagic(){
        return mMagicDataByteArray;
    }

    public static void loadSomeTestMarkerFiles() throws IOException {
        File read_fset = new File("magic/pinball.fset");
         fsetByteArray = new byte[(int) read_fset.length()];
        FileInputStream f1 = new FileInputStream(read_fset);
        f1.read(fsetByteArray);
        f1.close();

        /* Read the fset3 file */
        File read_fset3 = new File("magic/pinball.fset3");
        fset3ByteArray = new byte[(int) read_fset3.length()];
        FileInputStream f2 = new FileInputStream(read_fset3);
        f2.read(fset3ByteArray);
        f2.close();

        /* Read the iset file */
        File read_iset = new File("magic/pinball.iset");
        isetByteArray = new byte[(int) read_iset.length()];
        FileInputStream f3 = new FileInputStream(read_iset);
        f3.read(isetByteArray);
        f3.close();

        File read_obj = new File("magic/cube.obj");
         objByteArray = new byte[(int) read_obj.length()];
        FileInputStream f4 = new FileInputStream(read_obj);
        f4.read(objByteArray);
        f4.close();

        File read_mtl = new File("magic/cube.mtl");
         mtlByteArray = new byte[(int) read_mtl.length()];
        FileInputStream f5 = new FileInputStream(read_mtl);
        f5.read(mtlByteArray);
        f5.close();

        File read_image1 = new File("magic/images/booknewcrop.jpg");
         image1ByteArray = new byte[(int) read_image1.length()];
        FileInputStream f6 = new FileInputStream(read_image1);
        f6.read(image1ByteArray);
        f6.close();

        File read_image2 = new File("magic/images/keepsilence.jpg");
         image2ByteArray = new byte[(int) read_image2.length()];
        FileInputStream f7 = new FileInputStream(read_image2);
        f7.read(image2ByteArray);
        f7.close();

        File read_image3 = new File("magic/images/pinball.jpg");
         image3ByteArray = new byte[(int) read_image3.length()];
        FileInputStream f8 = new FileInputStream(read_image3);
        f8.read(image3ByteArray);
        f8.close();

    }

    public static void loadSampleMagicData(){

        MagicData.Images images1 = new MagicData.Images.Builder()
                .imagebytes(ByteString.of(image1ByteArray))
                .imageNameWithExtension("booknewcrop.jpg")
                .build();

        MagicData.Images images2 = new MagicData.Images.Builder()
                .imagebytes(ByteString.of(image2ByteArray))
                .imageNameWithExtension("keepsilence.jpg")
                .build();

        MagicData.Images images3 = new MagicData.Images.Builder()
                .imagebytes(ByteString.of(image3ByteArray))
                .imageNameWithExtension("pinball.jpg")
                .build();


        LinkedList<MagicData.Images> listOfImages = new LinkedList<MagicData.Images>();
        listOfImages.add(images1);
        listOfImages.add(images2);
        listOfImages.add(images3);


        MagicData.Marker m = new MagicData.Marker.Builder()
                .markerName("pinball")
                .fset(ByteString.of(fsetByteArray))
                .fset3(ByteString.of(fset3ByteArray))
                .iset(ByteString.of(isetByteArray))
                .build();

        MagicData.Information i = new MagicData.Information.Builder()
                .mtl(ByteString.of(mtlByteArray))
                .obj(ByteString.of(objByteArray))
                .image(listOfImages)
                .build();

        MagicData magicData = new MagicData.Builder()
                .marker(m)
                .information(i)
                .build();

        //byte[] markerByteArray =  model.MagicData.Marker.ADAPTER.encode(m);

         mMagicDataByteArray = MagicData.ADAPTER.encode(magicData);

    }
    public  FileRead()  {
       // return bytesArray;
        try {
            loadSomeTestMarkerFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

  /*  public static void main(String[] args) throws IOException, ClassNotFoundException {
        //FileRead r = new FileRead();
        byte[] bytearr = new byte[1000];
        //bytearr = r.FileRead();



        // Serializable
        *//*
        FileOutputStream fout = new FileOutputStream("f.txt");
        ObjectOutputStream out = new ObjectOutputStream(fout);

        out.writeObject(r.FileRead());
        out.flush();
        *//*


        //Deserializable
        *//*
        ObjectInputStream in = new ObjectInputStream(new FileInputStream("f.txt"));


        *//*
        System.out.println("success");

    }

*/


}