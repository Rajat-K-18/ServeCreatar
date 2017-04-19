import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

@WebSocket
public class MyWebSocketHandler  {

    public final static HashMap<String, MyWebSocketHandler> mSockets = new HashMap<String, MyWebSocketHandler>();
    private static final String TAG = MyWebSocketHandler.class.getSimpleName();
    private String mUniqueId;

        private String getMyUniqueId() {
            // unique ID from this class' hash code
            return Integer.toHexString(this.hashCode());
        }

        public Session getSession() {
            return mSession;
        }

        public void setSession(Session mSession) {
            this.mSession = mSession;
        }

        public Session mSession;
        ImageProcessor mImageProcessor;

        @OnWebSocketClose
        public void onClose(int statusCode, String reason) {
            if (MyWebSocketHandler.mSockets.containsKey(this.mUniqueId)) {
                // remove connection
                MyWebSocketHandler.mSockets.remove(this.mUniqueId);

                // broadcast this lost connection to all other connected clients

                try {
                    this.getSession().getRemote().sendString(String.format("{\"msg\": \"lostClient\", \"lostClientId\": \"%s\"}",
                            this.mUniqueId));

                }catch (Exception e){}
            }
            System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);

        }


        @OnWebSocketError
        public void onError(Throwable t) {
            System.out.println("Error: " + t.getMessage());
        }

        @OnWebSocketConnect
        public void onConnect(Session session) {
            this.mSession = session;

            // this unique ID
            this.mUniqueId = this.getMyUniqueId();
            // map this unique ID to this connection
            MyWebSocketHandler.mSockets.put(this.mUniqueId, this);
            mImageProcessor=new ImageProcessor(mUniqueId,this);
            this.mSession = session;
            System.out.println("Connect: " + session.getRemoteAddress().getAddress());
            try {
                mSession.getRemote().sendString("Hello Webbrowser");
                mSession.getRemote().sendString(String.format("{\"msg\": \"newClient\", \"newClientId\": \"%s\"}",
                        this.mUniqueId));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }



        @OnWebSocketMessage
        public void onMessage(byte[] data, int offset, int length) throws IOException {

            System.out.println(TAG+":receiving binary data from mobile");
            //FileUtils.writeByteArrayToFile(new File("images/"+System.nanoTime()+"hello.jpg"), data);
            mImageProcessor.processImage(data);
            //System.out.println(TAG+":"+this.mSession.getRemoteAddress());



        }

        @OnWebSocketMessage
        public void onMessage(String message) {
            System.out.println("String Message ====> " + message);
            try {
                this.mSession.getRemote().sendString("Receiving String Data==>"+message);
                //this.mSession.getRemote().sendBytes();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        public void sendClient(String s){
            for(MyWebSocketHandler myWebSocketHandler : mSockets.values()){
                try {
                    System.out.println(mSockets.size()+":"+myWebSocketHandler.getMyUniqueId());
                    System.out.println(TAG+":"+"Entering the sendClient method with "+ s);
                    myWebSocketHandler.getSession().getRemote().sendString(s);
                }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void sendClient(byte[] b){

    }



}